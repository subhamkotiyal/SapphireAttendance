package com.webgurus;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.room.Database;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.webgurus.attendanceportal.APIClient;
import com.webgurus.attendanceportal.InternetSettingCheck;
import com.webgurus.attendanceportal.LocationUpdatesService55;
import com.webgurus.attendanceportal.R;
import com.webgurus.attendanceportal.demo.LocationCustomArray;
import com.webgurus.attendanceportal.demo.Utils;
import com.webgurus.attendanceportal.pojo.NewLocationPojo;
import com.webgurus.attendanceportal.ui.database.DatabaseClient;
import com.webgurus.attendanceportal.ui.database.DatabaseListenerCallback;
import com.webgurus.attendanceportal.ui.database.DeleteLocationData;
import com.webgurus.attendanceportal.ui.database.GetLocationData;
import com.webgurus.attendanceportal.ui.database.InsertLocationData;
import com.webgurus.attendanceportal.ui.database.LocationParam;
import com.webgurus.network.GetDataService;
import com.webgurus.utils.UtilsFound;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

@SuppressLint("NewApi")
public class MyService66 extends JobService implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ResultCallback<Status>, DatabaseListenerCallback {


    public static final int TWO_MINUTES = 20000; // 10 seconds
    public static Boolean isRunning = false;

    /**
     * Update interval of location request
     */
    private final int UPDATE_INTERVAL = 1000;

    /**
     * fastest possible interval of location request
     */
    private final int FASTEST_INTERVAL = UPDATE_INTERVAL / 2;

    /**
     * The Job scheduler.
     */
    JobScheduler jobScheduler;

    /**
     * The Tag.
     */
    String TAG = "MyService";

    /**
     * LocationRequest instance
     */
    private LocationRequest locationRequest;

    /**
     * GoogleApiClient instance
     */
    private GoogleApiClient googleApiClient;

    /**
     * Location instance
     */
    private Location lastLocation;

    /**
     * Method is called when location is changed
     *
     * @param location - location from fused location provider
     */

    GetDataService apiInterface;

    public Location previousBestLocation = null;

    int gps_status = 1;
    SharedPreferences.Editor editor;
    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    @Override
    public void onLocationChanged(Location location) {
        editor = getSharedPreferences("AcessToken", MODE_PRIVATE).edit();
        editor.putString("temp_lattitude", String.valueOf(location.getLatitude()));
        editor.putString("temp_longitude", String.valueOf(location.getLongitude()));
        editor.apply();
        SharedPreferences settings = getApplicationContext().getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE);
        String user_id = String.valueOf(settings.getInt("User_ID", 0));
        String attendance_id = settings.getString("Attendance_ID", "");
        if (!attendance_id.equals("") & !user_id.equals("")) {
            // Toast.makeText(MyService66.this, ""+location.getLatitude() + "" + location.getLongitude(), Toast.LENGTH_SHORT).show();
            if (previousBestLocation != null) {
                if (isBetterLocation(location, previousBestLocation)) {
                    if (!(chnageInFourPlace(Double.parseDouble(String.valueOf(previousBestLocation.getLatitude()))).compareTo(chnageInFourPlace(location.getLatitude())) == 0 && chnageInFourPlace(Double.parseDouble(String.valueOf(previousBestLocation.getLongitude()))).compareTo(chnageInFourPlace(location.getLongitude())) == 0)) {
                        // if (location.distanceTo(previousBestLocation) > 10) {
                        previousBestLocation = location;
                        writeActualLocation(location);
                        onNewLocationAgainwithLocalDatabase(location);
                        //  Toast.makeText(this,"location updated sucessfully",Toast.LENGTH_SHORT).show();
                        //  }

                    }
                }

            } else {
                previousBestLocation = location;
                writeActualLocation(location);
                onNewLocationAgainwithLocalDatabase(location);
            }


            //          if (isBetterLocation(location, previousBestLocation)) {
//                  if(! (chnageInFourPlace(Double.parseDouble(String.valueOf(previousBestLocation.getLatitude()))).compareTo(chnageInFourPlace(location.getLatitude()))==0 && chnageInFourPlace(Double.parseDouble(String.valueOf(previousBestLocation.getLongitude()))).compareTo(chnageInFourPlace(location.getLongitude()))==0 )) {
//                      previousBestLocation =location;
//                      writeActualLocation(location);
//                      // onNewLocation(location);
//                      //   onNewLocationAgain(location);
//                      onNewLocationAgainwithLocalDatabase(location);
//
//
//                 }
//
//
//
//
//            }
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

    public String chnageInFourPlace(double value) {
        DecimalFormat df = new DecimalFormat("#.####");
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


    private void onNewLocationAgainwithLocalDatabase(Location location) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm a");
// you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
        String localTime = date.format(currentLocalTime);
        new InsertLocationData(location, false,localTime,getApplicationContext()).execute();
        getLocationList();
    }


    private void getLocationList() {
        new GetLocationData(getApplicationContext(), this).execute();
    }

    @Override
    public void processData(List<LocationParam> locationData) {
        hitApitoBulkEnter(locationData);
    }

    private void hitApitoBulkEnter(List<LocationParam> location) {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE);
        String user_id = String.valueOf(settings.getInt("User_ID", 0));
        String attendance_id = settings.getString("Attendance_ID", "");
        ArrayList<LocationCustomArray> mTempList = new ArrayList<>();
        mTempList.clear();
        if (location.size() >= 15) {
            //  Toast.makeText(this, "true !!!!!!!!!", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < location.size(); i++) {
                if (!location.get(i).getSynczed()) {

                    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        gps_status = 1;
                    } else {
                        gps_status = 0;
                    }
                    String currentDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
                    LocationCustomArray locationCustomArray = new LocationCustomArray
                            (String.valueOf(location.get(i).getLattitude()),
                                    String.valueOf(location.get(i).getLongitude()),
                                    currentDate.toString(),
                                    user_id,
                                    attendance_id,
                                    String.valueOf(getBatteryPercentage(getApplicationContext())), "2.00", gps_status);

                    mTempList.add(locationCustomArray);
                }
            }
            LocationCustomArray locationCustomArray = new LocationCustomArray();
            String jsonLatLong = locationCustomArray.changeListToJson(mTempList);
            readWebPageBulkTest(jsonLatLong, location);
        } else {
            //     Toast.makeText(this, "false  !!!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void readWebPageBulkTest(String location, List<LocationParam> mTemp) {
        if (!Utils.isNetworkConnected(this)) {
            Toast.makeText(this, "No internet connection available. Please check your internet connection.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, InternetSettingCheck.class));
            return;
        }
        SharedPreferences settings = getApplicationContext().getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE);
        String access_token = settings.getString("Access_Token", "");
        String user_id = String.valueOf(settings.getInt("User_ID", 0));
        String attendance_id = settings.getString("Attendance_ID", "");
        apiInterface = APIClient.getClient().create(GetDataService.class);
        if (attendance_id.equals("")) {
            attendance_id = "0";
        }
        if (!user_id.equals("")) {
            Call<NewLocationPojo> call = apiInterface.updateLatLongforUserIDBulk("Bearer " + access_token, location);
            final ArrayList<LocationCustomArray>[] mTempList = new ArrayList[]{new ArrayList<>()};
            Log.e("JsonLocation  :", location);
            call.enqueue(new Callback<NewLocationPojo>() {
                @Override
                public void onResponse(Call<NewLocationPojo> call, Response<NewLocationPojo> response) {
                    try {
                        if (response.body().getStatus().equals("success")) {
                            updatelatlong(location, mTemp);
                            //      Toast.makeText(MyService66.this, "Api Hit  Updated SucessFully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MyService66.this, "" + response.body().toString(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(MyService66.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("ServerSam :", e.toString());
                    }
                }

                @Override
                public void onFailure(Call<NewLocationPojo> call, Throwable t) {

                }
            });
        }

    }

    private void updatelatlong(String location, List<LocationParam> mListparam) {

        LocationCustomArray locationCustomArray = new LocationCustomArray();
        ArrayList<LocationCustomArray> mTempList = locationCustomArray.changeStringintoList(location);

        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                for (int i = 0; i < mTempList.size(); i++) {
                    mListparam.get(i).setSynczed(true);
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                            .locationDao()
                            .update(mListparam.get(i));
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                getPostFinal();
            }
        }
        UpdateTask gt = new UpdateTask();
        gt.execute();

    }


    private void getPostFinal() {


        class GetTasks extends AsyncTask<Void, Void, List<LocationParam>> {
            @Override
            protected List<LocationParam> doInBackground(Void... voids) {
                List<LocationParam> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .locationDao()
                        .getAll();
                Log.e("Database after updation : ", taskList.toString());
                return taskList;
            }

            @Override
            protected void onPostExecute(List<LocationParam> tasks) {
                super.onPostExecute(tasks);
                new DeleteLocationData(getApplicationContext()).execute();
                new GetLocationData22().execute();
                // testDataAfterDeleteion();

            }
        }
        GetTasks gt = new GetTasks();
        gt.execute();
    }


    class GetLocationData22 extends AsyncTask<Void, Void, List<LocationParam>> {

        Context mContext;

        @Override
        protected List<LocationParam> doInBackground(Void... voids) {
            List<LocationParam> taskList = DatabaseClient
                    .getInstance(mContext)
                    .getAppDatabase()
                    .locationDao()
                    .getAll();
            Log.d("GetAfterDeleteion :", taskList.toString());
            return taskList;
        }

        @Override
        protected void onPostExecute(List<LocationParam> tasks) {
            super.onPostExecute(tasks);

        }
    }


//      new  GetLocationData22 getLocationData22=new GetLocationData22();
//        getLocationData22.execute();


    private void onNewLocationAgainTest(Location location) {

        SharedPreferences settings = getApplicationContext().getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        String user_id = String.valueOf(settings.getInt("User_ID", 0));
        String attendance_id = settings.getString("Attendance_ID", "");
        Log.i(TAG, "New location: " + location.getLatitude());
        String currentDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        LocationCustomArray locationCustomArray = new LocationCustomArray
                (String.valueOf(location.getLatitude()),
                        String.valueOf(location.getLongitude()), currentDate.toString(), user_id, attendance_id, String.valueOf(getBatteryPercentage(getApplicationContext())),
                        "20.0", 0);
        Log.e("Location Object: ", locationCustomArray.toString());

        String jString = settings.getString("LatLongJson22", "");

        ArrayList<LocationCustomArray> mList = new ArrayList<>();
        if (jString.length() > 0) {
            mList = locationCustomArray.changeStringintoList(jString);
        }

        Log.e("First List", String.valueOf(mList.size()));
        if (mList.size() > 0) {
            LocationCustomArray obj = mList.get(mList.size() - 1);
            if (!(chnageInFourPlace(Double.parseDouble(obj.lat)).compareTo(chnageInFourPlace(location.getLatitude())) == 0 && chnageInFourPlace(Double.parseDouble(obj.longi)).compareTo(chnageInFourPlace(location.getLongitude())) == 0)) {
                mList.add(locationCustomArray);
                Log.e("Second List ", String.valueOf(mList.size()));
                // Store notes to SharedPreferences
                editor.putString("LatLongJson", locationCustomArray.changeListToJson(mList));
                editor.putString("LatLongJson22", settings.getString("LatLongJson", ""));
                editor.apply();
                if (settings.getString("IsOnThread", "").equals("")) {
                    editor.putString("IsOnThread", "ServiceStart");
                    editor.apply();
                    String prefer2 = settings.getString("LatLongJson22", "");
                    editor.putString("LatLongJson", prefer2);
                    editor.putString("LatLongJson22", "");
                    editor.apply();
                    readWebPageBulk(settings.getString("LatLongJson", ""));

                }

            }

        } else {
            mList.add(locationCustomArray);
            Log.e("Second List ", String.valueOf(mList.size()));
            // Store notes to SharedPreferences
            editor.putString("LatLongJson", locationCustomArray.changeListToJson(mList));
            editor.apply();
            if (settings.getString("IsOnThread", "").equals("")) {
                editor.putString("IsOnThread", "ServiceStart");
                editor.apply();
                readWebPageBulk(settings.getString("LatLongJson", ""));
            } else {
                return;
            }


        }


    }


    private void onNewLocationAgain(Location location) {


        SharedPreferences settings = getApplicationContext().getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        String user_id = String.valueOf(settings.getInt("User_ID", 0));
        String attendance_id = settings.getString("Attendance_ID", "");
        Log.i(TAG, "New location: " + location.getLatitude());
        String currentDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        LocationCustomArray locationCustomArray = new LocationCustomArray
                (String.valueOf(location.getLatitude()),
                        String.valueOf(location.getLongitude()), currentDate.toString(), user_id, attendance_id, String.valueOf(getBatteryPercentage(getApplicationContext())),
                        "20.0", 0);
        Log.e("Location Object: ", locationCustomArray.toString());

        String jString = settings.getString("LatLongJson", "");

        ArrayList<LocationCustomArray> mList = new ArrayList<>();
        if (jString.length() > 0) {
            mList = locationCustomArray.changeStringintoList(jString);
        }

        Log.e("First List", String.valueOf(mList.size()));
        if (mList.size() > 0) {
            LocationCustomArray obj = mList.get(mList.size() - 1);
            if (!(chnageInFourPlace(Double.parseDouble(obj.lat)).compareTo(chnageInFourPlace(location.getLatitude())) == 0 && chnageInFourPlace(Double.parseDouble(obj.longi)).compareTo(chnageInFourPlace(location.getLongitude())) == 0)) {
                mList.add(locationCustomArray);
                Log.e("Second List ", String.valueOf(mList.size()));
                // Store notes to SharedPreferences
                editor.putString("LatLongJson", locationCustomArray.changeListToJson(mList));
                editor.putString("LatLongJson22", settings.getString("LatLongJson", ""));
                editor.apply();
                if (settings.getString("IsOnThread", "").equals("")) {
                    editor.putString("IsOnThread", "ServiceStart");
                    editor.apply();
                    String prefer2 = settings.getString("LatLongJson22", "");
                    editor.putString("LatLongJson", prefer2);
                    editor.apply();
                    readWebPageBulk(settings.getString("LatLongJson", ""));

                }

            }

        } else {
            mList.add(locationCustomArray);
            Log.e("Second List ", String.valueOf(mList.size()));
            // Store notes to SharedPreferences
            editor.putString("LatLongJson", locationCustomArray.changeListToJson(mList));
            editor.apply();
            if (settings.getString("IsOnThread", "").equals("")) {
                editor.putString("IsOnThread", "ServiceStart");
                editor.apply();
                readWebPageBulk(settings.getString("LatLongJson", ""));
            } else {
                return;
            }


        }


    }


    private void onNewLocation(Location location) {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        String access_token = settings.getString("Access_Token", "");
        String user_id = String.valueOf(settings.getInt("User_ID", 0));
        String attendance_id = settings.getString("Attendance_ID", "");
        Log.i(TAG, "New location: " + location.getLatitude());
        String currentDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        LocationCustomArray locationCustomArray = new LocationCustomArray
                (String.valueOf(location.getLatitude()),
                        String.valueOf(location.getLongitude()), currentDate.toString(), user_id, attendance_id, String.valueOf(getBatteryPercentage(getApplicationContext())),
                        "20.0", 0);
        Log.e("Location Object: ", locationCustomArray.toString());

        String jString = settings.getString("LatLongJson", "");

        ArrayList<LocationCustomArray> mList = new ArrayList<>();
        if (jString.length() > 0) {
            mList = locationCustomArray.changeStringintoList(jString);
        }

        Log.e("First List", String.valueOf(mList.size()));
        if (mList.size() > 0) {
            LocationCustomArray obj = mList.get(mList.size() - 1);
            if (!(chnageInFourPlace(Double.parseDouble(obj.lat)).compareTo(chnageInFourPlace(location.getLatitude())) == 0 && chnageInFourPlace(Double.parseDouble(obj.longi)).compareTo(chnageInFourPlace(location.getLongitude())) == 0)) {
                mList.add(locationCustomArray);
                Log.e("Second List ", String.valueOf(mList.size()));
                // Store notes to SharedPreferences
                editor.putString("LatLongJson", locationCustomArray.changeListToJson(mList));
                editor.apply();
                readWebPageBulk(settings.getString("LatLongJson", ""));
                if (!settings.getString("previousTime", "").equals("")) {
                    if (settings.getString("IsOnThread", "").equals("")) {
                        editor.putString("IsOnThread", "ServiceStart");
                        editor.apply();
                        readWebPageBulk(settings.getString("LatLongJson", ""));
                    } else {
                        return;
                    }

                } else {
                    if (settings.getString("previousTime", "") != null) {
                        long previousTime = Long.parseLong(settings.getString("previousTime", ""));
                        long currentTime = Long.parseLong(getCurrentTimestamp());
                        long totalmsg = (currentTime - previousTime) / 60;
                        Log.d("TimeDiff :", String.valueOf(totalmsg));
                        Log.d("PreviousTime :", String.valueOf(previousTime));
                        Log.d("CurrentTime :", String.valueOf(currentTime));
                        if (totalmsg > 1) {
                            if (settings.getString("IsOnThread", "").equals("")) {
                                editor.putString("IsOnThread", "ServiceStart");
                                editor.apply();
                                readWebPageBulk(settings.getString("LatLongJson", ""));
                            } else {
                                return;
                            }

                        }
                    }
                }
            } else {
                Log.e("Status :", "Location Not changed");
            }

        } else {
            mList.add(locationCustomArray);
            Log.e("Second List ", String.valueOf(mList.size()));
            // Store notes to SharedPreferences
            editor.putString("LatLongJson", locationCustomArray.changeListToJson(mList));
            editor.apply();
            readWebPageBulk(settings.getString("LatLongJson", ""));

//            if(!settings.getString("previousTime","").equals("")){
//                long previousTime=Long.parseLong(settings.getString("previousTime",""));
//                long currentTime=Long.parseLong(getCurrentTimestamp());
//                long totalmsg=(currentTime-previousTime)/60;
//                if(totalmsg>1){
//                    if(settings.getString("IsOnThread","").equals("")){
//                        editor.putString("IsOnThread","ServiceStart");
//                        editor.apply();
//                        readWebPageBulk( settings.getString("LatLongJson",""));
//                    }else {
//                        return;
//                    }
//                }
//            }else {
//                Log.e("dsf","dsf");
//            }


//            if(settings.getString("previousTime","")=="")
//            {
//
//                if(settings.getString("IsOnThread","").equals("")){
//                    editor.putString("IsOnThread","ServiceStart");
//                    editor.apply();
//                    readWebPageBulk( settings.getString("LatLongJson",""));
//                }else {
//                    return;
//                }
//
//
//            }
//            else
//            {
//                if(settings.getString("previousTime","")!=null){
//                    long previousTime=Long.parseLong(settings.getString("previousTime",""));
//                    long currentTime=Long.parseLong(getCurrentTimestamp());
//                    long totalmsg=(currentTime-previousTime)/60;
//                     if(totalmsg>1){
//                    if(settings.getString("IsOnThread","").equals("")){
//                        editor.putString("IsOnThread","ServiceStart");
//                        editor.apply();
//                        readWebPageBulk( settings.getString("LatLongJson",""));
//                    }else {
//                        return;
//                    }
//                     }
//                }
//            }
        }
    }


    private String getCurrentTimestamp() {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        return ts;
    }

    private void readWebPageBulk(String location) {
        if (!Utils.isNetworkConnected(this)) {
            Toast.makeText(this, "No internet connection available. Please check your internet connection.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, InternetSettingCheck.class));
            return;
        }
        SharedPreferences settings = getApplicationContext().getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE);
        String access_token = settings.getString("Access_Token", "");
        String user_id = String.valueOf(settings.getInt("User_ID", 0));
        String attendance_id = settings.getString("Attendance_ID", "");
        //apiInterface =APIClient.getClient().create(GetDataService.class);
        apiInterface = APIClient.getClient().create(GetDataService.class);
        if (attendance_id.equals("")) {
            attendance_id = "0";
        }
        if (!user_id.equals("")) {
            Call<NewLocationPojo> call = apiInterface.updateLatLongforUserIDBulk(
                    "Bearer " + access_token, location

            );
            Log.e("JsonLocation  :", location);
            call.enqueue(new Callback<NewLocationPojo>() {
                @Override
                public void onResponse(Call<NewLocationPojo> call, Response<NewLocationPojo> response) {
                    try {
                        if (response.body().getStatus().equals("success")) {
                            //   Toast.makeText(MyService66.this, "Location Updated SucessFully", Toast.LENGTH_SHORT).show();
                            Log.e("ApiResponse ", response.body().toString());
                            String current = getCurrentTimestamp();
                            SharedPreferences settings = getApplicationContext().getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("previousTime", current);
                            editor.putString("IsOnThread", "");

                            editor.putString("LatLongJson", "");
                            editor.apply();
                        } else {
                            Toast.makeText(MyService66.this, "" + response.body().toString(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(MyService66.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("ServerSam :", e.toString());
                    }
                }

                @Override
                public void onFailure(Call<NewLocationPojo> call, Throwable t) {
                    Toast.makeText(MyService66.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("ServerSam :", t.toString());
                }
            });
        }
    }

    /**
     * extract last location if location is not available
     */
    @SuppressLint("MissingPermission")
    private void getLastKnownLocation() {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation != null) {
            Log.i(TAG, "LasKnown location. " +
                    "Long: " + lastLocation.getLongitude() +
                    " | Lat: " + lastLocation.getLatitude());
            writeLastLocation();
            startLocationUpdates();
        } else {
            Log.w(TAG, "No location retrieved yet");
            startLocationUpdates();
        }
    }

    /**
     * this method writes location to text view or shared preferences
     *
     * @param location - location from fused location provider
     */
    @SuppressLint("SetTextI18n")
    private void writeActualLocation(Location location) {
        Log.d(TAG, location.getLatitude() + ", " + location.getLongitude());
        //here in this method you can use web service or any other thing
    }

    /**
     * this method only provokes writeActualLocation().
     */
    private void writeLastLocation() {
        writeActualLocation(lastLocation);
    }


    /**
     * this method fetches location from fused location provider and passes to writeLastLocation
     */
    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        //Log.i(TAG, "startLocationUpdates()");
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

    }


    /**
     * Default method of service
     *
     * @param params - JobParameters params
     * @return boolean
     */
    @Override
    public boolean onStartJob(JobParameters params) {
        getApplication().startService(new Intent(this, MyService.class)); //start service which is MyService.java
        //startJobAgain();
        createGoogleApi();
        return true;
    }


    /**
     * Create google api instance
     */
    private void createGoogleApi() {
        //Log.d(TAG, "createGoogleApi()");
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        //connect google api
        googleApiClient.connect();
        // Log.d(TAG, "createResult : ",googleApiClient.toString());

    }

    /**
     * disconnect google api
     *
     * @param params - JobParameters params
     * @return result
     */
    @Override
    public boolean onStopJob(JobParameters params) {
        googleApiClient.disconnect();
        return false;
    }

    /**
     * starting job again
     */
    private void startJobAgain() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(TAG, "Job Started");
            ComponentName componentName = new ComponentName(getApplicationContext(),
                    MyService66.class);
            jobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
            JobInfo jobInfo = new JobInfo.Builder(1, componentName)
                    .setMinimumLatency(50000) //10 sec interval
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY).setRequiresCharging(false).build();
            jobScheduler.schedule(jobInfo);
        }
    }

    /**
     * this method tells whether google api client connected.
     *
     * @param bundle - to get api instance
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Log.i(TAG, "onConnected()");
        getLastKnownLocation();
    }

    /**
     * this method returns whether connection is suspended
     *
     * @param i - 0/1
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "connection suspended");
    }

    /**
     * this method checks connection status
     *
     * @param connectionResult - connected or failed
     */


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {
            String dsf = "";
            // The Wearable API is unavailable
        }
        Log.d(TAG, "connection failed");
    }

    /**
     * this method tells the result of status of google api client
     *
     * @param status - success or failure
     */
    @Override
    public void onResult(Status status) {
        Log.d(TAG, "result of google api client : " + status);
    }


    public void loctionsend(String json) {
        /**
         POST name and job Url encoded.
         **/
//        Call<ResultJson> call3 = apiInterface.createUser(json);
//        call3.enqueue(new Callback<ResultJson>() {
//            @Override
//            public void onResponse(Call<ResultJson> call, Response<ResultJson> response) {
//                ResultJson userList = response.body();
//                Log.e("Response",response.body().toString());
//
//
//            }
//
//            @Override
//            public void onFailure(Call<ResultJson> call, Throwable t) {
//                call.cancel();
//            }
//
//
//        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getApplication().startService(new Intent(this, MyService.class)); //start service which is MyService.java
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivityFinal2.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Sapphire Industries")
                .setContentText(input)
                .setSmallIcon(R.mipmap.app_icon)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
        createGoogleApi();
        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public void json_object(double lat, double longe) throws JSONException {
        Date currentTime = Calendar.getInstance().getTime();
        JSONObject student1 = new JSONObject();
        try {
            student1.put("lat", lat);
            student1.put("long", longe);
            student1.put("battery", "3");
            student1.put("mobtime", currentTime.toString());
            student1.put("attendance_id", "11");
            student1.put("user_id", "111");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();

        jsonArray.put(student1);

        JSONObject studentsObj = new JSONObject();
        studentsObj.put("json", jsonArray);

        loctionsend(studentsObj.toString());

        String jsonStr = studentsObj.toString();
        Log.e("JSOnevalue", jsonStr);
    }


}
