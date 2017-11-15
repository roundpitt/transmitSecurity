/*
 * Copyright (C) Transmit Security, LTD - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.transmit.authenticationcontroldemo.baseactivities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.transmit.authenticationcontroldemo.R;
import com.ts.common.api.core.Error;
import com.ts.sdk.api.SDK;
import com.ts.sdk.api.ui.AuthenticationListener;

import org.json.JSONObject;

import java.util.HashMap;

public abstract class TSAuthenticationContainerActivity extends TSPlaceholderContainerActivity implements
        AuthenticationListener {

    final static private String TAG =  TSAuthenticationContainerActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    @Override
    public void forgotPassword() {
        new AlertDialog.Builder(this)
                .setTitle("Authentication")
                .setMessage("Password reset/restore was requested. Control was handed back to the application with the relevant request.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void onJSONDataReceived(JSONObject _data) {
        Log.d(TAG, "Received JSON data: " + _data);
    }

    @Override
    public abstract void authenticationComplete(@NonNull String _token);

    @Override
    public void authenticationFailed(int _errorCode, Error _error) {
        new AlertDialog.Builder(this)
                .setTitle("Authentication failed")
                .setMessage("The application received an error report from the authentication process: " + _error)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void authenticationCanceled() {
        finish();
    }

    protected void showSDKAuthenticateFragment(boolean _forceMenu, @Nullable HashMap<String, Object> _params, String _requestID) {
        Fragment frag = getSDKAuthenticationFragment(_forceMenu, _params, _requestID);
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        tr.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        tr.add(R.id._APP_authcontroldemo_frag_container, frag, SDK_FRAG_TAG).commit();
    }

    protected Fragment getSDKAuthenticationFragment(boolean _forceMenu, @Nullable HashMap<String, Object> _params, @Nullable String _requestID) {
        Fragment res = SDK.getInstance().getAuthenticationFragment(this, null, null, _forceMenu, _requestID, _params);
        return res;
    }


    protected Fragment getSDKLoginFragment(boolean _forceMenu, @Nullable HashMap<String, Object> _params, @Nullable String _requestID) {
        //Log.d(TAG, String.format("Registered authenticators for user '%1$s' are %2$s.", _username, SDK.getInstance().getRegisteredAuthenticators(_username)));

        Fragment res = SDK.getInstance().getLoginFragment(this, null, _forceMenu, _requestID, _params);
        return res;
    }

    protected void showSDKLoginFragment(boolean _forceMenu, @Nullable HashMap<String, Object> _params, @Nullable String _requestID) {
        Fragment frag = getSDKLoginFragment(_forceMenu, _params, _requestID);
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        tr.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        tr.add(R.id._APP_authcontroldemo_frag_container, frag, SDK_FRAG_TAG).commit();
    }

}
