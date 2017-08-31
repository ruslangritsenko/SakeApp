package com.sakewiz.android.model.rest;

import com.sakewiz.android.common.constants.DomainConstants;
import com.sakewiz.android.model.rest.exception.RxErrorHandlingCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dilshan_e on 29/05/2017.
 */
public class SakeWizService {

    private SakeWizAPI mSakeWizAPI;

    public SakeWizService() {
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(DomainConstants.SERVER_URL)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .build();

        mSakeWizAPI = restAdapter.create(SakeWizAPI.class);
    }

    private OkHttpClient getClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(); // Log Requests and Responses
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(logging);

        return client.build();
    }

    public SakeWizAPI getApi() {
        return mSakeWizAPI;
    }
}