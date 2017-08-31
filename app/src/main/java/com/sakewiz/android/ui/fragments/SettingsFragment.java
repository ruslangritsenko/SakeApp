package com.sakewiz.android.ui.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sakewiz.android.R;
import com.sakewiz.android.dto.SettingsItem;
import com.sakewiz.android.ui.adapters.SettingOptionsRecyclerAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dilshan_e on 08/06/2017.
 */
public class SettingsFragment extends BaseFragment {

    final String TAG = SettingsFragment.this.getClass().getSimpleName();

    public static String getTAG() {
        return "SettingsFragment";
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    public SettingsFragment() {
    }

    private SettingOptionsRecyclerAdapter mSettingOptionsRecyclerAdapter;

    @Bind(R.id.main_container) NestedScrollView mNestedScrollView;
    @Bind(R.id.settings_recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.user_img_container) RelativeLayout mUserImageContainer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        try {
            rootView = inflater.inflate(R.layout.fragment_settings, container, false);
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
        try {
            initRecyclerView();
            setSettingsItems();
            mUserImageContainer.requestFocus();
        } catch (Exception e) {
            Log.e(TAG, "setUpUI: " + e.toString());
        }
    }

    @Override
    protected void setUpToolBar() {
        View mCustomView = getActivity().getLayoutInflater().inflate(R.layout.custom_actionbar_with_back, null);
        TextView toolBarTitle = (TextView) mCustomView.findViewById(R.id.title_text);
        toolBarTitle.setText(getString(R.string.menu_item_settings));
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
        mRecyclerView.setNestedScrollingEnabled(false);

        mSettingOptionsRecyclerAdapter = new SettingOptionsRecyclerAdapter(getActivity(), new ArrayList<SettingsItem>(), this);
        mRecyclerView.setAdapter(mSettingOptionsRecyclerAdapter);
    }

    public void setSettingsItems() {
        try {
            ArrayList<SettingsItem> item = new ArrayList<>();
            SettingsItem settingsItemHeader1 = new SettingsItem();
            settingsItemHeader1.setItemCategory("header");
            settingsItemHeader1.setItemName(getResources().getString(R.string.review_settings));
            settingsItemHeader1.setSetDivider(false);
            item.add(settingsItemHeader1);

            SettingsItem settingsItem1 = new SettingsItem();
            settingsItem1.setItemCategory("item");
            settingsItem1.setItemName(getResources().getString(R.string.only_show_comments_in_my_language));
            settingsItem1.setSetDivider(true);
            item.add(settingsItem1);

            SettingsItem settingsItem2 = new SettingsItem();
            settingsItem2.setItemCategory("item");
            settingsItem2.setItemName(getResources().getString(R.string.notify_me_someone_likes_my_comment));
            settingsItem2.setSetDivider(false);
            item.add(settingsItem2);

            SettingsItem settingsItemHeader2 = new SettingsItem();
            settingsItemHeader2.setItemCategory("header");
            settingsItemHeader2.setItemName(getResources().getString(R.string.label_settings));
            settingsItemHeader2.setSetDivider(false);
            item.add(settingsItemHeader2);

            SettingsItem settingsItem3 = new SettingsItem();
            settingsItem3.setItemCategory("item");
            settingsItem3.setItemName(getResources().getString(R.string.save_labels_to_local_storage));
            settingsItem3.setSetDivider(true);
            item.add(settingsItem3);

            SettingsItem settingsItemHeader3 = new SettingsItem();
            settingsItemHeader3.setItemCategory("header");
            settingsItemHeader3.setItemName(getResources().getString(R.string.review_settings));
            settingsItemHeader3.setSetDivider(false);
            item.add(settingsItemHeader3);

            SettingsItem settingsItem4 = new SettingsItem();
            settingsItem4.setItemCategory("item");
            settingsItem4.setItemName(getResources().getString(R.string.notify_me_someone_likes_my_content));
            settingsItem4.setSetDivider(true);
            item.add(settingsItem4);

            SettingsItem settingsItem5 = new SettingsItem();
            settingsItem5.setItemCategory("item");
            settingsItem5.setItemName(getResources().getString(R.string.notify_me_on_new_events));
            settingsItem5.setSetDivider(true);
            item.add(settingsItem5);

            SettingsItem settingsItem6 = new SettingsItem();
            settingsItem6.setItemCategory("item");
            settingsItem6.setItemName(getResources().getString(R.string.notify_me_on_exclusive_sakewiz_deals));
            settingsItem6.setSetDivider(true);
            item.add(settingsItem6);

            SettingsItem settingsItemHeader4 = new SettingsItem();
            settingsItemHeader4.setItemCategory("rate");
            settingsItemHeader4.setItemName(getResources().getString(R.string.rate_reminder));
            settingsItemHeader4.setSetDivider(true);
            item.add(settingsItemHeader4);

            mSettingOptionsRecyclerAdapter.updateData(item, 0); // append

        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "setSettingsItems: " + e.toString());
        }
    }
}
