package com.sakewiz.android.ui.fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.sakewiz.android.R;
import com.sakewiz.android.common.CommonUtils;
import com.sakewiz.android.common.constants.ApplicationConstants;
import com.sakewiz.android.domain.GeographyService;
import com.sakewiz.android.domain.GeographyServiceImpl;
import com.sakewiz.android.dto.CountryResults;
import com.sakewiz.android.dto.RegionResult;
import com.sakewiz.android.dto.SubRegionResult;
import com.sakewiz.android.model.entities.response.CountryListResponse;
import com.sakewiz.android.model.entities.response.CountryResponse;
import com.sakewiz.android.model.entities.response.RegionListResponse;
import com.sakewiz.android.model.entities.response.RegionResponse;
import com.sakewiz.android.model.entities.response.SubRegionListResponse;
import com.sakewiz.android.model.entities.response.SubRegionResponse;
import com.sakewiz.android.model.rest.SakeWizService;
import com.sakewiz.android.mvp.presenters.GeographyPresenter;
import com.sakewiz.android.mvp.presenters.GeographyPresenterImpl;
import com.sakewiz.android.mvp.views.GeographyView;
import com.sakewiz.android.utils.AppScheduler;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * Created by dilshan_e on 6/14/17.
 */
public class FragmentLocation extends BaseFragment implements GeographyView{

    final String TAG = FragmentLocation.this.getClass().getSimpleName();

    public static String getTAG() {
        return "FragmentLocation";
    }

    private static String BUNDLE_EXTRA = "BUNDLE_EXTRA";
    private static int REP_DELAY = 50;
    private static String CODES = "codes";
    public static FragmentLocation fragmentLocation;

    public static FragmentLocation newInstance(String fragmentFrom) {
        FragmentLocation fragment = new FragmentLocation();
        Bundle args = new Bundle();
         args.putString(BUNDLE_EXTRA, fragmentFrom);
        fragment.setArguments(args);
        return fragment;
    }

    @Bind(R.id.sp_select_country) Spinner mCountrySpinner;
    @Bind(R.id.sp_select_city)Spinner mCitySpinner;
    @Bind(R.id.sp_select_province)Spinner mProvinceSpinner;

    private Fragment mFragment;
    private Activity mActivity;
    private String mFragmentFrom;
    private CountryResults mSelectedCountry;
    private RegionResult mSelectedCity;
    private SubRegionResult mSlectedProvince;
    private List<CountryResults> mCountryResultList;
    private List<RegionResult> mRegionResultList;
    private List<SubRegionResult> mSubRegionResultList;

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
            rootView = inflater.inflate(R.layout.fragment_location, container, false);
            Log.d(TAG, "onCreateView");
            ButterKnife.bind(this, rootView);
            this.fragmentLocation = this;
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
        setUpSpinnerBackground();
        String[] listCountry;
        listCountry = getCountrySpinnerList(new ArrayList<String>(), getResources().getString(R.string.title_spinner_country));
        if (listCountry.length > 0) setCountryListSpinner(listCountry);

        String[] listCity;
        listCity = getRegionSpinnerList(new ArrayList<String>(), getResources().getString(R.string.title_spinner_city));
        if (listCity.length > 0) setCityListSpinner(listCity);

        String[] listProvince;
        listProvince = getSubRegionSpinnerList(new ArrayList<String>(), getResources().getString(R.string.title_spinner_province));
        if (listProvince.length > 0) setProvinceListSpinner(listProvince);

        performGetCountryListRequest();
    }

    @Override
    public void initializePresenter() {
        GeographyService mGeographyService = new GeographyServiceImpl(new SakeWizService());
        presenter = new GeographyPresenterImpl(getActivity(), mGeographyService, new AppScheduler());
        presenter.attachView(FragmentLocation.this);
        presenter.onCreate();
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
        fragmentLocation = null;
    }

    @OnItemSelected(R.id.sp_select_country)
    public void CountrySelectSpinnerItemSelected(Spinner spinner, int position) {
        if (position > 0 ) {
            mSelectedCountry = mCountryResultList.get(position-1) ;
            if (mCountryResultList != null && mSelectedCountry != null)
                performGetRegionListRequest(mSelectedCountry.getId());
        }
    }

    @OnItemSelected(R.id.sp_select_city)
    public void citySelectSpinnerItemSelected(Spinner spinner, int position) {
        if (position > 0 ) {
            mSelectedCity = mRegionResultList.get(position-1);
            if (mRegionResultList != null &&  mSelectedCity != null)
                performGetSubRegionListRequest(mSelectedCountry.getId(), mSelectedCity.getId());
        }
    }

    @OnItemSelected(R.id.sp_select_province)
    public void provinceSelectSpinnerItemSelected(Spinner spinner, int position) {
        if (position > 0 ) {
            mSlectedProvince = mSubRegionResultList.get(position-1);
        }
    }

    private void setCountryListSpinner(String[] industryList) {
        ArrayAdapter<String> industryListDataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, industryList);
        industryListDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCountrySpinner.setAdapter(industryListDataAdapter);
    }

    private void setCityListSpinner(String[] industryList) {
        ArrayAdapter<String> industryListDataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, industryList);
        industryListDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCitySpinner.setAdapter(industryListDataAdapter);
    }

    private void setProvinceListSpinner(String[] industryList) {
        ArrayAdapter<String> industryListDataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, industryList);
        industryListDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mProvinceSpinner.setAdapter(industryListDataAdapter);
    }

    private String[] getCountrySpinnerList(List<?> SpinnerList, String title) {
        String[] list = new String[SpinnerList.size() + 1];
        list[0] = title;
        for (int i = 0; i < SpinnerList.size(); i++) {
            list[i+1] = ((CountryResults) SpinnerList.get(i)).getName().getEn();
        }
        return list;
    }

    private String[] getRegionSpinnerList(List<?> SpinnerList, String title) {
        String[] list = new String[SpinnerList.size() + 1];
        list[0] = title;
        for (int i = 0; i < SpinnerList.size(); i++) {
            list[i+1] = ((RegionResult) SpinnerList.get(i)).getName().getEn();
        }
        return list;
    }

    private String[] getSubRegionSpinnerList(List<?> SpinnerList, String title) {
        String[] list = new String[SpinnerList.size() + 1];
        list[0] = title;
        for (int i = 0; i < SpinnerList.size(); i++) {
            list[i+1] = ((SubRegionResult) SpinnerList.get(i)).getName().getEn();
        }
        return list;
    }

    private void setUpSpinnerBackground() {
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
            mCountrySpinner.setBackgroundResource(R.drawable.bg_spinner);
            mCitySpinner.setBackgroundResource(R.drawable.bg_spinner);
            mProvinceSpinner.setBackgroundResource(R.drawable.bg_spinner);
        }else {
            mCountrySpinner.setBackgroundResource(R.drawable.bg_edit_text);
            mCitySpinner.setBackgroundResource(R.drawable.bg_edit_text);
            mProvinceSpinner.setBackgroundResource(R.drawable.bg_edit_text);
        }
    }

    @OnClick(R.id.apply_btn_location)
    public void onApply(View view){
        String selectedCountry = null;
        String selectedCity = null;
        String selectedProvince = null;

        if ( mSelectedCountry != null && mSelectedCountry.getName() != null )
            selectedCountry = mSelectedCountry.getName().getEn();
        if ( mSelectedCity != null && mSelectedCity.getName() != null )
            selectedCity = mSelectedCity.getName().getEn();
        if ( mSlectedProvince != null && mSlectedProvince.getName() != null )
            selectedProvince = mSlectedProvince.getName().getEn();

            switch (mFragmentFrom) {
                case "SearchSakeFragment":
                    if (SearchSakeFragment.searchSakeFragment != null) {
                        SearchSakeFragment.searchSakeFragment.setPlaceTagKey(
                                selectedCountry, selectedCity, selectedProvince);
                        SearchSakeFragment.searchSakeFragment.updateTagView();
                     }
                    break;
                case  "SearchPlacesFragment"    :
                    if (SearchPlacesFragment.searchPlacesFragment != null) {
                        SearchPlacesFragment.searchPlacesFragment.setPlaceTagKey(
                                selectedCountry, selectedCity, selectedProvince);
                        SearchPlacesFragment.searchPlacesFragment.updateTagView();
                    }
                    break;
                case  "SearchBreweriesFragment" :
                    if (SearchBreweriesFragment.searchBreweriesFragment != null) {
                        SearchBreweriesFragment.searchBreweriesFragment.setPlaceTagKey(
                                selectedCountry, selectedCity, selectedProvince);
                        SearchBreweriesFragment.searchBreweriesFragment.updateTagView();
                    }
                    break;
                default                         :
                    break;

            }
        if (FilterMainFragment.mFilterMainFragment != null)
            FilterMainFragment.mFilterMainFragment.getFragmentManager().popBackStack();
    }

    private void performGetCountryListRequest() {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            ((GeographyPresenter) presenter).getCountryList();
        } else {
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        }
    }

    @Override
    public void showGetCountryListResponse(CountryListResponse countryListResponse) {
        setProgressDialog(false);
        if (countryListResponse.isSuccess()) {
            String [] listCountry;
            mCountryResultList = countryListResponse.getResults();
            listCountry = getCountrySpinnerList(mCountryResultList, getResources().getString(R.string.title_spinner_country));
            if (listCountry.length > 0) setCountryListSpinner(listCountry);

        } else {
            if (countryListResponse.isAPIError()) {
                showTopSnackBar(countryListResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            } else {
                showTopSnackBar(countryListResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            }
        }
    }

    @Override
    public void showGetCountryResponse(CountryResponse countryResponse) {

    }

    @Override
    public void showGetRegionResponse(RegionResponse regionResponse) {

    }

    @Override
    public void showGetSubRegionResponse(SubRegionResponse subRegionResponse) {

    }

    private void performGetRegionListRequest(String countryCd) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            ((GeographyPresenter) presenter).getRegionList(countryCd);
        } else {
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        }
    }

    @Override
    public void showGetSubRegionListResponse(SubRegionListResponse subRegionListResponse) {
        setProgressDialog(false);
        if (subRegionListResponse.isSuccess()) {
            String [] listSubRegion;
            mSubRegionResultList = subRegionListResponse.getResults();
            listSubRegion = getSubRegionSpinnerList(mSubRegionResultList, getResources().getString(R.string.title_spinner_province));
            if (listSubRegion.length > 0) setProvinceListSpinner(listSubRegion);

        } else {
            if (subRegionListResponse.isAPIError()) {
                showTopSnackBar(subRegionListResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            } else {
                showTopSnackBar(subRegionListResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            }
        }
    }

    private void performGetSubRegionListRequest(String countryCd, String regionCd) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            ((GeographyPresenter) presenter).getSubRegionList(countryCd, regionCd);
        } else {
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        }
    }

    @Override
    public void showGetRegionListResponse(RegionListResponse regionListResponse) {
        setProgressDialog(false);
        if (regionListResponse.isSuccess()) {
            String [] listRegion;
            mRegionResultList = regionListResponse.getResults();
            listRegion = getRegionSpinnerList(mRegionResultList, getResources().getString(R.string.title_spinner_city));
            if (listRegion.length > 0) setCityListSpinner(listRegion);

        } else {
            if (regionListResponse.isAPIError()) {
                showTopSnackBar(regionListResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            } else {
                showTopSnackBar(regionListResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            }
        }
    }
}
