package com.sakewiz.android.ui.fragments;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import com.sakewiz.android.R;
import com.sakewiz.android.common.CommonUtils;
import com.sakewiz.android.common.constants.ApplicationConstants;
import com.sakewiz.android.domain.ProductService;
import com.sakewiz.android.domain.ProductServiceImpl;
import com.sakewiz.android.dto.CountryName;
import com.sakewiz.android.dto.Product;
import com.sakewiz.android.dto.ProductName;
import com.sakewiz.android.dto.RegionName;
import com.sakewiz.android.model.entities.response.ReviewForProductResponse;
import com.sakewiz.android.model.entities.response.FavouriteSakeResponse;
import com.sakewiz.android.model.entities.response.ProductDetailResponse;
import com.sakewiz.android.model.entities.response.ProductFacadeResponse;
import com.sakewiz.android.model.entities.response.ReviewResponse;
import com.sakewiz.android.model.entities.response.UnknownSakeResponse;
import com.sakewiz.android.model.rest.SakeWizService;
import com.sakewiz.android.mvp.presenters.ProductPresenter;
import com.sakewiz.android.mvp.presenters.ProductPresenterImpl;
import com.sakewiz.android.mvp.views.ProductView;
import com.sakewiz.android.ui.activities.MainActivity;
import com.sakewiz.android.ui.adapters.FavoriteSakeItemRecycleAdapter;
import com.sakewiz.android.utils.AppScheduler;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FavoriteSakeFragment extends BaseFragment implements ProductView {

    final String TAG = FavoriteSakeFragment.this.getClass().getSimpleName();
    public static String getTAG() {
        return "FavoriteSakeFragment";
    }
    private static String BUNDLE_EXTRA = "BUNDLE_EXTRA";

    public static FavoriteSakeFragment newInstance(FavouriteSakeResponse favouriteSakeResponse) {
        FavoriteSakeFragment fragment = new FavoriteSakeFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_EXTRA, Parcels.wrap(favouriteSakeResponse));
        fragment.setArguments(args);
        return  fragment;
    }

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.scrollView) NestedScrollView scrollView;
    @Bind(R.id.swipe_refresh_layout_home) SwipeRefreshLayout mSwipeRefreshLayout;

    private SearchView searchView;
    private TextView mTitle;
    private FavoriteSakeItemRecycleAdapter mFavoriteSakeItemRecycleAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favorite_sake, container, false);
        ButterKnife.bind(this, rootView);
        initRecyclerView();
        return rootView;
    }

    @Override
    protected void initializePresenter() {

        ProductService mProductService = new ProductServiceImpl(new SakeWizService());
        presenter = new ProductPresenterImpl(getActivity(), mProductService, new AppScheduler());
        presenter.attachView(FavoriteSakeFragment.this);
        presenter.onCreate();
    }

    @Override
    protected void setUpUI() {
        performFavouriteSakeRequest();
        int[] colors = getResources().getIntArray(R.array.google_colors);
        mSwipeRefreshLayout.setColorSchemeColors(colors);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setSwipeLayoutLoading(true);
                clearAdapters();
                performFavouriteSakeRequest();
            }
        });
    }

    @Override
    protected void setUpToolBar() {
        View mCustomView = getActivity().getLayoutInflater().inflate(R.layout.custom_actionbar_main_with_back, null);
        searchView = ((SearchView) mCustomView.findViewById(R.id.searchView));
        mTitle = (TextView) mCustomView.findViewById(R.id.title);
        TextView mNotificationCount = (TextView) mCustomView.findViewById(R.id.text_notification_count);
        mToolBar.addView(mCustomView);

        mCustomView.findViewById(R.id.imgVback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        mTitle.setText(R.string.fragment_favourite_sake);
        mTitle.setAllCaps(true);
        mNotificationCount.setVisibility(View.GONE);
        ((MainActivity) getActivity()).setUpSearchBar(searchView, null);
    }

//    @OnClick(R.id.filter_by_container)
//    public void onClickFilterBy(View view){
//        MainActivity.mainActivity.addFragment(new FilterMainFragment().newInstance(FavoriteSakeFragment.getTAG()),FilterMainFragment.getTAG(),1);
//    }
//
//    @OnClick(R.id.sort_by_container)
//    public void onClickSortBy(View view){
//        MainActivity.mainActivity.addFragment(new SortByFragment().newInstance(FavoriteSakeFragment.getTAG()),SortByFragment.getTAG(),1);
//    }
//
//    public void setSortBy(String sortBy){
//        mSortBy = sortBy;
//    }

    protected void initRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);

        mFavoriteSakeItemRecycleAdapter = new FavoriteSakeItemRecycleAdapter(getActivity(),this, new ArrayList<Product>());
        mRecyclerView.setAdapter(mFavoriteSakeItemRecycleAdapter);
    }

    public void performFavouriteSakeRequest() {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            ((ProductPresenter) presenter).getFavouriteSake();
        } else {
            setSwipeLayoutLoading(false);
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        }
    }

    public void performUnFavourSakeRequest(String productId) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            ((ProductPresenter) presenter).doUnFavourProduct(productId);
        } else {
            setSwipeLayoutLoading(false);
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        }
    }

    private void setSwipeLayoutLoading(final boolean isLoading) {
        if (mSwipeRefreshLayout == null) return;
        if (isLoading) {
            mSwipeRefreshLayout.post(new Runnable() { // show refreshlayout progress
                @Override
                public void run() {
                    try {
                        if (mSwipeRefreshLayout != null && !mSwipeRefreshLayout.isRefreshing())
                            mSwipeRefreshLayout.setRefreshing(true);
                    } catch (Exception e) {
                        Log.e(TAG, "setSwipeLayoutLoading: " + e.toString());
                    }
                }
            });
        } else {
            mSwipeRefreshLayout.post(new Runnable() { // show refreshlayout progress
                @Override
                public void run() {
                    try {
                        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing())
                            mSwipeRefreshLayout.setRefreshing(false);
                    } catch (Exception e) {
                        Log.e(TAG, "setSwipeLayoutLoading: " + e.toString());
                    }
                }
            });
        }
    }

    private void clearAdapters(){
        mFavoriteSakeItemRecycleAdapter.updateData(null,1);
    }

    // --- mock data for products --- //
    private List<Product> getMockProducts(){
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            Product item = new Product();
            item.setMainImgUrl("/product-img/e2e6760d-24a5-40af-b950-519737c83158/ea81c303-9231-48f8-b21c-d10b1f6bc564.jpeg");
            item.setRate("4.0");
            item.setCategory("Sake");
            item.setType("JUNMAIDAIGINJOSHU");
                ProductName name = new ProductName();
                name.setEn("Sake Title");
            item.setName(name);
                CountryName countryName = new CountryName();
                countryName.setEn("Hakassan");
            item.setCountryName(countryName);
                RegionName regionName = new RegionName();
                regionName.setEn("Japan");
            item.setRegionName(regionName);
            item.setReviewCount("53");
            item.setFavoured("46");
            products.add(item);
        }
        return products;
    }

    @Override
    public void showProductDetailResponse(ProductDetailResponse productDetailResponse) {

    }

    @Override
    public void showProductFacadeResponse(ProductFacadeResponse productFacadeResponse) {

    }

    @Override
    public void showReviewsResponse(ReviewResponse reviewResponse) {

    }

    @Override
    public void showReviewProductResponse(ReviewForProductResponse reviewForProductResponse) {

    }

    @Override
    public void showLikeReviewResponse(ReviewForProductResponse reviewForProductResponse) {

    }

    @Override
    public void showGetFavouriteSakeResponse(FavouriteSakeResponse favouriteSakeResponse) {

        setProgressDialog(false);
        setSwipeLayoutLoading(false);

        if (favouriteSakeResponse.isSuccess()) {
            if (favouriteSakeResponse.getProducts() != null && favouriteSakeResponse.getProducts().size() > 0) {
                mFavoriteSakeItemRecycleAdapter.updateData(favouriteSakeResponse.getProducts(),0);
            } else {
                mFavoriteSakeItemRecycleAdapter.updateData(getMockProducts(), 0); // mock data
            }
            System.out.println("=======================++>>>>> Success ");
        } else {
            if (favouriteSakeResponse.isAPIError()) {
                showTopSnackBar(favouriteSakeResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            } else {
                showTopSnackBar(favouriteSakeResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            }
        }
    }

    @Override
    public void showUnFavourProductResponse(ReviewForProductResponse reviewForProductResponse) {
        setProgressDialog(false);
        setSwipeLayoutLoading(false);

        if (reviewForProductResponse.isSuccess()) {
            performFavouriteSakeRequest();
            System.out.println("=======================++>>>>> Success ");
        } else {
            if (reviewForProductResponse.isAPIError()) {
                showTopSnackBar(reviewForProductResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            } else {
                showTopSnackBar(reviewForProductResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            }
        }
    }

    @Override
    public void showFavourProductResponse(ReviewForProductResponse reviewForProductResponse) {

    }

    @Override
    public void showUnknownSakeResponse(UnknownSakeResponse unknownSakeResponse) {

    }
}
