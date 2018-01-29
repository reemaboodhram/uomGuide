package com.boodhram.guideme.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.boodhram.guideme.R;
import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Utils {

    public static void sendLastLocationToServer(final Context context, final Location lastLocation, final String user, final Boolean showProgress) {
        Firebase.setAndroidContext(context);
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Loading...");
        if (showProgress) {
            pd.show();
        }

        String url = "https://guideme-7a3a9.firebaseio.com/locations.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Firebase reference = new Firebase("https://guideme-7a3a9.firebaseio.com/locations");
                Long timestamp = Calendar.getInstance().getTimeInMillis();
                if (s.equals("null")) {
                    reference.child(user).child("lat").setValue(lastLocation.getLatitude());
                    reference.child(user).child("long").setValue(lastLocation.getLongitude());
                    reference.child(user).child("timestamp").setValue(timestamp);

                } else {
                    try {
                        JSONObject obj = new JSONObject(s);

                        Iterator i = obj.keys();
                        String key = "";

                        while (i.hasNext()) {
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

                if (showProgress) {
                    pd.dismiss();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
                if (showProgress) {
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

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Firebase reference = new Firebase("https://guideme-7a3a9.firebaseio.com/meetup");
                Long timestamp = Calendar.getInstance().getTimeInMillis();
                if (s.equals("null")) {
                    reference.child("point").child("lat").setValue(lastLocation.latitude);
                    reference.child("point").child("long").setValue(lastLocation.longitude);
                    reference.child("point").child("timestamp").setValue(timestamp);
                    reference.child("point").child("user").setValue(username);
                    Toast.makeText(context, "Succesfully set meeting point", Toast.LENGTH_LONG).show();
                } else {
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

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
                pd.dismiss();

            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }

    public static void getMeetingPointFromServer(final Context context, final GoogleMap mMap) {
        Firebase.setAndroidContext(context);
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Loading...");
        pd.show();
        final SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("EEE dd MMM HH:mm");

        String url = "https://guideme-7a3a9.firebaseio.com/meetup.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Firebase reference = new Firebase("https://guideme-7a3a9.firebaseio.com/meetup");
                Long timestamp = Calendar.getInstance().getTimeInMillis();
                if (s.equals("null")) {
                    Toast.makeText(context, "No meetup location found from server", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject obj = new JSONObject(s);
                        if (obj.getJSONObject("point") != null) {
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

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "Problem with the server", Toast.LENGTH_LONG).show();
                pd.dismiss();

            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }

    public static void getFriendOnMapMarkers(final Context context, final GoogleMap mMap) {
        Firebase.setAndroidContext(context);
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Loading...");
        pd.show();


        String url = "https://guideme-7a3a9.firebaseio.com/locations.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Firebase reference = new Firebase("https://guideme-7a3a9.firebaseio.com/locations");
                Long timestamp = Calendar.getInstance().getTimeInMillis();
                if (s.equals("null")) {
                    Toast.makeText(context, "No Friend Location found", Toast.LENGTH_LONG).show();

                } else {
                    try {
                        JSONObject obj = new JSONObject(s);

                        Iterator i = obj.keys();
                        String key = "";
                        SimpleDateFormat simpleDateFormat =
                                new SimpleDateFormat("EEE dd MMM HH:mm");
                        while (i.hasNext()) {
                            key = i.next().toString();
                            JSONObject jsonObject = obj.getJSONObject(key);
                            Double lat = jsonObject.optDouble("lat");
                            Double lon = jsonObject.optDouble("long");
                            Long ts = jsonObject.getLong("timestamp");
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(ts);

                            if (lat != null && lon != null && ts != null) {
                                mMap.addMarker(new MarkerOptions().
                                        position(new LatLng(lat, lon))
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

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
                pd.dismiss();

            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }

    public static List<ImageBuilding> getImageForBuilding(BuildingDTO buildingDTO) {
        List<ImageBuilding> list = new ArrayList<>();

        switch (buildingDTO.getPlaceName()) {
            case "Paul Octave Wiehé Auditorium":
                list.add(new ImageBuilding(R.drawable.a1));
                list.add(new ImageBuilding(R.drawable.a1));
                list.add(new ImageBuilding(R.drawable.a1));
                break;
            case "Library":
                list.add(new ImageBuilding(R.drawable.a1));
                list.add(new ImageBuilding(R.drawable.a1));
                list.add(new ImageBuilding(R.drawable.a1));
                break;
            case "Faculty of Agriculture":
                list.add(new ImageBuilding(R.drawable.a1));
                list.add(new ImageBuilding(R.drawable.a1));
                list.add(new ImageBuilding(R.drawable.a1));
                break;
            case "Finance Building":
                list.add(new ImageBuilding(R.drawable.a1));
                list.add(new ImageBuilding(R.drawable.a1));
                list.add(new ImageBuilding(R.drawable.a1));
                break;
            case "Faculty of Social Studies and Humanities":
                list.add(new ImageBuilding(R.drawable.a1));
                list.add(new ImageBuilding(R.drawable.a1));
                list.add(new ImageBuilding(R.drawable.a1));
                break;
            case "Gymnasium":
                list.add(new ImageBuilding(R.drawable.a1));
                list.add(new ImageBuilding(R.drawable.a1));
                list.add(new ImageBuilding(R.drawable.a1));
                break;
            case "Football Ground":
                list.add(new ImageBuilding(R.drawable.a1));
                list.add(new ImageBuilding(R.drawable.a1));
                list.add(new ImageBuilding(R.drawable.a1));
                break;
            case "Cafeteria":
                list.add(new ImageBuilding(R.drawable.a1));
                list.add(new ImageBuilding(R.drawable.a1));
                list.add(new ImageBuilding(R.drawable.a1));
                break;
            case "Ex-common":
                list.add(new ImageBuilding(R.drawable.a1));
                list.add(new ImageBuilding(R.drawable.a1));
                list.add(new ImageBuilding(R.drawable.a1));
                break;

            case "Engineering Tower":
                list.add(new ImageBuilding(R.drawable.a1));
                list.add(new ImageBuilding(R.drawable.a1));
                list.add(new ImageBuilding(R.drawable.a1));
                break;
            case "Phase II Building":
                list.add(new ImageBuilding(R.drawable.a1));
                list.add(new ImageBuilding(R.drawable.a1));
                list.add(new ImageBuilding(R.drawable.a1));
                break;
            case "Faculty of Law and Management":
                list.add(new ImageBuilding(R.drawable.a1));
                list.add(new ImageBuilding(R.drawable.a1));
                list.add(new ImageBuilding(R.drawable.a1));
                break;

        }

        return list;
    }
}
