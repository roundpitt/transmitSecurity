package com.transmit.authenticationcontroldemo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.transmit.authenticationcontroldemo.baseactivities.TSAuthenticationContainerActivity;
import com.transmit.authenticationcontroldemo.gcm.DemoGcmListenerService;
import com.transmit.authenticationcontroldemo.gcm.DemoRegistrationIntentService;
import com.ts.common.api.core.Error;
import com.ts.common.api.ui.AuthenticationCompletionListener;
import com.ts.sdk.api.SDK;
import com.ts.sdk.api.ui.AuthenticationListener;

public class MainActivity extends TSAuthenticationContainerActivity implements
        AuthenticationCompletionListener,
        AuthenticationListener,
        DemoAppLandingFragment.Listener {

    private static final String TAG = MainActivity.class.getName();

    // Tags for fragments management

    private static final String APP_LANDING_FRAG_TAG = "APPLandingTag";

    // IDs for SDK authentication policies

    private static final String REQUEST_ID_VALUE_LOGIN = "Login";

    // Permissions related

    private static final int MY_PERMISSIONS_REQUEST = 1;

    private static String[] PERMISSIONS = new String[] {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private boolean mPendingApprovalPush = false;

    // TSPlaceholderContainerActivity

    @Override
    protected void changeMethodFromPlaceholder () {
        login(true);
    }

    // AuthenticationCompletionListener

    @Override
    public void authenticationComplete(@NonNull String _token, boolean _hasPendingApprovals) {
        Log.d(TAG, "Authentication completed");

        // we don't want to turn this flag off in case a user clicked an "old" notification, since we want him to get into the approvals fragment
        if (!mPendingApprovalPush) {
            mPendingApprovalPush = _hasPendingApprovals;
        }

        authenticated(true, _token);
    }

    // AuthenticationListener

    @Override
    public void authenticationComplete(@NonNull String _token) {
        throw new IllegalStateException("Should not be called since AuthenticationCompletionListener is implemented");
    }

    @Override
    public void authenticationFailed(int _errorCode, Error _error) {
        Log.e(TAG, "An error was reported by the authentication fragment: " + _error);

        mPendingApprovalPush = false;

        String title;

        switch (_errorCode) {
            case Error.ACCESS_REJECTED:
                title = "Access Rejected";
                Log.e(TAG, "rejection info: " + _error.getAuthenticationRejectionInfo());
                break;

            case Error.ALL_AUTHENTICATORS_LOCKED:
                title = "Authenticators Locked";
                break;

            case Error.NO_REGISTERED_AUTHENTICATORS:
                title = "Unregistered Authenticators";
                break;

            default:
                title = "Authentication Failed";
        }

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage("The application received an error report from the authentication process: " + _error)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        showAPPLandingFragment();
                    }
                })
                .show();

        authenticated(false, null);
    }

    @Override
    public void authenticationCanceled() {
        Log.d(TAG, "authentication canceled");

        // if we're doing login, we're probably arriving from the landing page
        showAPPLandingFragment();
    }

    @Override
    public void forgotPassword() {
        new AlertDialog.Builder(this)
                .setTitle("Authentication")
                .setMessage("Password reset/restore was requested. Control was handed back to the application with the relevant request.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();

        authenticated(false, null);
        showAPPLandingFragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int errorCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

        if (errorCode == ConnectionResult.SUCCESS) {
            Log.d(TAG, "Google play services are available, trying to get push token");
            Intent regIntent = new Intent(this, DemoRegistrationIntentService.class);
            startService(regIntent);
        } else {
            Log.w(TAG, "No google play services, push notifications are disabled, error: " + errorCode);
        }

        try {
            // unlock android key store
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                startActivity(new Intent("android.credentials.UNLOCK"));
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                startActivity(new Intent("com.android.credentials.UNLOCK"));
            }
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "No UNLOCK activity: " + e.getMessage(), e);
        }

        askForPermissions();
    }

    // FragmentActivity

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, at least for phone, yay!
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mPendingApprovalPush) {
                                showApprovals();
                            }
                            else {
                                showAPPLandingFragment();
                            }
                        }
                    }, 100);

                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("Transmit Security Demo")
                            .setMessage("All permissions must be granted")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    askForPermissions();
                                }
                            }).show();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    // Activity

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id._TS_authcontrol_demo_action_clear_all:
                clearAll(true);
                return true;
            case R.id._TS_authcontrol_demo_action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id._TS_authcontrol_demo_action_about:
                about();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // DemoAppLandingFragment.Listener

    @Override
    public void loginSelected() {
        login(false);
    }

    @Override
    public void changeUserSelected() {
        startActivity(new Intent(this, UserManagementActivity.class));
    }

    @Override
    public void changeAuthMethodSelected() {
        mPlaceholderData = null;
        login(true);
    }

    @Override
    public void devOptionsSelected() {
        startActivity(new Intent(this, DevOptionsActivity.class));
    }

    /*
     PRIVATE API
     */

    // Fragments management utilities

    public void showAPPLandingFragment() {
        Log.d(TAG,"Launching landing fragment");

        DemoAppLandingFragment fr = DemoAppLandingFragment.newInstance();
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        tr.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        tr.replace(R.id._APP_authcontroldemo_frag_container, fr, APP_LANDING_FRAG_TAG).commit();
    }

    public void authenticated(boolean _status, String _token) {
        Log.d(TAG, String.format("Authenticated: %s", Boolean.toString(_status)));
        ((AuthControlApplication)getApplication()).setTSAuthToken(_token);

        // This may be called before onCreateOptionsMenu(), e.g. on startup when the connection fails

        if (_status) {
            showAPPLandingFragment();
            startActivity(new Intent(this, QuickViewActivity.class));

            if (mPendingApprovalPush) {
                showApprovals();
            }
        }

    }

    private void login(boolean _forceMenu) {
        Log.d(TAG, "Demo Activity logging in");

        showSDKLoginFragment(_forceMenu, null, REQUEST_ID_VALUE_LOGIN);
    }

    private void showApprovals () {
        mPendingApprovalPush = false;
        startActivity(new Intent(this, ApprovalsActivity.class).setAction(DemoGcmListenerService.ACTION_NEW_APPROVAL));
    }

    private void about () {
        new AlertDialog.Builder(this)
                .setTitle("Transmit Security SDK")
                .setMessage("Version: " + SDK.getInstance().getVersion())
                .setPositiveButton(android.R.string.ok, null).show();
    }

    private void clearAll (boolean _clearUserData) {
        if (_clearUserData) {
            SDK.getInstance().clearAll();
        }

        ((AuthControlApplication)getApplication()).setTSAuthToken(null);

        showAPPLandingFragment();
    }

    private void askForPermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    PERMISSIONS,
                    MY_PERMISSIONS_REQUEST);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        } else {
            if (mPendingApprovalPush) {
                showApprovals();
            }
            else {
                showAPPLandingFragment();
            }
        }
    }
}
