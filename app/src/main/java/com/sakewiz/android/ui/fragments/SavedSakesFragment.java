package com.sakewiz.android.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sakewiz.android.R;
import com.sakewiz.android.common.CommonUtils;
import com.sakewiz.android.common.constants.ApplicationConstants;
import com.sakewiz.android.domain.ProductService;
import com.sakewiz.android.domain.ProductServiceImpl;
import com.sakewiz.android.model.entities.response.FavouriteSakeResponse;
import com.sakewiz.android.model.entities.response.ReviewForProductResponse;
import com.sakewiz.android.dto.UnknownSakeItem;
import com.sakewiz.android.model.entities.response.ProductDetailResponse;
import com.sakewiz.android.model.entities.response.ProductFacadeResponse;
import com.sakewiz.android.model.entities.response.ReviewResponse;
import com.sakewiz.android.model.entities.response.UnknownSakeResponse;
import com.sakewiz.android.model.rest.SakeWizService;
import com.sakewiz.android.mvp.presenters.ProductPresenter;
import com.sakewiz.android.mvp.presenters.ProductPresenterImpl;
import com.sakewiz.android.mvp.views.ProductView;
import com.sakewiz.android.ui.adapters.UnknownSakeListRecycleAdapter;
import com.sakewiz.android.utils.AppScheduler;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dilshan_e on 08/06/2017.
 */
public class SavedSakesFragment extends BaseFragment implements ProductView {

    final String TAG = SavedSakesFragment.this.getClass().getSimpleName();

    public static String getTAG() {
        return "SavedSakesFragment";
    }

    public static SavedSakesFragment newInstance() {
        return new SavedSakesFragment();
    }

    private UnknownSakeListRecycleAdapter mUnknownSakeListRecycleAdapter;

    private String lastId = "";

    private int pageSize = 10;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        try {
            rootView = inflater.inflate(R.layout.fragment_saved_sakes, container, false);
            Log.d(TAG, "onCreateView");
            ButterKnife.bind(this, rootView);
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: " + e.toString());
        }
        return rootView;
    }

    @Override
    public void initializePresenter() {
        ProductService mUserDashBoardService = new ProductServiceImpl(new SakeWizService());
        presenter = new ProductPresenterImpl(getActivity(), mUserDashBoardService, new AppScheduler());
        presenter.attachView(SavedSakesFragment.this);
        presenter.onCreate();

    }

    @Override
    protected void setUpUI() {
        initRecyclerView();
        performRequestGetUnknownSake();
    }

    @Override
    protected void setUpToolBar() {
        View mCustomView = getActivity().getLayoutInflater().inflate(R.layout.custom_actionbar_with_back, null);
        TextView toolBarTitle = (TextView) mCustomView.findViewById(R.id.title_text);
        toolBarTitle.setText(getString(R.string.menu_item_unknown_sake));
        mCustomView.findViewById(R.id.imgVmain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        mToolBar.addView(mCustomView);
    }

    protected void initRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        mUnknownSakeListRecycleAdapter = new UnknownSakeListRecycleAdapter(getActivity(), new ArrayList<UnknownSakeItem>());
        mRecyclerView.setAdapter(mUnknownSakeListRecycleAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void performRequestGetUnknownSake() {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            ((ProductPresenter) presenter).getUnknownSakeList(pageSize, lastId);
        } else {
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        }
    }

    @Override
    public void showUnknownSakeResponse(UnknownSakeResponse unknownSakeResponse) {
        setProgressDialog(false);
        if (unknownSakeResponse.isSuccess()) {
            mUnknownSakeListRecycleAdapter.updateData(unknownSakeResponse.getResults(), 0);
        } else {
            if (unknownSakeResponse.isAPIError()) {
                showTopSnackBar(unknownSakeResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            } else {
                showTopSnackBar(unknownSakeResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            }
        }
    }

    @Override
    public void showGetFavouriteSakeResponse(FavouriteSakeResponse favouriteSakeResponse) {

    }

    @Override
    public void showProductDetailResponse(ProductDetailResponse productDetailResponse) { }

    @Override
    public void showProductFacadeResponse(ProductFacadeResponse productFacadeResponse) { }

    @Override
    public void showReviewsResponse(ReviewResponse reviewResponse) { }

    @Override
    public void showReviewProductResponse(ReviewForProductResponse reviewForProductResponse) { }

    @Override
    public void showLikeReviewResponse(ReviewForProductResponse reviewForProductResponse) { }

    @Override
    public void showUnFavourProductResponse(ReviewForProductResponse reviewForProductResponse) { }

    @Override
    public void showFavourProductResponse(ReviewForProductResponse reviewForProductResponse) {

    }
}
