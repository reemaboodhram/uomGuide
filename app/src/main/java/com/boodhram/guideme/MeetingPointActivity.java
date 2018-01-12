package com.boodhram.guideme;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.boodhram.guideme.Utils.AccountDTO;
import com.boodhram.guideme.Utils.SharedPreferenceHelper;
import com.boodhram.guideme.Utils.Utils;
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

public class MeetingPointActivity extends FragmentActivity implements OnMapReadyCallback,
      GoogleMap.OnMarkerClickListener,GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    private AccountDTO accountDTO;
    Marker markerOptions;
    SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat("EEE dd MMM HH:mm");

    Button btn_meeting_point;
    private Integer mode = 0;
    //0 - set meeting point
    //1- view meeting point

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        btn_meeting_point = findViewById(R.id.btn_meeting_point);
        accountDTO = SharedPreferenceHelper.getAccountFromShared(MeetingPointActivity.this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn_meeting_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode == 0){
                    mMap.clear();
                    //change map to view meeting point mode
                    viewMeetingPoint(mMap);
                    btn_meeting_point.setText("Setup Meeting Point");

                    mode = 1;

                }else {
                    //change map to setup meeting point mode
                    mMap.clear();
                    setupMeetingPoint(mMap);
                    btn_meeting_point.setText("View Meeting Point");

                    mode = 0;
                }
            }
        });

    }

    private void viewMeetingPoint(GoogleMap mMap) {
        Utils.getMeetingPointFromServer(MeetingPointActivity.this,mMap);
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
        googleMap.setOnMarkerClickListener(MeetingPointActivity.this);

        LatLng latLng = new LatLng(-20.233378, 57.497468);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

        setupMeetingPoint(mMap);
    }

    private void setupMeetingPoint(GoogleMap mMap) {
        markerOptions =  mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-20.233378, 57.497468))
                .title("Set meeting Point")
                .snippet(simpleDateFormat.format(new Date()))
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

        mMap.setOnMarkerDragListener(this);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                Utils.sendMeetingPointToServer(MeetingPointActivity.this,markerOptions.getPosition(),accountDTO.getUsername());
            }
        });
        LatLng latLng = new LatLng(-20.233378, 57.497468);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
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