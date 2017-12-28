package com.boodhram.guideme.GeoFencing;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;


import com.boodhram.guideme.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp pc on 12/02/2017.
 */
public class GeofenceTransitionsIntentService extends IntentService {
    protected static final String TAG = "GeofenceTransitionsIS";

    public GeofenceTransitionsIntentService() {
        super(TAG);  // use TAG to name the IntentService worker thread
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if (event.hasError()) {
            Log.e(TAG, "GeofencingEvent Error: " + event.getErrorCode());
            return;
        }
        else{
            String description = getGeofenceTransitionDetails(event);
            sendNotification(description);
        }
    }

    private static String getGeofenceTransitionDetails(GeofencingEvent event) {
        String transitionString =
                GeofenceStatusCodes.getStatusCodeString(event.getGeofenceTransition());
        List triggeringIDs = new ArrayList();
        for (Geofence geofence : event.getTriggeringGeofences()) {
            triggeringIDs.add(geofence.getRequestId());
        }
        return String.format("%s: %s", "Alert :", TextUtils.join(", ", triggeringIDs));
    }

    private void sendNotification(String notificationDetails) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.bubble_in)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(notificationDetails);
        int NOTIFICATION_ID = 12345;

        Intent targetIntent = new Intent(this, NotificationActivity.class);
        targetIntent.putExtra("notif",notificationDetails);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());
    }
}