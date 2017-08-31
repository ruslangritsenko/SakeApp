package com.sakewiz.android.mvp.presenters;


import com.sakewiz.android.mvp.views.View;

/**
 * Created by dilshan_e on 29/05/2017.
 */
public interface Presenter<T> {
    void onCreate();

    void onStart();

    void onStop();

    void onDestroy();

    void attachView(View v);
}
