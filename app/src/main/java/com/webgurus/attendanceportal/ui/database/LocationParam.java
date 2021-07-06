package com.webgurus.attendanceportal.ui.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity
public class LocationParam  implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "lattitude")
    private String lattitude;

    @ColumnInfo(name = "longitude")
    private String longitude;

    @ColumnInfo(name = "isSynczed")
    private Boolean isSynczed;

    @ColumnInfo(name = "distance")
    private String distance;

    @ColumnInfo(name = "time")
    private String time;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Boolean getSynczed() {
        return isSynczed;
    }

    public void setSynczed(Boolean synczed) {
        isSynczed = synczed;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
