package com.transmit.authenticationcontroldemo.web;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonObject;
import com.transmit.authenticationcontroldemo.AuthControlApplication;
import com.ts.common.api.core.ConfigurationMenuContextData;
import com.ts.common.api.core.PlaceholderData;
import com.ts.common.api.core.RegisterDeviceContextData;
import com.ts.common.api.core.TokenContextData;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by eran on 11/22/15.
 */
public class TokenServices {
    private static final String ACCESS_TOKEN_URI_PATH = "/token";
    private static final String REGISTRATION_TOKEN_URI_PATH = "/token/create";

    private static final String TAG = TokenServices.class.getName();

    public interface TokenListener {
        void success (String _Token);
        void failure ();
    }

    /*
    REGISTRATION TOKEN
     */

    private static class GetRegistrationTokenRequest {
        private String context_data;
        private String purpose;

        public <T extends TokenContextData> GetRegistrationTokenRequest (@NonNull T _contextData) {
            context_data = _contextData.encode();
            purpose = _contextData.getPurpose();
        }
    }

    private interface GetRegistrationTokenService {
        @POST(REGISTRATION_TOKEN_URI_PATH)
        void getRegistrationToken(@Header("Authorization") String _authorizationHeader,
                                  @Query("aid") String _appid,
                                  @Query("uid") String _username,
                                  @Body GetRegistrationTokenRequest _request,
                                  Callback<JsonObject> _callback);
    }

    public static <T extends TokenContextData> void getRegistrationToken (@NonNull AuthControlApplication _app,
                                                                         @NonNull String _username,
                                                                         @NonNull final String _tsApiToken,
                                                                         @NonNull final String _tsApiTokenName,
                                                                         @NonNull final T _contextData,
                                                                         @NonNull final TokenListener _listener) {
        GetRegistrationTokenService service = new RetrofitServiceGenerator(_app).createService(GetRegistrationTokenService.class);

        GetRegistrationTokenRequest request = new GetRegistrationTokenRequest(_contextData);

        service.getRegistrationToken("TSToken " + _tsApiToken + "; tid=" + _tsApiTokenName, _app.getPrefsAppID(), _username, request, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonResponse, retrofit.client.Response response) {
                Log.d(TAG, "get registration token succeeded");
                JsonObject data = jsonResponse.get("data").getAsJsonObject();
                _listener.success(data.get("token").getAsString());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "get registration token failed: " + error.getMessage());
                _listener.failure();
            }
        });
    }


}
