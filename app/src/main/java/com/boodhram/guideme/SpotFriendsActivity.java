package com.boodhram.guideme;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.boodhram.guideme.Utils.AccountDTO;
import com.boodhram.guideme.Utils.SharedPreferenceHelper;
import com.boodhram.guideme.Utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class SpotFriendsActivity extends FragmentActivity implements OnMapReadyCallback,
      GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    private AccountDTO accountDTO;
    Button btn_meeting_point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        btn_meeting_point = findViewById(R.id.btn_meeting_point);
        btn_meeting_point.setVisibility(View.INVISIBLE);
        accountDTO = SharedPreferenceHelper.getAccountFromShared(SpotFriendsActivity.this);
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
        googleMap.setOnMarkerClickListener(SpotFriendsActivity.this);
        Utils.getFriendOnMapMarkers(SpotFriendsActivity.this,mMap);
        LatLng latLng = new LatLng(-20.233378, 57.497468);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }


}