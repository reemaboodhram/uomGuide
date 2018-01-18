package com.boodhram.guideme;

import java.io.Serializable;

/**
 * Created by shaulkory on 12/28/2017.
 */

public class ImageBuilding implements Serializable{
    int resourceId;
    String description;

    public ImageBuilding(int resourceId) {
        this.resourceId = resourceId;
    }

    public ImageBuilding(int resourceId, String description) {
        this.resourceId = resourceId;
        this.description = description;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
