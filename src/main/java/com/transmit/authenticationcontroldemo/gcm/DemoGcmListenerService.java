package com.transmit.authenticationcontroldemo.gcm;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.transmit.authenticationcontroldemo.ApprovalsActivity;
import com.transmit.authenticationcontroldemo.R;

public class DemoGcmListenerService extends GcmListenerService {
    private static final String TAG = DemoGcmListenerService.class.getName();

    public static final int NOTIFICATION_ID = 1001;

    public static final String ACTION_NEW_APPROVAL = DemoGcmListenerService.class.getName() + ".action.NEW_APPROVAL";

    private static final String NOTIFICATION_TYPE_TAG = "push_type";
    private static final String NOTIFICATION_NEW_APPROVAL_TYPE = "approval";
    private static final String NOTIFICATION_DEVICE_NOTIFICATON_TYPE = "device_notification";
    private static final String NOTIFICATION_TITLE_TAG = "body";
    private static final String NOTIFICATION_DETAILS_TAG = "details";
    private static final String NOTIFICATION_SOURCE_TAG = "source";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.d(TAG, "Received message");

        Notification notification = formatNotification(data, getApplicationContext(), ApprovalsActivity.class);

        if (null == notification) {
            Log.e(TAG, "got NULL notification");
            return;
        }

        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);

    }

    private Notification formatNotification(Bundle _notificationDetails, Context _context, Class<? extends Activity> _activityClass) {
        String type = _notificationDetails.getString(NOTIFICATION_TYPE_TAG);
        NotificationCompat.Builder builder;

        if (null == type) {
            Log.e(TAG, "failed to parse notification type");
            return null;
        }

        switch (type) {
            case NOTIFICATION_NEW_APPROVAL_TYPE:
            case NOTIFICATION_DEVICE_NOTIFICATON_TYPE:
                builder = formatNewApprovalNotification(_notificationDetails, _context);
                break;
            default:
                Log.e(TAG, "Unknown notification type " + type);
                return null;
        }

        if (builder == null) {
            Log.e(TAG, "failed to build notification");
            return null;
        }

        Intent activityIntent = new Intent(_context, _activityClass);
        activityIntent.putExtra(NOTIFICATION_TYPE_TAG, NOTIFICATION_NEW_APPROVAL_TYPE);
        activityIntent.setAction(ACTION_NEW_APPROVAL);
        PendingIntent contentIntent = PendingIntent.getActivity(_context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        return builder.build();
    }

    private NotificationCompat.Builder formatNewApprovalNotification(Bundle _details, Context _context) {
        String title =  _details.getString(NOTIFICATION_TITLE_TAG);
        String details = _details.getString(NOTIFICATION_DETAILS_TAG);
        String source = _details.getString(NOTIFICATION_SOURCE_TAG);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(_context)
                .setSmallIcon(R.drawable.ic_ts_company_icon)
                .setContentTitle(title)
                //.setSubText(source)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        if (details != null) {
            builder.setContentText(details);
        }

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);


        return builder;

    }

}
