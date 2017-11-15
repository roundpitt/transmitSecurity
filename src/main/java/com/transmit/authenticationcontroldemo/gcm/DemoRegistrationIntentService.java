package com.transmit.authenticationcontroldemo.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.transmit.authenticationcontroldemo.R;
import com.ts.sdk.api.SDK;

import java.io.IOException;

public class DemoRegistrationIntentService extends IntentService {
    private static final String TAG = DemoRegistrationIntentService.class.getName();

    public DemoRegistrationIntentService() {
        super("AuthControlGCMRegistrationIntentService");
    }

    public DemoRegistrationIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Received intent, retrieving new token");
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(getString(R.string.gcm_sender_id), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.d(TAG, "Received new token, updating SDK");
            SDK.getInstance().setPushToken(token);
        } catch (IOException e) {
            Log.wtf("Could not get token", e);
        }
    }
}
