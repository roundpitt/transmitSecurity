package com.transmit.authenticationcontroldemo.web;

import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.transmit.authenticationcontroldemo.AuthControlApplication;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Header;

/**
 * Created by eran on 11/23/15.
 */
public class CredentialsService {
    private static final String CREDS_VALIDATION_URI_PATH = "/mng/demo_user/auth";

    private static final String TAG = CredentialsService.class.getName();

    public interface CredsValidationListener {
        void success();
        void failure(boolean _wrongCreds);
    }

    private interface CredsValidationService {
        @GET(CREDS_VALIDATION_URI_PATH)
        void validateCredentials(@Header("Authorization") String creds, retrofit.Callback<Object> _callback);
    }

    public static void validateCredentials(@NonNull final AuthControlApplication _app,
                                           @NonNull final String _username,
                                           @NonNull String _password,
                                           @NonNull final CredsValidationListener _listener) {

        CredsValidationService service = new RetrofitServiceGenerator(_app).createService(CredsValidationService.class);

        service.validateCredentials("Basic " + Base64.encodeToString((_username + ":" + _password).getBytes(), Base64.NO_WRAP), new Callback<Object>() {
            @Override
            public void success(Object emptyResponse, retrofit.client.Response response) {
                Log.d(TAG, "credentials validation succeeded");
                _listener.success();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "credentials validation failed: " + error.getMessage());
                _listener.failure(null != error.getResponse() && 401 == error.getResponse().getStatus());
            }
        });
    }

}
