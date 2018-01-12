package com.boodhram.guideme.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.boodhram.guideme.MeetingPointActivity;
import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by H on 19-Nov-16.
 */
public class Utils {

    public static void setFlicEnFlacBoundary(GoogleMap googleMap) {
        Polygon polygon = googleMap.addPolygon(new PolygonOptions()
                .add(new LatLng(-20.261551, 57.377976),
                        new LatLng(-20.262286, 57.381270),
                        new LatLng(-20.263091, 57.383223),
                        new LatLng(-20.264118, 57.384425),
                        new LatLng(-20.265245, 57.387096),
                        new LatLng(-20.266060, 57.389220),
                        new LatLng(-20.268838, 57.397320),
                        new LatLng(-20.270237, 57.401397),
                        new LatLng(-20.270791, 57.404106),
                        new LatLng(-20.270952, 57.405286),
                        new LatLng(-20.272230, 57.405222),
                        new LatLng(-20.273951, 57.404879),
                        new LatLng(-20.275451, 57.404686),
                        new LatLng(-20.276800, 57.404348),
                        new LatLng(-20.278028, 57.404273),
                        new LatLng(-20.279276, 57.404252),
                        new LatLng(-20.280001, 57.403962),
                        new LatLng(-20.282535, 57.403061),
                        new LatLng(-20.283642, 57.402846),
                        new LatLng(-20.285424, 57.402566),
                        new LatLng(-20.286259, 57.402298),
                        new LatLng(-20.292035, 57.399648),
                        new LatLng(-20.293756, 57.398897),
                        new LatLng(-20.299572, 57.397427),
                        new LatLng(-20.302128, 57.396322),
                        new LatLng(-20.305177, 57.395646),
                        new LatLng(-20.309564, 57.397749),
                        new LatLng(-20.315460, 57.396258),
                        new LatLng(-20.318146, 57.394745),
                        new LatLng(-20.323690, 57.389027),
                        new LatLng(-20.325199, 57.387278),
                        new LatLng(-20.327885, 57.385722),
                        new LatLng(-20.330430, 57.379843),
                        new LatLng(-20.330983, 57.378598),
                        new LatLng(-20.330882, 57.376538),
                        new LatLng(-20.330600, 57.373373),
                        new LatLng(-20.328638, 57.354919),
                        new LatLng(-20.280480, 57.354179),
                        new LatLng(-20.254705, 57.355939),
                        new LatLng(-20.258610, 57.380926),
                        new LatLng(-20.261551, 57.377976))
                .strokeColor(Color.parseColor("#ff4081"))
                .strokeWidth(5.0f)
                .fillColor(Color.parseColor("#75E96468")));
    }

    public static boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

    public static void sendLastLocationToServer(final Context context, final Location lastLocation, final String user, final Boolean showProgress) {
        Firebase.setAndroidContext(context);
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Loading...");
        if(showProgress){
            pd.show();
        }

        String url = "https://guideme-7a3a9.firebaseio.com/locations.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                Firebase reference = new Firebase("https://guideme-7a3a9.firebaseio.com/locations");
                Long timestamp = Calendar.getInstance().getTimeInMillis();
                if(s.equals("null")) {
                    reference.child(user).child("lat").setValue(lastLocation.getLatitude());
                    reference.child(user).child("long").setValue(lastLocation.getLongitude());
                    reference.child(user).child("timestamp").setValue(timestamp);

                }
                else {
                    try {
                        JSONObject obj = new JSONObject(s);

                        Iterator i = obj.keys();
                        String key = "";

                        while(i.hasNext()){
                            key = i.next().toString();
                            JSONObject jsonObject = obj.getJSONObject(key);
                        }
                        reference.child(user).child("lat").setValue(lastLocation.getLatitude());
                        reference.child(user).child("long").setValue(lastLocation.getLongitude());
                        reference.child(user).child("timestamp").setValue(timestamp);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if(showProgress){
                    pd.dismiss();
                }
            }

        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError );
                if(showProgress){
                    pd.dismiss();
                }
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }


    public static void sendMeetingPointToServer(final Context context, final LatLng lastLocation, final String username) {
        Firebase.setAndroidContext(context);
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Loading...");
        pd.show();

        String url = "https://guideme-7a3a9.firebaseio.com/meetup.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                Firebase reference = new Firebase("https://guideme-7a3a9.firebaseio.com/meetup");
                Long timestamp = Calendar.getInstance().getTimeInMillis();
                if(s.equals("null")) {
                    reference.child("point").child("lat").setValue(lastLocation.latitude);
                    reference.child("point").child("long").setValue(lastLocation.longitude);
                    reference.child("point").child("timestamp").setValue(timestamp);
                    reference.child("point").child("user").setValue(username);
                    Toast.makeText(context, "Succesfully set meeting point", Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        JSONObject obj = new JSONObject(s);

                        reference.child("point").child("lat").setValue(lastLocation.latitude);
                        reference.child("point").child("long").setValue(lastLocation.longitude);
                        reference.child("point").child("timestamp").setValue(timestamp);
                        reference.child("point").child("user").setValue(username);
                        Toast.makeText(context, "Succesfully set meeting point", Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        Toast.makeText(context, "Could not send to server", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }


                pd.dismiss();

            }

        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError );
                pd.dismiss();

            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }

    public static void getMeetingPointFromServer(final Context context,final GoogleMap mMap) {
        Firebase.setAndroidContext(context);
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Loading...");
        pd.show();
        final SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("EEE dd MMM HH:mm");

        String url = "https://guideme-7a3a9.firebaseio.com/meetup.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                Firebase reference = new Firebase("https://guideme-7a3a9.firebaseio.com/meetup");
                Long timestamp = Calendar.getInstance().getTimeInMillis();
                if(s.equals("null")) {
                    Toast.makeText(context, "No meetup location found from server", Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        JSONObject obj = new JSONObject(s);
                        if(obj.getJSONObject("point")!=null){
                            JSONObject jsonObject = obj.getJSONObject("point");
                            Double lat = jsonObject.optDouble("lat");
                            Double lon = jsonObject.optDouble("long");
                            Long ts = jsonObject.getLong("timestamp");
                            String user = jsonObject.getString("user");
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(ts);

                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lon))
                                    .title(user)
                                    .snippet(simpleDateFormat.format(new Date()))
                                    .draggable(true)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

                            mMap.setOnMarkerDragListener(null);
                            mMap.setOnInfoWindowClickListener(null);
                            LatLng latLng = new LatLng(-20.233378, 57.497468);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                        }



                    } catch (JSONException e) {
                        Toast.makeText(context, "Server issues", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }


                pd.dismiss();

            }

        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "Problem with the server", Toast.LENGTH_LONG).show();
                pd.dismiss();

            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }

    public static void getFriendOnMapMarkers(final Context context,final GoogleMap mMap) {
        Firebase.setAndroidContext(context);
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Loading...");
        pd.show();


        String url = "https://guideme-7a3a9.firebaseio.com/locations.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                Firebase reference = new Firebase("https://guideme-7a3a9.firebaseio.com/locations");
                Long timestamp = Calendar.getInstance().getTimeInMillis();
                if(s.equals("null")) {
                    Toast.makeText(context, "No Friend Location found", Toast.LENGTH_LONG).show();

                }
                else {
                    try {
                        JSONObject obj = new JSONObject(s);

                        Iterator i = obj.keys();
                        String key = "";
                        SimpleDateFormat simpleDateFormat =
                                new SimpleDateFormat("EEE dd MMM HH:mm");
                        while(i.hasNext()){
                            key = i.next().toString();
                            JSONObject jsonObject = obj.getJSONObject(key);
                            Double lat = jsonObject.optDouble("lat");
                            Double lon = jsonObject.optDouble("long");
                            Long ts = jsonObject.getLong("timestamp");
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(ts);

                            if(lat!=null && lon !=null && ts!= null){
                                mMap.addMarker(new MarkerOptions().
                                        position(new LatLng(lat,lon))
                                        .title(key)
                                        .snippet(simpleDateFormat.format(cal.getTime()))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                            }
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                pd.dismiss();

            }

        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError );
                pd.dismiss();

            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }
}
