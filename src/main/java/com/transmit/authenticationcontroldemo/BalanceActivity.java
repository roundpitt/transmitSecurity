/*
 * Copyright (C) Transmit Security, LTD - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.transmit.authenticationcontroldemo;

import android.support.annotation.NonNull;
import android.util.Log;

import com.transmit.authenticationcontroldemo.baseactivities.TSAuthenticationContainerActivity;
import com.ts.sdk.api.ui.AuthenticationListener;

public class BalanceActivity extends TSAuthenticationContainerActivity implements
        AuthenticationListener,
        BalanceFragment.Listener {

    final static private String TAG =  BalanceActivity.class.getName();

    // Tags for fragments management

    private static final String APP_BALANCE_FRAG_TAG = "APPBalanceTag";

    // IDs for SDK authentication policies

    private static final String REQUEST_ID_VALUE_BALANCE = "Balance";

    @Override
    protected void onResume() {
        super.onResume();

        showSDKAuthenticateFragment(false, null, REQUEST_ID_VALUE_BALANCE);
    }

    @Override
    public void authenticationComplete(@NonNull String _token) {
        Log.d(TAG, "authentication success");
        showAPPBalanceFragment();
    }

    @Override
    public void leaveBalanceFragmentSelected() {
        finish();
    }

    // TSPlaceholderContainerActivity

    @Override
    protected void changeMethodFromPlaceholder () {
        showSDKAuthenticateFragment(true, null, REQUEST_ID_VALUE_BALANCE);
    }

    /*
    PRIVATE HELPER METHODS
     */

    private void showAPPBalanceFragment() {
        Log.d(TAG, "Launching view balance fragment");
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id._APP_authcontroldemo_frag_container, BalanceFragment.newInstance(), APP_BALANCE_FRAG_TAG).commit();
    }

}
