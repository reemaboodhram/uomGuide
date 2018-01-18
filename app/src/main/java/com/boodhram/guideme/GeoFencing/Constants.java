package com.boodhram.guideme.GeoFencing;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;


public class Constants {
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = 12 * 60 * 60 * 1000;
    public static final float GEOFENCE_RADIUS_IN_METERS = 30;

    public static final HashMap<String, LatLng> LANDMARKS = new HashMap<String, LatLng>();
    static {
        // San Francisco International Airport.
        LANDMARKS.put("Lakaz", new LatLng(-20.147656, 57.685503));

        // Googleplex.
        LANDMARKS.put("Laventure", new LatLng(-20.147706, 57.685559));

        // Test
        LANDMARKS.put("SFO", new LatLng(-20.147812, 57.685664));
    }
}

