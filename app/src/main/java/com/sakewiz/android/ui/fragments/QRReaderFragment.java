package com.sakewiz.android.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.sakewiz.android.R;
import com.sakewiz.android.common.CommonUtils;
import com.sakewiz.android.common.constants.ApplicationConstants;
import com.sakewiz.android.domain.UserService;
import com.sakewiz.android.domain.UserServiceImpl;
import com.sakewiz.android.model.entities.response.CreateNewUserResponse;
import com.sakewiz.android.model.entities.response.LoginResponse;
import com.sakewiz.android.model.entities.response.ResetPwdNotifyResponse;
import com.sakewiz.android.model.entities.response.UserDetailOfGivenUserResponse;
import com.sakewiz.android.model.rest.SakeWizService;
import com.sakewiz.android.mvp.presenters.UserPresenter;
import com.sakewiz.android.mvp.presenters.UserPresenterImpl;
import com.sakewiz.android.mvp.views.UserView;
import com.sakewiz.android.ui.activities.MainActivity;
import com.sakewiz.android.ui.activities.SignUpWithEmailActivity;
import com.sakewiz.android.ui.customviews.CameraPreview;
import com.sakewiz.android.utils.AppScheduler;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by chamith_d on 12/22/2016.
 */
public class QRReaderFragment extends BaseFragment implements UserView{

    final String TAG = QRReaderFragment.this.getClass().getSimpleName();

    public static String getTAG() {
        return "QRReaderFragment";
    }

    public static QRReaderFragment qrReaderFragment = null;


    public static QRReaderFragment newInstance() {
        return new QRReaderFragment();
    }

    private SearchView searchView;
    private TextView mTitle;
    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;
    private ImageScanner scanner;
    private boolean barcodeScanned = false;
    private boolean previewing = true;
    private boolean isSetupQRScanCall = false;
    private String sequence;
    private String customerNo;
    private String mUserHandle = null;
    private SharedPreferences preferences;
    private String mName;

    @Bind (R.id.cameraPreview) FrameLayout preview;
    @Bind(R.id.flash_btn) ImageView btnFlash;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);;
        preferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        try {
            rootView = inflater.inflate(R.layout.fragment_qr_reader, container, false);
            Log.d(TAG, "onCreateView");
            ButterKnife.bind(this, rootView);
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: " + e.toString());
        }
        this.qrReaderFragment = this;
        initializePresenter();
        return rootView;
    }

    @Override
    public void initializePresenter() {
        UserService mUserService = new UserServiceImpl(new SakeWizService());
        presenter = new UserPresenterImpl(getActivity(), mUserService, new AppScheduler());
        presenter.attachView(QRReaderFragment.this);
        presenter.onCreate();
    }

    @Override
    protected void setUpUI() {
    }

    @Override
    protected void setUpToolBar() {
        View mCustomView = getActivity().getLayoutInflater().inflate(R.layout.custom_actionbar_with_back, null);
        TextView toolBarTitle = (TextView) mCustomView.findViewById(R.id.title_text);
        toolBarTitle.setText(getString(R.string.title_my_qr_code));
        mCustomView.findViewById(R.id.imgVmain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        mToolBar.addView(mCustomView);
    }

    @Override
    public void onResume() {
        if (!isSetupQRScanCall ) {
            if ( !MainActivity.mainActivity.isDrwawerOpen()) {
                setUpQrScanner();
            }

        }
        super.onResume();
    }

//    public Fragment getTopFragment() {
//        List<Fragment> fragentList = getActivity().getSupportFragmentManager().getFragments();
//        Fragment top = null;
//        for (int i = fragentList.size() -1; i>=0 ; i--) {
//            top = (Fragment) fragentList.get(i);
//            if (top != null) {
//                return top;
//            }
//        }
//        return top;
//    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        System.out.println();
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();
        if (BaseFragment.myAlertDialog != null && BaseFragment.myAlertDialog.isShowing()) BaseFragment.myAlertDialog.dismiss();
        onFlashLight(false);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
//        releaseCamera();
        ((MainActivity) getActivity()).lockNavigationDrawer(true);
        super.onDestroy();
    }



    @OnClick(R.id.flash_btn)
    public void onClickFlash(View view){
        if (btnFlash.getTag().equals(getActivity().getResources().getString(R.string.btn_edit_stat_unselected))){
            btnFlash.setImageResource(R.drawable.ic_flash_off);
            btnFlash.setTag(getActivity().getResources().getString(R.string.btn_edit_stat_selected));
            onFlashLight(true);
        } else {
            btnFlash.setImageResource(R.drawable.ic_flash_on);
            btnFlash.setTag(getActivity().getResources().getString(R.string.btn_edit_stat_unselected));
            onFlashLight(false);
        }
    }

    @OnClick(R.id.btn_show_my_code)
    public void onClickShowMyCode(View view){
        Toast.makeText(getActivity(), "User Name : "+mName, Toast.LENGTH_LONG).show();
    }

    public void onFlashLight(boolean flashLightOn) {
        if (flashLightOn) {
            openLight();
        } else {
            offLight();
        }
    }

    public void setUpQrScanner(){
        isSetupQRScanCall = true;
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(getActivity(), mCamera, previewCb, autoFocusCB);
        if (preview != null) preview.addView(mPreview);
        if (barcodeScanned) {
                    barcodeScanned = false;
                    mCamera.setPreviewCallback(previewCb);
                    mCamera.startPreview();
                    previewing = true;
                    safeAutoFocus();
        }
    }

    @Override
    public void onDestroyView() {
        releaseCamera();
        this.qrReaderFragment = null;
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    }

    public void safeAutoFocus() {
        try {
            mCamera.autoFocus(autoFocusCB);
        } catch (RuntimeException re) {
            scheduleAutoFocus();
        }
    }

    public void scheduleAutoFocus(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                safeAutoFocus();
            }
        },1000);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        setUpQrScanner();
        super.setUserVisibleHint(isVisibleToUser);
    }

    public void openLight() {
        if (mCamera != null) {
            Camera.Parameters parameter = mCamera.getParameters();
            parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(parameter);
        }
    }

    public void offLight() {
        if (mCamera != null) {
            Camera.Parameters parameter = mCamera.getParameters();
            parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(parameter);
        }
    }


    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
            Log.d("ERROR", e.toString());
        }
        return c;
    }

    public void releaseCamera() {
        try {
            if (mCamera != null) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mPreview.getHolder().removeCallback(mPreview);
                preview.removeView(mPreview);
                mCamera.release();
                mCamera = null;
                mPreview = null;
                barcodeScanned = true;
            }
            isSetupQRScanCall = false;
        } catch(Exception ex) {
            Log.e(TAG, "releaseCamera: " + ex.toString());
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                safeAutoFocus();
        }
    };

    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            Camera.Parameters parameters = camera.getParameters();
            List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
            Camera.Size size = previewSizes.get(0);

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                previewing = false;
                if( mCamera != null ) {
                    mCamera.setPreviewCallback(null);
                    mCamera.stopPreview();
                }

                SymbolSet syms = scanner.getResults();
                    for (Symbol sym : syms) {
                        mUserHandle = sym.getData();
                        releaseCamera();
                        if ( mUserHandle != null) {
                            Toast.makeText(getActivity(), "read token : "+mUserHandle, Toast.LENGTH_LONG).show();
                            performSuggestProductsRequest(mUserHandle);
                        } else {
                            setUpQrScanner();
                            Toast.makeText(getActivity(), "Invalid Token"+sym.getData(), Toast.LENGTH_LONG).show();
                        }
                    }
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    private void performSuggestProductsRequest(String userHandle) {
        setProgressDialog(true);
        if (CommonUtils.getInstance().isNetworkConnected()) {
            ((UserPresenter) presenter).getUserDetailOfGivenUser(userHandle);
        } else {
            setUpQrScanner();
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        }
    }

    @Override
    public void showGetUserDetailOfGivenUserResponse(UserDetailOfGivenUserResponse userDetailOfGivenUserResponse) {
        setProgressDialog(false);
        setUpQrScanner();
        if (userDetailOfGivenUserResponse.isSuccess()) {
            mName = userDetailOfGivenUserResponse.getId();
        } else {
            if (userDetailOfGivenUserResponse.isAPIError()) {
                showTopSnackBar(userDetailOfGivenUserResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            } else {
                showTopSnackBar(userDetailOfGivenUserResponse.getMessage(), getResources().getColor(R.color.error_background_color));
            }
        }
    }

    @Override
    public void showCreateNewUserResponse(CreateNewUserResponse createNewUserResponse) {

    }

    @Override
    public void showLoginResponse(LoginResponse loginResponse) {

    }

    @Override
    public void showResetPasswordNotifyResponse(ResetPwdNotifyResponse resetPwdNotifyResponse) {

    }


}
