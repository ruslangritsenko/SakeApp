package com.sakewiz.android.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
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

/**
 * Created by dilshan_e on 6/10/17.
 */

public class SearchFragment extends BaseFragment {
    final String TAG = SearchFragment.this.getClass().getSimpleName();

    public static String getTAG() {
        return "SearchFragment";
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    public static SearchFragment searchFragment = null;

    private SearchView searchView;
    private TextView mTitle;
    private int textStrings[] = new int[3];
    private int iconSelectors[] = new int[3];
    private int[] tabIcons = {
            R.drawable.ic_green_bottle,
            R.drawable.ic_star_default,
            R.drawable.ic_location_gray
    };

    @Bind(R.id.coordinator_content) CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.viewpager) ViewPager mViewPager;
    @Bind(R.id.tabs) TabLayout mTabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        try {
            rootView = inflater.inflate(R.layout.fragment_search, container, false);
            Log.d(TAG, "onCreateView");
            ButterKnife.bind(this, rootView);
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: " + e.toString());
        }
        this.searchFragment = this;
        return rootView;
    }

    @Override
    public void initializePresenter() {
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

    @Override
    protected void setUpToolBar() {

        View mCustomView = getActivity().getLayoutInflater().inflate(R.layout.custom_actionbar_with_back, null);
        TextView toolBarTitle = (TextView) mCustomView.findViewById(R.id.title_text);
        toolBarTitle.setText(getString(R.string.fragment_search));
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
        searchFragment = null;
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (BaseFragment.myAlertDialog != null && BaseFragment.myAlertDialog.isShowing())
            BaseFragment.myAlertDialog.dismiss();
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter viewPagerAdapter = new Adapter(getChildFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        viewPagerAdapter.addFragment(new SearchSakeFragment(), getString(R.string.fragment_search_sake));
        iconSelectors[0] = R.drawable.tab_sake_selector;
        textStrings[0] = R.string.fragment_search_sake;

        viewPagerAdapter.addFragment(new SearchBreweriesFragment(), getString(R.string.fragment_search_breweries));
        iconSelectors[1] = R.drawable.tab_breweries_selector;
        textStrings[1] = R.string.fragment_search_breweries;

        viewPagerAdapter.addFragment(new SearchPlacesFragment(), getString(R.string.fragment_search_places));
        iconSelectors[2] = R.drawable.tab_place_selector;
        textStrings[2] = R.string.fragment_search_places;

        viewPagerAdapter.notifyDataSetChanged();
    }

    private void setupTabIcons() {
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.tab_custom_view, null);
            if (i == 0) view.setSelected(true); // initially select the first tab view
            ((ImageView) view.findViewById(R.id.tab_icon)).setImageResource(iconSelectors[i]);
            ((TextView) view.findViewById(R.id.tab_text)).setText(textStrings[i]);
            if (i == 2) view.findViewById(R.id.tab_divider).setVisibility(View.GONE);
            mTabLayout.getTabAt(i).setCustomView(view);
        }
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
