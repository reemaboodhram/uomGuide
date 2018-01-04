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

import com.boodhram.guideme.Chat.Login;
import com.boodhram.guideme.Chat.Users;
import com.boodhram.guideme.GeoFencing.Constants;
import com.boodhram.guideme.GeoFencing.GeofenceTransitionsIntentService;
import com.boodhram.guideme.Utils.AccountDTO;
import com.boodhram.guideme.Utils.BuildingDTO;
import com.boodhram.guideme.Utils.CONSTANTS;
import com.boodhram.guideme.Utils.SharedPreferenceHelper;
import com.boodhram.guideme.Utils.UomService;
import com.boodhram.guideme.Utils.Utils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
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

import java.util.Date;
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
    private Boolean isGeofenceAdded = false;
    private AccountDTO accountDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        service = new UomService(MainActivity.this);
        mGeofenceList = new ArrayList<Geofence>();
        buildGoogleApiClient();
        // Get the geofences used. Geofence data is hard coded in this sample.
        populateGeofenceList();
        accountDTO = SharedPreferenceHelper.getAccountFromShared(MainActivity.this);
        findViewById();



    }

    private void findViewById() {

        //floating menu

        final FloatingActionMenu materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        final FloatingActionButton fab_meetingPoint = (FloatingActionButton) findViewById(R.id.fab_meetingPoint);
        final FloatingActionButton fab_logout = (FloatingActionButton) findViewById(R.id.fab_logout);
        final FloatingActionButton fab_myfriends = (FloatingActionButton) findViewById(R.id.fab_myfriends);
        final FloatingActionButton fab_chat = (FloatingActionButton) findViewById(R.id.fab_chat);
        final FloatingActionButton fab_myplaces = (FloatingActionButton) findViewById(R.id.fab_myplaces);


        materialDesignFAM.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {

                if (opened) {
                    materialDesignFAM.setClosedOnTouchOutside(true);
                }
            }
        });

        fab_myplaces.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,MapsActivity.class);
                startActivity(i);


            }

        });
        fab_logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                materialDesignFAM.close(false);
                SharedPreferenceHelper.clearall(MainActivity.this);
                Intent login = new Intent(MainActivity.this, Login.class);
                startActivity(login);
                finish();

            }
        });


        fab_chat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                materialDesignFAM.close(false);
                Intent intent = new Intent(MainActivity.this, Users.class);
                startActivity(intent);

            }
        });
        fab_myfriends.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                materialDesignFAM.close(false);
                Intent i = new Intent(MainActivity.this,FriendsMapActivity.class);
                startActivity(i);

            }
        });

        fab_meetingPoint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                materialDesignFAM.close(false);
                Intent i = new Intent(MainActivity.this,SimpleMapActivity.class);
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
                        .setRequestId(buildingDTO.getId()+"")
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
        if(!isGeofenceAdded){
            setListeners();
        }

        getLastKnownLocation();
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
                if(lastLocation!=null){
                    Utils.sendLastLocationToServer(MainActivity.this,lastLocation,accountDTO.getUsername(),false);
                }
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
            isGeofenceAdded = true;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 2) {
            if(checkPermission()){
                setListeners();
            }
        }
    }

}

