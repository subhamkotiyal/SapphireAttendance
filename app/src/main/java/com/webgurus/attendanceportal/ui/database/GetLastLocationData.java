package com.webgurus.attendanceportal.ui.database;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

public class GetLastLocationData extends AsyncTask<Void, Void, LocationParam> {

    Context mContext;
    DatabaseListenerCallback databaseListenerCallback;

    public GetLastLocationData(Context context,DatabaseListenerCallback databaseListenerCallback) {
        this.mContext=context;
        this.databaseListenerCallback=databaseListenerCallback;
    }

    @Override
    protected LocationParam doInBackground(Void... voids) {
        LocationParam taskList =DatabaseClient
                .getInstance(mContext)
                .getAppDatabase()
                .locationDao()
                .getLastValue();
      //  databaseListenerCallback.processLastData(taskList);
        return taskList;
    }
    @Override
    protected void onPostExecute(LocationParam tasks) {
        super.onPostExecute(tasks);
        //  databaseListenerCallback.processData(tasks);
    }
}