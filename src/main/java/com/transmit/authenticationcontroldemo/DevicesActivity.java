package com.transmit.authenticationcontroldemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ts.common.api.core.Error;
import com.ts.sdk.api.SDK;
import com.ts.sdk.api.ui.DeviceManagementListener;

import static com.transmit.authenticationcontroldemo.UserManagementActivity.SDK_FRAG_TAG;

public class DevicesActivity extends FragmentActivity implements DeviceManagementListener {

    private FrameLayout mFlRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.devices_activity);
        mFlRoot = (FrameLayout) findViewById(R.id.root);

        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        tr.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        tr.add(mFlRoot.getId(), getSDKDevicesFragment(), SDK_FRAG_TAG).commit();
    }

    protected Fragment getSDKDevicesFragment() {
       /* Fragment res = SDK.getInstance().getDeviceManagementFragment(this, null, AuthControlApplication.getInstance().getTSAuthToken());
        return res;*/
       return null;
    }

    @Override
    public void deviceManagementInteractionCompleted() {
        finish();
    }

    @Override
    public void deviceManagementFailed(Error error) {
        Toast.makeText(DevicesActivity.this, "Device management failed with error: " + error.getReason(), Toast.LENGTH_SHORT).show();
        finish();
    }

}
