package com.webgurus.attendanceportal.ui.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

public class InsertLocationData extends AsyncTask<String, String, String> {

    Location mLocation;
    @SuppressLint("StaticFieldLeak")
    Context mContext;
    float distance;
    String time ;

    public InsertLocationData(Location location, boolean isSynczed, String time , Context context) {
        this.mLocation = location;
        this.mContext = context;
        this.time = time;
    }

//    public InsertLocationData(Location location, boolean isSynczed,float  distance, Context context) {
//        this.mLocation=location;
//        this.mContext=context;
//        this.distance=distance;
//    }

    @Override
    protected String doInBackground(String... strings) {
        String lattitude = String.valueOf(mLocation.getLatitude());
        String longitude = String.valueOf(mLocation.getLongitude());
        LocationParam locationParam = new LocationParam();
        locationParam.setLattitude(String.valueOf(lattitude));
        locationParam.setLongitude(String.valueOf(longitude));
        locationParam.setTime(time);
        // locationParam.setDistance(String.valueOf(distance));
        locationParam.setSynczed(false);
        DatabaseClient.getInstance(mContext).getAppDatabase()
                .locationDao()
                .insert(locationParam);
        return null;
    }
}
