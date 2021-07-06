package com.webgurus.attendanceportal.demo;

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
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.webgurus.attendanceportal.APIClient;
import com.webgurus.attendanceportal.InternetSettingCheck;
import com.webgurus.attendanceportal.LocationUpdatesService55;
import com.webgurus.attendanceportal.pojo.NewLocationPojo;
import com.webgurus.network.GetDataService;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationUpdaterService66 extends Service {


    public static final int TWO_MINUTES = 1000; // 120 seconds
    public static final int TWO_Second = 20000; // 120 seconds
    public static Boolean isRunning = false;

    public LocationManager mLocationManager;
    public LocationUpdaterListener mLocationListener;
    public Location previousBestLocation = null;
    GetDataService apiInterface;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationUpdaterListener();
        super.onCreate();
    }

    Handler mHandler = new Handler();
    Runnable mHandlerTask = new Runnable() {
        @Override
        public void run() {
            if (!isRunning) {
                startListening();
            }
            mHandler.postDelayed(mHandlerTask, TWO_Second);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandlerTask.run();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopListening();
        mHandler.removeCallbacks(mHandlerTask);
        super.onDestroy();
    }

    private void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mLocationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);

            if (mLocationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        }
        isRunning = true;
    }

    private void stopListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.removeUpdates(mLocationListener);
        }
        isRunning = false;
    }

    public class LocationUpdaterListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (isBetterLocation(location, previousBestLocation)) {
                previousBestLocation = location;
                try {
                    onNewLocation(location);
                    Toast.makeText(LocationUpdaterService66.this, "Location Updated", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stopListening();
                }
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            stopListening();
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    private void onNewLocation(Location location) {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        String access_token = settings.getString("Access_Token", "");
        String user_id = String.valueOf(settings.getInt("User_ID", 0));
        String attendance_id = settings.getString("Attendance_ID", "");
        String currentDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        LocationCustomArray locationCustomArray = new LocationCustomArray
                (String.valueOf(location.getLatitude()),
                        String.valueOf(location.getLongitude()), currentDate.toString(), user_id, attendance_id, String.valueOf(getBatteryPercentage(getApplicationContext())),
                        "20.00",0);
        Log.e("Location Object: ", locationCustomArray.toString());

        String jString = settings.getString("LatLongJson", "");

        ArrayList<LocationCustomArray> mList = new ArrayList<>();
        if (jString.length() > 0) {
            mList = locationCustomArray.changeStringintoList(jString);
        }

        Log.e("First List", String.valueOf(mList.size()));
        if (mList.size() > 0) {
            LocationCustomArray obj = mList.get(mList.size() - 1);
            Log.d("lastLat", chnageInFourPlace(Double.parseDouble(obj.lat)));
            Log.d("lastLong", chnageInFourPlace(Double.parseDouble(obj.longi)));
            Log.d("prevcureentLat", String.valueOf(location.getLatitude()));
            Log.d("prevcureentLong", String.valueOf(location.getLongitude()));

            if (!(chnageInFourPlace(Double.parseDouble(obj.lat)).compareTo(chnageInFourPlace(location.getLatitude())) == 0 && chnageInFourPlace(Double.parseDouble(obj.longi)).compareTo(chnageInFourPlace(location.getLongitude())) == 0)) {
                Toast.makeText(LocationUpdaterService66.this, "Location Changed", Toast.LENGTH_SHORT).show();
                mList.add(locationCustomArray);
                Log.e("Second List ", String.valueOf(mList.size()));
                // Store notes to SharedPreferences
                editor.putString("LatLongJson", locationCustomArray.changeListToJson(mList));
                editor.apply();
                //readWebPageBulk( settings.getString("LatLongJson",""));

                if (settings.getString("previousTime", "") == "") {
                    readWebPageBulk(settings.getString("LatLongJson", ""));
                } else {
                    if (settings.getString("previousTime", "") != null) {
                        long previousTime = Long.parseLong(settings.getString("previousTime", ""));
                        long currentTime = Long.parseLong(getCurrentTimestamp());
                        long totalmsg = (currentTime - previousTime) / 60;
                        if (totalmsg > 1) {
                            readWebPageBulk(settings.getString("LatLongJson", ""));
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Location Not changed", Toast.LENGTH_SHORT).show();
            }

        } else {
            mList.add(locationCustomArray);
            Log.e("Second List ", String.valueOf(mList.size()));
            // Store notes to SharedPreferences
            editor.putString("LatLongJson", locationCustomArray.changeListToJson(mList));
            editor.apply();


            if (settings.getString("previousTime", "") == "") {
                readWebPageBulk(settings.getString("LatLongJson", ""));
            } else {
                if (settings.getString("previousTime", "") != null) {
                    long previousTime = Long.parseLong(settings.getString("previousTime", ""));
                    long currentTime = Long.parseLong(getCurrentTimestamp());
                    long totalmsg = (currentTime - previousTime) / 60;
                    if (totalmsg > 1) {
                        readWebPageBulk(settings.getString("LatLongJson", ""));
                    }
                }
            }
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



    public void readWebPageBulk(String location)  {

        if (!Utils.isNetworkConnected(this)) {
            Toast.makeText(
                    this,
                    "No internet connection available. Please check your internet connection.",
                    Toast.LENGTH_SHORT
            ).show();
            startActivity(new Intent(this, InternetSettingCheck.class));
            return;
        }

        SharedPreferences settings = getApplicationContext().getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE);
        String access_token = settings.getString("Access_Token", "");
        String user_id= String.valueOf(settings.getInt("User_ID", 0));
        String attendance_id=settings.getString("Attendance_ID", "");
        //apiInterface =APIClient.getClient().create(GetDataService.class);
        apiInterface = APIClient.getClient().create(GetDataService.class);
        if(attendance_id.equals("")){
            attendance_id="0";
        }
        if(!user_id.equals("")){
            Call<NewLocationPojo> call = apiInterface.updateLatLongforUserIDBulk(
                    "Bearer " + access_token,location

            );
            Log.e("JsonLocation  :",location);
            call.enqueue(new Callback<NewLocationPojo>() {
                @Override
                public void onResponse(Call<NewLocationPojo> call, Response<NewLocationPojo> response) {
                    try {
                        if(response.body().getStatus().equals("success")){
                            Log.e("ApiResponse ",response.body().toString());
                            Toast.makeText(LocationUpdaterService66.this, "Location Updated SucessFully", Toast.LENGTH_SHORT).show();
                            String current =getCurrentTimestamp();
                            SharedPreferences settings = getApplicationContext().getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("previousTime",current);
                            editor.apply();

                        }else {
                            Toast.makeText(LocationUpdaterService66.this, ""+response.body().toString(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(LocationUpdaterService66.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("ServerSam :",e.toString());
                    }
                }

                @Override
                public void onFailure(Call<NewLocationPojo> call, Throwable t) {
                    Toast.makeText(LocationUpdaterService66.this, ""+t.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("ServerSam :",t.toString());
                }
            });
        }


    }


    private String getCurrentTimestamp(){
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        return ts;
    }


    public String chnageInFourPlace(double value){
        DecimalFormat df = new DecimalFormat("#.######");
        df.setRoundingMode(RoundingMode.FLOOR);
        return df.format(value);
    }

}