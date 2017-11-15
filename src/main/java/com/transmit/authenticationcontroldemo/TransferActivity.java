/*
 * Copyright (C) Transmit Security, LTD - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.transmit.authenticationcontroldemo;

import android.support.annotation.NonNull;
import android.util.Log;

import com.transmit.authenticationcontroldemo.baseactivities.TSAuthenticationContainerActivity;

import java.util.HashMap;

public class TransferActivity extends TSAuthenticationContainerActivity implements
    TransferFragment.Listener {

    final static private String TAG =  TransferActivity.class.getName();

    // Tags for fragments management

    private static final String APP_TRANSFER_FRAG_TAG = "APPTransferTag";

    // IDs for SDK authentication policies

    private static final String REQUEST_ID_VALUE_TRANSFER = "Transfer";

    // Params for transfer requests

    private static final String PARAM_NAME_AMOUNT = "amount";
    private static final String PARAM_NAME_RECIPIENT = "recipient";
    private static final String PARAM_NAME_COMMENT = "comment";

    @Override
    protected void onResume() {
        super.onResume();

        showAPPTransferFragment();
    }

    @Override
    public void authenticationComplete(@NonNull String _token) {
        Log.d(TAG, "authentication success");
        finish();
    }

    @Override
    public void doTransferSelected(CharSequence _to, int _amount, CharSequence _comment) {
        Log.d(TAG, "Launching authentication for transfer");
        HashMap<String, Object> params = new HashMap<>();
        params.put(PARAM_NAME_RECIPIENT, _to);
        params.put(PARAM_NAME_AMOUNT, _amount);
        params.put(PARAM_NAME_COMMENT, _comment);

        showSDKAuthenticateFragment(false, params, REQUEST_ID_VALUE_TRANSFER);
    }

    @Override
    public void cancelTransferSelected() {
        finish();
    }

    // TSPlaceholderContainerActivity

    @Override
    protected void changeMethodFromPlaceholder () {
        showSDKAuthenticateFragment(true, null, REQUEST_ID_VALUE_TRANSFER);
    }

    /*
    PRIVATE HELPER METHODS
    */

    private void showAPPTransferFragment () {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id._APP_authcontroldemo_frag_container, TransferFragment.newInstance(), APP_TRANSFER_FRAG_TAG).commit();
    }
}
