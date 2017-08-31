package com.sakewiz.android.ui.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sakewiz.android.R;
import com.sakewiz.android.common.constants.ApplicationConstants;
import com.sakewiz.android.model.rest.SakeWizService;
import com.sakewiz.android.mvp.presenters.Presenter;
import com.sakewiz.android.mvp.presenters.ProductPresenter;
import com.sakewiz.android.ui.activities.BaseActivity;
import com.sakewiz.android.ui.activities.MainActivity;
import com.sakewiz.android.utils.AppScheduler;
import com.sakewiz.android.utils.IScheduler;

import butterknife.ButterKnife;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by dilshan_e on 29/05/2017.
 */
public abstract class BaseFragment extends Fragment {

    protected SakeWizService sakeWizService;
    protected IScheduler scheduler;
    protected Presenter presenter;

    protected Toolbar mToolBar;
    protected RelativeLayout loadingView;
    private ProgressDialog progressDialog = null;

    public static AlertDialog myAlertDialog;
    public static AlertDialog myAlertDialogOne;
    public static AlertDialog myAlertDialogTwo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scheduler = new AppScheduler();
        sakeWizService = new SakeWizService();
        initializePresenter();
        if(presenter != null) presenter.onCreate();
        mToolBar = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null) presenter.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) presenter.onStart();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getActionBarToolbar(view);
        setupLoading(view);
        setUpUI();
        setUpToolBar();
    }

    @Override
    public void onDestroyView() {
        setProgressDialog(false);
        progressDialog = null;

        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    protected abstract void initializePresenter();

    protected abstract void setUpUI();

    protected abstract void setUpToolBar();

    protected Toolbar getActionBarToolbar(View v) {
        mToolBar = (Toolbar) v.findViewById(R.id.toolbar);
        if (mToolBar != null) {
            ((MainActivity) getActivity()).setSupportActionBar(mToolBar);
            mToolBar.setContentInsetsAbsolute(0, 0); /** remove actionbar unnecessary left margin */
        }
        return mToolBar;
    }

    protected void setupLoading(View v) {
        loadingView = (RelativeLayout) v.findViewById(R.id.rl_progress);
    }

    public void setLoading(boolean isLoading) {
        if(loadingView != null) loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    public void setProgressDialog(boolean isLoading) {
        try {
            if(isLoading) {
                if (progressDialog != null) progressDialog.show();
                else progressDialog = ProgressDialog.show(getActivity(), ApplicationConstants.EMPTY_STRING, getString(R.string.please_wait), true);
            } else {
                if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
            }
        } catch (Exception e) {
            Log.e("BaseFragment", "setProgressDialog: " + e.toString());
        }
    }

    protected void showTopSnackBar(String message, int bColor) {
        Snackbar snack = Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackbarView = snack.getView();
        snackbarView.setBackgroundColor(bColor);
//        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
//        textView.setTextColor(Color.WHITE);
//        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        snack.show();
    }

    protected void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView(), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    protected void showAlertDialog(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton("Ok", defaultDialogClickListener());

        if (!getActivity().isFinishing()) alertDialog.show();
    }

    protected void showAlertDialog(boolean setCancelable, String title, String message, DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                .setCancelable(setCancelable)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", positiveListener);
        if (!getActivity().isFinishing()) myAlertDialogTwo = alertDialog.show();
    }

/*    protected AlertDialog.Builder showAlertDialog(String title, String message) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton("Ok", defaultDialogClickListener());
    }*/

    protected void showAlertDialog(boolean setCancelable, String title, String message, String positiveBtnTxt, String negativeBtnTxt,
                                   DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        if(myAlertDialog != null && myAlertDialog.isShowing()) return;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
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
        myAlertDialogOne = myAlertDialog;
        if (!getActivity().isFinishing()) alertDialog.show();
    }

    protected void showAlertDialogForNotification(boolean setCancelable, String title, String message, String positiveBtnTxt, String negativeBtnTxt,
                                   DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
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
//        myAlertDialogOne = myAlertDialog;
        if (!getActivity().isFinishing()) myAlertDialog.show();
    }

    protected DialogInterface.OnClickListener defaultDialogClickListener() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        };
    }

    public void hideSoftKeyboard() {
        if(getActivity().getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

}

