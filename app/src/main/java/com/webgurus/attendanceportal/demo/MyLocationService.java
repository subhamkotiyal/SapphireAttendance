package com.webgurus.attendanceportal.demo;

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
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.webgurus.attendanceportal.InternetSettingCheck;
import com.webgurus.attendanceportal.MainActivity;
import com.webgurus.attendanceportal.R;
import com.webgurus.attendanceportal.pojo.NewLocationPojo;
import com.webgurus.attendanceportal.pojo.UpdateLocationPojo;
import com.webgurus.network.GetDataService;
import com.webgurus.utils.security.NetworkStateReceiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

//import io.nlopez.smartlocation.OnLocationUpdatedListener;
//import io.nlopez.smartlocation.SmartLocation;
//import io.nlopez.smartlocation.location.config.LocationAccuracy;
//import io.nlopez.smartlocation.location.config.LocationParams;
//import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyLocationService  extends Service {

    private static final String PACKAGE_NAME =
            "com.google.android.gms.location.sample.locationupdatesforegroundservice";
//    private LocationGooglePlayServicesProvider provider;
    private Location mLocation;
    GetDataService apiInterface;
    private static final String TAG = MyLocationService.class.getSimpleName();
    private NotificationManager mNotificationManager;
    private Handler mServiceHandler;
    private static final String CHANNEL_ID = "channel_01";
    private static final int NOTIFICATION_ID = 12345678;
    private static final String EXTRA_STARTED_FROM_NOTIFICATION = PACKAGE_NAME + ".started_from_notification";
    private boolean mChangingConfiguration = false;
    private final IBinder mBinder = new LocalBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        startLocationListener();

    }

    public class LocalBinder extends Binder {
        public MyLocationService getService() {
            return MyLocationService.this;
        }
    }

    private void startLocationListener() {
//
//        provider = new LocationGooglePlayServicesProvider();
//        provider.setCheckLocationSettings(true);
//        long mLocTrackingInterval = 1000 * 5; // 5 sec
//        float trackingDistance = 0;
//        LocationAccuracy trackingAccuracy = LocationAccuracy.HIGH;
//
//        LocationParams.Builder builder = new LocationParams.Builder()
//                .setAccuracy(trackingAccuracy)
//                .setDistance(trackingDistance)
//                .setInterval(mLocTrackingInterval);
//
//        SmartLocation.with(this)
//                .location()
//                .continuous()
//                .config(builder.build())
//                .start(new OnLocationUpdatedListener() {
//                    @Override
//                    public void onLocationUpdated(Location location) {
//                        mLocation=location;
//                        onNewLocation(location);
//                       Toast.makeText(getApplicationContext(), "Sucess : "+location.getLatitude()+location.getLongitude(), Toast.LENGTH_SHORT).show();
//                    }
//                });


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
        // mServiceHandler.removeCallbacksAndMessages(null);
        Log.i(TAG, "Last client unbound from service");
        // Called when the last client (MainActivity in case of this sample) unbinds from this
        // service. If this method is called due to a configuration change in MainActivity, we
        // do nothing. Otherwise, we make this service a foreground service.
        if (!mChangingConfiguration && Utils.requestingLocationUpdates(this)) {
            Log.i(TAG, "Starting foreground service");
            startForeground(NOTIFICATION_ID, getNotification());
        }
        /*if(!isRunning){
            mythread.interrupt();
            mythread.stop();
        }
*/
    }

    public void requestLocationUpdates() {
        Log.i(TAG, "Requesting location updates");
        Utils.setRequestingLocationUpdates(this, true);
        startService(new Intent(getApplicationContext(), MyLocationService.class));
        try {
           startLocationListener();
        } catch (SecurityException unlikely) {
            Utils.setRequestingLocationUpdates(this, false);
            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
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

    public void removeLocationUpdates() {
        Log.i(TAG, "Removing location updates");
        try {
           // SmartLocation.with(MyLocationService.this).location().stop();
            Utils.setRequestingLocationUpdates(this, false);
            stopSelf();
        } catch (SecurityException unlikely) {
            Utils.setRequestingLocationUpdates(this, true);
            Log.e(TAG, "Lost location permission. Could not remove updates. " + unlikely);
        }
    }



    private void onNewLocation(Location location) {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
       String access_token = settings.getString("Access_Token", "");
       String user_id= String.valueOf(settings.getInt("User_ID", 0));
       String attendance_id=settings.getString("Attendance_ID", "");
       Log.i(TAG, "New location: " + location.getLatitude());
     //   Date currentTime = Calendar.getInstance().getTime();
        String currentDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());


        LocationCustomArray locationCustomArray= new LocationCustomArray
                (String.valueOf(location.getLatitude()),
                        String.valueOf(location.getLongitude()),currentDate.toString(),user_id,attendance_id,
                        String.valueOf(getBatteryPercentage(getApplicationContext())),"4.00",0);
        Log.e("Location Object: ", locationCustomArray.toString());

        String jString = settings.getString("LatLongJson","");

        ArrayList<LocationCustomArray> mList = new ArrayList<>();
        if(jString.length()>0)
        {
            mList = locationCustomArray.changeStringintoList(jString);
        }

        Log.e("First List", String.valueOf(mList.size()));


        mList.add(locationCustomArray);
        Log.e("Second List ", String.valueOf(mList.size()));
        // Store notes to SharedPreferences
        editor.putString("LatLongJson", locationCustomArray.changeListToJson(mList));
        editor.apply();

        if(settings.getString("previousTime","")==""){
            readWebPageBulk( settings.getString("LatLongJson",""));
        }else {
            if(settings.getString("previousTime","")!=null){
                long previousTime=Long.parseLong(settings.getString("previousTime",""));
                long currentTime=Long.parseLong(getCurrentTimestamp());
                long totalmsg=(currentTime-previousTime)/60;
                if(totalmsg>1){
                    readWebPageBulk( settings.getString("LatLongJson",""));
                }
            }
        }

       // readWebPageBulk( settings.getString("LatLongJson",""));
        if (serviceIsRunningInForeground(this)) {
            // startForeground(intent.getIntExtra("notification_Id", NOTIFICATION_ID));
            mNotificationManager.notify(NOTIFICATION_ID, getNotification());
        }
    }

    private String getCurrentTimestamp(){
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        return ts;
    }
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

    private Notification getNotification() {
        Intent intent = new Intent(this, MyLocationService.class);

        CharSequence text = Utils.getLocationText(mLocation);

        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);

        // The PendingIntent that leads to a call to onStartCommand() in this service.
        PendingIntent servicePendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // The PendingIntent to launch activity.
        PendingIntent activityPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

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
      //  apiInterface = APIClient.getClient().create(GetDataService.class);
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
                           Toast.makeText(MyLocationService.this, "Location Updated SucessFully", Toast.LENGTH_SHORT).show();
                             String current =getCurrentTimestamp();
                             SharedPreferences settings = getApplicationContext().getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE);
                             SharedPreferences.Editor editor = settings.edit();
                             editor.putString("previousTime",current);
                             editor.apply();
                             SharedPreferences preferences = getApplicationContext().getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE);
                             SharedPreferences.Editor ewditt = preferences.edit();
                             ewditt.remove("LatLongJson");
                             ewditt.commit();


                        }else {
                            Toast.makeText(MyLocationService.this, ""+response.body().toString(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(MyLocationService.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("ServerSam :",e.toString());
                    }
                }

                @Override
                public void onFailure(Call<NewLocationPojo> call, Throwable t) {
                    Toast.makeText(MyLocationService.this, ""+t.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("ServerSam :",t.toString());
                }
            });
        }


    }




    public void readWebPage(Location location)  {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE);
        String access_token = settings.getString("Access_Token", "");
        String user_id= String.valueOf(settings.getInt("User_ID", 0));
        String attendance_id=settings.getString("Attendance_ID", "");
      //  apiInterface = APIClient.getClient().create(GetDataService.class);
        if(attendance_id.equals("")){
            attendance_id="0";
        }
        if(!user_id.equals("")){
            Call<UpdateLocationPojo> call = apiInterface.updateLatLongforUserID(
                    "Bearer " + access_token,
                    String.valueOf(location.getLatitude()),
                    String.valueOf(location.getLongitude()),
                    user_id,
                    String.valueOf(getBatteryPercentage(getApplicationContext())),
                    attendance_id
            );
            call.enqueue(new Callback<UpdateLocationPojo>() {
                @Override
                public void onResponse(Call<UpdateLocationPojo> call, Response<UpdateLocationPojo> response) {
                    try {
                        Log.e("Api Response ",response.body().toString());
                        Toast.makeText(MyLocationService.this, "Location Updated SucessFully", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(MyLocationService.this, "Server Not Responding", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UpdateLocationPojo> call, Throwable t) {
                    Toast.makeText(MyLocationService.this, "Server Not Responding", Toast.LENGTH_SHORT).show();
                }
            });
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mChangingConfiguration = true;
    }



    @Nullable
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
}
