package com.webgurus.attendanceportal;

import java.util.List;


public interface DatabaseExceptionListenerCallback {
    void getData(List<ExceptionParam> locationData);
    //    void processLastData(LocationParam locationData);
}