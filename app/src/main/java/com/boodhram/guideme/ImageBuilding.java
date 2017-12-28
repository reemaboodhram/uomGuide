package com.boodhram.guideme;

import java.io.Serializable;

/**
 * Created by shaulkory on 12/28/2017.
 */

class ImageBuilding implements Serializable{
    int resourceId;

    public ImageBuilding(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

}
