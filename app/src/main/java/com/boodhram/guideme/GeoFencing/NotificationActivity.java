package com.boodhram.guideme.GeoFencing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.boodhram.guideme.CarouselAdapterImages;
import com.boodhram.guideme.R;
import com.boodhram.guideme.Utils.BuildingDTO;
import com.boodhram.guideme.Utils.BuildingsDAO;


public class NotificationActivity extends AppCompatActivity {

    TextView txt_notif;
    BuildingDTO buildingDTO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        txt_notif = (TextView) findViewById(R.id.txt_notif);

        Integer id = getIntent().getIntExtra("id",-1);

        if(id > 0){
            buildingDTO = new BuildingsDAO(this).getbuildingById(id);

            String placeName = "Hello, you are near the "+buildingDTO.getPlaceName()+". See the building images below: ";
            txt_notif.setText(placeName);
            RecyclerView recyclerview =  findViewById(R.id.recycler);
            CarouselLayoutManager layoutManager2 = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
            recyclerview.setLayoutManager(layoutManager2);
            recyclerview.setHasFixedSize(true);

            recyclerview.addOnScrollListener(new CenterScrollListener());
            layoutManager2.setPostLayoutListener(new CarouselZoomPostLayoutListener());
            CarouselAdapterImages adapterPremium = new CarouselAdapterImages(this,buildingDTO);
            recyclerview.setAdapter(adapterPremium);
        }

    }
}
