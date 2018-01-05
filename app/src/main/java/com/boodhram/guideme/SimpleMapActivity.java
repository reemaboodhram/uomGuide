package com.boodhram.guideme;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.boodhram.guideme.Utils.AccountDTO;
import com.boodhram.guideme.Utils.BuildingDTO;
import com.boodhram.guideme.Utils.CONSTANTS;
import com.boodhram.guideme.Utils.ConnectivityHelper;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SimpleMapActivity extends FragmentActivity implements OnMapReadyCallback,
      GoogleMap.OnMarkerClickListener,GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    private AccountDTO accountDTO;
    Marker markerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        accountDTO = SharedPreferenceHelper.getAccountFromShared(SimpleMapActivity.this);
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
        googleMap.setOnMarkerClickListener(SimpleMapActivity.this);

        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("EEE dd MMM HH:mm");
        markerOptions =  mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-20.233378, 57.497468))
                .title(accountDTO.getUsername())
                .snippet(simpleDateFormat.format(new Date()))
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

        mMap.setOnMarkerDragListener(this);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                Utils.sendMeetingPointToServer(SimpleMapActivity.this,markerOptions.getPosition(),accountDTO.getUsername());
            }
        });
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        markerOptions.setPosition(marker.getPosition());
    }
}