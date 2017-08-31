package com.sakewiz.android.ui.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.auth.UserRecoverableNotifiedException;
import com.sakewiz.android.R;
import com.sakewiz.android.common.CommonUtils;
import com.sakewiz.android.common.constants.ApplicationConstants;
import com.sakewiz.android.domain.UserDashBoardService;
import com.sakewiz.android.domain.UserDashBoardServiceImpl;
import com.sakewiz.android.dto.NotificationItem;
import com.sakewiz.android.model.entities.response.BarFacadeResponse;
import com.sakewiz.android.model.entities.response.BreweryFacadeResponse;
import com.sakewiz.android.model.entities.response.FavouriteSakeResponse;
import com.sakewiz.android.model.entities.response.NotificationResponse;
import com.sakewiz.android.model.entities.response.UserDashboardResponse;
import com.sakewiz.android.model.rest.SakeWizService;
import com.sakewiz.android.mvp.presenters.UserDashBoardPresenter;
import com.sakewiz.android.mvp.presenters.UserDashBoardPresenterImpl;
import com.sakewiz.android.mvp.views.UserDashBoardView;
import com.sakewiz.android.ui.adapters.NewsFeedRecyclerAdapter;
import com.sakewiz.android.utils.AppScheduler;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dilshan_e on 08/06/2017.
 */
public class NewsFeedFragment extends BaseFragment implements UserDashBoardView{

    final String TAG = NewsFeedFragment.this.getClass().getSimpleName();

    public static String getTAG() {
        return "NewsFeedFragment";
    }

    public static NewsFeedFragment newInstance() {
        return new NewsFeedFragment();
    }

    private NewsFeedRecyclerAdapter mNewsFeedRecyclerAdapter;

    private String lastNotificationId = "";

    private int pageSize = 10;

    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.feed_recycler_view) RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        try {
            rootView = inflater.inflate(R.layout.fragment_newsfeed, container, false);
            Log.d(TAG, "onCreateView");
            ButterKnife.bind(this, rootView);
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: " + e.toString());
        }
        return rootView;
    }

    @Override
    public void initializePresenter() {
        UserDashBoardService mUserDashBoardService = new UserDashBoardServiceImpl(new SakeWizService());
        presenter = new UserDashBoardPresenterImpl(getActivity(), mUserDashBoardService, new AppScheduler());
        presenter.attachView(NewsFeedFragment.this);
        presenter.onCreate();
    }

    @Override
    protected void setUpUI() {
        try {
            initRecyclerView();
            performRequestGetNotifications();

            int[] colors = getResources().getIntArray(R.array.google_colors);
            mSwipeRefreshLayout.setColorSchemeColors(colors);

            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    setSwipeLayoutLoading(true);
//                    isPulltoRefreshCall = true;
                    performRequestGetNotifications();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "setUpUI: " + e.toString());
        }
    }

    @Override
    protected void setUpToolBar() {
        View mCustomView = getActivity().getLayoutInflater().inflate(R.layout.custom_actionbar_with_back, null);
        TextView toolBarTitle = (TextView) mCustomView.findViewById(R.id.title_text);
        toolBarTitle.setText(getString(R.string.menu_item_newsfeed));
        mCustomView.findViewById(R.id.imgVmain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        mToolBar.addView(mCustomView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    protected void initRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mNewsFeedRecyclerAdapter = new NewsFeedRecyclerAdapter(getActivity(), new ArrayList<NotificationItem>(), this);
        mRecyclerView.setAdapter(mNewsFeedRecyclerAdapter);
    }

    private void performRequestGetNotifications() {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            ((UserDashBoardPresenter) presenter).getUserNotifications(pageSize, lastNotificationId);
        } else {
            setSwipeLayoutLoading(false);
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        }
    }

    @Override
    public void showUserNotificationsResponse(NotificationResponse notificationResponse) {
        setProgressDialog(false);
        setSwipeLayoutLoading(false);
        if (notificationResponse.isSuccess()) {
            mNewsFeedRecyclerAdapter.updateData(notificationResponse.getNotifications(), 0);
        } else {
            if (notificationResponse.isAPIError()) {
                showTopSnackBar(notificationResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            } else {
                showTopSnackBar(notificationResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            }
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

    @Override
    public void showGetBarFacadeResponse(BarFacadeResponse barFacadeResponse) { }

    @Override
    public void showGetBreweryFacadeResponse(BreweryFacadeResponse breweryFacadeResponse) { }

    @Override
    public void showGetUserDashboardResponse(UserDashboardResponse userDashboardResponse) { }
}
