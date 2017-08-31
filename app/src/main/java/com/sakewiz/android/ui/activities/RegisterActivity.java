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
import com.sakewiz.android.model.entities.request.CreateNewUserRequest;
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
public class RegisterActivity extends BaseActivity implements UserView{
    final String TAG = RegisterActivity.this.getClass().getSimpleName();

    @Bind(R.id.username) EditText username;
    @Bind(R.id.email) EditText email;
    @Bind(R.id.password) EditText password;
    @Bind(R.id.btn_register) Button register;
    @Bind(R.id.error_container) LinearLayout errorContainer;
    @Bind(R.id.txt_error) TextView errorText;

    private ProgressDialog progressDialog;
    protected Presenter presenter;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_register);
            ButterKnife.bind(this);
            initializePresenter();
            setUpToolBar();

            preferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

            password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) register.performClick();
//                    doRegister();
                    return false;
                }
            });

        } catch (Exception ex) {
            Log.e(TAG, "onCreate: " + ex.toString());
        }
    }

    public void initializePresenter() {
        UserService mUserService = new UserServiceImpl(new SakeWizService());
        presenter = new UserPresenterImpl(RegisterActivity.this, mUserService, new AppScheduler());
        presenter.attachView(RegisterActivity.this);
        presenter.onCreate();
    }

    protected void setUpToolBar() {
        final View mCustomView = getLayoutInflater().inflate(R.layout.custom_actionbar_with_back, null);
        ((TextView) mCustomView.findViewById(R.id.title_text)).setText(getString(R.string.register_activity_title));
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
    void onEmailTextChanged(CharSequence s, int start, int count, int after) {
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

    @OnClick(R.id.btn_register)
    public void onSignInClick(View view) {
        doRegister();
    }

    private void doRegister() {
        if (!CommonUtils.getInstance().isNetworkConnected()) {
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        } else if (username.getText().toString().isEmpty()) {
            errorContainer.setVisibility(View.VISIBLE);
            errorText.setText(getString(R.string.register_validation_empty_handle));
        } else if (email.getText().toString().isEmpty()) {
            errorContainer.setVisibility(View.VISIBLE);
            errorText.setText(getString(R.string.register_validation_empty_email));
        } else if (!isValidEmail(email.getText().toString())) {
            errorContainer.setVisibility(View.VISIBLE);
            errorText.setText(getString(R.string.login_validation_email));
        } else if (password.getText().toString().isEmpty()) {
            errorContainer.setVisibility(View.VISIBLE);
            errorText.setText(getString(R.string.login_validation_password));
        } else {
            performCreateNewUserRequest(username.getText().toString().trim(), email.getText()
                    .toString().trim(),password.getText().toString().trim());
        }
    }

    private void performCreateNewUserRequest(String handle, String email, String password) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setProgressDialog(true);
            CreateNewUserRequest createNewUserRequest = new CreateNewUserRequest();
            createNewUserRequest.setHndl(handle);
            createNewUserRequest.setEmail(email);
            createNewUserRequest.setPwd(password);
            createNewUserRequest.setLang("en");

            ((UserPresenter) presenter).createNewUser(BaseApplication.getBaseApplication()
                    .getBase64Key(ApplicationConstants.SYSTEM_PASSWORD_AND_USER_NAME),createNewUserRequest);
        } else {
            showAlertDialog(ApplicationConstants.WARNING, ApplicationConstants.ERROR_MSG_CONNECTION_LOST);
        }
    }

    @Override
    public void showCreateNewUserResponse(CreateNewUserResponse createNewUserResponse) {
        setProgressDialog(false);
        if (createNewUserResponse.isSuccess()) {
            String text = createNewUserResponse.getHndl()+":"+createNewUserResponse.getPwd();
            preferences.edit().putString(IPreferencesKeys.ACCESS_TOKEN, BaseApplication
                    .getBaseApplication().getBase64Key(text)).apply();
            performUserInfoRequest();
        } else {
            if (createNewUserResponse.isAPIError()) {
                errorContainer.setVisibility(View.VISIBLE);
                errorText.setText(createNewUserResponse.getMessage());
            } else {
                errorContainer.setVisibility(View.VISIBLE);
                errorText.setText(createNewUserResponse.getMessage());
            }
        }
    }

    @Override
    public void showLoginResponse(LoginResponse loginResponse) {
    }

    @Override
    public void showResetPasswordNotifyResponse(ResetPwdNotifyResponse resetPwdNotifyResponse) {

    }

    @Override
    public void showGetUserDetailOfGivenUserResponse(UserDetailOfGivenUserResponse userDetailOfGivenUserResponse) {

    }

    public void performUserInfoRequest() {
        setProgressDialog(false);
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();
    }

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
            else progressDialog = ProgressDialog.show(RegisterActivity.this,
                    ApplicationConstants.EMPTY_STRING, "loading", true);
        } else {
            if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
        }
    }

    public void showMessage(String message) {}


}
