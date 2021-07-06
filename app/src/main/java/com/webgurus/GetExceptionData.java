package com.webgurus;

import android.content.Context;
import android.os.AsyncTask;
import com.webgurus.attendanceportal.DatabaseExceptionListenerCallback;
import com.webgurus.attendanceportal.ExceptionParam;
import java.util.List;


public class GetExceptionData extends AsyncTask<Void, Void, List<ExceptionParam>> {

    Context mContext;
    DatabaseExceptionListenerCallback databaseListenerCallback;

    public GetExceptionData(Context context, DatabaseExceptionListenerCallback databaseListenerCallback) {
        this.mContext = context;
        this.databaseListenerCallback = databaseListenerCallback;
    }

    @Override
    protected List<ExceptionParam> doInBackground(Void... voids) {
        List<ExceptionParam> taskList = DatabaseExceptionClientTemp
                .getInstance(mContext)
                .getAppDatabase()
                .exceptionDao()
                .getAllException();
        databaseListenerCallback.getData(taskList);
        return taskList;
    }

    @Override
    protected void onPostExecute(List<ExceptionParam> tasks) {
        super.onPostExecute(tasks);
        databaseListenerCallback.getData(tasks);
    }

}