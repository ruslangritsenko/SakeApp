package com.sakewiz.android.mvp.presenters;

import android.app.Activity;
import android.util.Log;

import com.sakewiz.android.common.constants.ApplicationConstants;
import com.sakewiz.android.domain.Service;
import com.sakewiz.android.domain.UserService;
import com.sakewiz.android.model.entities.request.CreateNewUserRequest;
import com.sakewiz.android.model.entities.response.CreateNewUserResponse;
import com.sakewiz.android.model.entities.response.LoginResponse;
import com.sakewiz.android.model.entities.response.ResetPwdNotifyResponse;
import com.sakewiz.android.model.entities.response.UserDetailOfGivenUserResponse;
import com.sakewiz.android.model.rest.exception.RetrofitException;
import com.sakewiz.android.mvp.views.UserView;
import com.sakewiz.android.mvp.views.View;
import com.sakewiz.android.utils.IScheduler;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by dilshan_e on 29/05/2017.
 */
public class UserPresenterImpl extends BasePresenter implements UserPresenter {

    private final static String TAG = "UserPresenterImpl";

    private UserView mUserView;

    public UserPresenterImpl(Activity activityContext, Service userAccountService, IScheduler scheduler) {
        super(activityContext, userAccountService, scheduler);
    }

    @Override
    public void createNewUser(String authorizationKey, CreateNewUserRequest createNewUserRequest) {
        subscription = createNewUserObservable(authorizationKey, createNewUserRequest).subscribe(createNewUserSubscriber());
    }

    @Override
    public void doLogin(String authorizationKey) {
        subscription = doLoginObservable(authorizationKey).subscribe(doLoginSubscriber());
    }

    @Override
    public void doResetPasswordNotify(String authorizationKey, String userHandle, String email) {
        subscription = doResetPasswordNotifObservable(authorizationKey, userHandle, email).subscribe(doResetPasswordNotifSubscriber());
    }

    @Override
    public void getUserDetailOfGivenUser(String userHandle) {
        subscription = getUserDetailOfGivenUserObservable(userHandle).subscribe(getUserDetailOfGivenUserSubscriber());
    }

    @Override
    public void attachView(View v) {
        if (v instanceof UserView) {
            mUserView = (UserView) v;
            mView = mUserView;
        }
    }

    public Subscriber<LoginResponse> doLoginSubscriber() {
        return new DefaultSubscriber<LoginResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    LoginResponse response = error.getErrorBodyAs(LoginResponse.class);
                    if (response == null) {
                        response = new LoginResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mUserView.showLoginResponse(response);
                } catch (IOException ex) {
                    LoginResponse exceptionResponse = new LoginResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mUserView.showLoginResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(LoginResponse response) {
                if (response != null) {
                    response.setSuccess(true);
//                    preferences.edit().putString(IPreferencesKeys.ACCESS_TOKEN, response.getToken()).apply();
                    mUserView.showLoginResponse(response);
                }
            }
        };
    }

    public Observable<LoginResponse> doLoginObservable(String authorizationKey) {
        try {
            return getService().doLoginService(authorizationKey)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Subscriber<CreateNewUserResponse> createNewUserSubscriber() {
        return new DefaultSubscriber<CreateNewUserResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    CreateNewUserResponse response = error.getErrorBodyAs(CreateNewUserResponse.class);
                    if (response == null) {
                        response = new CreateNewUserResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mUserView.showCreateNewUserResponse(response);
                } catch (IOException ex) {
                    CreateNewUserResponse exceptionResponse = new CreateNewUserResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mUserView.showCreateNewUserResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(CreateNewUserResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mUserView.showCreateNewUserResponse(response);
                }
            }
        };
    }

    public Observable<CreateNewUserResponse> createNewUserObservable(String authorizationKey, CreateNewUserRequest createNewUserRequest) {
        try {
            return getService().createNewUserService(authorizationKey,createNewUserRequest)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Subscriber<ResetPwdNotifyResponse> doResetPasswordNotifSubscriber() {
        return new DefaultSubscriber<ResetPwdNotifyResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    ResetPwdNotifyResponse response = error.getErrorBodyAs(ResetPwdNotifyResponse.class);
                    if (response == null) {
                        response = new ResetPwdNotifyResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mUserView.showResetPasswordNotifyResponse(response);
                } catch (IOException ex) {
                    ResetPwdNotifyResponse exceptionResponse = new ResetPwdNotifyResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mUserView.showResetPasswordNotifyResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(ResetPwdNotifyResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mUserView.showResetPasswordNotifyResponse(response);
                }
            }
        };
    }

    public Observable<ResetPwdNotifyResponse> doResetPasswordNotifObservable(String authorizationKey, String userHandle, String email) {
        try {
            return getService().doResetPasswordNotifyService(authorizationKey,userHandle, email)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Subscriber<UserDetailOfGivenUserResponse> getUserDetailOfGivenUserSubscriber() {
        return new DefaultSubscriber<UserDetailOfGivenUserResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    UserDetailOfGivenUserResponse response = error.getErrorBodyAs(UserDetailOfGivenUserResponse.class);
                    if (response == null) {
                        response = new UserDetailOfGivenUserResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mUserView.showGetUserDetailOfGivenUserResponse(response);
                } catch (IOException ex) {
                    UserDetailOfGivenUserResponse exceptionResponse = new UserDetailOfGivenUserResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mUserView.showGetUserDetailOfGivenUserResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(UserDetailOfGivenUserResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mUserView.showGetUserDetailOfGivenUserResponse(response);
                }
            }
        };
    }

    public Observable<UserDetailOfGivenUserResponse> getUserDetailOfGivenUserObservable(String userHandle) {
        try {
            return getService().getUserDetailOfGivenUserService(getAccessToken(),userHandle)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private UserService getService() {
        return (UserService) mService;
    }
}