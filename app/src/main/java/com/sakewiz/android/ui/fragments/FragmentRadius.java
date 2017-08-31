package com.sakewiz.android.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.sakewiz.android.R;
import com.sakewiz.android.model.entities.request.SearchPlacesRequest;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dilshan_e on 6/14/17.
 */
public class FragmentRadius extends BaseFragment {

    final String TAG = FragmentRadius.this.getClass().getSimpleName();

    public static String getTAG() {
        return "FragmentRadius";
    }

    private static String BUNDLE_EXTRA = "BUNDLE_EXTRA";
    private static int REP_DELAY = 50;
    private static String CODES = "codes";
    public static FragmentRadius fragmentRadius;

    public static FragmentRadius newInstance(String fragmentFrom) {
        FragmentRadius fragment = new FragmentRadius();
        Bundle args = new Bundle();
        args.putString(BUNDLE_EXTRA, fragmentFrom);
        fragment.setArguments(args);
        return fragment;
    }

    @Bind(R.id.chk_3_km) CheckBox mCheck3km;
    @Bind(R.id.chk_5_km) CheckBox mCheck5km;
    @Bind(R.id.chk_10_km) CheckBox mCheck10km;
    @Bind(R.id.chk_15_km) CheckBox mCheck15km;

    private Fragment mFragment;
    private Activity mActivity;
    private String mFragmentFrom;
    private String mSelectedRadius;
    private boolean filterChange = false;


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
            // Fragment screen orientation normal both portait and landscape
            rootView = inflater.inflate(R.layout.fragment_radius, container, false);
            Log.d(TAG, "onCreateView");
            ButterKnife.bind(this, rootView);
            this.fragmentRadius = this;
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
        mCheck3km.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                filterChange = true;
                if(b) {
                    onCheckboxClicked(mCheck3km);
                    mSelectedRadius = compoundButton.getText().toString();
                }
            }
        });

        mCheck5km.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                filterChange = true;
                if(b) {
                    onCheckboxClicked(mCheck5km);
                    mSelectedRadius = compoundButton.getText().toString();
                }
            }
        });

        mCheck10km.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                filterChange = true;
                if(b) {
                    onCheckboxClicked(mCheck10km);
                    mSelectedRadius = compoundButton.getText().toString();
                }
            }
        });

        mCheck15km.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                filterChange = true;
                if(b) {
                    onCheckboxClicked(mCheck15km);
                    mSelectedRadius = compoundButton.getText().toString();
                }
            }
        });
    }


    @OnClick(R.id.apply_btn_radius)
    public void onApply(View view){
        if (filterChange) {
            filterChange = false;
            switch (mFragmentFrom){
                case  "SearchSakeFragment"      :
                    if (SearchSakeFragment.searchSakeFragment != null) {
                        SearchSakeFragment.searchSakeFragment.setRadius(mSelectedRadius);
                        SearchSakeFragment.searchSakeFragment.updateTagView();
                    }
                    break;
                case  "SearchPlacesFragment"    :
                    if (SearchPlacesFragment.searchPlacesFragment != null) {
                        SearchPlacesFragment.searchPlacesFragment.setRadius(mSelectedRadius);
                        SearchPlacesFragment.searchPlacesFragment.updateTagView();
                    }
                    break;
                case  "SearchBreweriesFragment" :
                    if (SearchBreweriesFragment.searchBreweriesFragment != null) {
                        SearchBreweriesFragment.searchBreweriesFragment.setRadius(mSelectedRadius);
                        SearchBreweriesFragment.searchBreweriesFragment.updateTagView();
                    }
                    break;
                default                         :
                    break;

            }
        }
        if (FilterMainFragment.mFilterMainFragment != null)
            FilterMainFragment.mFilterMainFragment.getFragmentManager().popBackStack();
    }

    @Override
    public void initializePresenter() {
    }

    @Override
    protected void setUpToolBar() {}

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentRadius = null;
    }

    public void onCheckboxClicked(View view) {

        switch(view.getId()) {
            case R.id.chk_3_km:
                mCheck5km.setChecked(false);
                mCheck10km.setChecked(false);
                mCheck15km.setChecked(false);
                break;
            case R.id.chk_5_km:
                mCheck3km.setChecked(false);
                mCheck10km.setChecked(false);
                mCheck15km.setChecked(false);
                break;
            case R.id.chk_10_km:
                mCheck3km.setChecked(false);
                mCheck5km.setChecked(false);
                mCheck15km.setChecked(false);
                break;
            case R.id.chk_15_km:
                mCheck3km.setChecked(false);
                mCheck5km.setChecked(false);
                mCheck10km.setChecked(false);
                break;
            default:
                break;
        }
    }
}