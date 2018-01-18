package com.boodhram.guideme.Utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.boodhram.guideme.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GoogleMapAsyncTasks {
    // Fetches data from url passed
    public static class FetchUrl extends AsyncTask<String, Void, String> {

        public GoogleMap map;
        public String distanceDuration;
        private Dialog loadingDialog;
        private Context context;

        Resources res;
        public FetchUrl(GoogleMap googleMap, Context ctx){
            this.map = googleMap;
            this.context = ctx;
            res = context.getResources();
        }

        public String getDistanceDuration() {
            return distanceDuration;
        }

        public void setDistanceDuration(String distanceDuration) {
            this.distanceDuration = distanceDuration;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = ProgressDialog.show(context, context.getResources().getString(R.string.fetching_directions),  context.getResources().getString(R.string.loading));

        }
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }

            String val = "";
            //result is the json string
            try {
                JSONObject jObject = new JSONObject(data);
                DataParser parser = new DataParser();
                // Starts parsing data
                val = parser.getDurationDistance(jObject);
                setDistanceDuration(val);

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            loadingDialog.dismiss();
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask(map);
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }
    public static String getUrl(LatLng origin, LatLng dest, String mode) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false&units=metric&mode="+mode;

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?"
                + parameters;

        return url;
    }

    public static String geturlWayPoint(List<LatLng> list,String mode) {

        LatLng origin = list.get(0);
        list.remove(0);

        LatLng dest = list.get(list.size()-1);
        list.remove(list.size()-1);

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        String waypoints = "waypoints=optimize:true|";
        if(!list.isEmpty()){
            for(LatLng latLng:list){
                waypoints = waypoints + latLng.latitude+","+latLng.longitude+"|";
            }
        }


        // Sensor enabled
        String sensor = "sensor=false&units=metric&mode="+mode;

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        if(!list.isEmpty()){
            parameters = str_origin + "&" + waypoints + "&" + str_dest + "&" + sensor;
        }

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?"
                + parameters;

        return url;
    }

    private static String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public static class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        private GoogleMap googleMap;
        public ParserTask(GoogleMap map) {
            this.googleMap = map;
        }

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                // Starts parsing data
                routes = parser.parse(jObject);



            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.GREEN);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                googleMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }
}
