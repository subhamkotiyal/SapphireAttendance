package com.webgurus.newservice;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import com.webgurus.attendanceportal.ui.database.DatabaseClientTemp;
import com.webgurus.attendanceportal.ui.database.LocationParam;

public class LocalDatabaseInsert extends AsyncTask<String, String, String> {

        Location mLocation;
         Context mContext;
         String time;

public LocalDatabaseInsert(Location location, String time, Context context) {
        this.mLocation = location;
        this.mContext = context;
        this.time = time;
        }

@Override
protected String doInBackground(String... strings) {
        String lattitude = String.valueOf(mLocation.getLatitude());
        String longitude = String.valueOf(mLocation.getLongitude());
        LocationParam locationParam = new LocationParam();
        locationParam.setLattitude(String.valueOf(lattitude));
        locationParam.setLongitude(String.valueOf(longitude));
        locationParam.setSynczed(false);
        locationParam.setTime(String.valueOf(time));
        DatabaseClientTemp.getInstance(mContext).getAppDatabase()
        .locationDao()
        .insert(locationParam);
        return null;
        }
        }
