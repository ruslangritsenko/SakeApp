package com.sakewiz.android.ui.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.sakewiz.android.dto.Comments;
import com.sakewiz.android.dto.ProductDetailHorizontalItem;
import com.sakewiz.android.model.entities.response.FavouriteSakeResponse;
import com.sakewiz.android.model.entities.response.ReviewForProductResponse;
import com.sakewiz.android.dto.UserReview;
import com.sakewiz.android.model.entities.request.ReviewForProductRequest;
import com.sakewiz.android.model.entities.response.ProductDetailResponse;
import com.sakewiz.android.model.entities.response.ProductFacadeResponse;
import com.sakewiz.android.model.entities.response.ReviewResponse;
import com.sakewiz.android.model.entities.response.UnknownSakeResponse;
import com.sakewiz.android.model.entities.response.UserDashboardResponse;
import com.sakewiz.android.model.rest.SakeWizService;
import com.sakewiz.android.mvp.presenters.ProductPresenter;
import com.sakewiz.android.mvp.presenters.ProductPresenterImpl;
import com.sakewiz.android.mvp.views.ProductView;
import com.sakewiz.android.ui.activities.MainActivity;
import com.sakewiz.android.ui.adapters.EndlessParentScrollListener;
import com.sakewiz.android.ui.adapters.ProductDetailEnchantedPagerAdapter;
import com.sakewiz.android.ui.adapters.ProductReviewsRecycleAdapter;
import com.sakewiz.android.utils.AppScheduler;
import com.tiagosantos.enchantedviewpager.EnchantedViewPager;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dilshan_e on 29/05/2017.
 */
public class ProductDetailsFragment extends BaseFragment implements ProductView{

    final String TAG = ProductDetailsFragment.this.getClass().getSimpleName();

    public static ProductDetailsFragment mainFragment = null;

    public static String getTAG() {
        return "MainFragment";
    }

    private static String BUNDLE_EXTRA = "BUNDLE_EXTRA";

    private int mDefaultTab =  -1;

    public static ProductDetailsFragment newInstance(ProductFacadeResponse productFacadeResponse) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_EXTRA, Parcels.wrap(productFacadeResponse));
        fragment.setArguments(args);
        return fragment;
    }

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh_layout_home) SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.comment_container) LinearLayout mCommentContainer;
    @Bind(R.id.star_point_1) ImageView starPoint_1;
    @Bind(R.id.star_point_2) ImageView starPoint_2;
    @Bind(R.id.star_point_3) ImageView starPoint_3;
    @Bind(R.id.star_point_4) ImageView starPoint_4;
    @Bind(R.id.star_point_5) ImageView starPoint_5;
    @Bind(R.id.rating_star_point_1) ImageView ratingStarPoint_1;
    @Bind(R.id.rating_star_point_2) ImageView ratingStarPoint_2;
    @Bind(R.id.rating_star_point_3) ImageView ratingStarPoint_3;
    @Bind(R.id.rating_star_point_4) ImageView ratingStarPoint_4;
    @Bind(R.id.rating_star_point_5) ImageView ratingStarPoint_5;
    @Bind(R.id.comment_text) EditText mComment;
    @Bind(R.id.scrollView) NestedScrollView scrollView;
    @Bind(R.id.hori_progress) RelativeLayout horizontalProgress;
    @Bind(R.id.txt_sake_name) TextView sakeName;
    @Bind(R.id.txt_value_rating) TextView valueRating;
    @Bind(R.id.img_favourite) ImageView userFavourite;
    @Bind(R.id.image_progress) ProgressBar imageProgress;
    @Bind(R.id.img_main) ImageView mainImage;

    private SearchView searchView;
    private TextView mTitle;
    private ProductReviewsRecycleAdapter mProductReviewRecycleAdapter;
    private ProductDetailEnchantedPagerAdapter mEnchantedPagerAdapter;
    private EnchantedViewPager mViewPager;
    private UserDashboardResponse mResponse;
    private boolean isPulltoRefreshCall = false;
    private List<ProductDetailHorizontalItem> productDetailHorizontalItems = new ArrayList<>();
    private int mRate = 0;
    private ProductFacadeResponse mItem;
    private final int TAKE_CONSTANT = 10;
    private boolean isOnLoadMoreProduct = false;
    private int pageProduct = 0;
    private String lastID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItem = Parcels.unwrap(getArguments().getParcelable(BUNDLE_EXTRA));

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_details, container, false);
        Log.d(TAG, "onCreateView");
        ButterKnife.bind(this, rootView);
        mViewPager = (EnchantedViewPager) rootView.findViewById(R.id.homepage_card_view_pager);

        mEnchantedPagerAdapter = new ProductDetailEnchantedPagerAdapter(getActivity(), new ArrayList<ProductDetailHorizontalItem>());
        mViewPager.setAdapter(mEnchantedPagerAdapter);
        mViewPager.useScale();

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager, true);
        mainFragment = this;

        initRecyclerView();
        return rootView;
    }

    @Override
    public void initializePresenter() {
        ProductService mProductService = new ProductServiceImpl(new SakeWizService());
        presenter = new ProductPresenterImpl(getActivity(), mProductService, new AppScheduler());
        presenter.attachView(ProductDetailsFragment.this);
        presenter.onCreate();
    }

    @Override
    protected void setUpUI() {
//        performUserDashBoardRequest();
        setData();
        int[] colors = getResources().getIntArray(R.array.google_colors);
        mSwipeRefreshLayout.setColorSchemeColors(colors);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setSwipeLayoutLoading(true);
                isPulltoRefreshCall = true;
                clearAdapters();
                if (mItem.getProduct() != null ) performProductFacadeRequest(mItem.getProduct().getId());
                mItem = null;
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
        mTitle.setText(R.string.fragment_product_details);
        mTitle.setAllCaps(true);
        mNotificationCount.setVisibility(View.GONE);
        ((MainActivity) getActivity()).setUpSearchBar(searchView, null);
    }

    @OnClick(R.id.taste_chart_container)
    public void onClickTasteChart(View view) {
        ((MainActivity)getActivity()).addFragment(new TasteChartFragment(), TasteChartFragment.getTAG(), 2);
    }

    @OnClick(R.id.add_note_container)
    public void onClickAddNote(View view) {
        ((MainActivity)getActivity()).addFragment(new AddProductNoteFragment(), AddProductNoteFragment.getTAG(), 2);
    }

    @OnClick(R.id.apply_btn_submit_review)
    public void onClickSubmit(View view) {
        CommonUtils.getInstance().hideKeyboard(getActivity());
        ReviewForProductRequest request = new ReviewForProductRequest();
        Comments c = new Comments();
        c.setEn(mComment.getText().toString().trim());
        request.setComment(c);
        request.setRate(mRate);
        if (mItem != null && mItem.getProduct() != null ) performReviewProductRequest(mItem.getProduct().getId(),request);
        toggleCommentView(false);
        System.out.println("=======================>>>> Comment : "+mComment.getText().toString()+" Rating : "+mRate);
    }

    @OnClick(R.id.rating_main_container)
    public void onClickRatingMainContainer(View view) {
        if (mCommentContainer.getVisibility() == View.GONE ) toggleCommentView(true);
        else toggleCommentView(false);
    }

    @OnClick(R.id.star_point_1)
    public void onClickStarPoint1(View view) {
        toggleCommentView(true);
        if (view.getTag().toString().equals("0")) {
            starPoint_1.setImageResource(R.drawable.ic_star);
            starPoint_1.setTag("1");
            mRate = 1;
        } else {
            starPoint_1.setImageResource(R.drawable.ic_star_outline);
            starPoint_2.setImageResource(R.drawable.ic_star_outline);
            starPoint_3.setImageResource(R.drawable.ic_star_outline);
            starPoint_4.setImageResource(R.drawable.ic_star_outline);
            starPoint_5.setImageResource(R.drawable.ic_star_outline);
            starPoint_1.setTag("0");
            starPoint_2.setTag("0");
            starPoint_3.setTag("0");
            starPoint_4.setTag("0");
            starPoint_5.setTag("0");
            mRate = 0;
        }
    }

    @OnClick(R.id.star_point_2)
    public void onClickStarPoint2(View view) {
        toggleCommentView(true);
        if (view.getTag().toString().equals("0")) {
            starPoint_1.setImageResource(R.drawable.ic_star);
            starPoint_2.setImageResource(R.drawable.ic_star);
            starPoint_1.setTag("1");
            starPoint_2.setTag("1");
            mRate = 2;
        } else {
            starPoint_2.setImageResource(R.drawable.ic_star_outline);
            starPoint_3.setImageResource(R.drawable.ic_star_outline);
            starPoint_4.setImageResource(R.drawable.ic_star_outline);
            starPoint_5.setImageResource(R.drawable.ic_star_outline);
            starPoint_2.setTag("0");
            starPoint_3.setTag("0");
            starPoint_4.setTag("0");
            starPoint_5.setTag("0");
            mRate = 1;
        }
    }

    @OnClick(R.id.star_point_3)
    public void onClickStarPoint3(View view) {
        toggleCommentView(true);
        if (view.getTag().toString().equals("0")) {
            starPoint_1.setImageResource(R.drawable.ic_star);
            starPoint_2.setImageResource(R.drawable.ic_star);
            starPoint_3.setImageResource(R.drawable.ic_star);
            starPoint_1.setTag("1");
            starPoint_2.setTag("1");
            starPoint_3.setTag("1");
            mRate = 3;
        } else {
            starPoint_3.setImageResource(R.drawable.ic_star_outline);
            starPoint_4.setImageResource(R.drawable.ic_star_outline);
            starPoint_5.setImageResource(R.drawable.ic_star_outline);
            starPoint_3.setTag("0");
            starPoint_4.setTag("0");
            starPoint_5.setTag("0");
            mRate = 2;
        }
    }

    @OnClick(R.id.star_point_4)
    public void onClickStarPoint4(View view) {
        toggleCommentView(true);
        if (view.getTag().toString().equals("0")) {
            starPoint_1.setImageResource(R.drawable.ic_star);
            starPoint_2.setImageResource(R.drawable.ic_star);
            starPoint_3.setImageResource(R.drawable.ic_star);
            starPoint_4.setImageResource(R.drawable.ic_star);
            starPoint_1.setTag("1");
            starPoint_2.setTag("1");
            starPoint_3.setTag("1");
            starPoint_4.setTag("1");
            mRate = 4;
        } else {
            starPoint_4.setImageResource(R.drawable.ic_star_outline);
            starPoint_5.setImageResource(R.drawable.ic_star_outline);
            starPoint_4.setTag("0");
            starPoint_5.setTag("0");
            mRate = 3;
        }
    }

    @OnClick(R.id.star_point_5)
    public void onClickStarPoint5(View view) {
        toggleCommentView(true);
        if (view.getTag().toString().equals("0")) {
            starPoint_1.setImageResource(R.drawable.ic_star);
            starPoint_2.setImageResource(R.drawable.ic_star);
            starPoint_3.setImageResource(R.drawable.ic_star);
            starPoint_4.setImageResource(R.drawable.ic_star);
            starPoint_5.setImageResource(R.drawable.ic_star);
            starPoint_1.setTag("1");
            starPoint_2.setTag("1");
            starPoint_3.setTag("1");
            starPoint_4.setTag("1");
            starPoint_5.setTag("1");
            mRate = 5;
        } else {
            starPoint_5.setImageResource(R.drawable.ic_star_outline);
            starPoint_5.setTag("0");
            mRate = 4;
        }
    }

    private void toggleCommentView(boolean flag){
        if (flag) mCommentContainer.setVisibility(View.VISIBLE);
        else mCommentContainer.setVisibility(View.GONE);
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
        scrollView.setOnScrollChangeListener(new EndlessParentScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (lastID != null) {
                    isOnLoadMoreProduct = true;
                    performReviewsRequest(mItem.getProduct().getId(),lastID+"",TAKE_CONSTANT);
                }


            }
        });
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);

        mProductReviewRecycleAdapter = new ProductReviewsRecycleAdapter(getActivity(), new ArrayList<UserReview>());
        mRecyclerView.setAdapter(mProductReviewRecycleAdapter);
    }

    private void clearAdapters(){
        mEnchantedPagerAdapter.updateData(null,1);
        mProductReviewRecycleAdapter.updateData(null,1);
        productDetailHorizontalItems.clear();
        pageProduct = 0;
        lastID = "0";
    }



    private void setData(){

       if (mItem != null) {
           if (mItem.getProduct() != null ) performReviewsRequest(mItem.getProduct().getId(), pageProduct + "", TAKE_CONSTANT);

           if (mItem.getProduct().getImgs().get(0) != null)
               setProductImage(mItem.getProduct().getImgs().get(0), mainImage, imageProgress);
           if (mItem.getProduct().getName().getEn() != null)
               sakeName.setText(mItem.getProduct().getName().getEn());
           if (mItem.getAvgRate() != null && !mItem.getAvgRate().isEmpty())
               valueRating.setText(mItem.getAvgRate() + " " + getResources().getString(R.string.rating));
           if (mItem.getAvgRate() != null && !mItem.getAvgRate().isEmpty())
               setAverageProductRate(mItem.getAvgRate());
           if (mItem.getUserRate() != null && !mItem.getUserRate().isEmpty())
               setUserRate(mItem.getUserRate());
           if (mItem.getUserFavourite() != null) {
               if (mItem.getUserFavourite().equals("true"))
                   userFavourite.setImageResource(R.drawable.ic_like_fill);
               else userFavourite.setImageResource(R.drawable.ic_like);
           }

           ProductDetailHorizontalItem productDetailHorizontalItem = new ProductDetailHorizontalItem();
           productDetailHorizontalItem.setTypeAdd(false);
           if (mItem.getProduct().getAlcPerc() != null)
               productDetailHorizontalItem.setAlcoholContent(mItem.getProduct().getAlcPerc());
           if (mItem.getCountryName() != null)
               productDetailHorizontalItem.setCountry(mItem.getCountryName().getEn());
           if (mItem.getRegionName() != null)
               productDetailHorizontalItem.setRegion(mItem.getRegionName().getEn());
           if (mItem.getProduct().getSmv() != null)
               productDetailHorizontalItem.setSmv(mItem.getProduct().getSmv());
           if (mItem.getProduct().getBrewYear() != null)
               productDetailHorizontalItem.setBrewingYear(mItem.getProduct().getBrewYear());
           if (mItem.getProduct().getPressAndSqueeze() != null)
               productDetailHorizontalItem.setPressSqueez(mItem.getProduct().getPressAndSqueeze());
           if (mItem.getProduct().getKakeRatio() != null)
               productDetailHorizontalItem.setPolishRate(mItem.getProduct().getKakeRatio());
           if (mItem.getProduct().getPastorizeTankStorage() != null)
               productDetailHorizontalItem.setStorageType(mItem.getProduct().getPastorizeTankStorage());

           productDetailHorizontalItems.add(productDetailHorizontalItem);
           mEnchantedPagerAdapter.updateData(productDetailHorizontalItems, 0);
           mViewPager.setCurrentItem(productDetailHorizontalItems.size() - 1, true);
       }
    }

    public void setHorizontalLoading(boolean isLoading) {
        if (horizontalProgress != null) {
            horizontalProgress.bringToFront();
            horizontalProgress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void showProductDetailResponse(ProductDetailResponse productDetailResponse) {

    }

    public void performProductFacadeRequest(String productId) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setSwipeLayoutLoading(true);
            ((ProductPresenter) presenter).getProductFacade(productId);
        } else {
            setSwipeLayoutLoading(false);
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        }
    }

    @Override
    public void showProductFacadeResponse(ProductFacadeResponse productFacadeResponse) {
        setSwipeLayoutLoading(false);
        if (productFacadeResponse.isSuccess()) {
            mItem = productFacadeResponse;
            setData();
        } else {
            if (productFacadeResponse.isAPIError()) {
                showTopSnackBar(productFacadeResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            } else {
                showTopSnackBar(productFacadeResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            }
        }
    }

    public void performReviewsRequest(String productId , String lastId, int size) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            if ( isOnLoadMoreProduct ) {
                setHorizontalLoading(true);
            } else {
                setProgressDialog(true);
            }
            ((ProductPresenter) presenter).getReviews(productId,lastId, size);
        } else {
            setSwipeLayoutLoading(false);
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        }
    }

    @Override
    public void showReviewsResponse(ReviewResponse reviewResponse) {
        if ( isOnLoadMoreProduct ) {
            setHorizontalLoading(false);
            isOnLoadMoreProduct = false;
        } else {
            setProgressDialog(false);
        }
        if (reviewResponse.isSuccess()) {
            if (reviewResponse.getReviews() != null && reviewResponse.getReviews().size() > 0) {
                lastID = reviewResponse.getLastId();
                mProductReviewRecycleAdapter.updateData(reviewResponse.getReviews(),0);
            }
            System.out.println("=======================++>>>>> Success ");
        } else {
            if (reviewResponse.isAPIError()) {
                showTopSnackBar(reviewResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            } else {
                showTopSnackBar(reviewResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            }
        }
    }

    public void performReviewProductRequest(String productId ,ReviewForProductRequest reviewForProductRequest) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            ((ProductPresenter) presenter).doReviewProduct(productId, reviewForProductRequest);
        } else {
            setSwipeLayoutLoading(false);
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        }
    }

    @Override
    public void showReviewProductResponse(ReviewForProductResponse reviewForProductResponse) {
        setProgressDialog(false);
        if (reviewForProductResponse.isSuccess()) {
            System.out.println("=======================++>>>>> Success ");
            if (mItem.getProduct() != null ) performProductFacadeRequest(mItem.getProduct().getId());
        } else {
            if (reviewForProductResponse.isAPIError()) {
                showTopSnackBar(reviewForProductResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            } else {
                showTopSnackBar(reviewForProductResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            }
        }
    }

    private void setAverageProductRate(String rate) {
            switch (rate) {
                case "1.0" :
                    ratingStarPoint_1.setImageResource(R.drawable.ic_star);
                    break;
                case "2.0" :
                    ratingStarPoint_1.setImageResource(R.drawable.ic_star);
                    ratingStarPoint_2.setImageResource(R.drawable.ic_star);
                    break;
                case "3.0" :
                    ratingStarPoint_1.setImageResource(R.drawable.ic_star);
                    ratingStarPoint_2.setImageResource(R.drawable.ic_star);
                    ratingStarPoint_3.setImageResource(R.drawable.ic_star);
                    break;
                case "4.0" :
                    ratingStarPoint_1.setImageResource(R.drawable.ic_star);
                    ratingStarPoint_2.setImageResource(R.drawable.ic_star);
                    ratingStarPoint_3.setImageResource(R.drawable.ic_star);
                    ratingStarPoint_4.setImageResource(R.drawable.ic_star);
                    break;
                case "5.0" :
                    ratingStarPoint_1.setImageResource(R.drawable.ic_star);
                    ratingStarPoint_2.setImageResource(R.drawable.ic_star);
                    ratingStarPoint_3.setImageResource(R.drawable.ic_star);
                    ratingStarPoint_4.setImageResource(R.drawable.ic_star);
                    ratingStarPoint_5.setImageResource(R.drawable.ic_star);
                    break;
                default:
                    break;
            }
    }

    private void setUserRate(String rate) {
        switch (rate) {
            case "1.0" :
                starPoint_1.setImageResource(R.drawable.ic_star);
                break;
            case "2.0" :
                starPoint_1.setImageResource(R.drawable.ic_star);
                starPoint_2.setImageResource(R.drawable.ic_star);
                break;
            case "3.0" :
                starPoint_1.setImageResource(R.drawable.ic_star);
                starPoint_2.setImageResource(R.drawable.ic_star);
                starPoint_3.setImageResource(R.drawable.ic_star);
                break;
            case "4.0" :
                starPoint_1.setImageResource(R.drawable.ic_star);
                starPoint_2.setImageResource(R.drawable.ic_star);
                starPoint_3.setImageResource(R.drawable.ic_star);
                starPoint_4.setImageResource(R.drawable.ic_star);
                break;
            case "5.0" :
                starPoint_1.setImageResource(R.drawable.ic_star);
                starPoint_2.setImageResource(R.drawable.ic_star);
                starPoint_3.setImageResource(R.drawable.ic_star);
                starPoint_4.setImageResource(R.drawable.ic_star);
                starPoint_5.setImageResource(R.drawable.ic_star);
                break;
            default:
                break;
        }
    }

    private void setProductImage(String imageUrl, ImageView productImage, final ProgressBar imageProgress) {
        if(imageUrl != null && !imageUrl.isEmpty()){
            ImageLoader.getInstance().displayImage(DomainConstants.DEFAULT_IMAGE_URL + imageUrl, productImage, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    if (imageProgress != null) imageProgress.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    if (imageProgress != null) imageProgress.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if (imageProgress != null) imageProgress.setVisibility(View.GONE);
                }
            });
        } else {
            if (imageProgress != null) imageProgress.setVisibility(View.GONE);
        }

    }

    @Override
    public void showLikeReviewResponse(ReviewForProductResponse reviewForProductResponse) { }

    @Override
    public void showUnFavourProductResponse(ReviewForProductResponse reviewForProductResponse) { }

    @Override
    public void showFavourProductResponse(ReviewForProductResponse reviewForProductResponse) { }

    @Override
    public void showUnknownSakeResponse(UnknownSakeResponse unknownSakeResponse) { }

    @Override
    public void showGetFavouriteSakeResponse(FavouriteSakeResponse favouriteSakeResponse) {

    }
}