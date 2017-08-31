package com.sakewiz.android.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sakewiz.android.BaseApplication;
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
import com.sakewiz.android.mvp.presenters.Presenter;
import com.sakewiz.android.mvp.presenters.UserPresenter;
import com.sakewiz.android.mvp.presenters.UserPresenterImpl;
import com.sakewiz.android.mvp.views.UserView;
import com.sakewiz.android.utils.AppScheduler;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by dilshan_e on 30/05/2017.
 */
public class ForgotPasswordActivity extends BaseActivity implements UserView {

    final String TAG = ForgotPasswordActivity.this.getClass().getSimpleName();

    private ProgressDialog progressDialog = null;
    protected Presenter presenter;

    @Bind(R.id.et_email) EditText email;
    @Bind(R.id.btn_reset_pw) Button btnResetPassword;
    @Bind(R.id.error_container) LinearLayout errorContainer;
    @Bind(R.id.txt_error) TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_reset_password);
            ButterKnife.bind(this);
            initializePresenter();
            setUpToolBar();
        } catch (Exception ex) {
            Log.e(TAG, "onCreate: " + ex.toString());
        }
    }

    private void initializePresenter() {
        UserService mUserService = new UserServiceImpl(new SakeWizService());
        presenter = new UserPresenterImpl(ForgotPasswordActivity.this, mUserService, new AppScheduler());
        presenter.attachView(ForgotPasswordActivity.this);
        presenter.onCreate();
    }

    protected void setUpToolBar() {
        final View mCustomView = getLayoutInflater().inflate(R.layout.custom_actionbar_with_back, null);
        ((TextView) mCustomView.findViewById(R.id.title_text)).setText(getString(R.string.forgot_password_activity_title));
        mCustomView.findViewById(R.id.imgVmain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mToolBar.addView(mCustomView);
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
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) presenter.onDestroy();
    }

    @OnTextChanged(R.id.et_email)
    void onEmailTextChanged(CharSequence s, int start, int count, int after) {
        if(s.length() != 0){
            if(errorContainer.getVisibility()== View.VISIBLE)
                errorContainer.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.btn_reset_pw)
    public void requestResetPassword(View view) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        if (!CommonUtils.getInstance().isNetworkConnected()) {
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        } else if (email.getText().toString().isEmpty()) {
            errorContainer.setVisibility(View.VISIBLE);
            errorText.setText(getString(R.string.rp_validation_email));
        } else if (!isValidEmail(email.getText().toString())) {
            errorContainer.setVisibility(View.VISIBLE);
            errorText.setText(getString(R.string.login_validation_email));
        } else {
            performRequest(email.getText().toString());
        }
    }

    private void performRequest(String email) {
        setProgressDialog(true);
        ((UserPresenter) presenter).doResetPasswordNotify(BaseApplication.getBaseApplication()
                .getBase64Key(ApplicationConstants.SYSTEM_PASSWORD_AND_USER_NAME), "", email);
    }

    private boolean isValidEmail(String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void setProgressDialog(boolean isLoading) {
        if (isLoading) {
            if (progressDialog != null) progressDialog.show();
            else
                progressDialog = ProgressDialog.show(ForgotPasswordActivity.this, ApplicationConstants.EMPTY_STRING, getString(R.string.please_wait), true);
        } else {
            if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
        }
    }

    @Override
    public void showLoginResponse(LoginResponse loginResponse) {}

    @Override
    public void showResetPasswordNotifyResponse(ResetPwdNotifyResponse resetPwdNotifyResponse) {
        setProgressDialog(false);
        if (resetPwdNotifyResponse.isSuccess()) {
//            TO do
            System.out.println("========================>> Success ");
        } else {
            if (resetPwdNotifyResponse.isAPIError()) {
                errorContainer.setVisibility(View.VISIBLE);
                errorText.setText(resetPwdNotifyResponse.getMessage());
            } else {
                errorContainer.setVisibility(View.VISIBLE);
                errorText.setText(resetPwdNotifyResponse.getMessage());
            }
        }
    }

    @Override
    public void showGetUserDetailOfGivenUserResponse(UserDetailOfGivenUserResponse userDetailOfGivenUserResponse) {

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

    @Override
    public void showCreateNewUserResponse(CreateNewUserResponse createNewUserResponse) {}

    public void showMessage(String message) {}
}
