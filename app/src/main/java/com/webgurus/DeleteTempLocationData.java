package com.webgurus;

import android.content.Context;
import android.os.AsyncTask;

import com.webgurus.attendanceportal.ui.database.DatabaseClient;
import com.webgurus.attendanceportal.ui.database.DatabaseClientTemp;

public class DeleteTempLocationData extends AsyncTask<String, String, String> {

    Context mContext;

    public DeleteTempLocationData(Context context) {
        this.mContext = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        DatabaseClientTemp.getInstance(mContext).getAppDatabase()
                .locationDao()
                .delete();
        return null;
    }
}
