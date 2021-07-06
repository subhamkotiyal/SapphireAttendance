package com.webgurus;

import android.content.Context;
import android.os.AsyncTask;

import com.webgurus.attendanceportal.ui.database.DatabaseClientTemp;
import com.webgurus.attendanceportal.ui.database.DatabaseListenerCallback;
import com.webgurus.attendanceportal.ui.database.LocationParam;

import java.util.List;

public class GetLocationDataTempForNow extends AsyncTask<Void, Void, List<LocationParam>> {

    Context mContext;
    DatabaseListenerCallback databaseListenerCallback;

    public GetLocationDataTempForNow(Context context, DatabaseListenerCallback databaseListenerCallback) {
        this.mContext = context;
        this.databaseListenerCallback = databaseListenerCallback;
    }

    @Override
    protected List<LocationParam> doInBackground(Void... voids) {
        List<LocationParam> taskList = DatabaseClientTemp
                .getInstance(mContext)
                .getAppDatabase()
                .locationDao()
                .getAllNotSync();
        databaseListenerCallback.processData(taskList);
        return taskList;
    }

    @Override
    protected void onPostExecute(List<LocationParam> tasks) {
        super.onPostExecute(tasks);
        //  databaseListenerCallback.processData(tasks);
    }
}