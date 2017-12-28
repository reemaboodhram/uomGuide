package com.boodhram.guideme.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.boodhram.guideme.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class BuildingsDAO {

    DatabaseHelper dbHelper;

    private final String TABLE_NAME = "buildings";

    public BuildingsDAO(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    public long savePlace(BuildingDTO buildingDTO) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("placeName", buildingDTO.getPlaceName());
        contentValues.put("placeDesc", buildingDTO.getPlaceDesc());
        contentValues.put("placeLong", buildingDTO.getPlaceLong());
        contentValues.put("placeLat", buildingDTO.getPlaceLat());
        contentValues.put("phone", buildingDTO.getPhone());
        if(buildingDTO.isFav() == false){
            contentValues.put("fav", 0);
        }else{
            contentValues.put("fav", 1);
        }

        dbHelper.open();
        final long id = db.insert(TABLE_NAME, null, contentValues);

        return id;
    }

    public void updatePlace(BuildingDTO buildingDTO) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", buildingDTO.getId());
        contentValues.put("placeName", buildingDTO.getPlaceName());
        contentValues.put("placeDesc", buildingDTO.getPlaceDesc());
        contentValues.put("placeLong", buildingDTO.getPlaceLong());
        contentValues.put("placeLat", buildingDTO.getPlaceLat());
        contentValues.put("phone", buildingDTO.getPhone());
        if(buildingDTO.isFav() == false){
            contentValues.put("fav", 0);
        }else{
            contentValues.put("fav", 1);
        }

        dbHelper.open();
        db.update(TABLE_NAME, contentValues, "id=" + buildingDTO.getId(), null);

    }

    public List<BuildingDTO> getPlaceListByType(int id) {
        List<BuildingDTO> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor res = db.rawQuery("select * from buildings where placeType = " + id+ " ORDER BY id ASC", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            BuildingDTO buildingDTO = new BuildingDTO();
            buildingDTO.setId(res.getInt(res.getColumnIndex("id")));
            buildingDTO.setPlaceDesc(res.getString(res.getColumnIndex("placeDesc")));
            buildingDTO.setPlaceName(res.getString(res.getColumnIndex("placeName")));
            buildingDTO.setPlaceLong(res.getDouble(res.getColumnIndex("placeLong")));
            buildingDTO.setPlaceLat(res.getDouble(res.getColumnIndex("placeLat")));
            buildingDTO.setPhone(res.getInt(res.getColumnIndex("phone")));
            if(res.getInt(res.getColumnIndex("fav")) == 0){
                buildingDTO.setFav(false);
            }
            else{
                buildingDTO.setFav(true);
            }
            list.add(buildingDTO);
            res.moveToNext();
        }
        res.close();
        return list;

    }
    public List<BuildingDTO> getbuildingsList() {
        List<BuildingDTO> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor res = db.rawQuery("select * from buildings ORDER BY id ASC", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            BuildingDTO buildingDTO = new BuildingDTO();
            buildingDTO.setId(res.getInt(res.getColumnIndex("id")));
            buildingDTO.setPlaceDesc(res.getString(res.getColumnIndex("placeDesc")));
            buildingDTO.setPlaceName(res.getString(res.getColumnIndex("placeName")));
            buildingDTO.setPlaceLong(res.getDouble(res.getColumnIndex("placeLong")));
            buildingDTO.setPlaceLat(res.getDouble(res.getColumnIndex("placeLat")));
            buildingDTO.setPhone(res.getInt(res.getColumnIndex("phone")));
            if(res.getInt(res.getColumnIndex("fav")) == 0){
                buildingDTO.setFav(false);
            }
            else{
                buildingDTO.setFav(true);
            }
            list.add(buildingDTO);
            res.moveToNext();
        }
        res.close();
        return list;

    }
    public BuildingDTO getbuildingById(int type) {
        BuildingDTO buildingDTO = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor res = db.rawQuery("select * from buildings where id = "+type + " ORDER BY id ASC", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            buildingDTO = new BuildingDTO();
            buildingDTO.setId(res.getInt(res.getColumnIndex("id")));
            buildingDTO.setPlaceDesc(res.getString(res.getColumnIndex("placeDesc")));
            buildingDTO.setPlaceName(res.getString(res.getColumnIndex("placeName")));
            buildingDTO.setPlaceLong(res.getDouble(res.getColumnIndex("placeLong")));
            buildingDTO.setPlaceLat(res.getDouble(res.getColumnIndex("placeLat")));
            buildingDTO.setPhone(res.getInt(res.getColumnIndex("phone")));
            if(res.getInt(res.getColumnIndex("fav")) == 0){
                buildingDTO.setFav(false);
            }
            else{
                buildingDTO.setFav(true);
            }
            res.moveToNext();
        }
        res.close();
        return buildingDTO;

    }
    public int getLastBatchId() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor res = db.rawQuery("select updateBatch from buildings ORDER BY updateBatch DESC", null);
        int batch = 0;
        if(res !=null && res.getCount() >0){
            res.moveToFirst();
            batch = res.getInt(res.getColumnIndex("updateBatch"));
        }

        res.close();
        return batch;

    }

    public List<BuildingDTO> getplan() {
        List<BuildingDTO> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor res = db.rawQuery("select * from buildings where fav = 1 ORDER BY id ASC", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            BuildingDTO buildingDTO = new BuildingDTO();
            buildingDTO.setId(res.getInt(res.getColumnIndex("id")));
            buildingDTO.setPlaceDesc(res.getString(res.getColumnIndex("placeDesc")));
            buildingDTO.setPlaceName(res.getString(res.getColumnIndex("placeName")));
            buildingDTO.setPlaceLong(res.getDouble(res.getColumnIndex("placeLong")));
            buildingDTO.setPlaceLat(res.getDouble(res.getColumnIndex("placeLat")));
            buildingDTO.setPhone(res.getInt(res.getColumnIndex("phone")));
            if(res.getInt(res.getColumnIndex("fav")) == 0){
                buildingDTO.setFav(false);
            }
            else{
                buildingDTO.setFav(true);
            }
            list.add(buildingDTO);
            res.moveToNext();
        }
        res.close();
        return list;
    }
}
