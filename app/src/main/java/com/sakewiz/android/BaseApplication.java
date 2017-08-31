package com.sakewiz.android;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Base64;

import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.twitter.sdk.android.core.Twitter;

import java.nio.charset.StandardCharsets;

import io.fabric.sdk.android.Fabric;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by dilshan_e on 29/05/2017.
 */
public class BaseApplication extends Application {

    private static BaseApplication baseApplication;
    private final String TAG = BaseApplication.this.getClass().getSimpleName();

    @Getter
    @Setter
    private String user_mail = null;

    @Getter
    @Setter
    private String currentLatitude = null;

    @Getter
    @Setter
    private String currentLongitude = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Twitter.initialize(this);
        baseApplication = (BaseApplication) getApplicationContext();
        initImageLoader(getApplicationContext());
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static void initImageLoader(Context context) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.defaultDisplayImageOptions(options);

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public String getBase64Key(String keyToEncode) {
        byte[] data = keyToEncode.getBytes(StandardCharsets.UTF_8);
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        base64 = "Basic "+base64;
        return base64.trim();
    }

    public BaseApplication() {
        super();
    }

    public static BaseApplication getBaseApplication() {
        return baseApplication;
    }


}