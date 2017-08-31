package com.sakewiz.android.mvp.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.sakewiz.android.BaseApplication;
import com.sakewiz.android.common.constants.ApplicationConstants;
import com.sakewiz.android.common.constants.IPreferencesKeys;
import com.sakewiz.android.domain.Service;
import com.sakewiz.android.model.rest.exception.RetrofitException;
import com.sakewiz.android.mvp.views.View;
import com.sakewiz.android.utils.IScheduler;

import rx.Subscription;
import rx.subscriptions.Subscriptions;

/**
 * Created by dilshan_e on 29/05/2017.
 */
public abstract class BasePresenter implements Presenter {
    protected Activity activity;
    protected Service mService;
    protected Subscription subscription = Subscriptions.empty();
    protected Subscription subscriptionCreate = Subscriptions.empty();
    protected Subscription subscriptionDelete = Subscriptions.empty();

    protected IScheduler scheduler;
    protected View mView;

    protected SharedPreferences preferences;
    private String access_token;

    protected BasePresenter(Activity activityContext, Service pService, IScheduler scheduler){
        this.activity = activityContext;
        this.mService = pService;
        this.scheduler = scheduler;

        this.preferences = activityContext.getSharedPreferences(activityContext.getPackageName(), Context.MODE_PRIVATE);
        this.access_token = preferences.getString(IPreferencesKeys.ACCESS_TOKEN, "");
    }

    public void unSubscribe(Subscription subscription) {
        if (subscription != null && subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public String getAccessToken() {
        if (access_token == null || access_token.equals("")) {
            Context mContext = BaseApplication.getBaseApplication();
            SharedPreferences preferences = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
            return preferences.getString(IPreferencesKeys.ACCESS_TOKEN, "");
        } else {
            return access_token;
        }
    }

    public String getExceptionMessage(Throwable e) {
        if (((RetrofitException) e).getKind() == RetrofitException.Kind.NETWORK) {
            return ApplicationConstants.ERROR_MSG_REST_NETWORK;
        } else if (((RetrofitException) e).getKind() == RetrofitException.Kind.HTTP) {
            return ApplicationConstants.ERROR_MSG_REST_HTTP;
        } else if (((RetrofitException) e).getKind() == RetrofitException.Kind.UNEXPECTED) {
            return ApplicationConstants.ERROR_MSG_REST_UNEXPECTED;
        }
        return null;
    }

    @Override
    public void onCreate() {}

    @Override
    public void onStart() {}

    @Override
    public void onStop() {}

    @Override
    public void attachView(View v) {
        mView =  v;
    }

    @Override
    public void onDestroy() {
        unSubscribe(subscription);
        unSubscribe(subscriptionCreate);
        unSubscribe(subscriptionDelete);
    }
}
