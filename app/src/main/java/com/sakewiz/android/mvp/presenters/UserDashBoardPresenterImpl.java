package com.sakewiz.android.mvp.presenters;

import android.app.Activity;
import android.util.Log;

import com.sakewiz.android.common.constants.ApplicationConstants;
import com.sakewiz.android.domain.Service;
import com.sakewiz.android.domain.UserDashBoardService;
import com.sakewiz.android.model.entities.response.BarFacadeResponse;
import com.sakewiz.android.model.entities.response.BreweryFacadeResponse;
import com.sakewiz.android.model.entities.response.FavouriteSakeResponse;
import com.sakewiz.android.model.entities.response.NotificationResponse;
import com.sakewiz.android.model.entities.response.UserDashboardResponse;
import com.sakewiz.android.model.rest.exception.RetrofitException;
import com.sakewiz.android.mvp.views.UserDashBoardView;
import com.sakewiz.android.mvp.views.View;
import com.sakewiz.android.utils.IScheduler;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by dilshan_e on 6/9/17.
 */
public class UserDashBoardPresenterImpl extends BasePresenter implements UserDashBoardPresenter{

    private final static String TAG = "DashBoardPresenterImpl";

    private UserDashBoardView mUserDashBoardView;

    public UserDashBoardPresenterImpl(Activity activityContext, Service userDashBoardService, IScheduler scheduler) {
        super(activityContext, userDashBoardService, scheduler);
    }

    @Override
    public void getBarFacade(String barId) {
        subscription = getBarFacadeObservable(barId).subscribe(getBarFacadeSubscriber());
    }

    @Override
    public void getBreweryFacade(String breweryId) {
        subscription = getBreweryFacadeObservable(breweryId).subscribe(getBreweryFacadeSubscriber());
    }

    @Override
    public void getUserDashboard() {
        subscription = getUserDashBoardObservable().subscribe(getUserDashBoardSubscriber());
    }

    @Override
    public void getUserNotifications(int pageSize, String lastId) {
        subscription = getUserNotificationsObservable(pageSize, lastId).subscribe(getUserNotificationsSubscriber());
    }


    @Override
    public void attachView(View v) {
        if (v instanceof UserDashBoardView) {
            mUserDashBoardView = (UserDashBoardView) v;
            mView = mUserDashBoardView;
        }
    }

    private UserDashBoardService getService() {
        return (UserDashBoardService) mService;
    }

    public Subscriber<BarFacadeResponse> getBarFacadeSubscriber() {
        return new DefaultSubscriber<BarFacadeResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    BarFacadeResponse response = error.getErrorBodyAs(BarFacadeResponse.class);
                    if (response == null) {
                        response = new BarFacadeResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mUserDashBoardView.showGetBarFacadeResponse(response);
                } catch (IOException ex) {
                    BarFacadeResponse exceptionResponse = new BarFacadeResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mUserDashBoardView.showGetBarFacadeResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(BarFacadeResponse response) {
                if (response != null) {
                    response.setSuccess(true);
//                    preferences.edit().putString(IPreferencesKeys.ACCESS_TOKEN, response.getToken()).apply();
                    mUserDashBoardView.showGetBarFacadeResponse(response);
                }
            }
        };
    }

    public Observable<BarFacadeResponse> getBarFacadeObservable(String barId) {
        try {
            return getService().getBarFacadeService(getAccessToken(),barId)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    public Subscriber<BreweryFacadeResponse> getBreweryFacadeSubscriber() {
        return new DefaultSubscriber<BreweryFacadeResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    BreweryFacadeResponse response = error.getErrorBodyAs(BreweryFacadeResponse.class);
                    if (response == null) {
                        response = new BreweryFacadeResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mUserDashBoardView.showGetBreweryFacadeResponse(response);
                } catch (IOException ex) {
                    BreweryFacadeResponse exceptionResponse = new BreweryFacadeResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mUserDashBoardView.showGetBreweryFacadeResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(BreweryFacadeResponse response) {
                if (response != null) {
                    response.setSuccess(true);
//                    preferences.edit().putString(IPreferencesKeys.ACCESS_TOKEN, response.getToken()).apply();
                    mUserDashBoardView.showGetBreweryFacadeResponse(response);
                }
            }
        };
    }

    public Observable<BreweryFacadeResponse> getBreweryFacadeObservable(String breweryId) {
        try {
            return getService().getBreweryFacadeService(getAccessToken(), breweryId)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Subscriber<UserDashboardResponse> getUserDashBoardSubscriber() {
        return new DefaultSubscriber<UserDashboardResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    UserDashboardResponse response = error.getErrorBodyAs(UserDashboardResponse.class);
                    if (response == null) {
                        response = new UserDashboardResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mUserDashBoardView.showGetUserDashboardResponse(response);
                } catch (IOException ex) {
                    UserDashboardResponse exceptionResponse = new UserDashboardResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mUserDashBoardView.showGetUserDashboardResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(UserDashboardResponse response) {
                if (response != null) {
                    response.setSuccess(true);
//                    preferences.edit().putString(IPreferencesKeys.ACCESS_TOKEN, response.getToken()).apply();
                    mUserDashBoardView.showGetUserDashboardResponse(response);
                }
            }
        };
    }

    public Observable<UserDashboardResponse> getUserDashBoardObservable() {
        try {
            return getService().getUserDashboardService(getAccessToken())
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Subscriber<NotificationResponse> getUserNotificationsSubscriber() {
        return new DefaultSubscriber<NotificationResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    NotificationResponse response = error.getErrorBodyAs(NotificationResponse.class);
                    if (response == null) {
                        response = new NotificationResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mUserDashBoardView.showUserNotificationsResponse(response);
                } catch (IOException ex) {
                    NotificationResponse exceptionResponse = new NotificationResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mUserDashBoardView.showUserNotificationsResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(NotificationResponse response) {
                if (response != null) {
                    response.setSuccess(true);
//                    preferences.edit().putString(IPreferencesKeys.ACCESS_TOKEN, response.getToken()).apply();
                    mUserDashBoardView.showUserNotificationsResponse(response);
                }
            }
        };
    }

    public Observable<NotificationResponse> getUserNotificationsObservable(int pageSize, String lastId) {
        try {
            return getService().getUserNotificationsService(getAccessToken(), pageSize, lastId)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }
}
