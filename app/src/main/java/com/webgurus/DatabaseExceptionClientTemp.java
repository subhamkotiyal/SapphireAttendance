package com.webgurus;

import android.content.Context;

import androidx.room.Room;

import com.webgurus.attendanceportal.ui.database.AppDatabase;

public class DatabaseExceptionClientTemp {

    private Context mCtx;
    private static DatabaseExceptionClientTemp mInstance;

    //our app database object
    private AppDatabase appDatabase;

    private DatabaseExceptionClientTemp(Context mCtx) {
        this.mCtx = mCtx;
        //creating the app database with Room database builder
        //MyToDos is the name of the database
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, "MyExceptionTemp").build();
    }

    public static synchronized DatabaseExceptionClientTemp getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseExceptionClientTemp(mCtx);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}