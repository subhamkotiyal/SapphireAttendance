package com.webgurus;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.webgurus.attendanceportal.APIClient;
import com.webgurus.attendanceportal.InternetSettingCheck;
import com.webgurus.attendanceportal.demo.LocationCustomArray;
import com.webgurus.attendanceportal.demo.Utils;
import com.webgurus.attendanceportal.pojo.GpaStatusPojo;
import com.webgurus.attendanceportal.pojo.NewLocationPojo;
import com.webgurus.network.GetDataService;
import com.webgurus.newservice.MyService6666;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyService extends Service{

    public static final int notify = 3000;  //interval between two services(Here Service run every 5 Minute)
    private Timer mTimer = null;    //timer handling
    int gps_status=1;
    GetDataService apiInterface;
    SharedPreferences.Editor editor ;


    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        if (mTimer != null) // Cancel if already existed
            mTimer.cancel();
        else
            mTimer = new Timer();   //recreate new
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);   //Schedule task
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();    //For Cancel Timer
        Toast.makeText(this, "Service is Destroyed", Toast.LENGTH_SHORT).show();
    }
    //class TimeDisplay for handling task
    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            SharedPreferences settings = getApplicationContext().getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE);
            final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
            gps_status=1;
            if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                gps_status=0;
                editor= getSharedPreferences("AcessToken", MODE_PRIVATE).edit();
                editor.putString("temp_lattitude", "");
                editor.putString("temp_longitude", "");
                editor.apply();
            }
            hitApitoUpdate(gps_status);
        }
    }




    public static int getBatteryPercentage(Context context) {
        if (Build.VERSION.SDK_INT >= 21) {

            BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
            return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        } else {

            IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, iFilter);
            int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
            int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

            double batteryPct = level / (double) scale;

            return (int) (batteryPct * 100);
        }
    }



    public void hitApitoUpdate(Integer gpsstatus){
        if (!Utils.isNetworkConnected(this)) {
            Toast.makeText(this, "No internet connection available. Please check your internet connection.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, InternetSettingCheck.class));
            return;

        }
        SharedPreferences settings = getApplicationContext().getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE);
        String lattitude = settings.getString("temp_lattitude", "");
        String longitude = settings.getString("temp_longitude", "");
        String  userID = String.valueOf(settings.getInt("User_ID", 0));
        String access_token = settings.getString("Access_Token", "");
        Integer battery_staus=getBatteryPercentage(getApplicationContext());
        apiInterface = APIClient.getClient().create(GetDataService.class);
            Call<GpaStatusPojo> call = apiInterface.updateGpsStatus("Bearer " + access_token,gpsstatus,userID,
                    lattitude,longitude ,battery_staus,"s","1.2Ver");
            call.enqueue(new Callback<GpaStatusPojo>() {
                @Override
                public void onResponse(Call<GpaStatusPojo> call, Response<GpaStatusPojo> response) {
                    try {
                        String dsf="";
                        Toast.makeText(MyService.this, "Location Updated SucessFully", Toast.LENGTH_SHORT).show();
                       Log.d("Gps sttaus :",response.body().getMessage().toString());
                    } catch (Exception e) {
                        String dfgdsdsdsfds="";
                        Log.d("Gps sttaus 22 :",e.toString());
                    }
                }

                @Override
                public void onFailure(Call<GpaStatusPojo> call, Throwable t) {
                    Log.d("Gps sttaus 33 :",t.toString());
                }
            });
        }


}