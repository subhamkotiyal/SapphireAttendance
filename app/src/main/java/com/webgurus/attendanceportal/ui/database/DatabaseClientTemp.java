package com.webgurus.attendanceportal.ui.database;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClientTemp {

    private Context mCtx;
    private static DatabaseClientTemp mInstance;

    //our app database object
    private AppDatabase appDatabase;

    private DatabaseClientTemp(Context mCtx) {
        this.mCtx = mCtx;
        //creating the app database with Room database builder
        //MyToDos is the name of the database
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, "MyToDosTemp").build();
    }

    public static synchronized DatabaseClientTemp getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClientTemp(mCtx);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
