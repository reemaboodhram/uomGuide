package com.boodhram.guideme.Utils;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.IOException;

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
}
