package com.webgurus.newservice

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.robertohuertas.endless.Actions
import com.robertohuertas.endless.ServiceState
import com.robertohuertas.endless.log
import com.robertohuertas.endless.setServiceState
import com.webgurus.GPSTracker
import com.webgurus.MainActivityFinal2
import com.webgurus.MyService66
import com.webgurus.Restarter
import com.webgurus.attendanceportal.APIClient
import com.webgurus.attendanceportal.InternetSettingCheck
import com.webgurus.attendanceportal.LocalDatabaseForException
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.demo.LocationCustomArray
import com.webgurus.attendanceportal.demo.Utils
import com.webgurus.attendanceportal.pojo.GpaStatusPojo
import com.webgurus.attendanceportal.pojo.NewLocationPojo
import com.webgurus.attendanceportal.ui.database.*
import com.webgurus.network.GetDataService
import io.nlopez.smartlocation.OnLocationUpdatedListener
import io.nlopez.smartlocation.SmartLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class MyEndlessService : Service(), DatabaseListenerCallback , LocationListener {

    private var wakeLock: PowerManager.WakeLock? = null
    private var isServiceStarted = false
    var TAG = "MyService"
    var gps_status = 1
    var editor: SharedPreferences.Editor? = null
    var previousBestLocation: Location? = null
    var apiInterface: GetDataService? = null
    private var googleApiClient: GoogleApiClient? = null
    private var lastLocation: Location? = null
    private var locationRequest: LocationRequest? = null
    private val UPDATE_INTERVAL = 10000
    private val FASTEST_INTERVAL = UPDATE_INTERVAL / 2
    var locationManager: LocationManager? = null
    var isFirsttime = true
    var sharedPreferences: SharedPreferences? = null

    var battery_value: Int = 0

    // Gps Location
    private var checkGPS = false
    var checkNetwork = false
    var canGetLocation = false
    private val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong()
    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 10f
    private val mContext: Context? = null
    var loc: Location? = null
    var latitude = 0.0
    var longitude = 0.0


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand executed with startId: $startId")
        if (intent != null) {
            val action = intent.action
            log("using an intent with action $action")
            when (action) {
                Actions.START.name -> startService()
                Actions.STOP.name -> stopService()
                else -> log("This should never happen. No action in the received intent")
            }
        } else {
            log(
                "with a null intent. It has been probably restarted by the system."
            )
        }
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        log("The service has been created".toUpperCase())
        val notification = createNotification()
        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        log("The service has been destroyed".toUpperCase())
        val calendar = Calendar.getInstance()
        val mdformat = SimpleDateFormat("HH:mm:ss")
        val strDate = mdformat.format(calendar.time)
        LocalDatabaseForException(applicationContext, strDate, "Service Destroyed").execute()
        val broadcastIntent = Intent()
        broadcastIntent.action = "restartservice"
        broadcastIntent.setClass(this, Restarter::class.java)
        this.sendBroadcast(broadcastIntent)
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        val restartServiceIntent = Intent(applicationContext, MyEndlessService22::class.java).also {
            it.setPackage(packageName)
        };
        val restartServicePendingIntent: PendingIntent = PendingIntent.getService(
            this,
            1,
            restartServiceIntent,
            PendingIntent.FLAG_ONE_SHOT
        );
        applicationContext.getSystemService(Context.ALARM_SERVICE);
        val alarmService: AlarmManager =
            applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager;
        alarmService.set(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime() + 1000,
            restartServicePendingIntent
        )
        log("The service has been destroyed".toUpperCase())
        val calendar = Calendar.getInstance()
        val mdformat = SimpleDateFormat("HH:mm:ss")
        val strDate = mdformat.format(calendar.time)
        LocalDatabaseForException(applicationContext, strDate, "On tasked Removed").execute()
        val broadcastIntent = Intent()
        broadcastIntent.action = "restartservice"
        broadcastIntent.setClass(this, Restarter::class.java)
        this.sendBroadcast(broadcastIntent)
    }

    private fun startService() {

        val calendar = Calendar.getInstance()
        val mdformat = SimpleDateFormat("HH:mm:ss")
        val strDate = mdformat.format(calendar.time)
        LocalDatabaseForException(applicationContext, strDate, "Service Started").execute()

        if (isServiceStarted) return
        log("Starting the foreground service task")
        //  Toast.makeText(this, "Service starting its task", Toast.LENGTH_SHORT).show()
        isServiceStarted = true
        setServiceState(this, ServiceState.STARTED)

        // we need this lock so our service gets not affected by Doze Mode
        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EndlessService::lock").apply {
                    acquire()
                }
            }
        // we're starting a loop in a coroutine
        GlobalScope.launch(Dispatchers.IO) {
            while (isServiceStarted) {
                launch(Dispatchers.IO) {
                    pingFakeServer()
                    handleUserTracking()
                }
                delay(10000)
            }
            log("End of the loop for the service")
        }
    }

    private fun NoInternetConnection() {
        val gpsTracker = GPSTracker(this)
        if (gpsTracker.getIsGPSTrackingEnabled()){
            val stringLatitude = gpsTracker.latitude.toString()
            val stringLongitude = gpsTracker.longitude.toString()
            val dsg= ""
            val dsgsdfsd= ""
            val dsdsfdsfg= ""
        }
    }


    private fun stopService() {
        log("Stopping the foreground service")
        //  Toast.makeText(this, "Service stopping", Toast.LENGTH_SHORT).show()
        val calendar = Calendar.getInstance()
        val mdformat = SimpleDateFormat("HH:mm:ss")
        val strDate = mdformat.format(calendar.time)
        LocalDatabaseForException(applicationContext, strDate, "Service Stopped").execute()
        try {
            wakeLock?.let {
                if (it.isHeld) {
                    // startService()
                    it.release()
                }
            }
            stopForeground(true)
            stopSelf()
        } catch (e: Exception) {
            log("Service stopped without being started: ${e.message}")
            val calendar = Calendar.getInstance()
            val mdformat = SimpleDateFormat("HH:mm:ss")
            val strDate = mdformat.format(calendar.time)
            LocalDatabaseForException(
                applicationContext,
                strDate,
                "Service stopped without being started: ${e.message}"
            ).execute()
        }
        isServiceStarted = false
        setServiceState(this, ServiceState.STARTED)
    }

    private fun pingFakeServer() {
        SmartLocation.with(applicationContext).location()
            .oneFix()
            .start(object : OnLocationUpdatedListener {
                override fun onLocationUpdated(location: Location?) {
                    val sharedPreferences = applicationContext.getSharedPreferences("AcessToken", AppCompatActivity.MODE_MULTI_PROCESS)
                    Toast.makeText(applicationContext, "" + location!!.latitude, Toast.LENGTH_SHORT).show()
                    editor = getSharedPreferences("AcessToken", MODE_PRIVATE).edit()
                    editor!!.putString("temp_lattitude", location.latitude.toString())
                    editor!!.putString("temp_longitude", location.longitude.toString())
                    editor!!.apply()
                    val att = sharedPreferences!!.getString("Attendance_ID", "")
                    val userid = sharedPreferences.getInt("User_ID", 0).toString()
                    if ((att != "") and (userid != "")) {
                       // onNewLocationAgainwithLocalDatabase(location)
                        if (previousBestLocation != null) {
                            if (isBetterLocation(location, previousBestLocation)) {
                                if (!(chnageInFourPlace(
                                        previousBestLocation!!.getLatitude().toString().toDouble()
                                    )!!.compareTo(
                                        chnageInFourPlace(location.getLatitude())!!
                                    ) == 0 && chnageInFourPlace(
                                        previousBestLocation!!.getLongitude().toString().toDouble()
                                    )!!.compareTo(
                                        chnageInFourPlace(location.getLongitude())!!
                                    ) == 0)
                                ) {

                                    val from = LatLng(
                                        previousBestLocation!!.latitude,
                                        previousBestLocation!!.longitude
                                    )
                                    val to = LatLng(location.latitude, location.longitude)
                                    val distance: Double =
                                        SphericalUtil.computeDistanceBetween(from, to)
                                    val nf: NumberFormat = DecimalFormat("##.##")
                                    val distanceDiff = nf.format(distance)
                                    if (distanceDiff.toFloat() > 50f) {
                                        previousBestLocation = location
                                        onNewLocationAgainwithLocalDatabase(location)
                                    }

                                }
                            }
                        } else {
                            previousBestLocation = location
                            onNewLocationAgainwithLocalDatabase(location)
                        }
                    }
                }


            })

    }

    fun chnageInFourPlace(value: Double): String? {
        val df = DecimalFormat("#.####")
        df.roundingMode = RoundingMode.FLOOR
        return df.format(value)
    }


    fun getBatteryPercentage(context: Context): Int {
        return if (Build.VERSION.SDK_INT >= 21) {
            val bm = context.getSystemService(BATTERY_SERVICE) as BatteryManager
            bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } else {
            val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryStatus = context.registerReceiver(null, iFilter)
            val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
            val batteryPct = level / scale.toDouble()
            (batteryPct * 100).toInt()
        }
    }


    fun handleUserTracking() {
        val manager = getSystemService(LOCATION_SERVICE) as LocationManager
        gps_status = 1
        editor = getSharedPreferences("AcessToken", MODE_PRIVATE).edit()
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gps_status = 0
            editor!!.putString("temp_lattitude", "")
            editor!!.putString("temp_longitude", "")
            editor!!.apply()
        }
        hitApitoUpdate(gps_status)
        Log.d("Gps status ", gps_status.toString())
    }

    private fun hitApitoUpdate(gpsstatus: Int?) {
        val settings = applicationContext.getSharedPreferences("AcessToken", MODE_PRIVATE)
        val lattitude = settings.getString("temp_lattitude", "")
        val longitude = settings.getString("temp_longitude", "")
        val userID = settings.getInt("User_ID", 0).toString()
        val access_token = settings.getString("Access_Token", "")
        val battery_per = getBatteryPercentage(this)
        apiInterface = APIClient.getClient().create(GetDataService::class.java)
        val call = apiInterface!!.updateGpsStatus(
            "Bearer $access_token",
            gpsstatus,
            userID,
            lattitude,
            longitude,
            battery_per,
            "",
            "1.2Ver"
        )
        call.enqueue(object : Callback<GpaStatusPojo> {
            override fun onResponse(call: Call<GpaStatusPojo>, response: Response<GpaStatusPojo>) {
                try {
                    Log.d("Gps status ", response.body()!!.message)
                } catch (e: java.lang.Exception) {
                    val dfgdsdsdsfds = ""
                    Log.d("Gps sttaus 22 :", e.toString())
                }
            }

            override fun onFailure(call: Call<GpaStatusPojo>, t: Throwable) {
                Log.d("Gps sttaus 33 :", t.toString())
            }
        })
//        if (settings.getInt("battery_status", 0) == 0) {
//            editor!!.putInt("battery_status", battery_value)
//            editor!!.putInt("gps_status", gps_status)
//            editor!!.apply()
//            hitApiTocallUserStatus(
//                access_token!!,
//                gpsstatus,
//                userID,
//                lattitude!!,
//                longitude!!,
//                battery_value
//            )
//            Log.d(
//                "Userf Info Updation : ",
//                "First time : GPS :" + gpsstatus.toString() + "\n" + "Battery  :" +
//                        battery_value.toString() + "\n" + "Lat  :" + lattitude.toString() + "\n" + "Long  :" + longitude.toString() + "\n"
//            )
//        } else if (previousBestLocation != null) {
//            if (
//                settings.getInt("battery_status", 0) == battery_value &&
//                chnageInFourPlace(previousBestLocation!!.latitude)!! ==  chnageInFourPlace(settings.getString("temp_lattitude", "")!!.toDouble())!!  &&
//                chnageInFourPlace(previousBestLocation!!.longitude)!! ==  chnageInFourPlace(settings.getString("temp_longitude", "")!!.toDouble())!!  &&
//                gpsstatus!!.equals(settings.getInt("gps_status", 0))
//
//            ) {
//                Log.d("Userf Info Updation", "Nothing Updated")
//            }
//        } else {
//            Log.d(
//                "Userf Info Updation : ",
//                "Change in value  : GPS  : " + gpsstatus.toString() + "\n" + "Battery  :" +
//                        battery_value.toString() + "\n" + "Lat  :" + lattitude.toString() + "\n" + "Long  :" + longitude.toString() + "\n"
//            )
//            hitApiTocallUserStatus(
//                access_token!!,
//                gpsstatus,
//                userID,
//                lattitude!!,
//                longitude!!,
//                battery_value
//            )
//        }


//       else if(settings.getInt("battery_status", 0)==battery_value){
//           if(previousBestLocation!!.latitude.toString() .equals(settings.getString("temp_lattitude", "")) && previousBestLocation!!.longitude.toString() .equals(settings.getString("temp_longitude", "")) ){
//               if(gpsstatus!!.equals(settings.getInt("gps_status", 0))){
//                   val dsgf=""
//                   val dsgfsdf=""
//                   val dsgfasdasdas=""
//               }
//           }
//       }
    }


    fun hitApiTocallUserStatus(
        access_token: String,
        gpsstatus: Int?,
        userID: String,
        lattitude: String,
        longitude: String,
        battery_status: Int
    ) {

        val call = apiInterface!!.updateGpsStatus(
            "Bearer $access_token",
            gpsstatus,
            userID,
            lattitude,
            longitude,
            battery_status,
            "",
            "1.2Ver"
        )
        call.enqueue(object : Callback<GpaStatusPojo> {
            override fun onResponse(call: Call<GpaStatusPojo>, response: Response<GpaStatusPojo>) {
                try {
                    // Toast.makeText(applicationContext,""+response.body()!!.message,Toast.LENGTH_SHORT).show()
                    Log.d("Gps status ", response.body()!!.message)
                } catch (e: java.lang.Exception) {
                    val dfgdsdsdsfds = ""
                    Log.d("Gps sttaus 22 :", e.toString())
                }
            }

            override fun onFailure(call: Call<GpaStatusPojo>, t: Throwable) {
                Log.d("Gps sttaus 33 :", t.toString())
            }
        })

    }

    private fun onNewLocationAgainwithLocalDatabase(location: Location) {
        val calendar = Calendar.getInstance()
        val mdformat = SimpleDateFormat("HH:mm:ss")
        val strDate = mdformat.format(calendar.time)
        LocalDatabaseInsert(location, strDate, applicationContext).execute()
        InsertLocationData(location, false, strDate, applicationContext).execute()
        getLocationList()
    }


    override fun onLowMemory() {
        super.onLowMemory()
        log("The service has been destroyed".toUpperCase())
        val calendar = Calendar.getInstance()
        val mdformat = SimpleDateFormat("HH:mm:ss")
        val strDate = mdformat.format(calendar.time)
        LocalDatabaseForException(applicationContext, strDate, "On Low memory ").execute()
        val broadcastIntent = Intent()
        broadcastIntent.action = "restartservice"
        broadcastIntent.setClass(this, Restarter::class.java)
        this.sendBroadcast(broadcastIntent)

    }


    private fun getLocationList() {
        GetLocationData(applicationContext, this).execute()
    }

    override fun processData(locationData: MutableList<LocationParam>?) {
        hitApitoBulkEnter(locationData!!)
    }

    private fun hitApitoBulkEnter(location: List<LocationParam>) {
        if (!Utils.isNetworkConnected(this)) {
            Toast.makeText(
                this,
                "No internet connection available. Please check your internet connection.",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(this, InternetSettingCheck::class.java))
            return
        }
        val settings = applicationContext.getSharedPreferences("AcessToken", MODE_PRIVATE)
        val access_token = settings.getString("Access_Token", "")
        val user_id = settings.getInt("User_ID", 0).toString()
        var attendance_id = settings.getString("Attendance_ID", "")
        val currentDate =
            SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(Date())
        val mTempList = ArrayList<LocationCustomArray>()
        mTempList.clear()
        Log.d("Location sizess : ", location.size.toString())
        if (location.size >= 50) {
            for (i in location.indices) {
                if (!location[i].synczed) {
                    val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
                    gps_status =
                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            1
                        } else {
                            0
                        }
                    val currentDate =
                        SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(
                            Date()
                        )
                    Log.d("currentDate and time : ", currentDate.toString())
                    val locationCustomArray = LocationCustomArray(
                        location[i].lattitude.toString(),
                        location[i].longitude.toString(),
                        currentDate.toString(),
                        user_id,
                        attendance_id,
                        getBatteryPercentage(applicationContext).toString(),
                        "2.00",
                        gps_status
                    )
                    mTempList.add(locationCustomArray)
                }
            }
            val locationCustomArray = LocationCustomArray()
            val jsonLatLong = locationCustomArray.changeListToJson(mTempList)
            readWebPageBulkTest(jsonLatLong, location)
        }

//        val locationCustomArrays = LocationCustomArray(
//            location.latitude.toString(),
//            location.longitude.toString(),
//            currentDate.toString(),
//            user_id,
//            attendance_id,
//            getBatteryPercentage(applicationContext).toString(),
//            "2.00",
//            gps_status
//        )
//
//        mTempList.add(locationCustomArrays)
//        val locationCustomArray = LocationCustomArray()
//        val jsonLatLong = locationCustomArray.changeListToJson(mTempList)
//        apiInterface = APIClient.getClient().create(GetDataService::class.java)
//        if (attendance_id == "") {
//            attendance_id = "0"
//        }
//        if (user_id != "") {
//            val call: Call<NewLocationPojo> = apiInterface!!.updateLatLongforUserIDBulk(
//                "Bearer $access_token",
//                jsonLatLong
//            )
//            Log.e("JsonLocation  :", jsonLatLong)
//            call.enqueue(object : Callback<NewLocationPojo> {
//                override fun onResponse(
//                    call: Call<NewLocationPojo>,
//                    response: Response<NewLocationPojo>
//                ) {
//                    Toast.makeText(
//                        applicationContext,
//                        "Location SucessFully Updated" ,
//                        Toast.LENGTH_SHORT
//                    ).show()
//
//                }
//
//                override fun onFailure(call: Call<NewLocationPojo>, t: Throwable) {
//                    Log.e("ServerSam :", t.toString())
//                }
//            })
//        }
//
    }


    private fun readWebPageBulkTest(location: String, mTemp: List<LocationParam>) {
        if (!Utils.isNetworkConnected(this)) {
            Toast.makeText(
                this,
                "No internet connection available. Please check your internet connection.",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(this, InternetSettingCheck::class.java))
            return
        }
        val settings = applicationContext.getSharedPreferences("AcessToken", MODE_PRIVATE)
        val access_token = settings.getString("Access_Token", "")
        val user_id = settings.getInt("User_ID", 0).toString()
        var attendance_id = settings.getString("Attendance_ID", "")
        apiInterface = APIClient.getClient().create(GetDataService::class.java)
        if (attendance_id == "") {
            attendance_id = "0"
        }
        if (user_id != "") {
            val call: Call<NewLocationPojo> = apiInterface!!.updateLatLongforUserIDBulk(
                "Bearer $access_token",
                location
            )
            Log.e("JsonLocation  :", location)
            call.enqueue(object : Callback<NewLocationPojo> {
                override fun onResponse(
                    call: Call<NewLocationPojo>,
                    response: Response<NewLocationPojo>
                ) {
                    try {
                        if (response.body()!!.status == "success") {
                            Log.e("Api response :", response.body()!!.status)
                            Toast.makeText(
                                this@MyEndlessService,
                                "" + response.body()!!.status.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            updatelatlong(location, mTemp)
                        } else if (response.body()!!.status.equals("timeout")) {
                            updatelatlong(location, mTemp)
                            editor!!.remove("Attendance_ID")
                            editor!!.commit()
                            stopService()
                        } else {
                            Toast.makeText(
                                this@MyEndlessService,
                                "" + response.body()!!.status.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            LocalDatabaseForException(
                                applicationContext,
                                "",
                                "Api response " + response.body().toString()
                            ).execute()
                        }
                    } catch (e: java.lang.Exception) {
                        Toast.makeText(this@MyEndlessService, "" + e.toString(), Toast.LENGTH_SHORT)
                            .show()
                        Log.e("ServerSam :", e.toString())
                        LocalDatabaseForException(
                            applicationContext,
                            "",
                            "Api response : " + e.toString()
                        ).execute()
                    }
                }

                override fun onFailure(call: Call<NewLocationPojo>, t: Throwable) {
                    Log.e("ServerSam :", t.toString())
                    log("The service has been destroyed".toUpperCase())
                    val calendar = Calendar.getInstance()
                    val mdformat = SimpleDateFormat("HH:mm:ss")
                    val strDate = mdformat.format(calendar.time)
                    LocalDatabaseForException(
                        applicationContext,
                        strDate,
                        "Api response : " + t.toString()
                    ).execute()
                }
            })
        }

    }


    private fun updatelatlong(location: String, mListparam: List<LocationParam>) {
        val locationCustomArray = LocationCustomArray()
        val mTempList = locationCustomArray.changeStringintoList(location)

        class UpdateTask : AsyncTask<Void?, Void?, Void?>() {
            override fun onPostExecute(aVoid: Void?) {
                super.onPostExecute(aVoid)
                getPostFinal()
            }

            override fun doInBackground(vararg params: Void?): Void? {
                for (i in mTempList.indices) {
                    mListparam[i].synczed = true
                    DatabaseClient.getInstance(applicationContext).appDatabase
                        .locationDao()
                        .update(mListparam[i])
                }
                return null
            }
        }

        val gt = UpdateTask()
        gt.execute()
    }


    private fun getPostFinal() {

        class GetTasks : AsyncTask<Void?, Void?, List<LocationParam>>() {

            override fun onPostExecute(tasks: List<LocationParam>) {
                super.onPostExecute(tasks)
                DeleteLocationData(applicationContext).execute()
                GetLocationData22().execute()
                // testDataAfterDeleteion();
            }

            override fun doInBackground(vararg params: Void?): List<LocationParam> {
                val taskList = DatabaseClient
                    .getInstance(applicationContext)
                    .appDatabase
                    .locationDao()
                    .all
                Log.e("Database after updation", taskList.toString())
                return taskList
            }
        }

        val gt = GetTasks()
        gt.execute()
    }


    internal class GetLocationData22 : AsyncTask<Void?, Void?, List<LocationParam>>() {
        var mContext: Context? = null


        override fun onPostExecute(tasks: List<LocationParam>) {
            super.onPostExecute(tasks)
        }

        override fun doInBackground(vararg params: Void?): List<LocationParam> {
            val taskList = DatabaseClient.getInstance(mContext)
                .appDatabase
                .locationDao()
                .all
            Log.d("GetAfterDeleteion :", taskList.toString())
            return taskList
        }
    }


    protected fun isBetterLocation(location: Location, currentBestLocation: Location?): Boolean {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true
        }

        // Check whether the new location fix is newer or older
        val timeDelta = location.time - currentBestLocation.time
        val isSignificantlyNewer = timeDelta > MyService66.TWO_MINUTES
        val isSignificantlyOlder = timeDelta < -MyService66.TWO_MINUTES
        val isNewer = timeDelta > 0

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false
        }

        // Check whether the new location fix is more or less accurate
        val accuracyDelta = (location.accuracy - currentBestLocation.accuracy).toInt()
        val isLessAccurate = accuracyDelta > 0
        val isMoreAccurate = accuracyDelta < 0
        val isSignificantlyLessAccurate = accuracyDelta > 200
        // Check if the old and new location are from the same provider
        val isFromSameProvider: Boolean =
            isSameProvider(location.provider, currentBestLocation.provider)
        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true
        } else if (isNewer && !isLessAccurate) {
            return true
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true
        }
        return false
    }


    /**
     * Checks whether two providers are the same
     */
    private fun isSameProvider(provider1: String?, provider2: String?): Boolean {
        return if (provider1 == null) {
            provider2 == null
        } else provider1 == provider2
    }

    private fun createNotification(): Notification {
        val notificationChannelId = "ENDLESS SERVICE CHANNEL"

        // depending on the Android API that we're dealing with we will have
        // to use a specific method to create the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                notificationChannelId,
                "Endless Service notifications channel",
                NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.description = "Endless Service channel"
                it.enableLights(true)
                it.lightColor = Color.RED
                it.enableVibration(true)
                it.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                it
            }
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent: PendingIntent =
            Intent(this, MainActivityFinal2::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val builder: Notification.Builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(
                this,
                notificationChannelId
            ) else Notification.Builder(this)

        return builder
            .setContentTitle("Sapphire location on")
            // .setContentText("This is your favorite endless service working")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.app_icon)
            .setOngoing(true)
            //   .setTicker("Ticker text")
            .setPriority(Notification.PRIORITY_HIGH) // for under android 26 compatibility
            .build()
    }


    override fun onLocationChanged(location: Location) {
        TODO("Not yet implemented")
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        TODO("Not yet implemented")
    }



}