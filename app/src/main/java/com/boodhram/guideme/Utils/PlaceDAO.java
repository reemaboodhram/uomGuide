package com.boodhram.guideme.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.boodhram.guideme.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class PlaceDAO {

    DatabaseHelper dbHelper;

    private final String TABLE_NAME = "buildings";

    public PlaceDAO(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    public long savePlace(PlaceDTO placeDTO) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("placeName", placeDTO.getPlaceName());
        contentValues.put("placeDesc", placeDTO.getPlaceDesc());
        contentValues.put("placeLong", placeDTO.getPlaceLong());
        contentValues.put("placeLat", placeDTO.getPlaceLat());
        contentValues.put("phone",placeDTO.getPhone());
        if(placeDTO.isFav() == false){
            contentValues.put("fav", 0);
        }else{
            contentValues.put("fav", 1);
        }

        dbHelper.open();
        final long id = db.insert(TABLE_NAME, null, contentValues);

        return id;
    }

    public void updatePlace(PlaceDTO placeDTO) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", placeDTO.getId());
        contentValues.put("placeName", placeDTO.getPlaceName());
        contentValues.put("placeDesc", placeDTO.getPlaceDesc());
        contentValues.put("placeLong", placeDTO.getPlaceLong());
        contentValues.put("placeLat", placeDTO.getPlaceLat());
        contentValues.put("phone",placeDTO.getPhone());
        if(placeDTO.isFav() == false){
            contentValues.put("fav", 0);
        }else{
            contentValues.put("fav", 1);
        }

        dbHelper.open();
        db.update(TABLE_NAME, contentValues, "id=" + placeDTO.getId(), null);

    }

    public List<PlaceDTO> getPlaceListByType(int id) {
        List<PlaceDTO> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor res = db.rawQuery("select * from buildings where placeType = " + id+ " ORDER BY id ASC", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            PlaceDTO placeDTO = new PlaceDTO();
            placeDTO.setId(res.getInt(res.getColumnIndex("id")));
            placeDTO.setPlaceDesc(res.getString(res.getColumnIndex("placeDesc")));
            placeDTO.setPlaceName(res.getString(res.getColumnIndex("placeName")));
            placeDTO.setPlaceLong(res.getDouble(res.getColumnIndex("placeLong")));
            placeDTO.setPlaceLat(res.getDouble(res.getColumnIndex("placeLat")));
            placeDTO.setPhone(res.getInt(res.getColumnIndex("phone")));
            if(res.getInt(res.getColumnIndex("fav")) == 0){
                placeDTO.setFav(false);
            }
            else{
                placeDTO.setFav(true);
            }
            list.add(placeDTO);
            res.moveToNext();
        }
        res.close();
        return list;

    }
    public List<PlaceDTO> getbuildingsList() {
        List<PlaceDTO> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor res = db.rawQuery("select * from buildings ORDER BY id ASC", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            PlaceDTO placeDTO = new PlaceDTO();
            placeDTO.setId(res.getInt(res.getColumnIndex("id")));
            placeDTO.setPlaceDesc(res.getString(res.getColumnIndex("placeDesc")));
            placeDTO.setPlaceName(res.getString(res.getColumnIndex("placeName")));
            placeDTO.setPlaceLong(res.getDouble(res.getColumnIndex("placeLong")));
            placeDTO.setPlaceLat(res.getDouble(res.getColumnIndex("placeLat")));
            placeDTO.setPhone(res.getInt(res.getColumnIndex("phone")));
            if(res.getInt(res.getColumnIndex("fav")) == 0){
                placeDTO.setFav(false);
            }
            else{
                placeDTO.setFav(true);
            }
            list.add(placeDTO);
            res.moveToNext();
        }
        res.close();
        return list;

    }
    public List<PlaceDTO> getbuildingsListByType(int type) {
        List<PlaceDTO> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor res = db.rawQuery("select * from buildings where placeType "+type + " ORDER BY id ASC", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            PlaceDTO placeDTO = new PlaceDTO();
            placeDTO.setId(res.getInt(res.getColumnIndex("id")));
            placeDTO.setPlaceDesc(res.getString(res.getColumnIndex("placeDesc")));
            placeDTO.setPlaceName(res.getString(res.getColumnIndex("placeName")));
            placeDTO.setPlaceLong(res.getDouble(res.getColumnIndex("placeLong")));
            placeDTO.setPlaceLat(res.getDouble(res.getColumnIndex("placeLat")));
            placeDTO.setPhone(res.getInt(res.getColumnIndex("phone")));
            if(res.getInt(res.getColumnIndex("fav")) == 0){
                placeDTO.setFav(false);
            }
            else{
                placeDTO.setFav(true);
            }
            list.add(placeDTO);
            res.moveToNext();
        }
        res.close();
        return list;

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

    public List<PlaceDTO> getplan() {
        List<PlaceDTO> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor res = db.rawQuery("select * from buildings where fav = 1 ORDER BY id ASC", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            PlaceDTO placeDTO = new PlaceDTO();
            placeDTO.setId(res.getInt(res.getColumnIndex("id")));
            placeDTO.setPlaceDesc(res.getString(res.getColumnIndex("placeDesc")));
            placeDTO.setPlaceName(res.getString(res.getColumnIndex("placeName")));
            placeDTO.setPlaceLong(res.getDouble(res.getColumnIndex("placeLong")));
            placeDTO.setPlaceLat(res.getDouble(res.getColumnIndex("placeLat")));
            placeDTO.setPhone(res.getInt(res.getColumnIndex("phone")));
            if(res.getInt(res.getColumnIndex("fav")) == 0){
                placeDTO.setFav(false);
            }
            else{
                placeDTO.setFav(true);
            }
            list.add(placeDTO);
            res.moveToNext();
        }
        res.close();
        return list;
    }
}
