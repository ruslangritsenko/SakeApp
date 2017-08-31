package com.sakewiz.android.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sakewiz.android.R;
import com.sakewiz.android.dto.NotificationItem;
import com.sakewiz.android.dto.UserReview;
import com.sakewiz.android.ui.activities.MainActivity;
import com.sakewiz.android.ui.adapters.FriendsListRecycleAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dilshan_e on 08/06/2017.
 */
public class FollowersFragment extends BaseFragment {

    final String TAG = FollowersFragment.this.getClass().getSimpleName();

    public static String getTAG() {
        return "FollowersFragment";
    }

    public static FollowersFragment newInstance() {
        return new FollowersFragment();
    }

    private FriendsListRecycleAdapter mFriendsListRecyclerAdapter;

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        try {
            rootView = inflater.inflate(R.layout.fragment_followers, container, false);
            Log.d(TAG, "onCreateView");
            ButterKnife.bind(this, rootView);
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: " + e.toString());
        }
        return rootView;
    }

    @Override
    public void initializePresenter() {

    }

    @Override
    protected void setUpUI() {
        initRecyclerView();
    }

    @Override
    protected void setUpToolBar() {
        View mCustomView = getActivity().getLayoutInflater().inflate(R.layout.custom_actionbar_with_back, null);
        TextView toolBarTitle = (TextView) mCustomView.findViewById(R.id.title_text);
        toolBarTitle.setText(getString(R.string.friends));
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

        mFriendsListRecyclerAdapter = new FriendsListRecycleAdapter(getActivity(), new ArrayList<UserReview>());
        mRecyclerView.setAdapter(mFriendsListRecyclerAdapter);


        setData();
    }

    @OnClick(R.id.qr_code_container)
    public void onClickQrCodeContainer(View view) {
        ((MainActivity) getActivity()).addFragment(new MyQRCodeFragment(), MyQRCodeFragment.getTAG(), 2);
    }

    @OnClick(R.id.invite_by_username_container)
    public void onClickInviteFriendsContainer(View view) {
        ((MainActivity) getActivity()).addFragment(new SearchUserByNameFragment(), SearchUserByNameFragment.getTAG(), 2);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    // used to set test data
    private void setData() {

        ArrayList<UserReview> userList = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            UserReview rw = new UserReview();
            rw.setUserHandle("abc");
            userList.add(rw);
        }
        mFriendsListRecyclerAdapter.updateData(userList, 0);

    }
}
