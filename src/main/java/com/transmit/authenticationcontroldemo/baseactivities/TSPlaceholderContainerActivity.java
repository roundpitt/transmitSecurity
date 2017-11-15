/*
 * Copyright (C) Transmit Security, LTD - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.transmit.authenticationcontroldemo.baseactivities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.transmit.authenticationcontroldemo.AuthControlApplication;
import com.transmit.authenticationcontroldemo.R;
import com.transmit.authenticationcontroldemo.web.CredentialsService;
import com.transmit.authenticationcontroldemo.web.TokenServices;
import com.ts.common.api.core.Error;
import com.ts.common.api.core.PlaceholderData;
import com.ts.common.api.core.authenticator.PlaceholderAuthenticatorBase;
import com.ts.common.api.ui.OperationListener;
import com.ts.common.api.ui.PlaceholderAuthenticationListener;
import com.ts.sdk.api.SDK;
import com.ts.sdk.api.ui.fragments.PlaceholderFragment;

public abstract class TSPlaceholderContainerActivity extends FragmentActivity implements
        PlaceholderAuthenticationListener,
        PlaceholderPasswordInputFragment.Listener {

    private static final String TAG = TSPlaceholderContainerActivity.class.getName();

    // Tags for fragments management

    protected static final String SDK_FRAG_TAG = "TSSDKTag";
    private static final String APP_PASSWORD_FRAG_TAG = "AppPasswordTag";

    protected PlaceholderData mPlaceholderData;
    private PlaceholderFragment mPendingPlaceholderFragment;

    protected ProgressDialog mProgressDialog = null;

    abstract protected void changeMethodFromPlaceholder ();

    @Override
    public void performAuthentication(@NonNull PlaceholderAuthenticatorBase _placeholderAuthenticator, @NonNull PlaceholderData _placeholderData) {
        Log.d(TAG, "App is requested to present an authenticator: " + _placeholderAuthenticator.typeName());
        mPlaceholderData = _placeholderData;
        mPendingPlaceholderFragment = removeSDKFragment();
        if (_placeholderAuthenticator.getDisplayTypeName().toLowerCase().equals("password")) {
            showAPPPasswordInputFragment();
        } else {
            Toast.makeText(this, "Simulating non password placeholder - " + _placeholderAuthenticator.typeName(), Toast.LENGTH_LONG).show();
            mProgressDialog = ProgressDialog.show(this, null, "Authenticating placeholder...", false, false);
            new DelayedCompletePlaceholderTask().execute();
        }

        new AlertDialog.Builder(this)
                .setTitle("Authentication")
                .setMessage("A placeholder authenticator was selected. Control was handed back to the application with the relevant authentication request.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    // PlaceholderPasswordInputFragment.Listener

    @Override
    public void changeMethodFromPasswordFragment() {
        // we're getting password as a password "placeholder" authenticator (so, we go back to the auth menu)
        changeMethodFromPlaceholder();
    }

    @Override
    public void setPasswordFromPasswordFragment(final String _password) {
        final AuthControlApplication app = (AuthControlApplication) getApplication();

        mProgressDialog = ProgressDialog.show(this, null, "Authenticating...", false, false);

        CredentialsService.validateCredentials(app, getUsername(), _password,
                new CredentialsService.CredsValidationListener() {
                    @Override
                    public void success() {
                        Log.d(TAG, "credentials validation succeeded");
                        // In case we do "external" (placeholder) authentication, we don't get an authentication
                        // token from the SDK.
                        // Hence, the application must get the registration token from its own server in order to be
                        // able to use the configuration or approvalsSelected APIs.
                        completePlaceholderAuthentication();
                    }

                    @Override
                    public void failure(boolean _wrongCreds) {
                        Log.e(TAG, "credentials validation failed");
                        mProgressDialog.dismiss();

                        String errorMessage;

                        if (_wrongCreds) {
                            errorMessage = "Invalid credentials entered!";
                        } else {
                            errorMessage = "Credentials validation failed";
                        }

                        Toast.makeText(TSPlaceholderContainerActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public String getUsername() {
        return SDK.getInstance().getCurrentUsername();
    }

    /*
    PRIVATE HELPER METHODS
     */

    private PlaceholderFragment removeSDKFragment() {
        Fragment authFragment = getSupportFragmentManager().findFragmentByTag(SDK_FRAG_TAG);

        if (null != authFragment) {
            getSupportFragmentManager().beginTransaction().remove(authFragment).commit();
        }

        return (PlaceholderFragment) authFragment;
    }

    private void showAPPPasswordInputFragment() {
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        PlaceholderPasswordInputFragment fr = new PlaceholderPasswordInputFragment();

        tr.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        tr.replace(R.id._APP_authcontroldemo_frag_container, fr, APP_PASSWORD_FRAG_TAG).commit();
    }

    private void completePlaceholderAuthentication() {
        final AuthControlApplication app = (AuthControlApplication) getApplication();

        getRegistrationToken(app, new TokenServices.TokenListener() {
            @Override
            public void success(String _regToken) {
                Log.d(TAG, "get registration token succeeded");
                if (null != mProgressDialog) {
                    mProgressDialog.dismiss();
                }
                FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
                tr.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                mPendingPlaceholderFragment.placeholderSuccess(_regToken, new OperationListener() {
                    @Override
                    public void operationSuccess() {
                        Log.d(TAG, "Operation success");
                        mPlaceholderData = null;
                    }

                    @Override
                    public void operationFailure(int _errorCode, Error _error) {
                        Log.e(TAG, "Operation failure " + _error);
                        new AlertDialog.Builder(TSPlaceholderContainerActivity.this)
                                .setTitle("Authentication failed")
                                .setMessage("Could not complete placeholder auth: " + _error)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                .show();
                    }
                });
                Fragment frag = (Fragment) mPendingPlaceholderFragment;
                tr.replace(R.id._APP_authcontroldemo_frag_container, frag, SDK_FRAG_TAG).commit();
            }

            @Override
            public void failure() {
                Log.e(TAG, "get registration token failed");
                mProgressDialog.dismiss();
                new AlertDialog.Builder(TSPlaceholderContainerActivity.this)
                        .setTitle("Authentication failed")
                        .setMessage("Could not get registration token!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .show();
            }
        });
    }

    // WEB SERVICES

    private void getRegistrationToken (@NonNull final AuthControlApplication _app, @NonNull final TokenServices.TokenListener _listener) {
        TokenServices.getRegistrationToken(_app, getUsername(), _app.getPrefsApiToken(), _app.getPrefsApiTokenName(), mPlaceholderData, new TokenServices.TokenListener() {
            @Override
            public void success(String _regToken) {
                Log.d(TAG, "get registration token succeeded");
                _listener.success(_regToken);
            }

            @Override
            public void failure() {
                Log.e(TAG, "get registration token failed");
                _listener.failure();
            }
        });
    }

    /*
    PRIVATE HELPER CLASSES
     */

    public class DelayedCompletePlaceholderTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            mProgressDialog.hide();
            completePlaceholderAuthentication();
        }
    }

}

