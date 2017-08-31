package com.sakewiz.android.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.sakewiz.android.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by dilshan_e on 30/05/2017.
 */
public abstract class BaseActivity extends AppCompatActivity {

//    protected Presenter presenter;
    protected Toolbar mToolBar;
    public AlertDialog myAlertDialog;

    public static BaseActivity baseActivity = null;

    @Override
    public void setContentView(int layoutResID) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.setContentView(layoutResID);
        getActionBarToolbar();
//        initializePresenterForzLogOut();
        baseActivity = this;
    }

    protected Toolbar getActionBarToolbar() {
        if (mToolBar == null) {
            mToolBar = (Toolbar) findViewById(R.id.toolbar);
            if (mToolBar != null) {
                setSupportActionBar(mToolBar);
                ActionBar mActionBar = BaseActivity.this.getSupportActionBar();
                mActionBar.setDisplayShowHomeEnabled(false);
                mActionBar.setDisplayShowTitleEnabled(false);
                mActionBar.setDisplayShowCustomEnabled(true);

                // remove previously created actionbar
                mActionBar.invalidateOptionsMenu();

                /** remove actionbar unnecessary left margin */
                mToolBar.setContentInsetsAbsolute(0, 0);
            }
        }
        return mToolBar;
    }

    protected void printFaceBookKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.sakewiz.android", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash: ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    protected void showToast(String message) {
        Toast.makeText(BaseActivity.this, message, Toast.LENGTH_LONG).show();
    }
    protected void showTopSnackBar(String message, int bColor) {
/*        TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content), message, TSnackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(bColor);
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        snackbar.show();*/

        Snackbar snack = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackbarView = snack.getView();
        snackbarView.setBackgroundColor(bColor);
//        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
//        textView.setTextColor(Color.WHITE);
//        textView.setGravity(Gravity.CENTER_HORIZONTAL);
//        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)snackbarView.getLayoutParams();
//        params.gravity = Gravity.TOP;
//        snackbarView.setLayoutParams(params);
        snack.show();
    }

    protected void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    protected void showAlertDialog(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BaseActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton("Ok", defaultDialogClickListener());

        if (!BaseActivity.this.isFinishing()) alertDialog.show();
    }

    protected void showAlertDialog(String title, String message, String positiveBtnTxt, String negativeBtnTxt,
                                                  DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BaseActivity.this);

        String titleTxt = (title != null) ? title : "Warning";
        String messageTxt = (message != null) ? message : "";
        alertDialog.setTitle(titleTxt);
        alertDialog.setMessage(messageTxt);

        if(positiveBtnTxt != null) {
            DialogInterface.OnClickListener positiveClickListener = (positiveListener != null) ? positiveListener : defaultDialogClickListener();
            alertDialog.setPositiveButton(positiveBtnTxt, positiveClickListener);
        }

        if(negativeBtnTxt != null) {
            DialogInterface.OnClickListener negativeClickListener = (negativeListener != null) ? negativeListener : defaultDialogClickListener();
            alertDialog.setNeutralButton(negativeBtnTxt, negativeClickListener);
        }

        if (!BaseActivity.this.isFinishing()) alertDialog.show();
    }

    protected void showAlertDialog(boolean setCancelable, String title, String message, String positiveBtnTxt, String negativeBtnTxt,
                                   DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        if(myAlertDialog != null && myAlertDialog.isShowing()) return;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BaseActivity.this);
        alertDialog.setCancelable(setCancelable);

        String titleTxt = (title != null) ? title : "Warning";
        String messageTxt = (message != null) ? message : "";
        alertDialog.setTitle(titleTxt);
        alertDialog.setMessage(messageTxt);

        if(positiveBtnTxt != null) {
            DialogInterface.OnClickListener positiveClickListener = (positiveListener != null) ? positiveListener : defaultDialogClickListener();
            alertDialog.setPositiveButton(positiveBtnTxt, positiveClickListener);
        }

        if(negativeBtnTxt != null) {
            DialogInterface.OnClickListener negativeClickListener = (negativeListener != null) ? negativeListener : defaultDialogClickListener();
            alertDialog.setNeutralButton(negativeBtnTxt, negativeClickListener);
        }

        myAlertDialog = alertDialog.create();
        if (!BaseActivity.this.isFinishing()) myAlertDialog.show();
    }

    protected void showAlertDialog(boolean setCancelable, String title, String message, String positiveBtnTxt, String negativeBtnTxt, String neutralBtnTxt,
                                   DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener, DialogInterface.OnClickListener neutralListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BaseActivity.this);
        alertDialog.setCancelable(setCancelable);

        String titleTxt = (title != null) ? title : "Warning";
        String messageTxt = (message != null) ? message : "";
        alertDialog.setTitle(titleTxt);
        alertDialog.setMessage(messageTxt);

        if(positiveBtnTxt != null) {
            DialogInterface.OnClickListener positiveClickListener = (positiveListener != null) ? positiveListener : defaultDialogClickListener();
            alertDialog.setPositiveButton(positiveBtnTxt, positiveClickListener);
        }

        if(negativeBtnTxt != null) {
            DialogInterface.OnClickListener negativeClickListener = (negativeListener != null) ? negativeListener : defaultDialogClickListener();
            alertDialog.setNegativeButton(negativeBtnTxt, negativeClickListener);
        }

        if(neutralBtnTxt != null) {
            DialogInterface.OnClickListener neutralClickListener = (neutralListener != null) ? neutralListener : defaultDialogClickListener();
            alertDialog.setNeutralButton(neutralBtnTxt, neutralClickListener);
        }

        alertDialog.create();
        if (!BaseActivity.this.isFinishing()) alertDialog.show();
    }

    protected DialogInterface.OnClickListener defaultDialogClickListener() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        };
    }

    public void showMessage(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }

    public void showErrorMessage(Activity activity, String error) {
        Toast.makeText(activity, error, Toast.LENGTH_LONG).show();
    }

//    public void initializePresenterForzLogOut() {
//        LogOutService mLogOutService = new LogOutServiceImpl(new NZSService());
//        presenter = new LogOutPresenterImpl(BaseActivity.this, mLogOutService, new AppScheduler());
//        presenter.attachView(BaseActivity.this);
//        presenter.onCreate();
//    }

//    public void performLogOutRequest(String gcmToken) {
//        ((LogOutPresenter) presenter).doLogOut(gcmToken);
//    }
//
//    @Override
//    public void showLogOutResponse(LogOutResponse logOutResponse) {
//
//    }
}
