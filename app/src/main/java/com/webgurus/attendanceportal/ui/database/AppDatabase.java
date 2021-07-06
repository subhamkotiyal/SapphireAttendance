package com.webgurus.attendanceportal.ui.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.webgurus.LocationExceptionParamDao;
import com.webgurus.attendanceportal.ExceptionParam;

@Database(entities = {LocationParam.class, ExceptionParam.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LocationParamDao locationDao();
    public abstract LocationExceptionParamDao exceptionDao();
}
