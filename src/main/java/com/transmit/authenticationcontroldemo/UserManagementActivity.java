/*
 * Copyright (C) Transmit Security, LTD - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.transmit.authenticationcontroldemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.ts.sdk.api.SDK;
import com.ts.sdk.api.ui.UserManagementListener;

public class UserManagementActivity extends FragmentActivity implements
        UserManagementListener {

    // Tags for fragments management

    protected static final String SDK_FRAG_TAG = "TSSDKTag";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        showSDKUserManagementFragment();
    }

    // UserManagementListener

    @Override
    public void changeUserDone() {
        finish();
    }

    @Override
    public void changeUserCanceled() {
        finish();
    }

    /*
    PRIVATE HELPER METHODS
     */
    private void showSDKUserManagementFragment () {
        Fragment frag = SDK.getInstance().getUserManagementFragment(this);
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        tr.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        tr.replace(R.id._APP_authcontroldemo_frag_container, frag, SDK_FRAG_TAG).commit();
    }

}
