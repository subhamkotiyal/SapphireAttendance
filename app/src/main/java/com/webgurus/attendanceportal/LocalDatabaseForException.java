package com.webgurus.attendanceportal;

import android.content.Context;
import android.os.AsyncTask;
import com.webgurus.DatabaseExceptionClientTemp;

public class LocalDatabaseForException extends AsyncTask<String, String, String> {

    Context mContext;
    String time;
    String exception ;

    public LocalDatabaseForException(Context mContext, String time, String exception) {
        this.mContext = mContext;
        this.time = time;
        this.exception = exception;
    }


    @Override
    protected String doInBackground(String... strings) {
        ExceptionParam exceptionParam= new ExceptionParam();
        exceptionParam.setException(exception);
        exceptionParam.setTime(time);
        DatabaseExceptionClientTemp.getInstance(mContext).getAppDatabase()
                .exceptionDao()
                .insertexcep(exceptionParam);
        return null;
    }
}
