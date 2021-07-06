package com.webgurus.attendanceportal.ui.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LocationParamDao {

    // "SELECT * FROM LocationParam order By id desc
    @Query("SELECT * FROM LocationParam ")
    List<LocationParam> getAll();

    // "SELECT * FROM LocationParam order By id desc
    @Query("SELECT * FROM LocationParam WHERE isSynczed = 0 limit 10  ")
    List<LocationParam> getAllNotSync();


    @Query("SELECT * FROM LocationParam  order By id desc limit 1  ")
    LocationParam getLastValue();

    @Insert
    void insert(LocationParam task);


    @Query("DELETE FROM LocationParam WHERE isSynczed = 1")
    void delete();

    @Update
    void update(LocationParam task);
}
























