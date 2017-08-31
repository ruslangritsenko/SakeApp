package com.sakewiz.android.ui.fragments;

import android.os.Bundle;
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
import android.widget.SearchView;
import android.widget.TextView;

import com.sakewiz.android.R;
import com.sakewiz.android.ui.activities.MainActivity;
import com.sakewiz.android.utils.BaseBackPressedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dilshan_e on 6/14/17.
 */
public class FilterMainFragment extends BaseFragment implements BaseBackPressedListener.OnBackPressedListener{
    final String TAG = FilterMainFragment.this.getClass().getSimpleName();

    public static String getTAG() {
        return "FilterMainFragment";
    }

    private static String BUNDLE_EXTRA = "BUNDLE_EXTRA";

    public static FilterMainFragment newInstance() {
        return new FilterMainFragment();
    }

    public static FilterMainFragment mFilterMainFragment = null;

    public static FilterMainFragment newInstance(String fragmentFrom) {
        FilterMainFragment fragment = new FilterMainFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_EXTRA, fragmentFrom);
        fragment.setArguments(args);
        return fragment;
    }

    private SearchView searchView;
    private TextView mTitle;
    private int textStrings[] = new int[3];
    private int iconSelectors[] = new int[3];
    private String mFragmentFrom;

    @Bind(R.id.coordinator_content) CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.viewpager) ViewPager mViewPager;
    @Bind(R.id.tabs) TabLayout mTabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFragmentFrom = getArguments().getString(BUNDLE_EXTRA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        try {
            rootView = inflater.inflate(R.layout.fragment_filter_main, container, false);
            Log.d(TAG, "onCreateView");
            ButterKnife.bind(this, rootView);
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: " + e.toString());
        }
        this.mFilterMainFragment = this;
        return rootView;
    }

    @Override
    public void initializePresenter() {
    }

    @Override
    protected void setUpUI() {
        ((MainActivity) getActivity()).setOnBackPressedListener(this);
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
    }

    @Override
    public void onDestroyView() {
        ((MainActivity) getActivity()).setOnBackPressedListener(null);
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (BaseFragment.myAlertDialog != null && BaseFragment.myAlertDialog.isShowing())
            BaseFragment.myAlertDialog.dismiss();
    }

    @OnClick(R.id.coordinator_content)
    public void onClickMainContainer(View view){
        dismissFragment();
    }

    public void dismissFragment(){
        switch (mFragmentFrom){
            case  "SearchSakeFragment"      :
//                if (SearchSakeFragment.searchSakeFragment != null)
//                    SearchSakeFragment.searchSakeFragment.updateTagView();
                break;
            case  "SearchPlacesFragment"    :
//                if (SearchPlacesFragment.searchPlacesFragment != null)
//                    SearchPlacesFragment.searchPlacesFragment.updateTagView();
                break;
            case  "SearchBreweriesFragment" :
                break;
            default                         :
                break;

        }
        getFragmentManager().popBackStack();
    }

    private void setupViewPager(ViewPager viewPager) {
        FilterMainFragment.Adapter viewPagerAdapter = new FilterMainFragment.Adapter(getChildFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        switch (mFragmentFrom){
            case  "SearchSakeFragment"      :
                viewPagerAdapter.addFragment(new FragmentLocation().newInstance(mFragmentFrom), getString(R.string.fragment_location));
                textStrings[0] = R.string.fragment_location;

                viewPagerAdapter.addFragment(new FragmentSakeType().newInstance(mFragmentFrom), getString(R.string.fragment_sake_type));
                textStrings[1] = R.string.fragment_sake_type;
                break;
            case  "SearchPlacesFragment"    :
                viewPagerAdapter.addFragment(new FragmentRadius().newInstance(mFragmentFrom), getString(R.string.fragment_radius));
                textStrings[0] = R.string.fragment_radius;
//
//                viewPagerAdapter.addFragment(new FragmentLocation().newInstance(mFragmentFrom), getString(R.string.fragment_location));
//                textStrings[1] = R.string.fragment_location;
//
//                viewPagerAdapter.addFragment(new FragmentSakeType().newInstance(mFragmentFrom), getString(R.string.fragment_sake_type));
//                textStrings[2] = R.string.fragment_sake_type;
                break;
            case  "SearchBreweriesFragment" :
                viewPagerAdapter.addFragment(new FragmentRadius().newInstance(mFragmentFrom), getString(R.string.fragment_radius));
                textStrings[0] = R.string.fragment_radius;
//
//                viewPagerAdapter.addFragment(new FragmentLocation().newInstance(mFragmentFrom), getString(R.string.fragment_location));
//                textStrings[1] = R.string.fragment_location;
//
//                viewPagerAdapter.addFragment(new FragmentSakeType().newInstance(mFragmentFrom), getString(R.string.fragment_sake_type));
//                textStrings[2] = R.string.fragment_sake_type;
                break;
            default                         :
                break;

        }

        viewPagerAdapter.notifyDataSetChanged();
    }

    private void setupTabIcons() {
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.filter_tab_custom_view, null);
            if (i == 0) view.setSelected(true); // initially select the first tab view
            ((TextView) view.findViewById(R.id.tab_text)).setText(textStrings[i]);
            if (i == mTabLayout.getTabCount() - 1) view.findViewById(R.id.tab_divider).setVisibility(View.GONE);
            mTabLayout.getTabAt(i).setCustomView(view);
        }
    }

    @Override
    public void doBack() {
        dismissFragment();
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
