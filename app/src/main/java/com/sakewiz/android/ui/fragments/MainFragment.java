package com.sakewiz.android.ui.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sakewiz.android.R;
import com.sakewiz.android.common.CommonUtils;
import com.sakewiz.android.common.constants.ApplicationConstants;
import com.sakewiz.android.common.constants.DomainConstants;
import com.sakewiz.android.domain.ProductService;
import com.sakewiz.android.domain.ProductServiceImpl;
import com.sakewiz.android.domain.UserDashBoardService;
import com.sakewiz.android.domain.UserDashBoardServiceImpl;
import com.sakewiz.android.dto.DashBoardHorizontalItem;
import com.sakewiz.android.dto.Product;
import com.sakewiz.android.model.entities.response.ReviewForProductResponse;
import com.sakewiz.android.model.entities.response.BarFacadeResponse;
import com.sakewiz.android.model.entities.response.BreweryFacadeResponse;
import com.sakewiz.android.model.entities.response.FavouriteSakeResponse;
import com.sakewiz.android.model.entities.response.NotificationResponse;
import com.sakewiz.android.model.entities.response.ProductDetailResponse;
import com.sakewiz.android.model.entities.response.ProductFacadeResponse;
import com.sakewiz.android.model.entities.response.ReviewResponse;
import com.sakewiz.android.model.entities.response.UnknownSakeResponse;
import com.sakewiz.android.model.entities.response.UserDashboardResponse;
import com.sakewiz.android.model.rest.SakeWizService;
import com.sakewiz.android.mvp.presenters.Presenter;
import com.sakewiz.android.mvp.presenters.ProductPresenter;
import com.sakewiz.android.mvp.presenters.ProductPresenterImpl;
import com.sakewiz.android.mvp.presenters.UserDashBoardPresenter;
import com.sakewiz.android.mvp.presenters.UserDashBoardPresenterImpl;
import com.sakewiz.android.mvp.views.ProductView;
import com.sakewiz.android.mvp.views.UserDashBoardView;
import com.sakewiz.android.ui.activities.MainActivity;
import com.sakewiz.android.ui.adapters.AlbumArt;
import com.sakewiz.android.ui.adapters.EnchantedPagerAdapter;
import com.sakewiz.android.ui.adapters.LatestRecommendItemRecycleAdapter;
import com.sakewiz.android.utils.AppScheduler;
import com.tiagosantos.enchantedviewpager.EnchantedViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dilshan_e on 29/05/2017.
 */
public class MainFragment extends BaseFragment implements UserDashBoardView, ProductView {

    final String TAG = MainFragment.this.getClass().getSimpleName();

    public static MainFragment mainFragment = null;

    public static String getTAG() {
        return "MainFragment";
    }

    private static String BUNDLE_EXTRA = "BUNDLE_EXTRA";

    private int mDefaultTab =  -1;

    public static MainFragment newInstance(int defaultTab) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(BUNDLE_EXTRA, defaultTab);
        fragment.setArguments(args);
        return fragment;
    }

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh_layout_home) SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.wiz_credit_till_next_level) TextView mWizCreditTillNextLevel;
    @Bind(R.id.profile_picture) CircleImageView mProfilePicture;
    @Bind(R.id.fav_sake_count) TextView mFaveSakeCount;
    @Bind(R.id.fav_places_count) TextView mFavPlacesCount;
    @Bind(R.id.followers_count) TextView mFollowers;
    @Bind(R.id.following_count) TextView mFollowing;
    @Bind(R.id.labels_count) TextView mLabels;
    @Bind(R.id.wiz_credits_count) TextView mWizCredits;
    @Bind(R.id.item_image_progress) ProgressBar itemImageProgress;
    @Bind(R.id.fav_sake_container) RelativeLayout mFavSakeLayout;

    protected Presenter productPresenter;
    private SearchView searchView;
    private TextView mTitle;
    private TextView mNotificationCount;
    private LatestRecommendItemRecycleAdapter mLatestRecommendItemRecycleAdapter;
    private EnchantedPagerAdapter mEnchantedPagerAdapter;
    private EnchantedViewPager mViewPager;
    private UserDashboardResponse mResponse;
    private boolean isPulltoRefreshCall = false;
    private List<DashBoardHorizontalItem> dashBoardHorizontalItems = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Log.d(TAG, "onCreateView");
        ButterKnife.bind(this, rootView);
        if (productPresenter != null) productPresenter.onCreate();
        mViewPager = (EnchantedViewPager) rootView.findViewById(R.id.homepage_card_view_pager);

        mEnchantedPagerAdapter = new EnchantedPagerAdapter(getActivity(), new ArrayList<DashBoardHorizontalItem>());
        mViewPager.setAdapter(mEnchantedPagerAdapter);
        mViewPager.useScale();

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager, true);
        mainFragment = this;

        initRecyclerView();
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (productPresenter != null) productPresenter.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (productPresenter != null) productPresenter.onStart();
    }

    @Override
    public void initializePresenter() {
        UserDashBoardService mUserDashBoardService = new UserDashBoardServiceImpl(new SakeWizService());
        presenter = new UserDashBoardPresenterImpl(getActivity(), mUserDashBoardService, new AppScheduler());
        presenter.attachView(MainFragment.this);
        presenter.onCreate();

        ProductService mProductService = new ProductServiceImpl(new SakeWizService());
        productPresenter = new ProductPresenterImpl(getActivity(), mProductService, new AppScheduler());
        productPresenter.attachView(MainFragment.this);
        productPresenter.onCreate();
    }

    @Override
    protected void setUpUI() {
        performUserDashBoardRequest();
        int[] colors = getResources().getIntArray(R.array.google_colors);
        mSwipeRefreshLayout.setColorSchemeColors(colors);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setSwipeLayoutLoading(true);
                isPulltoRefreshCall = true;
                performUserDashBoardRequest();
            }
        });
    }

    @Override
    protected void setUpToolBar() {
        View mCustomView = getActivity().getLayoutInflater().inflate(R.layout.custom_actionbar_main, null);
        searchView = ((SearchView) mCustomView.findViewById(R.id.searchView));
        mTitle = (TextView) mCustomView.findViewById(R.id.title);
        mNotificationCount = (TextView) mCustomView.findViewById(R.id.text_notification_count);
        mToolBar.addView(mCustomView);

        mCustomView.findViewById(R.id.imgVback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).toggleNavigationDrawer();
            }
        });
//        mTitle.setTypeface(CommonUtils.getInstance().getFont(getActivity(), ApplicationConstants.FONT_ACUMIN_PRO_REGULAR));
        mTitle.setText(R.string.fragment_main_dash_board);
        ((MainActivity) getActivity()).setUpSearchBar(searchView, null);
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

    protected void initRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);

        mLatestRecommendItemRecycleAdapter = new LatestRecommendItemRecycleAdapter(getActivity(), new ArrayList<Product>());
        mRecyclerView.setAdapter(mLatestRecommendItemRecycleAdapter);
    }

    private ArrayList<AlbumArt> createAlbumList() {
        ArrayList<AlbumArt> albumList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            albumList.add(new AlbumArt(getAlbumArtReference(i)));
        }
        return albumList;
    }

    private int getAlbumArtReference(int position) {
        if (position == 0) {
            return R.drawable.ic_background_pattern;
        } else if (position == 1) {
            return R.drawable.bg_dashboard;
        } else if (position == 2) {
            return R.drawable.test_profile_pic;
        }

        return R.drawable.ic_background_pattern;
    }

    private void performUserDashBoardRequest() {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            ((UserDashBoardPresenter) presenter).getUserDashboard();
        } else {
            setSwipeLayoutLoading(false);
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        }
    }

    private void setData(){
        if (mResponse != null){

            if (mResponse.getNotificationCount() != null && !mResponse.getNotificationCount().isEmpty()){
                if(!mResponse.getNotificationCount().equals("0")) {
                    if(mNotificationCount.getVisibility() == View.GONE) mNotificationCount.setVisibility(View.VISIBLE);
                    mNotificationCount.setText(mResponse.getNotificationCount());
                }
            }
            if (mResponse.getUserProfileImg() != null) {
                ImageLoader.getInstance().displayImage(DomainConstants.DEFAULT_IMAGE_URL
                        + mResponse.getUserProfileImg(), mProfilePicture, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        showHideImageProgress(true);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        showHideImageProgress(false);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        showHideImageProgress(false);
                    }
                });
            } else {
                showHideImageProgress(false);
            }

            if(mResponse.getFavSakeCount() !=null && !mResponse.getFavSakeCount().isEmpty()) mFaveSakeCount.setText(mResponse.getFavSakeCount());
            if(mResponse.getFavBarCount() != null && !mResponse.getFavBarCount().isEmpty()) mFavPlacesCount.setText(mResponse.getFavBarCount());
            if(mResponse.getFollowerCount() != null && !mResponse.getFollowerCount().isEmpty()) mFollowers.setText(mResponse.getFollowerCount());
            if(mResponse.getFollowingCount() != null && !mResponse.getFollowingCount().isEmpty()) mFollowing.setText(mResponse.getFollowingCount());
            if(mResponse.getLabelCount() != null && !mResponse.getLabelCount().isEmpty()) mLabels.setText(mResponse.getLabelCount());
            if(mResponse.getWizPoints() != null && !mResponse.getWizPoints().isEmpty()) {
                mWizCredits.setText(mResponse.getWizPoints());
                mWizCreditTillNextLevel.setText(mResponse.getWizPoints() + " " + getString(R.string.wiz_redit_untill_next_level));
            }
            if(mResponse.getRecommendedProducts() != null && mResponse.getRecommendedProducts().size() > 0) {
                mLatestRecommendItemRecycleAdapter.updateData(mResponse.getRecommendedProducts(),0);
            }
            DashBoardHorizontalItem dashBoardHorizontalItem = new DashBoardHorizontalItem();
            dashBoardHorizontalItem.setTypeAdd(false);
            if (mResponse.getSakeIdentified() != null && !mResponse.getSakeIdentified().isEmpty())
                dashBoardHorizontalItem.setSakeIdentified(mResponse.getSakeIdentified());
            if (mResponse.getSakeUnidentified() != null && !mResponse.getSakeUnidentified().isEmpty())
                dashBoardHorizontalItem.setSakeUnidentified(mResponse.getSakeUnidentified());
            dashBoardHorizontalItems.add(dashBoardHorizontalItem);
            mEnchantedPagerAdapter.updateData(dashBoardHorizontalItems,0);
            mViewPager.setCurrentItem(dashBoardHorizontalItems.size()-1,true);

        }
    }

    private void showHideImageProgress(boolean isShow) {
        if (itemImageProgress == null) return;
        itemImageProgress.setVisibility((isShow) ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.fav_sake_container)
    public void onClickFavSake(View view) {
        ((MainActivity)getActivity()).addFragment(new FavoriteSakeFragment(), FavoriteSakeFragment.getTAG(), 2);
    }

    @OnClick(R.id.fav_places_container)
    public void onClickFavPlaces(View view) {
        ((MainActivity)getActivity()).addFragment(new FavoritePlacesFragment(), FavoritePlacesFragment.getTAG(), 2);
    }

    @Override
    public void showGetBarFacadeResponse(BarFacadeResponse barFacadeResponse) {

    }

    @Override
    public void showGetBreweryFacadeResponse(BreweryFacadeResponse breweryFacadeResponse) {

    }

    @Override
    public void showGetUserDashboardResponse(UserDashboardResponse userDashboardResponse) {
        setProgressDialog(false);
        setSwipeLayoutLoading(false);
        if (userDashboardResponse.isSuccess()) {
            if (isPulltoRefreshCall){
                isPulltoRefreshCall = false;
                mLatestRecommendItemRecycleAdapter.updateData(null, 1);
                mEnchantedPagerAdapter.updateData(null,1);
                dashBoardHorizontalItems.clear();
            }
            mResponse = userDashboardResponse;
            setData();
        } else {
            if (userDashboardResponse.isAPIError()) {
                showTopSnackBar(userDashboardResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            } else {
                showTopSnackBar(userDashboardResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            }
        }
    }

    @Override
    public void showUserNotificationsResponse(NotificationResponse notificationResponse) {

    }

    @Override
    public void showGetFavouriteSakeResponse(FavouriteSakeResponse favouriteSakeResponse) {

    }

    @Override
    public void showProductDetailResponse(ProductDetailResponse productDetailResponse) {

    }

    public void performProductFacadeRequest(String productId) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            ((ProductPresenter) productPresenter).getProductFacade(productId);
        } else {
            setSwipeLayoutLoading(false);
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        }
    }

    @Override
    public void showProductFacadeResponse(ProductFacadeResponse productFacadeResponse) {
        setProgressDialog(false);
        if (productFacadeResponse.isSuccess()) {
            ((MainActivity)getActivity()).addFragment(new ProductDetailsFragment().newInstance(productFacadeResponse), ProductDetailsFragment.getTAG(), 2);
        } else {
            if (productFacadeResponse.isAPIError()) {
                showTopSnackBar(productFacadeResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            } else {
                showTopSnackBar(productFacadeResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            }
        }
    }

    @Override
    public void showReviewsResponse(ReviewResponse reviewResponse) { }

    @Override
    public void showReviewProductResponse(ReviewForProductResponse reviewForProductResponse) { }

    @Override
    public void showLikeReviewResponse(ReviewForProductResponse reviewForProductResponse) { }

    @Override
    public void showUnFavourProductResponse(ReviewForProductResponse reviewForProductResponse) { }

    @Override
    public void showFavourProductResponse(ReviewForProductResponse reviewForProductResponse) { }

    @Override
    public void showUnknownSakeResponse(UnknownSakeResponse unknownSakeResponse) { }
}