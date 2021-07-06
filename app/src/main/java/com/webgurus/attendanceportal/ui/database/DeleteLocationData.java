package com.webgurus.attendanceportal.ui.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

public class DeleteLocationData extends AsyncTask<String, String, String> {

    Context mContext;

    public DeleteLocationData(Context context) {
        this.mContext = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        DatabaseClient.getInstance(mContext).getAppDatabase()
                .locationDao()
                .delete();
        return null;
    }
}
