package com.sakewiz.android.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.sakewiz.android.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.refactor.library.SmoothCheckBox;

/**
 * Created by dilshan_e on 6/15/17.
 */

public class SortByFragment extends BaseFragment {

    final String TAG = SortByFragment.this.getClass().getSimpleName();

    public static String getTAG() {
        return "SortByFragment";
    }

    private static String BUNDLE_EXTRA = "BUNDLE_EXTRA";
    private static int REP_DELAY = 50;
    private static String CODES = "codes";
    public static SortByFragment sortByFragment;
    private String mFragmentFrom;

    public static SortByFragment newInstance(String fragmentFrom) {
        SortByFragment fragment = new SortByFragment();
        Bundle args = new Bundle();
       args.putString(BUNDLE_EXTRA, fragmentFrom);
        fragment.setArguments(args);
        return fragment;
    }

    @Bind(R.id.highest_rated_check) SmoothCheckBox mHighestRatedCheck;
    @Bind(R.id.favoured_check) SmoothCheckBox mFavouredCheck;
    @Bind(R.id.review_check) SmoothCheckBox mReviewCheck;
//    @Bind(R.id.least_commented_check) SmoothCheckBox mLeastCommentedCheck;
    @Bind(R.id.highest_rated_text) TextView mHighestRatedText;
    @Bind(R.id.favoured_text) TextView mFavouredText;
    @Bind(R.id.review_text) TextView mReviewText;
//    @Bind(R.id.least_commented_text) TextView mLeastCommentedText;

    private Fragment mFragment;
    private Activity mActivity;
    private String mSelectedSortType;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
             mFragmentFrom = getArguments().getString(BUNDLE_EXTRA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        try {
            // Fragment screen orientation normal both portait and landscape
            rootView = inflater.inflate(R.layout.fragment_sort_by, container, false);
            Log.d(TAG, "onCreateView");
            ButterKnife.bind(this, rootView);
            this.sortByFragment = this;
        } catch (Exception ex) {
            Log.e(TAG, "onCreateView: " + ex.toString());
        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    protected void setUpUI() {
        mHighestRatedCheck.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if(isChecked) {
                    onCheckboxClicked(mHighestRatedCheck);
                    mSelectedSortType = checkBox.getTag().toString();
                }
            }
        });

        mFavouredCheck.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if(isChecked) {
                    onCheckboxClicked(mFavouredCheck);
                    mSelectedSortType = checkBox.getTag().toString();
                }
            }
        });

        mReviewCheck.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if(isChecked) {
                    onCheckboxClicked(mReviewCheck);
                    mSelectedSortType = checkBox.getTag().toString();
                }
            }
        });

//        mLeastCommentedCheck.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
//                if(isChecked) {
//                    onCheckboxClicked(mLeastCommentedCheck);
//                    mSelectedSortType = mLeastCommentedText.getText().toString();
//                }
//            }
//        });
    }

    @Override
    public void initializePresenter() {
    }

    @Override
    protected void setUpToolBar() {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sortByFragment = null;
    }

    public void onCheckboxClicked(View view) {

        switch(view.getId()) {
            case R.id.highest_rated_check:
                mFavouredCheck.setChecked(false);
                mReviewCheck.setChecked(false);
                break;
            case R.id.favoured_check:
                mHighestRatedCheck.setChecked(false);
                mReviewCheck.setChecked(false);
                break;
            case R.id.review_check:
                mHighestRatedCheck.setChecked(false);
                mFavouredCheck.setChecked(false);
                break;
//            case R.id.least_commented_check:
//                mHighestRatedCheck.setChecked(false);
//                mLowestRatedCheck.setChecked(false);
//                mMostCommentedCheck.setChecked(false);
//                break;
            default:
                break;
        }
    }

    public void dismissFragment(){
        switch (mFragmentFrom){
            case  "SearchSakeFragment"      :
                if (SearchSakeFragment.searchSakeFragment != null) {
                    SearchSakeFragment.searchSakeFragment.setSortBy(mSelectedSortType);
                    SearchSakeFragment.searchSakeFragment.updateTagView();
                }
                break;
            case  "SearchPlacesFragment"    :
                if (SearchPlacesFragment.searchPlacesFragment != null) {
                    SearchPlacesFragment.searchPlacesFragment.setSortBy(mSelectedSortType);
                    SearchPlacesFragment.searchPlacesFragment.updateTagView();
                }
                break;
            case  "SearchBreweriesFragment" :
                if (SearchBreweriesFragment.searchBreweriesFragment != null) {
                    SearchBreweriesFragment.searchBreweriesFragment.setSortBy(mSelectedSortType);
                    SearchBreweriesFragment.searchBreweriesFragment.updateTagView();
                }
                break;
            default                         :
                break;

        }
        getFragmentManager().popBackStack();
    }

    @OnClick(R.id.btn_done)
    public void onClickDone(View view){
        dismissFragment();
    }
}