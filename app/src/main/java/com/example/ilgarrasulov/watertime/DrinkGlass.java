package com.example.ilgarrasulov.watertime;

import android.os.Parcel;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ilgarrasulov on 15.05.2017.
 */

public class DrinkGlass implements Serializable {

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id;


    public DrinkGlass(long id, Date glass_time) {
        this.id = id;
        this.glass_time = glass_time;
    }

    public Date getGlass_time() {
        return glass_time;
    }

    public void setGlass_time(Date glass_time) {
        this.glass_time = glass_time;
    }

    private Date glass_time;

}
