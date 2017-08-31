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

import com.sakewiz.android.R;
import com.sakewiz.android.common.CommonUtils;
import com.sakewiz.android.common.constants.ApplicationConstants;
import com.sakewiz.android.domain.PlaceService;
import com.sakewiz.android.domain.PlaceServiceImpl;
import com.sakewiz.android.domain.ProductService;
import com.sakewiz.android.domain.ProductServiceImpl;
import com.sakewiz.android.dto.Place;
import com.sakewiz.android.dto.Product;
import com.sakewiz.android.dto.ProductName;
import com.sakewiz.android.model.entities.response.FavouritePlacesResponse;
import com.sakewiz.android.model.entities.response.ReviewForPlaceResponse;
import com.sakewiz.android.model.rest.SakeWizService;
import com.sakewiz.android.mvp.presenters.PlacePresenter;
import com.sakewiz.android.mvp.presenters.PlacePresenterImpl;
import com.sakewiz.android.mvp.presenters.Presenter;
import com.sakewiz.android.mvp.presenters.ProductPresenter;
import com.sakewiz.android.mvp.presenters.ProductPresenterImpl;
import com.sakewiz.android.mvp.views.PlaceView;
import com.sakewiz.android.ui.adapters.FavoritePlacesBarsItemRecycleAdapter;
import com.sakewiz.android.ui.adapters.FavoritePlacesBreweriesItemRecycleAdapter;
import com.sakewiz.android.utils.AppScheduler;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class FavoritePlacesBreweriesFragment extends BaseFragment implements PlaceView {

    final String TAG = FavoritePlacesBreweriesFragment.this.getClass().getSimpleName();
    public static String getTAG() {
        return "FavoritePlacesBarFragment";
    }
    private static String BUNDLE_EXTRA = "BUNDLE_EXTRA";

    public static FavoritePlacesBarsFragment newInstance() {
        return new FavoritePlacesBarsFragment();
    }

    public static FavoritePlacesBreweriesFragment favoritePlacesBreweriesFragment = null;

    public static FavoritePlacesBarsFragment newInstance(String param1, String param2) {
        FavoritePlacesBarsFragment fragment = new FavoritePlacesBarsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.scrollView) NestedScrollView scrollView;
    @Bind(R.id.swipe_refresh_layout_home)

    SwipeRefreshLayout mSwipeRefreshLayout;
    private FavoritePlacesBreweriesItemRecycleAdapter mFavoritePlacesBreweriesItemRecycleAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_favorite_places_breweries, container, false);
        ButterKnife.bind(this, rootView);
        initRecyclerView();
        return  rootView;
    }


    @Override
    protected void initializePresenter() {
        PlaceService mPlaceService = new PlaceServiceImpl(new SakeWizService());
        presenter = new PlacePresenterImpl(getActivity(), mPlaceService, new AppScheduler());
        presenter.attachView(FavoritePlacesBreweriesFragment.this);
        presenter.onCreate();
    }

    @Override
    protected void setUpUI() {
        performFavouritePlacesBreweriesRequest();
        int[] colors = getResources().getIntArray(R.array.google_colors);
        mSwipeRefreshLayout.setColorSchemeColors(colors);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setSwipeLayoutLoading(true);
                clearAdapters();
                performFavouritePlacesBreweriesRequest();
            }
        });
    }

    @Override
    protected void setUpToolBar() {

    }

    protected void initRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);

        mFavoritePlacesBreweriesItemRecycleAdapter = new FavoritePlacesBreweriesItemRecycleAdapter(getActivity(),this, new ArrayList<Place>());
        mRecyclerView.setAdapter(mFavoritePlacesBreweriesItemRecycleAdapter);
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

    public void performFavouritePlacesBreweriesRequest() {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            ((PlacePresenter) presenter).getFavouritePlacesBreweries();
        } else {
            setSwipeLayoutLoading(false);
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        }
    }

    public void performUnFavourPlaceBreweryRequest(String placeId) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            ((PlacePresenter) presenter).doUnFavouritePlacesBrewery(placeId);
        } else {
            setSwipeLayoutLoading(false);
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        }
    }


    private void clearAdapters(){
        mFavoritePlacesBreweriesItemRecycleAdapter.updateData(null,1);
    }
    @Override
    public void onDestroyView() {
        favoritePlacesBreweriesFragment = null;
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (BaseFragment.myAlertDialog != null && BaseFragment.myAlertDialog.isShowing())
            BaseFragment.myAlertDialog.dismiss();
    }


    @Override
    public void showGetFavouritePlacesBarsResponse(FavouritePlacesResponse favouritePlacesResponse) {

    }

    @Override
    public void showGetFavouritePlacesBreweriesResponse(FavouritePlacesResponse favouritePlacesResponse) {
        setProgressDialog(false);
        setSwipeLayoutLoading(false);

        if (favouritePlacesResponse.isSuccess()) {
            if (favouritePlacesResponse.getPlaces() != null && favouritePlacesResponse.getPlaces().size() > 0) {
                mFavoritePlacesBreweriesItemRecycleAdapter.updateData(favouritePlacesResponse.getPlaces(),0);
            } else {
                mFavoritePlacesBreweriesItemRecycleAdapter.updateData(getMockPlaces(), 0); // mock data
            }
            System.out.println("=======================++>>>>> Success ");
        } else {
            if (favouritePlacesResponse.isAPIError()) {
                showTopSnackBar(favouritePlacesResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            } else {
                showTopSnackBar(favouritePlacesResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            }
        }
    }

    @Override
    public void showDoUnFavourPlaceBarResponse(ReviewForPlaceResponse reviewForPlaceResponse) {

    }

    @Override
    public void showDoUnFavourPlaceBreweryResponse(ReviewForPlaceResponse reviewForPlaceResponse) {
        setProgressDialog(false);
        setSwipeLayoutLoading(false);

        if (reviewForPlaceResponse.isSuccess()) {
            performFavouritePlacesBreweriesRequest();
            System.out.println("=======================++>>>>> Success ");
        } else {
            if (reviewForPlaceResponse.isAPIError()) {
                showTopSnackBar(reviewForPlaceResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            } else {
                showTopSnackBar(reviewForPlaceResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            }
        }
    }

    /* --- mock data for products --- // */
    private List<Place> getMockPlaces(){
        List<Place> places = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            Place item = new Place();
            item.setImgUrl("/product-img/e2e6760d-24a5-40af-b950-519737c83158/ea81c303-9231-48f8-b21c-d10b1f6bc564.jpeg");
            item.setRate("4.0");
//            item.setCategory("Sake");
            item.setType("STAND UP BAR");
            ProductName name = new ProductName();
            name.setEn("Breweries Title");
            item.setName(name);
            item.setLat("30.44");
            item.setLon("142.123");
            item.setReviewCount("103");
            item.setFavoured("35");
            places.add(item);
        }
        return places;
    }
}
