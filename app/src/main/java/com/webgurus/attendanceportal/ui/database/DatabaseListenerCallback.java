package com.webgurus.attendanceportal.ui.database;

import com.webgurus.attendanceportal.ExceptionParam;
import com.webgurus.attendanceportal.pojo.Location;

import java.util.List;

public interface DatabaseListenerCallback {
    void processData(List<LocationParam> locationData);
    //    void processLastData(LocationParam locationData);
}


