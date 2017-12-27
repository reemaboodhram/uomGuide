package com.boodhram.guideme.GeoFencing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.boodhram.guideme.R;


public class NotificationActivity extends AppCompatActivity {

    TextView txt_notif;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        txt_notif = (TextView) findViewById(R.id.txt_notif);

        String val = getIntent().getStringExtra("notif");
        if(val !=null){
            txt_notif.setText(val);
        }
        else{
            txt_notif.setText("No place found");
        }
    }
}
