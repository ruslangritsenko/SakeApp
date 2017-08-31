package com.sakewiz.android.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.sakewiz.android.R;
import com.sakewiz.android.common.constants.ApplicationConstants;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dilshan_e on 30/05/2017.
 */
public class SignUpActivity extends BaseActivity {

    final String TAG = SignUpActivity.this.getClass().getSimpleName();

    CallbackManager callbackManager;

    List<String> fbPermissionList = Arrays.asList("public_profile", "user_friends");

    private TwitterAuthClient mTwitterAuthClient;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            callbackManager = CallbackManager.Factory.create();
            TwitterCore.getInstance();

            setContentView(R.layout.activity_signup);
            ButterKnife.bind(this);

            mTwitterAuthClient = new TwitterAuthClient();

            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            // If the access token is available already assign it.
                            String token = loginResult.getAccessToken().getToken();
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            showMessage(SignUpActivity.this, token);
                        }

                        @Override
                        public void onCancel() {
                            showErrorMessage(SignUpActivity.this, getString(R.string.cancel_fb_login));
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            showErrorMessage(SignUpActivity.this, exception.toString());
                        }
                    });

        } catch (Exception ex) {
            Log.e(TAG, "onCreate: " + ex.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
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
        super.onDestroy();
    }

    @OnClick(R.id.layout_sing_in_with_twitter)
    public void onClickLayoutSignInWithTwitter(View view) {
        loginToTwitter();
    }

    @OnClick(R.id.btn_sing_in_with_twitter)
    public void onClickSignInWithTwitter(View view) {
        loginToTwitter();
    }

    @OnClick(R.id.layout_sing_in_with_fb)
    public void onClickLayoutSignInWithFacebook(View view) {
        LoginManager.getInstance().logInWithReadPermissions(this, fbPermissionList);
    }

    @OnClick(R.id.btn_sing_in_with_fb)
    public void onClickBtnSignInWithFacebook(View view) {
        LoginManager.getInstance().logInWithReadPermissions(this, fbPermissionList);
    }

    @OnClick(R.id.layout_sing_in_with_email)
    public void onClickLayoutSignInWithEmail(View view) {
        startActivity(new Intent(SignUpActivity.this, SignUpWithEmailActivity.class));
    }

    @OnClick(R.id.btn_sing_in_with_email)
    public void onClickBtnSignInWithEmail(View view) {
        startActivity(new Intent(SignUpActivity.this, SignUpWithEmailActivity.class));
    }

    @OnClick(R.id.txt_register_with_email)
    public void  onClickRegisterWithEmail(View view){
        startActivity(new Intent(SignUpActivity.this, RegisterActivity.class));
    }

    private void loginToTwitter() {
        mTwitterAuthClient.authorize(SignUpActivity.this, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> twitterSessionResult) {
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
//                String secret = authToken.secret;
                showMessage(SignUpActivity.this, token);
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            }

            @Override
            public void failure(TwitterException exception) {
                showMessage(SignUpActivity.this, exception.getMessage());
            }
        });
    }

    public void setProgressDialog(boolean isLoading) {
        if (isLoading) {
            if (progressDialog != null) progressDialog.show();
            else progressDialog = ProgressDialog.show(SignUpActivity.this,
                    ApplicationConstants.EMPTY_STRING, "loading", true);
        } else {
            if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
        }
    }
}
