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
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;

import com.transmit.authenticationcontroldemo.web.TokenServices;
import com.ts.sdk.api.SDK;

public class DevOptionsActivity extends FragmentActivity implements
        DevOptionsFragment.Listener {

    static private final String TAG = DevOptionsActivity.class.getName();

    // Tags for fragments management

    private static final String APP_DEV_OPT_FRAG_TAG = "APPDevOptTag";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        showAPPDevOptionsFragment();
    }

    // DevOptionsFragment.Listener

    @Override
    public void oobConfigurationSelected() {
        final AuthControlApplication app = (AuthControlApplication) getApplication();

        getConfigurationToken(app, new TokenServices.TokenListener() {
            @Override
            public void success(String _Token) {
                startActivity(new Intent(DevOptionsActivity.this, ConfigurationActivity.class).putExtra(ConfigurationActivity.KEY_EXTRAS_TOKEN, _Token));
            }

            @Override
            public void failure() {
                Log.e(TAG, "failed to get configuration token");
            }
        });
    }

    @Override
    public void backFromDevOptionsSelected() {
        finish();
    }

    /*
    PRIVATE HELPER METHODS
     */

    private void showAPPDevOptionsFragment() {
        Log.d(TAG,"Launching dev options fragment");

        DevOptionsFragment fr = DevOptionsFragment.newInstance();
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        tr.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        tr.replace(R.id._APP_authcontroldemo_frag_container, fr, APP_DEV_OPT_FRAG_TAG).commit();
    }

    private void getConfigurationToken (@NonNull final AuthControlApplication _app, @NonNull final TokenServices.TokenListener _listener) {
        String username = SDK.getInstance().getCurrentUsername();

        if (!TextUtils.isEmpty(username)) {
            TokenServices.getRegistrationToken(_app, username, _app.getPrefsApiToken(), _app.getPrefsApiTokenName(), SDK.getInstance().getRegistrationMenuContextData(username), new TokenServices.TokenListener() {
                @Override
                public void success(String _regToken) {
                    Log.d(TAG, "get configuration token succeeded");
                    _listener.success(_regToken);
                }

                @Override
                public void failure() {
                    Log.e(TAG, "get configuration token failed");
                    new AlertDialog.Builder(DevOptionsActivity.this)
                            .setTitle("Dev Options")
                            .setMessage("Failed to get cfg token.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .show();

                    _listener.failure();
                }
            });
        }
        else {
            new AlertDialog.Builder(this)
                    .setTitle("Dev Options")
                    .setMessage("Username is empty.\nEither login once or invoke the user management UI.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }
    }

}
