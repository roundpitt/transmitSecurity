package com.transmit.authenticationcontroldemo.web;

import android.util.Log;

import com.jakewharton.retrofit.Ok3Client;
import com.transmit.authenticationcontroldemo.AuthControlApplication;

import java.net.URI;

import okhttp3.OkHttpClient;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;

/**
 * @author eran on 11/22/15.
 */
public class RetrofitServiceGenerator {
    private static final String TAG = RetrofitServiceGenerator.class.getName();

    private AuthControlApplication mApp;

    public RetrofitServiceGenerator (AuthControlApplication _app) {
        mApp = _app;
    }

    public String getServerBaseURI () {
        String uri = null;

        try {
            String pathPrefix = mApp.getPrefsServerPathPrefix();
            String path = "/api/v2";
            if (pathPrefix != null) {
                path = "/" + pathPrefix + "/api/v2";
            }
            uri = new URI(mApp.getPrefsIsSecuredConnection() ? "https" : "http",
                    null,
                    mApp.getPrefsServerUri(),
                    Integer.parseInt(mApp.getPrefsIsSecuredConnection() ? mApp.getPrefsHttpsPort() : mApp.getPrefsHttpPort()),
                    path,
                    null, null).toString();
        }
        catch (Exception err) {
            Log.e(TAG, "failed to get server uri: " + err.getMessage());
        }

        return uri;
    }

    public <S> S createService(Class<S> serviceClass) {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog(serviceClass.getName()))
                .setEndpoint(getServerBaseURI())
                .setClient(new Ok3Client(new OkHttpClient.Builder().build()));

        RestAdapter adapter = builder.build();

        return adapter.create(serviceClass);
    }
}
