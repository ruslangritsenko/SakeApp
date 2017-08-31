package com.sakewiz.android.utils;

import android.support.v4.app.FragmentActivity;

/**
 * Created by dilshan_e on 6/1/2017.
 */
public class BaseBackPressedListener {

    private final FragmentActivity activity;

    public BaseBackPressedListener(FragmentActivity activity) {
        this.activity = activity;
    }

    /** fragment on back pressed interface */
    public interface OnBackPressedListener {
        void doBack();
    }
}
