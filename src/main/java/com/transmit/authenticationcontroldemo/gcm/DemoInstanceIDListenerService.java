package com.transmit.authenticationcontroldemo.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

public class DemoInstanceIDListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Intent intent = new Intent(this, DemoRegistrationIntentService.class);
        startService(intent);
    }
}
