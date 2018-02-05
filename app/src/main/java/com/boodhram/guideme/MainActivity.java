package com.boodhram.guideme;

import android.Manifest;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.boodhram.guideme.Chat.HomeActivity;
import com.boodhram.guideme.Chat.Users;
import com.boodhram.guideme.GeoFencing.Constants;
import com.boodhram.guideme.GeoFencing.GeofenceTransitionsIntentService;
import com.boodhram.guideme.Utils.AccountDTO;
import com.boodhram.guideme.Utils.BuildingDTO;
import com.boodhram.guideme.Utils.CONSTANTS;
import com.boodhram.guideme.Utils.SharedPreferenceHelper;
import com.boodhram.guideme.Utils.UomService;
import com.boodhram.guideme.Utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
        ResultCallback<Status> {

    Location lastLocation;
    private LocationRequest mLocationRequest;
    private final int UPDATE_INTERVAL = 3 * 60 * 1000; // 3 minutes
    private final int FASTEST_INTERVAL = 30 * 1000;  // 30 secs
    UomService service;
    String TAG = "LOCATION SERVICE";
    private DrawerLayout drawer;
    protected ArrayList<Geofence> mGeofenceList;
    protected GoogleApiClient mGoogleApiClient;
    private Button btn_map;
    private Boolean isGeofenceAdded = false;
    private AccountDTO accountDTO;
    TextView txt_username;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Fragment fragment = new DashboardFragment();
        openFrag(fragment);
        service = new UomService(MainActivity.this);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mGeofenceList = new ArrayList<Geofence>();
        buildGoogleApiClient();
        openFrag(new DashboardFragment());
        populateGeofenceList();
        accountDTO = SharedPreferenceHelper.getAccountFromShared(MainActivity.this);
        findViewById();
        FirebaseMessaging.getInstance().subscribeToTopic(CONSTANTS.UOM);
        Utils.setOnlineStatus(MainActivity.this, true, accountDTO.getUsername());
    }


    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void findViewById() {
        txt_username = navigationView.getHeaderView(0).findViewById(R.id.txt_username);
        if (accountDTO != null && accountDTO.getUsername() != null) {
            txt_username.setText(accountDTO.getUsername());

        }

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


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(MainActivity.this, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    public void populateGeofenceList() {

        List<BuildingDTO> list = service.getList();
        if (!list.isEmpty()) {
            for (BuildingDTO buildingDTO : list) {
                mGeofenceList.add(new Geofence.Builder()
                        .setRequestId(buildingDTO.getId() + "")
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

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.e("LOCATION SERVICE ", "onConnected");
        if (!isGeofenceAdded) {
            setListeners();
        }

        getLastKnownLocation();
    }

    // Get last known location
    private void getLastKnownLocation() {
        Log.d("LOCATION SERVICE", "getLastKnownLocation()");
        if (checkPermission()) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (lastLocation != null) {
                Log.i("LOCATION SERVICE", "LasKnown location. " +
                        "Long: " + lastLocation.getLongitude() +
                        " | Lat: " + lastLocation.getLatitude());
                if (lastLocation != null) {
                    Utils.sendLastLocationToServer(MainActivity.this, lastLocation, accountDTO.getUsername(), false);
                }
                writeLastLocation();
                startLocationUpdates();
            } else {
                Log.w(TAG, "No location retrieved yet");
                startLocationUpdates();
            }
        } else askPermission();
    }

    // Write location coordinates on UI
    private void writeActualLocation(Location location) {

    }

    private void startLocationUpdates() {

        Log.i(TAG, "startLocationUpdates()");
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        if (checkPermission())
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
                == PackageManager.PERMISSION_GRANTED);
    }

    // Asks for permission
    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Do something with result.getErrorCode());
        Log.e("LOCATION SERVICE ", "onConnectionFailed");
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.e("LOCATION SERVICE ", "onConnectionSuspended");
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
            if (checkPermission()) {
                setListeners();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.setOnlineStatus(MainActivity.this, false, accountDTO.getUsername());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;
        if (id == R.id.nav_uom_places) {
            intent = new Intent(MainActivity.this, UomPlacesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_meeting_point) {
            intent = new Intent(MainActivity.this, MeetingPointActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_friends) {
            intent = new Intent(MainActivity.this, SpotFriendsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_chat) {
            intent = new Intent(MainActivity.this, Users.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            SharedPreferenceHelper.clearall(MainActivity.this);
            intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openFrag(Fragment fragment) {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        drawer.closeDrawer(GravityCompat.START);
    }
}
