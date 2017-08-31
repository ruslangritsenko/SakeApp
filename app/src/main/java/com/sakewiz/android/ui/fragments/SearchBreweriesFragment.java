package com.sakewiz.android.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sakewiz.android.BaseApplication;
import com.sakewiz.android.R;
import com.sakewiz.android.common.CommonUtils;
import com.sakewiz.android.common.constants.ApplicationConstants;
import com.sakewiz.android.domain.SearchService;
import com.sakewiz.android.domain.SearchServiceImpl;
import com.sakewiz.android.dto.Place;
import com.sakewiz.android.model.entities.request.SearchPlacesRequest;
import com.sakewiz.android.model.entities.response.SearchPlacesResponse;
import com.sakewiz.android.model.entities.response.SearchProductResponse;
import com.sakewiz.android.model.entities.response.SuggestProductResponse;
import com.sakewiz.android.model.rest.SakeWizService;
import com.sakewiz.android.mvp.presenters.SearchPresenter;
import com.sakewiz.android.mvp.presenters.SearchPresenterImpl;
import com.sakewiz.android.mvp.views.SearchView;
import com.sakewiz.android.ui.activities.MainActivity;
import com.sakewiz.android.ui.adapters.EndlessParentScrollListener;
import com.sakewiz.android.ui.adapters.SearchBreweriesRecycleAdapter;
import com.sakewiz.android.ui.adapters.SearchPlaceRecycleAdapter;
import com.sakewiz.android.utils.AppScheduler;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dilshan_e on 6/10/17.
 */
public class SearchBreweriesFragment extends BaseFragment implements SearchView, OnMapReadyCallback{
    final String TAG = SearchBreweriesFragment.this.getClass().getSimpleName();

    public static String getTAG() {
        return "SearchBreweriesFragment";
    }

    private static String BUNDLE_EXTRA = "BUNDLE_EXTRA";
    private static final int typeRadius = 1;
    private static final int typeCountry = 21;
    private static final int typeCity = 22;
    private static final int typeProvince = 23;
    private static final int typeYeasts = 3;
    private static final int typeClasification = 41;
    private static final int typeFilterationMethod = 42;
    private static final int typePressSqueeze = 43;
    private static final int typeSortBy = 5;
    private static int REP_DELAY = 50;
    private static String CODES = "codes";
    public static SearchBreweriesFragment searchBreweriesFragment;

    public static SearchBreweriesFragment newInstance() {
        SearchBreweriesFragment fragment = new SearchBreweriesFragment();
        Bundle args = new Bundle();
//        args.putParcelable(BUNDLE_EXTRA, Parcels.wrap());
        fragment.setArguments(args);
        return fragment;
    }

    @Bind(R.id.recycler_view_search_breweries) RecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh_layout_home_search_breweries) SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.search_edit_text_search_breweries) AutoCompleteTextView mSearchText;
    @Bind(R.id.hori_progress) RelativeLayout horizontalProgress;
    @Bind(R.id.scrollView_search_breweries) NestedScrollView scrollView;
    @Bind(R.id.tag_view_search_breweries) TagView mTagView;

    private Fragment mFragment;
    private Activity mActivity;
    private SearchBreweriesRecycleAdapter mSearchBreweriesRecycleAdapter;
    private boolean isPulltoRefreshCall = false;
    private final int TAKE_CONSTANT = 10;
    private boolean isOnLoadMorePlace = false;
    private int pagePlace = 0;
    private String mCurrentSearchKey;
    private List<String> mKeyWords = new ArrayList<>();
    private boolean flagSuggestionRequest = true;
    private String mRadius;
    private String mCountry;
    private String mCity;
    private String mProvince;
    private String mSortBy;
    private boolean mRadiusFlag = false;
    private int loadedItemCount;
    private List<String> mClassification = new ArrayList<>();
    private List<String> mFilterationMethod = new ArrayList<>();
    private List<String> mPressSqueeze = new ArrayList<>();
    private List<String> mYeasts = new ArrayList<>();
    private ArrayAdapter<String> autoCompleteAdapter;
    private GoogleMap mMap;
    private SupportMapFragment mapView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mItem = Parcels.unwrap(getArguments().getParcelable(BUNDLE_EXTRA));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        try {
            // Fragment screen orientation normal both portait and landscape
            rootView = inflater.inflate(R.layout.fragment_search_breweries, container, false);
            Log.d(TAG, "onCreateView");
            ButterKnife.bind(this, rootView);
            this.searchBreweriesFragment = this;

            MapsInitializer.initialize(getActivity());

            switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()) )
            {
                case ConnectionResult.SUCCESS:
                    mapView = (SupportMapFragment)
                            getFragmentManager().findFragmentById(R.id.map_breweries);
                    mapView.getMapAsync(this);

                    break;
                case ConnectionResult.SERVICE_MISSING:
                    Toast.makeText(getActivity(), "SERVICE MISSING", Toast.LENGTH_SHORT).show();
                    break;
                case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                    Toast.makeText(getActivity(), "UPDATE REQUIRED", Toast.LENGTH_SHORT).show();
                    break;
                default: Toast.makeText(getActivity(), GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            Log.e(TAG, "onCreateView: " + ex.toString());
        }
        initRecyclerView();
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    protected void setUpUI() {
        resetRecyclerView();
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mCurrentSearchKey = mSearchText.getText().toString();
                    hideSoftKeyboard();
                    callSearchProductRequest();
                }
                return false;
            }
        });

        mTagView.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
            }
        });

        //set delete listener
        mTagView.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(final TagView view, final Tag tag, final int position) {
                view.remove(position);
                removeFilterItems(tag.text, tag.type);
            }
        });

        //set long click listener
        mTagView.setOnTagLongClickListener(new TagView.OnTagLongClickListener() {
            @Override
            public void onTagLongClick(Tag tag, int position) {
            }
        });

        autoCompleteAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.hint_item,mKeyWords);
        autoCompleteAdapter.setNotifyOnChange(true);
        mSearchText.setThreshold(1);
        mSearchText.setAdapter(autoCompleteAdapter);
        autoCompleteAdapter.addAll(mKeyWords);
        mSearchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0 && flagSuggestionRequest ) performSuggestBreweriesRequest(s.toString(),20);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mSearchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCurrentSearchKey = autoCompleteAdapter.getItem(i).toString();
                callSearchProductRequest();
            }
        });


        int[] colors = getResources().getIntArray(R.array.google_colors);
        mSwipeRefreshLayout.setColorSchemeColors(colors);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setSwipeLayoutLoading(true);
                isPulltoRefreshCall = true;
                callSearchProductRequest();
            }
        });
    }

    @Override
    public void initializePresenter() {
        SearchService mSearchService = new SearchServiceImpl(new SakeWizService());
        presenter = new SearchPresenterImpl(getActivity(), mSearchService, new AppScheduler());
        presenter.attachView(SearchBreweriesFragment.this);
        presenter.onCreate();
    }

    @Override
    protected void setUpToolBar() {}

    @Override
    public void onResume() {
//        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        searchBreweriesFragment = null;
        if (mMap != null) mMap.clear();
        super.onDestroy();
//        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    private void clearMapData(){
        if (mMap != null) mMap.clear();
    }

    protected void initRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);

        scrollView.setOnScrollChangeListener(new EndlessParentScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (loadedItemCount <= ((pagePlace+1)*TAKE_CONSTANT)) {
                    pagePlace ++;
                    isOnLoadMorePlace = true;
                    performSearchBreweriesRequest( pagePlace, TAKE_CONSTANT,  getSearchRequest());
                }


            }
        });

        mSearchBreweriesRecycleAdapter = new SearchBreweriesRecycleAdapter(getActivity(), new ArrayList<Place>());
        mRecyclerView.setAdapter(mSearchBreweriesRecycleAdapter);
    }

    private void resetRecyclerView() {
        mSearchBreweriesRecycleAdapter.updateData(null, 1);
        pagePlace = 0;
        loadedItemCount = 0;
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

    public void setCurrentSearchKey(String mCurrentSearchKey) {
        this.mCurrentSearchKey = mCurrentSearchKey;
    }

    public void setTextToSearchBar(String mCurrentSearchKey) {
        this.flagSuggestionRequest = false;
        this.mSearchText.setText(mCurrentSearchKey);
        this.flagSuggestionRequest = true;
    }

    public Tag getChip(String key, int type) {
        Tag tag = new Tag(key);
        tag.type = type;
        tag.radius = 20f;
        tag.layoutColor = getActivity().getResources().getColor(R.color.dark_text_color);
        tag.isDeletable = true;
        return tag;
    }

    public void updateTagView(){
        mTagView.removeAll();
        if (mRadius != null) mTagView.addTag(getChip(mRadius, typeRadius));

        if (mCountry != null) mTagView.addTag(getChip(mCountry, typeCountry));
        if (mCity != null) mTagView.addTag(getChip(mCity, typeCity));
        if (mProvince != null) mTagView.addTag(getChip(mProvince, typeProvince));

        if (mClassification != null && mClassification.size() > 0) {
            for (int i = 0; i< mClassification.size(); i++){
                mTagView.addTag(getChip(mClassification.get(i), typeClasification));
            }
        }

        if (mFilterationMethod != null && mFilterationMethod.size() > 0) {
            for (int i = 0; i< mFilterationMethod.size(); i++){
                mTagView.addTag(getChip(mFilterationMethod.get(i), typeFilterationMethod));
            }
        }

        if (mYeasts != null && mYeasts.size() > 0) {
            for (int i = 0; i< mYeasts.size(); i++){
                mTagView.addTag(getChip(mYeasts.get(i), typeYeasts));
            }
        }

        if (mPressSqueeze != null && mPressSqueeze.size() > 0) {
            for (int i = 0; i< mPressSqueeze.size(); i++){
                mTagView.addTag(getChip(mPressSqueeze.get(i), typePressSqueeze));
            }
        }


        if(mSortBy != null) mTagView.addTag(getChip(mSortBy, typeSortBy));

        // recall search request
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callSearchProductRequest();
            }
        }, 100);
    }

    public void removeFilterItems(String key, int type){
        switch (type){
            case typeRadius:
                mRadius = null;
                break;
            case typeCountry:
                mCountry = null;
                break;
            case typeCity:
                mCity = null;
                break;
            case typeProvince:
                mProvince = null;
                break;
            case typeClasification:
                mClassification.remove(key);
                break;
            case typeFilterationMethod:
                mFilterationMethod.remove(key);
                break;
            case typeYeasts:
                mYeasts.remove(key);
                break;
            case typePressSqueeze:
                mPressSqueeze.remove(key);
                break;
            case typeSortBy:
                mSortBy = null;
                break;
            default:
                break;
        }
    }

    public void setSortBy(String sortBy){
        mSortBy = sortBy;
    }

    public void setRadius(String radius){
        mRadius = radius;
    }

    public void setPlaceTagKey(String country, String city,String province){
        mCountry = country;
        mCity = city;
        mProvince = province;
    }

    public void setSakeTypeTag(List<String> classification, List<String> filterationMethod, List<String> yeasts, List<String> pressSqueeze ){
        mClassification.clear();
        mClassification = classification;
        mFilterationMethod.clear();
        mFilterationMethod = filterationMethod;
        mYeasts.clear();
        mYeasts = yeasts;
        mPressSqueeze.clear();
        mPressSqueeze = pressSqueeze;
    }

    public SearchPlacesRequest getSearchRequest(){
        SearchPlacesRequest request = new SearchPlacesRequest();
        String lat = BaseApplication.getBaseApplication().getCurrentLatitude();
        String lon = BaseApplication.getBaseApplication().getCurrentLongitude();
        Log.d("locations : ", "================>>>" + lat + " " + lon);
        if (mRadius != null) request.setDistance(getRadiusDoubleValue(mRadius));
//        else request.setDistance("5");
        if(lat != null && lon != null) {
            Log.d("locations : 2222", "================>>>" + lat + " " + lon);
            request.setLat(Float.parseFloat(lat));
            request.setLon(Float.parseFloat(lon));

        }
        if (mCurrentSearchKey != null ) request.setKeyword(mCurrentSearchKey);
//        request.setBarType("SAKE_BREWERY");
//        request.setType("BREWERY");
//        if (mClassification != null && mClassification.size() > 0) request.setTypes(mClassification);
//        if (mFilterationMethod != null && mFilterationMethod.size() > 0) request.setFilterWaters(mFilterationMethod);
//        if (mPressSqueeze != null && mPressSqueeze.size() > 0) request.setPressAndSqueezes(mPressSqueeze);
//        if (mYeasts != null && mYeasts.size() > 0) request.setYeasts(mYeasts);
//        if (mCountry != null ) request.setCountryCd(mCountry);
//        if (mCity != null ) request.setRegionCd(mCity);
//        if (mProvince != null ) request.setSubRegionCd(mProvince);
//        if (mSortBy != null ) request.setSortBy(mSortBy);
//        request.setSortOrder(getResources().getString(R.string.default_sort_by));
        return request;
    }

    public void callSearchProductRequest(){
        resetRecyclerView();
        performSearchBreweriesRequest(pagePlace, TAKE_CONSTANT, getSearchRequest());
    }

    public double getRadiusDoubleValue (String distance) {
        switch (distance) {
            case "3 Km":
                return  3;
            case "5 Km":
                return  5;
            case "10 Km":
                return  10;
            case "15 Km":
                return  15;
            default:
                return 0;
        }
    }

    @OnClick(R.id.filter_by_container_search_breweries)
    public void onClickFilterBy(View view){
        MainActivity.mainActivity.addFragment(new FilterMainFragment().newInstance(SearchBreweriesFragment.getTAG()),FilterMainFragment.getTAG(),1);
    }

   /* @OnClick(R.id.sort_by_container_search_breweries)
    public void onClickSortBy(View view){
        MainActivity.mainActivity.addFragment(new SortByFragment().newInstance(SearchBreweriesFragment.getTAG()),SortByFragment.getTAG(),1);
    }*/

    public void setHorizontalLoading(boolean isLoading) {
        if (horizontalProgress != null) {
            horizontalProgress.bringToFront();
            horizontalProgress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
    }

    public void performSearchBreweriesRequest(int page, int size, SearchPlacesRequest searchPlacesRequest) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            if ( isOnLoadMorePlace ) {
                setHorizontalLoading(true);
            } else {
                setProgressDialog(true);
            }
            ((SearchPresenter) presenter).doSearchBreweries(page, size, searchPlacesRequest);
        } else {
            setSwipeLayoutLoading(false);
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        }
    }

    @Override
    public void showSearchBreweriesResponse(SearchPlacesResponse searchPlacesResponse) {
        if ( isOnLoadMorePlace ) {
            setHorizontalLoading(false);
            isOnLoadMorePlace = false;
        } else {
            setProgressDialog(false);
        }
        setSwipeLayoutLoading(false);
        if (searchPlacesResponse.isSuccess()) {
            if (isPulltoRefreshCall){
                isPulltoRefreshCall = false;
                mSearchBreweriesRecycleAdapter.updateData(null, 1);
            }
            loadedItemCount = loadedItemCount+searchPlacesResponse.getPlaces().size();
            mSearchBreweriesRecycleAdapter.updateData(searchPlacesResponse.getPlaces(),0);
        } else {
            if (searchPlacesResponse.isAPIError()) {
                showTopSnackBar(searchPlacesResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            } else {
                showTopSnackBar(searchPlacesResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            }
        }
    }

    private void performSuggestBreweriesRequest(String text, int count) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            ((SearchPresenter) presenter).getSuggestBreweries(text, count);
        } else {
            setSwipeLayoutLoading(false);
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        }
    }

    @Override
    public void showSuggestBreweriesResponse(Object response) {
        if(response instanceof List){
            mKeyWords.clear();
            mKeyWords = (List<String>)response;
            if (mKeyWords != null) {
                autoCompleteAdapter.addAll(new ArrayList<String>());
                if(mKeyWords.size() > 0)  {
                    clearMapData();
                    autoCompleteAdapter.clear();
                    autoCompleteAdapter.addAll(mKeyWords);
                }
            }

        } else if (response instanceof SuggestProductResponse) {
            SuggestProductResponse suggestProductResponse = (SuggestProductResponse) response;
            if (suggestProductResponse.isAPIError()) {
                showTopSnackBar(suggestProductResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            } else {
                showTopSnackBar(suggestProductResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            }

        }
    }

    public void createMarker(String lat, String lon) {
        LatLng location = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
        mMap.addMarker(new MarkerOptions().position(location)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_circle_sake)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    @Override
    public void showSuggestProductsResponse(Object response) { }

    @Override
    public void showSearchPlacesResponse(SearchPlacesResponse searchPlacesResponse) { }

    @Override
    public void showSearchProductsResponse(SearchProductResponse searchProductResponse) { }

    @Override
    public void showSuggestPlacesResponse(Object response) { }

}
