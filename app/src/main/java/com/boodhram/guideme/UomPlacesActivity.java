package com.boodhram.guideme;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.boodhram.guideme.Utils.AccountDTO;
import com.boodhram.guideme.Utils.BuildingDTO;
import com.boodhram.guideme.Utils.ConnectivityHelper;
import com.boodhram.guideme.Utils.CONSTANTS;
import com.boodhram.guideme.Utils.GoogleMapAsyncTasks;
import com.boodhram.guideme.Utils.SharedPreferenceHelper;
import com.boodhram.guideme.Utils.UomService;
import com.boodhram.guideme.Utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class UomPlacesActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    UomService service;
    List<BuildingDTO> list;
    private AccountDTO accountDTO;
    Button btn_meeting_point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        btn_meeting_point = findViewById(R.id.btn_meeting_point);
        btn_meeting_point.setVisibility(View.INVISIBLE);
        accountDTO = SharedPreferenceHelper.getAccountFromShared(UomPlacesActivity.this);
        service = new UomService(UomPlacesActivity.this);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        list = service.getList();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setOnMarkerClickListener(UomPlacesActivity.this);
        if(!list.isEmpty()){
            LatLng latLng = new LatLng(list.get(0).getPlaceLat(),list.get(0).getPlaceLong());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
            setMarkersToMap(list);
        }
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                if(!marker.getTitle().equalsIgnoreCase( "Current Position")){
                    showDescription(marker);
                }


            }
        });
    }

    private void showDescription(Marker marker) {
        final Dialog dialog = new Dialog(UomPlacesActivity.this,R.style.WalkthroughTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog);
        BuildingDTO buildingDTO = getMarkerBySnippet(marker.getTitle());
        TextView txt_phone =  dialog.findViewById(R.id.txt_phone);
        ImageView imgPhone =  dialog.findViewById(R.id.imgPhone);
        ImageView imgWalk =  dialog.findViewById(R.id.imgWalk);
        TextView txt_walk =  dialog.findViewById(R.id.txt_walk);
        TextView txt_details =  dialog.findViewById(R.id.txt_details);

        txt_phone.setOnClickListener(new PhoneListener(buildingDTO.getPhone()+""));
        imgPhone.setOnClickListener(new PhoneListener(buildingDTO.getPhone()+""));

        imgWalk.setOnClickListener(new WalkListener(marker,dialog));
        txt_walk.setOnClickListener(new WalkListener(marker, dialog));
        txt_details.setText(buildingDTO.getPlaceDesc());
        txt_phone.setText(buildingDTO.getPhone()+"");
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        RecyclerView recyclerview = (RecyclerView) dialog.findViewById(R.id.recycler);
        CarouselLayoutManager  layoutManager2 = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        recyclerview.setLayoutManager(layoutManager2);
        recyclerview.setHasFixedSize(true);


        recyclerview.addOnScrollListener(new CenterScrollListener());
        layoutManager2.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        CarouselAdapterImages adapterPremium = new CarouselAdapterImages(this,buildingDTO);
        recyclerview.setAdapter(adapterPremium);

        dialog.show();
    }

    private BuildingDTO getMarkerBySnippet(String name) {
        BuildingDTO building = null;
        if(!list.isEmpty()){
            for(BuildingDTO buildingDTO:list){
                if(buildingDTO.getPlaceName().equalsIgnoreCase(name)){
                    building = buildingDTO;
                }
            }
        }
        return building;
    }

    private void setMarkersToMap(List<BuildingDTO> placeDTOList) {
        for(BuildingDTO place:placeDTOList){
            mMap.addMarker(new MarkerOptions().
                    position(new LatLng(place.getPlaceLat(),place.getPlaceLong()))
                    .title(place.getPlaceName())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        setcurrentPositionMarker();

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    private void setcurrentPositionMarker() {
        //Place current location marker
        Location location = mLastLocation;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        Utils.sendLastLocationToServer(UomPlacesActivity.this,mLastLocation,accountDTO.getUsername(),false);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    private class PhoneListener implements View.OnClickListener {
        String num;
        public PhoneListener(String phone) {
            this.num = phone;
        }

        @Override
        public void onClick(View view) {
            if(!num.isEmpty()){
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+ Uri.encode(num.trim())));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);
            }
        }
    }

    private class WalkListener implements View.OnClickListener {
        Marker marker;
        Dialog dialog;
        public WalkListener(Marker mark, Dialog dialog) {
            this.marker = mark;
            this.dialog = dialog;
        }

        @Override
        public void onClick(View view) {
            //Check for gps on
            if(mLastLocation != null){
             if(ConnectivityHelper.isConnected(UomPlacesActivity.this)){
                 mMap.clear();

                        String url = GoogleMapAsyncTasks.getUrl(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()), marker.getPosition(), CONSTANTS.MODE_WALKING);
                        GoogleMapAsyncTasks.FetchUrl FetchUrl = new GoogleMapAsyncTasks.FetchUrl(mMap,UomPlacesActivity.this);
                        FetchUrl.execute(url);
                        setMarkersToMap(list);
                        setcurrentPositionMarker();
                        dialog.dismiss();
                    }
                    else{
                        Toast.makeText(UomPlacesActivity.this,getResources().getString(R.string.internet_off),Toast
                                .LENGTH_SHORT).show();
                    }
                }
                else{
                Toast.makeText(UomPlacesActivity.this,getResources().getString(R.string.user_location_not_found),Toast
                        .LENGTH_SHORT).show();
            }

            }
        }
    }
