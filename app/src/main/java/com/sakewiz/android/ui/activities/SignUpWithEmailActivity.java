package com.sakewiz.android.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sakewiz.android.BaseApplication;
import com.sakewiz.android.R;
import com.sakewiz.android.common.CommonUtils;
import com.sakewiz.android.common.constants.ApplicationConstants;
import com.sakewiz.android.common.constants.IPreferencesKeys;
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

import java.nio.charset.StandardCharsets;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by dilshan_e on 30/05/2017.
 */
public class SignUpWithEmailActivity extends BaseActivity implements UserView {

    final String TAG = SignUpWithEmailActivity.this.getClass().getSimpleName();

    @Bind(R.id.username) EditText username;
    @Bind(R.id.password) EditText password;
    @Bind(R.id.btn_sing_in) Button signIn;
    @Bind(R.id.error_container) LinearLayout errorContainer;
    @Bind(R.id.txt_error) TextView errorText;

    private ProgressDialog progressDialog;
    protected Presenter presenter;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_signup_with_email);
            ButterKnife.bind(this);
            initializePresenter();
            setUpToolBar();

            preferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

            // show alert dialog when access token expired
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                if (bundle.containsKey("TOKEN_EX_MSG")) {
                    showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ALERT_MSG_TOKEN_EXPIRED);
                }
            }

            password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) signIn.performClick();
                    doLogin();
                    return false;
                }
            });

        } catch (Exception ex) {
            Log.e(TAG, "onCreate: " + ex.toString());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("username", username.getText().toString());
        outState.putString("password", password.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        username.setText(savedInstanceState.getString("username"));
        password.setText(savedInstanceState.getString("password"));
    }

    public void initializePresenter() {
        UserService mUserService = new UserServiceImpl(new SakeWizService());
        presenter = new UserPresenterImpl(SignUpWithEmailActivity.this, mUserService, new AppScheduler());
        presenter.attachView(SignUpWithEmailActivity.this);
        presenter.onCreate();
    }

    protected void setUpToolBar() {
        final View mCustomView = getLayoutInflater().inflate(R.layout.custom_actionbar_with_back, null);
        ((TextView) mCustomView.findViewById(R.id.title_text)).setText(getString(R.string.sign_in_activity_title));
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

    @OnTextChanged(R.id.username)
    void onUserNameTextChanged(CharSequence s, int start, int count, int after) {
        if(s.length() != 0){
            if(errorContainer.getVisibility()== View.VISIBLE)
                errorContainer.setVisibility(View.INVISIBLE);
        }
    }

    @OnTextChanged(R.id.password)
    void onPasswordTextChanged(CharSequence s, int start, int count, int after) {
        if(s.length() != 0){
            if(errorContainer.getVisibility()== View.VISIBLE)
                errorContainer.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.btn_sing_in)
    public void onSignInClick(View view) {
        CommonUtils.getInstance().hideKeyboard(this);
        doLogin();
    }

    @OnClick(R.id.txt_forgot_password)
    public void onForgotPasswordClick(View view) {
        startActivity(new Intent(SignUpWithEmailActivity.this, ForgotPasswordActivity.class));
    }

    private void doLogin() {
        if (!CommonUtils.getInstance().isNetworkConnected()) {
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        } else if (username.getText().toString().isEmpty()) {
            errorContainer.setVisibility(View.VISIBLE);
            errorText.setText(getString(R.string.register_validation_empty_handle));
        } else if (password.getText().toString().isEmpty()) {
            errorContainer.setVisibility(View.VISIBLE);
            errorText.setText(getString(R.string.login_validation_password));
        } else {
            performRequest(username.getText().toString(), password.getText().toString());
        }
    }

    private void performRequest(String username, String password) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            String text = username+":"+password;
            ((UserPresenter) presenter).doLogin(BaseApplication.getBaseApplication().getBase64Key(text));
        } else {
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        }
    }

    @Override
    public void showLoginResponse(LoginResponse loginResponse) {
        setProgressDialog(false);
        if (loginResponse.isSuccess()) {
            String text = loginResponse.getHndl()+":"+loginResponse.getPwd();
            preferences.edit().putString(IPreferencesKeys.ACCESS_TOKEN, BaseApplication
                    .getBaseApplication().getBase64Key(text)).apply();
            performUserInfoRequest(loginResponse);
        } else {
            if (loginResponse.isAPIError()) {
                errorContainer.setVisibility(View.VISIBLE);
                errorText.setText(loginResponse.getMessage());
            } else {
                errorContainer.setVisibility(View.VISIBLE);
                errorText.setText(loginResponse.getMessage());
            }
        }
    }

    @Override
    public void showResetPasswordNotifyResponse(ResetPwdNotifyResponse resetPwdNotifyResponse) {

    }

    @Override
    public void showGetUserDetailOfGivenUserResponse(UserDetailOfGivenUserResponse userDetailOfGivenUserResponse) {

    }

    public void performUserInfoRequest(LoginResponse userInfoResponse) {
//        setProgressDialog(false);
        startActivity(new Intent(SignUpWithEmailActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void showCreateNewUserResponse(CreateNewUserResponse createNewUserResponse) {}

    private boolean isValidEmail(String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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

    public void setProgressDialog(boolean isLoading) {
        if (isLoading) {
            if (progressDialog != null) progressDialog.show();
            else progressDialog = ProgressDialog.show(SignUpWithEmailActivity.this,
                    ApplicationConstants.EMPTY_STRING, "loading", true);
        } else {
            if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
        }
    }

    public void showMessage(String message) {}

}
