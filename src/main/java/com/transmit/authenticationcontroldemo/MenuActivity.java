/*
 * Copyright (C) Transmit Security, LTD - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.transmit.authenticationcontroldemo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.transmit.authenticationcontroldemo.gcm.DemoGcmListenerService;
import com.ts.common.api.core.Error;
import com.ts.common.api.ui.OperationListener;
import com.ts.common.internal.core.logger.Log;
import com.ts.sdk.api.SDK;

public class MenuActivity extends FragmentActivity implements
        DemoAppMenuFragment.Listener {

    private static final String TAG = Log.getLogTag(MenuActivity.class);

    // Tags for fragments management

    private static final String APP_MENU_FRAG_TAG = "APPMenuTag";

    private ProgressDialog mProgressDialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        if (null == ((AuthControlApplication)getApplication()).getTSAuthToken()) {
//            new AlertDialog.Builder(this)
//                    .setTitle("Authentication")
//                    .setMessage("Authentication token was not provided for this action.")
//                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
//                        }
//                    })
//                    .show();
//
//            return;
//        }

        showAPPMenuFragment();
    }

    @Override
    public void devicesSelected() {
        startActivity(new Intent(this, DevicesActivity.class));
    }

    // DemoAppMenuFragment.Listener

    @Override
    public void viewBalanceSelected() {
        startActivity(new Intent(this, BalanceActivity.class));
    }

    @Override
    public void transferSelected() {
        startActivity(new Intent(this, TransferActivity.class));
    }

    @Override
    public void approvalsSelected() {
        startActivity(new Intent(this, ApprovalsActivity.class).setAction(DemoGcmListenerService.ACTION_NEW_APPROVAL));
    }

    @Override
    public void configurationSelected() {
        startActivity(new Intent(this, ConfigurationActivity.class));
    }

    @Override
    public void logoutSelected() {
        logout();
    }

    /*
    PRIVATE HELPER METHODS
     */

    private void logout() {
        final Runnable logoutOp = new Runnable() {
            @Override
            public void run() {
                ((AuthControlApplication)getApplication()).setTSAuthToken(null);
                finish();
            }
        };

        mProgressDialog = ProgressDialog.show(MenuActivity.this, null, "Logging out...", false, false);

        SDK.getInstance().logout(((AuthControlApplication)getApplication()).getTSAuthToken(), new OperationListener() {
            @Override
            public void operationSuccess() {
                Log.d(TAG, "logout success");
                mProgressDialog.dismiss();
                logoutOp.run();
            }

            @Override
            public void operationFailure(int _errorCode, Error _error) {
                Log.d(TAG, "logout failure: " + _error);
                mProgressDialog.dismiss();
                // We ignore the failure and do a "local logout" anyhow
                logoutOp.run();
            }
        });
    }

    public void showAPPMenuFragment() {
        Log.d(TAG,"Launching menu fragment");

        if (getSupportFragmentManager().findFragmentByTag(APP_MENU_FRAG_TAG) == null) {
            FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
            tr.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            tr.replace(R.id._APP_authcontroldemo_frag_container, DemoAppMenuFragment.newInstance(), APP_MENU_FRAG_TAG).commit();
        }
    }

}