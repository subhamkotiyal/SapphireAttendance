package com.webgurus.attendanceportal.ui.database;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

public class GetLocationDataTemp extends AsyncTask<Void, Void, List<LocationParam>> {

    Context mContext;
    DatabaseListenerCallback databaseListenerCallback;

    public GetLocationDataTemp(Context context, DatabaseListenerCallback databaseListenerCallback) {
        this.mContext = context;
        this.databaseListenerCallback = databaseListenerCallback;
    }

    @Override
    protected List<LocationParam> doInBackground(Void... voids) {
        List<LocationParam> taskList = DatabaseClientTemp
                .getInstance(mContext)
                .getAppDatabase()
                .locationDao()
                .getAll();
        databaseListenerCallback.processData(taskList);
        return taskList;
    }

    @Override
    protected void onPostExecute(List<LocationParam> tasks) {
        super.onPostExecute(tasks);
        //  databaseListenerCallback.processData(tasks);
    }
}