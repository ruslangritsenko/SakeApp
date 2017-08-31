package com.sakewiz.android.ui.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.sakewiz.android.BaseApplication;
import com.sakewiz.android.R;
import com.sakewiz.android.common.CommonUtils;
import com.sakewiz.android.common.constants.ApplicationConstants;
import com.sakewiz.android.mvp.presenters.Presenter;
import com.sakewiz.android.ui.fragments.FollowersFragment;
import com.sakewiz.android.ui.fragments.InventoryAddFragment;
import com.sakewiz.android.ui.fragments.MainFragment;
import com.sakewiz.android.ui.fragments.MyProfileFragment;
import com.sakewiz.android.ui.fragments.MyPurchasesFragment;
import com.sakewiz.android.ui.fragments.NewsFeedFragment;
import com.sakewiz.android.ui.fragments.PrivateNotesFragment;
import com.sakewiz.android.ui.fragments.SavedSakesFragment;
import com.sakewiz.android.ui.fragments.SearchFragment;
import com.sakewiz.android.ui.fragments.SearchPlacesFragment;
import com.sakewiz.android.ui.fragments.SettingsFragment;
import com.sakewiz.android.ui.utils.NavigationRecyclerAdapter;
import com.sakewiz.android.ui.utils.RecyclerItemClickListener;
import com.sakewiz.android.utils.BaseBackPressedListener;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dilshan_e on 29/05/2017.
 */

public class MainActivity extends BaseActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    final String TAG = MainActivity.this.getClass().getSimpleName();
    public static MainActivity mainActivity = null;

    private static String MAIN_LEVEL = "MAIN_LEVEL";
    protected Presenter presenter;
    private SharedPreferences preferences;
    private ProgressDialog progressDialog;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private List<String> menuItemResList;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView mRecyclerView;
    private NavigationRecyclerAdapter navigationRecyclerAdapter;
    protected BaseBackPressedListener.OnBackPressedListener onBackPressedListener;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);
            initializePresenter();
            this.mainActivity = this;
            preferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);

            navigationView = (NavigationView) findViewById(R.id.navigation_view);
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            ImageButton mClose = (ImageButton) findViewById(R.id.btn_close);
            RelativeLayout mScanLabel = (RelativeLayout) findViewById(R.id.btnLabelContainer);

            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolBar, R.string.openDrawer, R.string.closeDrawer) {

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                    invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
                }
            };


            drawerLayout.setDrawerListener(actionBarDrawerToggle);

            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            menuItemResList = Arrays.asList(getResources().getStringArray(R.array.nav_drawer_items));
            navigationRecyclerAdapter = new NavigationRecyclerAdapter(MainActivity.this, menuItemResList);
            mRecyclerView.setAdapter(navigationRecyclerAdapter);

            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            String navTitle = navigationRecyclerAdapter.getItem(position);
                            onNavigationDrawerItemSelected(position, navTitle);
                        }
                    })
            );

            mClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawerLayout.closeDrawers();
                }
            });

            mScanLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawerLayout.closeDrawers();
                    Toast.makeText(MainActivity.this, getString(R.string.scan_label), Toast.LENGTH_SHORT).show();
                }
            });

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    // The next two lines tell the new client that “this” current class will handle connection stuff
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    //fourth line adds the LocationServices API endpoint from GooglePlayServices
                    .addApi(LocationServices.API)
                    .build();

            // Create the LocationRequest object
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                    .setFastestInterval(1 * 1000); // 1 second, in milliseconds

            if (findViewById(R.id.fragment_container) != null) {
                setFragment(MainFragment.newInstance(R.string.bottom_bar_home));
            }

        } catch (Exception ex) {
            Log.e(TAG, "onCreate: " + ex.toString());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawerPopulate();
        Log.d(TAG, "onResume");

        //Now lets connect to the API
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        BaseApplication.getBaseApplication().setCurrentLatitude(null);
        BaseApplication.getBaseApplication().setCurrentLongitude(null);
        super.onPause();
        Log.d(TAG, "onPause");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    public void initializePresenter() {
    }

    public void onNavigationDrawerItemSelected(int position, String navTitle) {
        // Closing drawer on item click
        drawerLayout.closeDrawers();

        switch (navTitle) {
            case "Search Places":
                new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        try {
                            addFragment(new SearchFragment(), SearchFragment.getTAG(), 2);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                }).sendEmptyMessage(0);
                break;
            case "My Profile":
                addFragment(new MyProfileFragment(), MyProfileFragment.getTAG(), 2);
                break;
            case "Followers":
                addFragment(new FollowersFragment(), FollowersFragment.getTAG(), 2);
                break;
            case "Private Notes":
                addFragment(new PrivateNotesFragment(), PrivateNotesFragment.getTAG(), 2);
                break;
            case "Saved Sakes":
                addFragment(new SavedSakesFragment(), SavedSakesFragment.getTAG(), 2);
                break;
            case "Newsfeed":
                addFragment(new NewsFeedFragment(), NewsFeedFragment.getTAG(), 2);
                break;
            case "My Purchases":
                addFragment(new MyPurchasesFragment(), MyPurchasesFragment.getTAG(), 2);
                break;
            case "Inventory Add":
                addFragment(new InventoryAddFragment(), InventoryAddFragment.getTAG(), 2);
                break;
            case "Settings":
                addFragment(new SettingsFragment(), SettingsFragment.getTAG(), 2);
                break;
            case "Logout":
                checkUserLogout();
                break;
            default:
                break;
        }
    }

    public void toggleNavigationDrawer() {
        if (drawerLayout == null) return;
        if (drawerLayout.isDrawerOpen(navigationView)) drawerLayout.closeDrawer(navigationView);
        else drawerLayout.openDrawer(navigationView);
    }

    public void lockNavigationDrawer(boolean status) {
        if (drawerLayout == null) return;
        if (status) drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        else drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    private void clearFragmentBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        for (int i = 0; i < count; ++i) {
            fm.popBackStack();
        }
    }

    public boolean isDrwawerOpen(){
        if (drawerLayout == null) return false;
        if (drawerLayout.isDrawerOpen(navigationView)) {
            return true;
        }
        else {
            return false;
        }
    }

    private void checkUserLogout(){
        DialogInterface.OnClickListener logOutOnClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (CommonUtils.getInstance().isNetworkConnected()) {
                    userLogOut();
                } else {
                    showToast(ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
                }
            }
        };

        showAlertDialog(false, ApplicationConstants.CONFIRM_LOGOUT, getString(R.string.logout_message),
                getString(R.string.logout), getString(R.string.cancel), logOutOnClickListener, null);
    }

    public void userLogOut() {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    public void setUpSearchBar(final SearchView searchView, final Fragment parentFragment){
        searchView.setQueryHint(getResources().getString(R.string.search_view_hint));
        searchView.setIconifiedByDefault(true);

        int searchPlateId = getResources().getIdentifier("android:id/search_plate", null, null);
        ViewGroup viewGroup = (ViewGroup) searchView.findViewById(searchPlateId);
        viewGroup.setBackgroundColor(getResources().getColor(R.color.toolbar_gradient_bottom));

        int searchCloseBtn = getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView searchCloseIcon = (ImageView) searchView.findViewById(searchCloseBtn);
        searchCloseIcon.setColorFilter(getResources().getColor(R.color.white));

        int searchBtn = getResources().getIdentifier("android:id/search_button", null, null);
        ImageView searchBtnIcon = (ImageView) searchView.findViewById(searchBtn);
        searchBtnIcon.setColorFilter(getResources().getColor(R.color.white));

        int searchEditTextID = getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = (EditText) searchView.findViewById(searchEditTextID);
//        searchEditText.setTypeface(CommonUtils.getInstance().getFont(MainActivity.this, ApplicationConstants.FONT_ACUMIN_PRO_REGULAR));
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white));
        searchEditText.setFocusable(true);
        searchEditText.setCursorVisible(true);

        searchEditText.setBackgroundResource(R.drawable.abc_textfield_search_default_mtrl_alpha);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.setQuery(null, false);
                searchView.onActionViewCollapsed();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void setFragment(Fragment fragment) {
        if (fragment == null) return;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commitAllowingStateLoss();
    }

    public void setFragment(Fragment fragment, String TAG) {
        try {
            if (fragment == null) return;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment, TAG);
            if (TAG != null) transaction.addToBackStack(TAG);
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFragment(Fragment fragment, String TAG, int animationType) {
        try {
            if (fragment == null) return;
            FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();

            if (animationType == 1) fragTransaction.setCustomAnimations(R.anim.fade_in, 0, 0, R.anim.fade_out);
            else fragTransaction.setCustomAnimations(R.anim.fragment_slide_in_left, 0, 0, R.anim.fragment_slide_out_left);

            fragTransaction.add(R.id.fragment_container, fragment, TAG);
            fragTransaction.addToBackStack(TAG);
            fragTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null) onBackPressedListener.doBack();
        else super.onBackPressed();
    }

    public void setOnBackPressedListener(BaseBackPressedListener.OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    private void drawerPopulate() {
        List<String> menuItemResArrayList = Arrays.asList(getResources().getStringArray(R.array.nav_drawer_items));
        navigationRecyclerAdapter.updateData(menuItemResArrayList, 0);
    }

    public void saveObjectToSharedPreferences(String preferencesKeys,List<?> catalogResponse){
        SharedPreferences.Editor prefsEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(catalogResponse);
        prefsEditor.putString(preferencesKeys, json);
        prefsEditor.apply();
    }

    public void setProgressDialog(boolean isLoading) {
        if (isLoading) {
            if (progressDialog != null) progressDialog.show();
            else progressDialog = ProgressDialog.show(MainActivity.this,
                    ApplicationConstants.EMPTY_STRING, "loading", true);
        } else {
            if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
        }
    }

    /**
     * Used to hide soft keyboard when touch out side of the edit text
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP &&
                    (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {
                CommonUtils.getInstance().hideKeyboard(this);
            }
        }
        return ret;
    }

    /**
     * If connected get lat and long
     *
     */
    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            BaseApplication.getBaseApplication().setCurrentLatitude(String.valueOf(currentLatitude));
            BaseApplication.getBaseApplication().setCurrentLongitude(String.valueOf(currentLongitude));
        }
    }


    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /**
     * If locationChanges change lat and long
     *
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        BaseApplication.getBaseApplication().setCurrentLatitude(String.valueOf(currentLatitude));
        BaseApplication.getBaseApplication().setCurrentLongitude(String.valueOf(currentLongitude));
    }
}
