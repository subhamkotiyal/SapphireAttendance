package com.webgurus.attendanceportal.demo;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LocationCustomArray {
    public String user_id;
    public String attendance_id;
    public String lat;
    public String longi;
    public String mobtime;
    public String battery;
    public String distance ;
    public Integer gps_status  ;

    public LocationCustomArray()
    {

    }

    public LocationCustomArray(String lattitude, String longitude, String time, String userID, String attendanceID, String batteryInfo,String distance,Integer gps_status) {
        this.lat = lattitude;
        this.longi = longitude;
        this.mobtime = time;
        this.user_id = userID;
        this.attendance_id = attendanceID;
        this.battery = batteryInfo;
        this.distance = distance;
        this.gps_status  = gps_status;

    }

    public LocationCustomArray(JSONObject jsonObject) {
        try {
            this.attendance_id = jsonObject.getString("attendance_id");
            this.battery = jsonObject.getString("battery");
            this.lat = jsonObject.getString("lat");
            this.longi = jsonObject.getString("longi");
            this.mobtime = jsonObject.getString("mobtime");
            this.user_id = jsonObject.getString("user_id");
            this.battery = jsonObject.getString("battery");
           this.distance = jsonObject.getString("distance");
           this.gps_status  = jsonObject.getInt("gps_status ");
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public JSONObject changeClasstoJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("attendance_id", this.attendance_id);
            jsonObject.put("battery", this.battery);
            jsonObject.put("lat", this.lat);
            jsonObject.put("longi", this.longi);
            jsonObject.put("mobtime", this.mobtime);
            jsonObject.put("user_id", this.user_id);
            jsonObject.put("battery", this.battery);
            jsonObject.put("distance", this.distance);
            jsonObject.put("gps_status ", this.gps_status );

        } catch (Exception e) {
          e.printStackTrace();
        }
        return jsonObject;
    }

    public String changeListToJson(ArrayList<LocationCustomArray> mList){

        JSONObject jsonObject =new JSONObject();
        JSONArray jsonArray =new JSONArray();
        try {
            for (int i=0;i<mList.size();i++){
                LocationCustomArray locationCustomArray = mList.get(i);
                JSONObject jsonObject1= locationCustomArray.changeClasstoJson();
                jsonArray.put(jsonObject1);
            }
            jsonObject.put("location",jsonArray);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return  jsonObject.toString();
    }


    public ArrayList<LocationCustomArray> changeStringintoList(String str){

        ArrayList<LocationCustomArray> mList =new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray jsonArray=jsonObject.getJSONArray("location");
            for (int i=0;i<jsonArray.length();i++){
                LocationCustomArray locationCustomArray= new LocationCustomArray(jsonArray.getJSONObject(i));
                mList.add(locationCustomArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mList;

    }


}