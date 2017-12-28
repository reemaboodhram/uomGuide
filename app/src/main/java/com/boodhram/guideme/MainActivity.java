package com.boodhram.guideme;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.PendingIntent;

import android.content.Intent;
import android.location.Location;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.boodhram.guideme.GeoFencing.Constants;
import com.boodhram.guideme.GeoFencing.GeofenceTransitionsIntentService;
import com.boodhram.guideme.Utils.BuildingDTO;
import com.boodhram.guideme.Utils.UomService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import java.util.ArrayList;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener,
        ResultCallback<Status> {

    Location lastLocation;
    private LocationRequest mLocationRequest;
    private final int UPDATE_INTERVAL =  3 * 60 * 1000; // 3 minutes
    private final int FASTEST_INTERVAL = 30 * 1000;  // 30 secs
    UomService service;
    String TAG ="LOCATION SERVICE";
    private DrawerLayout drawer;
    protected ArrayList<Geofence> mGeofenceList;
    protected GoogleApiClient mGoogleApiClient;
    private Button btn_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        service = new UomService(MainActivity.this);
        mGeofenceList = new ArrayList<Geofence>();
        buildGoogleApiClient();
        // Get the geofences used. Geofence data is hard coded in this sample.
        populateGeofenceList();

        btn_map = findViewById(R.id.btn_map);
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,MapsActivity.class);
                startActivity(i);
            }
        });

    }


    private void setListeners() {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, "Google API Client not connected!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(MainActivity.this,this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    public void populateGeofenceList() {

        List<BuildingDTO> list = service.getList();
        if(!list.isEmpty()){
            for (BuildingDTO buildingDTO :list) {
                mGeofenceList.add(new Geofence.Builder()
                        .setRequestId(buildingDTO.getPlaceName())
                        .setCircularRegion(
                                buildingDTO.getPlaceLat(),
                                buildingDTO.getPlaceLong(),
                                Constants.GEOFENCE_RADIUS_IN_METERS
                        )
                        .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                        .build());
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mGoogleApiClient.isConnecting() || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnecting() || mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.e("LOCATION SERVICE ","onConnected");
        getLastKnownLocation();
        setListeners();
    }

    // Get last known location
    private void getLastKnownLocation() {
        Log.d("LOCATION SERVICE", "getLastKnownLocation()");
        if ( checkPermission() ) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if ( lastLocation != null ) {
                Log.i("LOCATION SERVICE", "LasKnown location. " +
                        "Long: " + lastLocation.getLongitude() +
                        " | Lat: " + lastLocation.getLatitude());
                writeLastLocation();
                startLocationUpdates();
            } else {
                Log.w(TAG, "No location retrieved yet");
                startLocationUpdates();
            }
        }
        else askPermission();
    }
    // Write location coordinates on UI
    private void writeActualLocation(Location location) {

    }
    private void startLocationUpdates(){

        Log.i(TAG, "startLocationUpdates()");
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        if ( checkPermission() )
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }
    private void writeLastLocation() {
        writeActualLocation(lastLocation);
    }

    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }

    // Asks for permission
    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION}, 2);
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Do something with result.getErrorCode());
        Log.e("LOCATION SERVICE ","onConnectionFailed");
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.e("LOCATION SERVICE ","onConnectionSuspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onResult(Status status) {
        if (status.isSuccess()) {
            Toast.makeText(
                    this,
                    "Geofences Added",
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            Toast.makeText(
                    this,
                    status.getStatus().getStatusMessage(),
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();

    }
    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling addgeoFences()
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        writeActualLocation(location);
    }

}

