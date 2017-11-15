/*
 * Copyright (C) Transmit Security, LTD - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.transmit.authenticationcontroldemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.transmit.authenticationcontroldemo.baseactivities.TSPlaceholderContainerActivity;
import com.ts.common.api.core.Error;
import com.ts.common.api.core.authenticator.PlaceholderAuthenticatorBase;
import com.ts.sdk.api.SDK;
import com.ts.sdk.api.ui.ConfigurationListener;

public class ConfigurationActivity extends TSPlaceholderContainerActivity implements
        ConfigurationListener {

    public final static String KEY_EXTRAS_TOKEN = "token";

    final static private String TAG =  ConfigurationActivity.class.getName();

    private String mToken;

    protected String getToken () { return mToken; }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        mToken = intent.getStringExtra(KEY_EXTRAS_TOKEN);

        showSDKConfigurationFragment();
    }

    @Override
    protected void changeMethodFromPlaceholder() {
        finish();
    }

    // ConfigurationListener

    @Override
    public void configurationCanceled() {
        // We must be logged in at this stage so we can just display the menu fragment
        finish();
    }

    @Override
    public void configurationDone() {
        // We must be logged in at this stage so we can just display the menu fragment
        finish();
    }

    @Override
    public void configurationSuccess(@NonNull String _typeName) {
        Log.d(TAG, "received configuration success event for: " + _typeName);
    }

    @Override
    public void performConfiguration(PlaceholderAuthenticatorBase _placeholderAuthenticator) {
        Log.d(TAG, "App is requested to configure an authenticator: " + _placeholderAuthenticator.typeName());

        Toast.makeText(this, "Placeholder authenticator registration is not supported!", Toast.LENGTH_LONG).show();

        new AlertDialog.Builder(this)
                .setTitle("Configuration")
                .setCancelable(false)
                .setMessage("A placeholder authenticator was selected. Control was handed back to the application with the relevant registration request.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    @Override
    public void performUnregistration(PlaceholderAuthenticatorBase _placeholderAuthenticator) {
        Log.d(TAG, "App is requested to unregister an authenticator: " + _placeholderAuthenticator.typeName());

        Toast.makeText(this, "Placeholder authenticator unregistration is not supported!", Toast.LENGTH_LONG).show();

        new AlertDialog.Builder(this)
                .setTitle("Configuration")
                .setCancelable(false)
                .setMessage("A placeholder authenticator was selected. Control was handed back to the application with the relevant unregistration request.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    @Override
    public void configurationFailed(int _errorCode, Error _error) {
        Log.e(TAG, "An error was reported by the configuration fragment: " + _error);

        new AlertDialog.Builder(this)
                .setTitle("Configuration failed")
                .setMessage("The application received an error report from the configuration process: " + _error)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    /*
    PRIVATE HELPER METHODS
     */

    private void showSDKConfigurationFragment() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id._APP_authcontroldemo_frag_container, getSDKConfigurationFragment(), SDK_FRAG_TAG).commit();
    }

    private Fragment getSDKConfigurationFragment() {
        Fragment res = SDK.getInstance().getConfigurationFragment(this, null, getToken() /*, AuthenticatorType.Configurable.PIN*/);
        return res;
    }

}
