package com.boodhram.guideme.Utils;

/**
 * Created by H on 03-Dec-16.
 */

        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.location.LocationManager;
        import android.provider.Settings;

        import com.boodhram.guideme.R;


public class ConnectionDetection {
    private Context _context;

    public ConnectionDetection(Context _context){
        this._context = _context;
    }

    public boolean isGpsOn(){
        LocationManager lm = (LocationManager)_context.getSystemService(_context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled) {
            // notify user
            final AlertDialog.Builder dialog = new AlertDialog.Builder(_context);
            dialog.setMessage(_context.getResources().getString(R.string.gps_not_enabled_only));
            dialog.setPositiveButton(_context.getString(R.string.gps_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    _context.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                   paramDialogInterface.dismiss();

                }
            });
            dialog.show();
        }
        return gps_enabled;
    }


}
