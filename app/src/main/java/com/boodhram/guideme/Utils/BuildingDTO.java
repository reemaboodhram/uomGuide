package com.boodhram.guideme.Utils;

import java.io.Serializable;

/**
 * Created by hp pc on 06/11/2016.
 */
public class BuildingDTO implements Serializable {
    private int id;
    private String placeName,placeDesc;
    private Double placeLong,placeLat;
    private boolean isFav;
    private int phone;

    public BuildingDTO(int id, String placeName, String placeDesc, Double placeLong, Double placeLat, boolean isFav, int phone) {
        this.id = id;
        this.placeName = placeName;
        this.placeDesc = placeDesc;
        this.placeLong = placeLong;
        this.placeLat = placeLat;
        this.isFav = isFav;
        this.phone = phone;
    }

    public BuildingDTO() {
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceDesc() {
        return placeDesc;
    }

    public void setPlaceDesc(String placeDesc) {
        this.placeDesc = placeDesc;
    }

    public Double getPlaceLong() {
        return placeLong;
    }

    public void setPlaceLong(Double placeLong) {
        this.placeLong = placeLong;
    }

    public Double getPlaceLat() {
        return placeLat;
    }

    public void setPlaceLat(Double placeLat) {
        this.placeLat = placeLat;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

}
