package com.sakewiz.android.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.sakewiz.android.R;
import com.sakewiz.android.common.constants.IPreferencesKeys;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dilshan_e on 30/05/2017.
 */
public class SplashActivity extends Activity {

    final String TAG = SplashActivity.this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            // Remove title bar
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            final SharedPreferences preferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

            // Remove notification bar
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_splash);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Class classObj = (preferences.contains(IPreferencesKeys.ACCESS_TOKEN)) ? MainActivity.class : SignUpActivity.class;
                            startActivity(new Intent(SplashActivity.this, classObj));
                            finish();
                        }
                    });
                }
            }, 2000);
        } catch (Exception ex) {
            Log.e(TAG, "onCreate: " + ex.toString());
        }
    }
}
