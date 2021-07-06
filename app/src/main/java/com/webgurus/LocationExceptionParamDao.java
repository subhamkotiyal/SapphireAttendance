package com.webgurus;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.webgurus.attendanceportal.ExceptionParam;
import com.webgurus.attendanceportal.ui.database.LocationParam;

import java.util.List;

@Dao
public interface LocationExceptionParamDao {

    @Insert
    void insertexcep(ExceptionParam exceptionParam);

    @Query("SELECT * FROM ExceptionParam ")
    List<ExceptionParam> getAllException();

}
