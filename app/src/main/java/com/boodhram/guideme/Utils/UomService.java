package com.boodhram.guideme.Utils;

import android.content.Context;

import java.util.List;

/**
 * Created by H on 19-Nov-16.
 */
public class UomService {
    private BuildingsDAO buildingsDAO;
    public UomService(Context context){
        buildingsDAO = new BuildingsDAO(context);
    }

    public int getLastBatch(){
        return buildingsDAO.getLastBatchId();
    }

    public long savePlace(BuildingDTO buildingDTO){
        return buildingsDAO.savePlace(buildingDTO);
    }
    public List<BuildingDTO> getList(){
        return buildingsDAO.getbuildingsList();
    }

    public BuildingDTO getplaceById(int type){
        return buildingsDAO.getbuildingById(type);
    }

    public void updatePlace(BuildingDTO buildingDTO){
        buildingsDAO.updatePlace(buildingDTO);
    }

    public List<BuildingDTO> getmyPlans(){
        return buildingsDAO.getplan();
    }


}
