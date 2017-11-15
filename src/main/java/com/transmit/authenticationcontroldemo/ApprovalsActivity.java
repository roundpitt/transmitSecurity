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

import com.transmit.authenticationcontroldemo.baseactivities.TSAuthenticationContainerActivity;
import com.transmit.authenticationcontroldemo.gcm.DemoGcmListenerService;
import com.ts.common.api.core.Error;
import com.ts.sdk.api.SDK;
import com.ts.sdk.api.ui.ApprovalsListener;

public class ApprovalsActivity extends TSAuthenticationContainerActivity implements
        ApprovalsListener {

    // IDs for SDK authentication policies

    private static final String REQUEST_ID_VALUE_PRE_APPROVE = "PreApprove";

    final static private String TAG =  ApprovalsActivity.class.getName();

    private String mToken;

    private void setToken (String _token) { mToken = _token; }

    private String getToken() {
        // TODO: remove this when the server can except approvals request with out an existing session.
        String token = ((AuthControlApplication)getApplication()).getTSAuthToken();

        if (null == token) {
            token = mToken;
        }

        return token;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        // TODO: remove this when the server can except approvals request with out an existing session.
        if (null != getToken()) {
            showSDKApprovalsFragment();
        }
        else {
            if (null != intent && intent.getAction().equals(DemoGcmListenerService.ACTION_NEW_APPROVAL)) {
                showSDKLoginFragment(false, null, REQUEST_ID_VALUE_PRE_APPROVE);
            }
            else {
                new AlertDialog.Builder(this)
                        .setTitle("Approvals")
                        .setMessage("Approvals are only available after authentication or by clicking a relevant push notification")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .show();
            }
        }
    }

    @Override
    public void authenticationComplete(@NonNull String _token) {
        setToken(_token);
        showSDKApprovalsFragment();
    }

    @Override
    protected void changeMethodFromPlaceholder() {
        finish();
    }

    // ApprovalsListener

    @Override
    public void fetchFailed(int _errorCode, Error _error) {
        Log.e(TAG, "An error was reported by the approvals fragment: " + _error);

        new AlertDialog.Builder(this)
                .setTitle("Approvals")
                .setMessage("Failed to fetch approvals: " + _error)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void approveFailed(int _errorCode, Error _error) {
        Log.e(TAG, "An error was reported by the approvals fragment: " + _error);
    }

    @Override
    public void declineFailed(int _errorCode, Error _error) {
        Log.e(TAG, "An error was reported by the approvals fragment: " + _error);
    }

    @Override
    public void deleteFailed(int _errorCode) {
        // DEPRECATED
    }

    @Override
    public void approvalsDone() {
        // We must be logged in at this stage so we can just display the menu fragment
        finish();
    }

    private void showSDKApprovalsFragment () {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id._APP_authcontroldemo_frag_container, getSDKApprovalsFragment(), SDK_FRAG_TAG).commit();
    }

    private Fragment getSDKApprovalsFragment() {
        return SDK.getInstance().getApprovalsFragment(this, null, null);
    }

}
