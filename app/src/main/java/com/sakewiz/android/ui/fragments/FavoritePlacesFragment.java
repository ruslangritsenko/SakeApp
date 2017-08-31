package com.sakewiz.android.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.sakewiz.android.R;
import com.sakewiz.android.ui.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FavoritePlacesFragment extends BaseFragment {

    final String TAG = FavoritePlacesFragment.this.getClass().getSimpleName();
    public static String getTAG() {
        return "FavoritePlacesFragment";
    }
    private static String BUNDLE_EXTRA = "BUNDLE_EXTRA";

    public static FavoritePlacesFragment newInstance() {
        return new FavoritePlacesFragment();
    }
    public static FavoritePlacesFragment favoritePlacesFragment = null;

    @Bind(R.id.coordinator_content) CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.viewpager) ViewPager mViewPager;
    @Bind(R.id.tabs) TabLayout mTabLayout;

    private int textStrings[] = new int[2];

    private SearchView searchView;
    private TextView mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_favorite_places, container, false);
        ButterKnife.bind(this, rootView);
        this.favoritePlacesFragment = this;
        return rootView;
    }

    @Override
    protected void initializePresenter() {

    }

    @Override
    protected void setUpUI() {
        setupViewPager(mViewPager);
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    mTabLayout.setupWithViewPager(mViewPager);
                    setupTabIcons();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

        mViewPager.clearOnPageChangeListeners();
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout) {
            @Override
            public void onPageSelected(int position) {
                //super.onPageSelected(position);
            }
        });
    }

    private void setupTabIcons() {
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.tab_custom_view, null);
            if (i == 0) view.setSelected(true); // initially select the first tab view
            ((TextView) view.findViewById(R.id.tab_text)).setText(textStrings[i]);
            if (i == 2) view.findViewById(R.id.tab_divider).setVisibility(View.GONE);
            mTabLayout.getTabAt(i).setCustomView(view);
        }
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
        mTitle.setText(R.string.fragment_favorite_places);
        mTitle.setAllCaps(true);
        mNotificationCount.setVisibility(View.GONE);
        ((MainActivity) getActivity()).setUpSearchBar(searchView, null);
    }

    @Override
    public void onDestroyView() {
        favoritePlacesFragment = null;
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (BaseFragment.myAlertDialog != null && BaseFragment.myAlertDialog.isShowing())
            BaseFragment.myAlertDialog.dismiss();
    }

    private void setupViewPager(ViewPager viewPager) {
        FavoritePlacesFragment.Adapter viewPagerAdapter = new FavoritePlacesFragment.Adapter(getChildFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        viewPagerAdapter.addFragment(new FavoritePlacesBarsFragment(), getString(R.string.fav_places_bars_title));
        textStrings[0] = R.string.fav_places_bars_title;

        viewPagerAdapter.addFragment(new FavoritePlacesBreweriesFragment(), getString(R.string.fav_places_breweries_title));
        textStrings[1] = R.string.fav_places_breweries_title;

        viewPagerAdapter.notifyDataSetChanged();
    }

    private static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }


}
