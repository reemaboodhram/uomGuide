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
import com.boodhram.guideme.Utils.BuildingDTO;
import com.boodhram.guideme.Utils.UomService;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;


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
            String geoFenceId = getGeofenceTransitionDetails(event);
            if(geoFenceId!= null){
                int id = Integer.valueOf(geoFenceId);
                BuildingDTO buildingDTO = new UomService(this).getplaceById(id);
                sendNotification(buildingDTO);
            }

        }
    }

    private static String getGeofenceTransitionDetails(GeofencingEvent event) {

        String geoFenceId = null;
        if(!event.getTriggeringGeofences().isEmpty()){
            geoFenceId = event.getTriggeringGeofences().get(0).getRequestId();
        }

        return geoFenceId;
    }

    private void sendNotification(BuildingDTO buildingDTO) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.bubble_in)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(buildingDTO.getPlaceName());
        int NOTIFICATION_ID = 12345;

        Intent targetIntent = new Intent(this, NotificationActivity.class);
        targetIntent.putExtra("notif",buildingDTO.getPlaceDesc());
        targetIntent.putExtra("id",buildingDTO.getId());
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());
    }
}