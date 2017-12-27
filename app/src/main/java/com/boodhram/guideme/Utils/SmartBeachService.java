package com.boodhram.guideme.Utils;

import android.content.Context;

import java.util.List;

/**
 * Created by H on 19-Nov-16.
 */
public class SmartBeachService  {
    private PlaceDAO placeDAO;
    public SmartBeachService(Context context){
        placeDAO = new PlaceDAO(context);
    }

    public int getLastBatch(){
        return placeDAO.getLastBatchId();
    }

    public long savePlace(PlaceDTO placeDTO){
        return placeDAO.savePlace(placeDTO);
    }
    public List<PlaceDTO> getList(){
        return placeDAO.getbuildingsList();
    }

    public List<PlaceDTO> getPlacesByType(int type){
        return placeDAO.getPlaceListByType(type);
    }

    public void updatePlace(PlaceDTO placeDTO){
        placeDAO.updatePlace(placeDTO);
    }

    public List<PlaceDTO> getmyPlans(){
        return placeDAO.getplan();
    }


}
