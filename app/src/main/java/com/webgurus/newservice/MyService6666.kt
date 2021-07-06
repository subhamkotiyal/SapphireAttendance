package com.webgurus.newservice

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.*
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.robertohuertas.endless.*
import com.webgurus.MainActivityFinal2
import com.webgurus.MyService66
import com.webgurus.Restarter
import com.webgurus.attendanceportal.*
import com.webgurus.attendanceportal.demo.LocationCustomArray
import com.webgurus.attendanceportal.demo.Utils
import com.webgurus.attendanceportal.pojo.GpaStatusPojo
import com.webgurus.attendanceportal.pojo.NewLocationPojo
import com.webgurus.attendanceportal.ui.database.*
import com.webgurus.endlessservice.ServiceAdmin
import com.webgurus.network.GetDataService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


 class MyService6666 : Service(),
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    com.google.android.gms.location.LocationListener,
    com.google.android.gms.common.api.ResultCallback<Status>,
    DatabaseListenerCallback,
    LocationListener {


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



     override fun onLocationChanged(location: Location) {

         val calendar = Calendar.getInstance()
         val mdformat = SimpleDateFormat("HH:mm:ss")
         val strDate = mdformat.format(calendar.time)


         Toast.makeText(applicationContext,"Location Updated Sucessfully ",Toast.LENGTH_SHORT).show()
         Log.e("bug", strDate.toString()+"------->>>" + location!!.latitude + location.longitude)
         val sharedPreferences = applicationContext.getSharedPreferences(
             "AcessToken",
             AppCompatActivity.MODE_MULTI_PROCESS
         )
         editor = getSharedPreferences("AcessToken", MODE_PRIVATE).edit()
         editor!!.putString("temp_lattitude", location.latitude.toString())
         editor!!.putString("temp_longitude", location.longitude.toString())
         editor!!.apply()
         val att = sharedPreferences!!.getString("Attendance_ID", "")
         val userid = sharedPreferences.getInt("User_ID", 0).toString()
         handleUserTracking()
         onNewLocationAgainwithLocalDatabase(location)
         Log.i("Location Updated At :", strDate.toString() + ": "  + location.latitude  + "," + location.longitude)




//         if ((att != "") and (userid != "")) {
//             if (previousBestLocation != null) {
//                 if (isBetterLocation(location, previousBestLocation)) {
//                     if (!(chnageInFourPlace(
//                             previousBestLocation!!.getLatitude().toString().toDouble()
//                         )!!.compareTo(chnageInFourPlace(location.getLatitude())!!) == 0 && chnageInFourPlace(
//                             previousBestLocation!!.getLongitude().toString().toDouble()
//                         )!!.compareTo(
//                             chnageInFourPlace(location.getLongitude())!!
//                         ) == 0)
//                     ) {
//                         val from = LatLng(
//                             previousBestLocation!!.latitude,
//                             previousBestLocation!!.longitude
//                         )
//                         val to = LatLng(location.latitude, location.longitude)
//                         val distance: Double = SphericalUtil.computeDistanceBetween(from, to)
//                         val nf: NumberFormat = DecimalFormat("##.##")
//                         val distanceDiff = nf.format(distance)
//                         if (distanceDiff.toFloat() > 10f) {
//                             previousBestLocation = location
//                             onNewLocationAgainwithLocalDatabase(location)
//                             Toast.makeText(this, "" + location.latitude, Toast.LENGTH_SHORT).show()
//                         }
//                     }
//                 }
//             } else {
//                 previousBestLocation = location
//                 writeActualLocation(location!!)
//                 onNewLocationAgainwithLocalDatabase(location)
//             }
//         }
     }



     override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }


    override fun onBind(intent: Intent): IBinder? {
        //    log("Some component want to bind with the service")
        return null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //    log("onStartCommand executed with startId: $startId")
        if (intent != null) {
            val action = intent.action
            //      log("using an intent with action $action")
            when (action) {
                Actions.START.name -> startService()
                Actions.STOP.name -> stopService()
                else -> Log.e("t", "This should never happen. No action in the received intent")
            }
        } else {
            startService()
//            log(
//                "with a null intent. It has been probably restarted by the system."
//            )
        }
        return START_STICKY
    }

    fun handleUserTracking() {
        val settings = applicationContext.getSharedPreferences("AcessToken", MODE_PRIVATE)
        val manager = getSystemService(LOCATION_SERVICE) as LocationManager
        gps_status = 1
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gps_status = 0
            editor = getSharedPreferences("AcessToken", MODE_PRIVATE).edit()
            editor!!.putString("temp_lattitude", "")
            editor!!.putString("temp_longitude", "")
            editor!!.apply()
        }
        hitApitoUpdate(gps_status)
        Log.d("Gps status ", gps_status.toString())
    }


    fun getAddressFromLatLong(latitude: String, longitude: String): String {
        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(this, Locale.getDefault())
        addresses = geocoder.getFromLocation(
            latitude.toDouble(),
            longitude.toDouble(),
            1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5


        val address: String =
            addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

        val city: String = addresses[0].getLocality()
        val state: String = addresses[0].getAdminArea()
        val country: String = addresses[0].getCountryName()
        val postalCode: String = addresses[0].getPostalCode()
        val knownName: String = addresses[0].getFeatureName() //
        return address

    }

    fun hitApitoUpdate(gpsstatus: Int?) {
        if (!Utils.isNetworkConnected(this)) {
            startActivity(Intent(this, InternetSettingCheck::class.java))
            return
        }
        val settings = applicationContext.getSharedPreferences("AcessToken", MODE_PRIVATE)
        val lattitude = settings.getString("temp_lattitude", "")
        val longitude = settings.getString("temp_longitude", "")
        val address = getAddressFromLatLong(lattitude!!, longitude!!)
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
            address,
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


    override fun onCreate() {
        super.onCreate()
        //   log("The service has been created".toUpperCase())
        val notification = createNotification()
        startForeground(1, notification)

    }

    private fun createGoogleApi() {
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        }
        googleApiClient!!.connect()
    }


    override fun onDestroy() {
        super.onDestroy()
        val broadcastIntent = Intent()
        broadcastIntent.action = "restartservice"
        broadcastIntent.setClass(this, Restarter::class.java)
        this.sendBroadcast(broadcastIntent)

    }

    override fun onTaskRemoved(rootIntent: Intent) {
        val restartServiceIntent = Intent(
            applicationContext,
            MyService6666::class.java
        ).also { it.setPackage(packageName) }
        val restartServicePendingIntent: PendingIntent = PendingIntent.getService(
            this,
            1,
            restartServiceIntent,
            PendingIntent.FLAG_ONE_SHOT
        )
        applicationContext.getSystemService(Context.ALARM_SERVICE);
        val alarmService: AlarmManager =
            applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager;
        alarmService.set(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime() + 1000,
            restartServicePendingIntent
        )
        val notification = createNotification()
        startForeground(1, notification)

    }


    private fun startService() {
        val intent = Intent()
        val manufacturer = android.os.Build.MANUFACTURER
        when(manufacturer) {
            "xiaomi" ->
                intent.component=
                    ComponentName("com.miui.securitycenter",
                        "com.miui.permcenter.autostart.AutoStartManagementActivity")
                        "oppo" ->
            intent.component =
                ComponentName("com.coloros.safecenter",
                    "com.coloros.safecenter.permission.startup.StartupAppListActivity")
                    "vivo" ->
            intent.component =
                ComponentName("com.vivo.permissionmanager",
                    "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")
        }

        val list = applicationContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (list.size > 0) {
            applicationContext.startActivity(intent)
        }


        if (isServiceStarted) return
        //   log("Starting the foreground service task")
        isServiceStarted = true
        //  setServiceState(this, ServiceState.STARTED)
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
                    if (!checkGPS()) {
                        if (isFirsttime) {
                            val intent = Intent(
                                applicationContext,
                                LocationPermissionCheck::class.java
                            )
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            isFirsttime = false
                        }

                    }
                    createGoogleApi()
                }
                delay(1000)
            }
            //  log("End of the loop for the service")
        }
    }


    private fun checkGPS(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    private fun stopService() {
        //   log("Stopping the foreground service")
        try {
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                }
            }
            stopForeground(false)
            stopSelf()
            val calendar = Calendar.getInstance()
            val mdformat = SimpleDateFormat("HH:mm:ss")
            val strDate = mdformat.format(calendar.time)
            LocalDatabaseForException(
                applicationContext,
                strDate,
                "Service Stopped from stop service"
            ).execute()
        } catch (e: Exception) {
            Log.d("My Excption :", e.toString())
            //   log("Service stopped without being started: ${e.message}")
        }
        isServiceStarted = false
        // setServiceState(this, ServiceState.STOPPED)
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
            .setAutoCancel(false)
            .setContentTitle("Sapphire Industries")
            .setContentText("User Tracking")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.app_icon)
            .setTicker("Ticker text")
            .setPriority(Notification.PRIORITY_HIGH) // for under android 26 compatibility
            .build()
    }

    override fun onConnected(p0: Bundle?) {
        getLastKnownLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation() {
        //Log.d(TAG, "getLastKnownLocation()");
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
        if (lastLocation != null) {
            Log.i(
                TAG, "LasKnown location. " +
                        "Long: " + lastLocation!!.getLongitude() +
                        " | Lat: " + lastLocation!!.getLatitude()
            )
            startLocationUpdates()
            // loctionsend();
        } else {
            Log.w(TAG, "No location retrieved yet")
            startLocationUpdates()
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        if (Utils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                Toast.makeText(this, "start", Toast.LENGTH_LONG).show()
                // requestper()
                requestPermissions(
                    Activity(),
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    101
                )
            }
        }
        locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL.toLong())
            .setFastestInterval(FASTEST_INTERVAL.toLong())
        Manifest.permission.ACCESS_FINE_LOCATION
        LocationServices.FusedLocationApi.requestLocationUpdates(
            googleApiClient,
            locationRequest,
            this
        )
    }

    private fun checkPermissions(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }


    override fun onConnectionSuspended(p0: Int) {
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()

        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        if (connectionResult.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {
            val dsf = ""
            // The Wearable API is unavailable
        }
        Log.d(TAG, "connection failed")
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

    private fun onNewLocationAgainwithLocalDatabase(location: Location) {
        val calendar = Calendar.getInstance()
        val mdformat = SimpleDateFormat("HH:mm:ss")
        val strDate = mdformat.format(calendar.time)
        LocalDatabaseInsert(location, strDate, applicationContext).execute()
        InsertLocationData(location, false, strDate, applicationContext).execute()
        getLocationList()
    }

    private fun getLocationList() {
        GetLocationData(applicationContext, this).execute()
    }


    @SuppressLint("SetTextI18n")
    private fun writeActualLocation(location: Location) {
        Log.d(TAG, location.latitude.toString() + ", " + location.longitude)
        //here in this method you can use web service or any other thing
    }


    fun chnageInFourPlace(value: Double): String? {
        val df = DecimalFormat("#.####")
        df.roundingMode = RoundingMode.FLOOR
        return df.format(value)
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


    override fun onResult(status: Status) {
        Log.d(TAG, "result of google api client : $status")
    }

    override fun processData(locationData: MutableList<LocationParam>?) {
        hitApitoBulkEnter(locationData!!)
    }

    private fun hitApitoBulkEnter(location: List<LocationParam>) {
//        if (!Utils.isNetworkConnected(this)) {
//            startActivity(Intent(this, InternetSettingCheck::class.java))
//            return
//        }
        val settings = applicationContext.getSharedPreferences("AcessToken", MODE_PRIVATE)
        val access_token = settings.getString("Access_Token", "")
        val user_id = settings.getInt("User_ID", 0).toString()
        var attendance_id = settings.getString("Attendance_ID", "")
        val currentDate =
            SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(Date())
        val mTempList = ArrayList<LocationCustomArray>()
        mTempList.clear()
        Log.d("Location sizess : ", location.size.toString())
        if (location.size >= 1) {
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
                        SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(Date())
                    Log.d("currentDate and time : ", currentDate.toString())
                    val locationCustomArray = LocationCustomArray(
                        location[i].lattitude.toString(),
                        location[i].longitude.toString(),
                        currentDate,
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
    }

    private fun readWebPageBulkTest(location: String, mTemp: List<LocationParam>) {
        if (!Utils.isNetworkConnected(this)) {
            val i = Intent(this, InternetSettingCheck::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
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
            val call: Call<NewLocationPojo> =
                apiInterface!!.updateLatLongforUserIDBulk("Bearer $access_token", location)
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
                                this@MyService6666,
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
                                this@MyService6666,
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
                        Toast.makeText(this@MyService6666, "" + e.toString(), Toast.LENGTH_SHORT)
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


//      new  GetLocationData22 getLocationData22=new GetLocationData22();
//        getLocationData22.execute();


}
