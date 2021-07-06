                                              /**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webgurus.attendanceportal;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Binder;
import android.os.Build;
 import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.webgurus.attendanceportal.demo.LocationCustomArray;
import com.webgurus.attendanceportal.demo.Utils;
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

/**
 * A bound and started service that is promoted to a foreground service when location updates have
 * been requested and all clients unbind.
 *
 * For apps running in the background on "O" devices, location is computed only once every 10
 * minutes and delivered batched every 30 minutes. This restriction applies even to apps
 * targeting "N" or lower which are run on "O" devices.
 *
 * This sample show how to use a long-running service for location updates. When an activity is
 * bound to this service, frequent location updates are permitted. When the activity is removed
 * from the foreground, the service promotes itself to a foreground service, and location updates
 * continue. When the activity comes back to the foreground, the foreground service stops, and the
 * notification associated with that service is removed.
 */
public class LocationUpdatesService55 extends Service {


    public static final int TWO_MINUTES = 20000; // 10 seconds
    public static Boolean isRunning = false;

    private static final String PACKAGE_NAME =
            "com.google.android.gms.location.sample.locationupdatesforegroundservice";

    private static final String TAG = LocationUpdatesService55.class.getSimpleName();

    /**
     * The name of the channel for notifications.
     */
    private static final String CHANNEL_ID = "channel_01";

    static final String ACTION_BROADCAST = PACKAGE_NAME + ".broadcast";

    static final String EXTRA_LOCATION = PACKAGE_NAME + ".location";
    private static final String EXTRA_STARTED_FROM_NOTIFICATION = PACKAGE_NAME +
            ".started_from_notification";

    private final IBinder mBinder = new LocalBinder();

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;

    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /**
     * The identifier for the notification displayed for the foreground service.
     */
    private static final int NOTIFICATION_ID = 12345678;

    /**
     * Used to check whether the bound activity has really gone away and not unbound as part of an
     * orientation change. We create a foreground service notification only if the former takes
     * place.
     */
    private boolean mChangingConfiguration = false;

    private NotificationManager mNotificationManager;

    /**
     * Contains parameters used by {@link com.google.android.gms.location.FusedLocationProviderApi}.
     */
    private LocationRequest mLocationRequest;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Callback for changes in location.
     */
    private LocationCallback mLocationCallback;

    private Handler mServiceHandler;

    /**
     * The current location.
     */
    private Location mLocation;

    GetDataService apiInterface;

    public LocationUpdatesService55() {
    }
    boolean isSimilar = false;

    public Location previousBestLocation = null;

    public boolean isFindResponse=false;

    @Override
    public void onCreate() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                super.onLocationResult(locationResult);
//                SharedPreferences settings = getApplicationContext().getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE);
//                String user_id= String.valueOf(settings.getInt("User_ID", 0));
//                String attendance_id=settings.getString("Attendance_ID", "");
//                if (isBetterLocation(locationResult.getLastLocation(), previousBestLocation)) {
//                    //Toast.makeText(LocationUpdatesService55.this, "Get Location Sucess", Toast.LENGTH_SHORT).show();
//                    previousBestLocation = locationResult.getLastLocation();
//                    onNewLocation(locationResult.getLastLocation());
//                    Log.e("Location Result :",locationResult.getLocations().toString());
//                    Log.e("Location Resultss :",locationResult.getLastLocation().toString());
//                }
              SharedPreferences settings = getApplicationContext().getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE);
              String user_id= String.valueOf(settings.getInt("User_ID", 0));
              String attendance_id=settings.getString("Attendance_ID", "");
                if(!attendance_id.equals("")&!user_id.equals("")){
                    Toast.makeText(LocationUpdatesService55.this, "L", Toast.LENGTH_SHORT).show();
                 //   if (isBetterLocation(locationResult.getLastLocation(), previousBestLocation)) {
                        previousBestLocation = locationResult.getLastLocation();
                        onNewLocation(locationResult.getLastLocation());
                        Log.e("Location Result :",locationResult.getLocations().toString());
                        Log.e("Location Resultss :",locationResult.getLastLocation().toString());
                   // }

                }


          }
        };

        createLocationRequest();
        getLastLocation();
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service started");
        boolean startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION,
                false);

        // We got here because the user decided to remove location updates from the notification.
        if (startedFromNotification) {
            removeLocationUpdates();
            stopSelf();
        }
        // Tells the system to not try to recreate the service after it has been killed.
        return START_NOT_STICKY;
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


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mChangingConfiguration = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) comes to the foreground
        // and binds with this service. The service should cease to be a foreground service
        // when that happens.
        Log.i(TAG, "in onBind()");
        stopForeground(true);
        mChangingConfiguration = false;
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) returns to the foreground
        // and binds once again with this service. The service should cease to be a foreground
        // service when that happens.
        Log.i(TAG, "in onRebind()");
        stopForeground(true);
        mChangingConfiguration = false;
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Last client unbound from service");

        // Called when the last client (MainActivity in case of this sample) unbinds from this
        // service. If this method is called due to a configuration change in MainActivity, we
        // do nothing. Otherwise, we make this service a foreground service.
        if (!mChangingConfiguration && Utils.requestingLocationUpdates(this)) {
            Log.i(TAG, "Starting foreground service");

            startForeground(NOTIFICATION_ID, getNotification());
        }
        return true; // Ensures onRebind() is called when a client re-binds.
    }

    @Override
    public void onDestroy() {
        mServiceHandler.removeCallbacksAndMessages(null);
    }

    /**
     * Makes a request for location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    public void requestLocationUpdates() {
        Log.i(TAG, "Requesting location updates");
        Utils.setRequestingLocationUpdates(this, true);
        startService(new Intent(getApplicationContext(), LocationUpdatesService55.class));
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,  Looper.getMainLooper());
        } catch (SecurityException unlikely) {
            Utils.setRequestingLocationUpdates(this, false);
            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }
    }

    /**
     * Removes location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    public void removeLocationUpdates() {
        Log.i(TAG, "Removing location updates");
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            Utils.setRequestingLocationUpdates(this, false);
            stopSelf();
        } catch (SecurityException unlikely) {
            Utils.setRequestingLocationUpdates(this, true);
            Log.e(TAG, "Lost location permission. Could not remove updates. " + unlikely);
        }
    }

    /**
     * Returns the {@link NotificationCompat} used as part of the foreground service.
     */
    private Notification getNotification() {
        Intent intent = new Intent(this, LocationUpdatesService55.class);

        CharSequence text = Utils.getLocationText(mLocation);

        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);

        // The PendingIntent that leads to a call to onStartCommand() in this service.
        PendingIntent servicePendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // The PendingIntent to launch activity.
        PendingIntent activityPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivityFinal.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .addAction(R.mipmap.app_icon, getString(R.string.launch_activity),
                        activityPendingIntent)
                .addAction(R.mipmap.app_icon, getString(R.string.remove_location_updates),
                        servicePendingIntent)
                .setContentText(text)
                .setContentTitle(Utils.getLocationTitle(this))
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.app_icon)
                .setTicker(text)
                .setWhen(System.currentTimeMillis());

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }

        return builder.build();
    }

    private void getLastLocation() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mLocation = task.getResult();
                            } else {
                                Log.w(TAG, "Failed to get location.");
                            }
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }
    }

    private void onNewLocation(Location location) {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        String access_token = settings.getString("Access_Token", "");
        String user_id= String.valueOf(settings.getInt("User_ID", 0));
        String attendance_id=settings.getString("Attendance_ID", "");
        Log.i(TAG, "New location: " + location.getLatitude());
        String currentDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        LocationCustomArray locationCustomArray= new LocationCustomArray
                (String.valueOf(location.getLatitude()),
                        String.valueOf(location.getLongitude()),currentDate.toString(),
                        user_id,attendance_id, String.valueOf(getBatteryPercentage(getApplicationContext())),"30.0",0);
        Log.e("Location Object: ", locationCustomArray.toString());

        String jString = settings.getString("LatLongJson","");

        ArrayList<LocationCustomArray> mList = new ArrayList<>();
        if(jString.length()>0)
        {
            mList = locationCustomArray.changeStringintoList(jString);
        }

        Log.e("First List", String.valueOf(mList.size()));
        if(mList.size()>0)
        {
            LocationCustomArray obj = mList.get(mList.size()-1);
            Log.d("lastLat",chnageInFourPlace(Double.parseDouble(obj.lat)));
            Log.d("lastLong",chnageInFourPlace(Double.parseDouble(obj.longi)));
            Log.d("prevcureentLat", String.valueOf(location.getLatitude()));
            Log.d("prevcureentLong",String.valueOf(location.getLongitude()));

            if(! (chnageInFourPlace(Double.parseDouble(obj.lat)).compareTo(chnageInFourPlace(location.getLatitude()))==0 && chnageInFourPlace(Double.parseDouble(obj.longi)).compareTo(chnageInFourPlace(location.getLongitude()))==0 ))
            {
                mList.add(locationCustomArray);
                Log.e("Second List ", String.valueOf(mList.size()));
                // Store notes to SharedPreferences
                editor.putString("LatLongJson", locationCustomArray.changeListToJson(mList));
                editor.apply();
                if(settings.getString("previousTime","")=="")
                {
                    if(settings.getString("IsOnThread","").equals("")){
                        editor.putString("IsOnThread","ServiceStart");
                        editor.apply();
                        readWebPageBulk( settings.getString("LatLongJson",""));
                    }else {
                        return;
                    }

                }
                else
                {
                    if(settings.getString("previousTime","")!=null){
                        long previousTime=Long.parseLong(settings.getString("previousTime",""));
                        long currentTime=Long.parseLong(getCurrentTimestamp());
                        long totalmsg=(currentTime-previousTime)/60;
//                     if(totalmsg>1){
                         if(settings.getString("IsOnThread","").equals("")){
                             editor.putString("IsOnThread","ServiceStart");
                             editor.apply();
                             readWebPageBulk( settings.getString("LatLongJson",""));
                         }else {
                             return;
                         }

                     // }
                    }
                }
            }else {
                Log.e("Status :","Location Not changed");
            }

        }else {
            mList.add(locationCustomArray);
            Log.e("Second List ", String.valueOf(mList.size()));
            // Store notes to SharedPreferences
            editor.putString("LatLongJson", locationCustomArray.changeListToJson(mList));
            editor.apply();


            if(settings.getString("previousTime","")=="")
            {

                if(settings.getString("IsOnThread","").equals("")){
                    editor.putString("IsOnThread","ServiceStart");
                    editor.apply();
                    readWebPageBulk( settings.getString("LatLongJson",""));
                }else {
                    return;
                }


            }
            else
            {
                if(settings.getString("previousTime","")!=null){
                    long previousTime=Long.parseLong(settings.getString("previousTime",""));
                    long currentTime=Long.parseLong(getCurrentTimestamp());
                    long totalmsg=(currentTime-previousTime)/60;
                  // if(totalmsg>1){
                       if(settings.getString("IsOnThread","").equals("")){
                           editor.putString("IsOnThread","ServiceStart");
                           editor.apply();
                           readWebPageBulk( settings.getString("LatLongJson",""));
                       }else {
                           return;
                       }
                 //  }
                }
            }
        }



        // readWebPageBulk( settings.getString("LatLongJson",""));
        if (serviceIsRunningInForeground(this)) {
            // startForeground(intent.getIntExtra("notification_Id", NOTIFICATION_ID));
            mNotificationManager.notify(NOTIFICATION_ID, getNotification());
        }
    }


    public String chnageInFourPlace(double value){
        DecimalFormat df = new DecimalFormat("#.######");
        df.setRoundingMode(RoundingMode.FLOOR);
        return df.format(value);
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
                            isFindResponse=true;
                            Toast.makeText(LocationUpdatesService55.this, "Location Updated SucessFully", Toast.LENGTH_SHORT).show();
                            Log.e("ApiResponse ",response.body().toString());
                            String current =getCurrentTimestamp();
                            SharedPreferences settings = getApplicationContext().getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("previousTime",current);
                            editor.putString("IsOnThread","");
                            editor.apply();

                        }else {
                            Toast.makeText(LocationUpdatesService55.this, ""+response.body().toString(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(LocationUpdatesService55.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("ServerSam :",e.toString());
                    }
                }

                @Override
                public void onFailure(Call<NewLocationPojo> call, Throwable t) {
                    Toast.makeText(LocationUpdatesService55.this, ""+t.toString(), Toast.LENGTH_SHORT).show();
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

    /**
     * Sets the location request parameters.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        LocationUpdatesService55 getService() {
            return LocationUpdatesService55.this;
        }
    }

    /**
     * Returns true if this is a foreground service.
     *
     * @param context The {@link Context}.
     */
    public boolean serviceIsRunningInForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (getClass().getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
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

}
