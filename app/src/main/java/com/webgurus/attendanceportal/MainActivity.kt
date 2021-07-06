package com.webgurus.attendanceportal

//import com.webgurus.attendanceportal.demo.LocationUpdatesService
import android.Manifest
import android.app.Activity
import android.content.*
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.*
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.baseproject.utils.Utils
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.webgurus.Constants
import com.webgurus.attendanceportal.pojo.NewLocationPojo
import com.webgurus.attendanceportal.pojo.SessionOutPojo
import com.webgurus.attendanceportal.pojo.UpdateprofilePojo
import com.webgurus.attendanceportal.ui.login.LoginActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.app_bar_main.*
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response


class MainActivity : AppCompatActivity(), OnSharedPreferenceChangeListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    var drawerLayout: DrawerLayout? = null
    var sharedPreferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    var navView: NavigationView? = null
    var navController: NavController? = null
    // Location Service


    private val TAG = "resPMain"
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34

    //   private var mService: LocationUpdatesService? = null
    private var mService: LocationUpdatesService55? = null
    private var mBound = false
    var battery_percentage: Int? = null
    var access_token = ""
    var user_id = ""
    var attendance_id = ""
    var apiInterface: GetDataService? = null

    // Monitors the state of the connection to the service.
    public val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as LocationUpdatesService55.LocalBinder
            mService = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
            mBound = false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = this.getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE)
        checkBatteryStatus()
        loationinit()
        initview()
        listeners()
        setToolBar()
    }


    private fun loationinit() {
        if (Utils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions()
            }
        }
    }

    private fun checkBatteryStatus() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val bm: BatteryManager? =
                this.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            battery_percentage = bm!!.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } else {
            val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryStatus = this.registerReceiver(null, iFilter)
            val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
            battery_percentage = level / scale
        }
    }

//    public fun hitAPitoupdateLatLong(lat: String, longi: String, mContext: Context) {
//        if (sharedPreferences == null) {
//            sharedPreferences = mContext.getSharedPreferences(
//                "AcessToken",
//                AppCompatActivity.MODE_PRIVATE
//            )
//            access_token = sharedPreferences!!.getString("Access_Token", "")!!
//            user_id = sharedPreferences!!.getInt("User_ID", 0).toString()
//            attendance_id = sharedPreferences!!.getString("Attendance_ID", "").toString()
//
//        }
//        if (attendance_id.equals("")) {
//            attendance_id = "0"
//        }
//        if (!user_id.equals("")) {
//            val service =
//                RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
//            val call = service.updateLatLongforUserID(
//                "Bearer " + access_token,
//                lat,
//                longi,
//                user_id,
//                battery_percentage.toString(),
//                attendance_id
//            )
//            call.enqueue(object : Callback<UpdateLocationPojo> {
//                override fun onResponse(
//                    call: Call<UpdateLocationPojo>,
//                    response: Response<UpdateLocationPojo>
//                ) {
//
//                    try {
//                        Toast.makeText(
//                            mContext,
//                            "Hit Api sucessfully with ID : " + response.body()!!.location.id.toString(),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    } catch (e: java.lang.Exception) {
//                        Toast.makeText(
//                            this@MainActivity,
//                            resources.getString(R.string.servernotrespond),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//
//                }
//
//                override fun onFailure(call: Call<UpdateLocationPojo>, t: Throwable) {
//                      Toast.makeText(this@MainActivity,t.toString(),Toast.LENGTH_SHORT)
//                }
//
//
//            })
//
//
//        }
//
//
//
//    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
//            Snackbar.make(
//                findViewById(R.id.activity_main),
//                R.string.permission_rationale,
//                Snackbar.LENGTH_INDEFINITE
//            )
//                .setAction(R.string.ok) { // Request permission
//                    ActivityCompat.requestPermissions(
//                        this,
//                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                        REQUEST_PERMISSIONS_REQUEST_CODE
//                    )
//                }
//                .show()
        } else {
            Log.i(TAG, "Requesting permission")
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }


    /**
     * Returns the current state of the permissions needed.
     */
    private fun checkPermissions(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }


    override fun onStart() {
        super.onStart()
        if (sharedPreferences == null) {
            sharedPreferences = this.getSharedPreferences(
                "AcessToken",
                AppCompatActivity.MODE_PRIVATE
            )
        }
        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(
                this
            )
        if (!checkPermissions()) {
            requestPermissions()
        } else {
            if (mService != null) {
                if (sharedPreferences!!.getInt("User_ID", 0) != 0) {
                    mService!!.requestLocationUpdates()
                }
            } else {
                if (sharedPreferences!!.getInt("User_ID", 0) != 0) {
                    bindService(
                        Intent(this, LocationUpdatesService55::class.java), mServiceConnection,
                        BIND_AUTO_CREATE
                    )
                    Handler().postDelayed({
                        if (mService != null) {
                            mService!!.requestLocationUpdates()
                        }
                    }, 5000)
                }

            }
        }
        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(
            Intent(this, LocationUpdatesService55::class.java), mServiceConnection,
            BIND_AUTO_CREATE
        )
    }


    override fun onResume() {
        super.onResume()
        if (battery_percentage == null) {
            checkBatteryStatus()
        }

    }

    override fun onPause() {
        super.onPause()
    }


    override fun onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection)
            mBound = false
        }
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
        super.onStop()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.size <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.")
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                mService!!.requestLocationUpdates()
            } else {
                Snackbar.make(
                    findViewById(R.id.activity_main),
                    R.string.permission_denied_explanation,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(R.string.settings) { // Build intent that displays the App settings screen.
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts(
                            "package",
                            applicationContext.packageName, null
                        )
                        intent.data = uri
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    .show()
            }
        }
    }


    private fun setToolBar() {
        setSupportActionBar(toolbar)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_slideshow,

            ), drawerLayout
        )
        setupActionBarWithNavController(navController!!, appBarConfiguration)
        navView!!.setupWithNavController(navController!!)
        hitGetProfileAPI()
        updateLeftMenu()

    }

    private fun listeners() {
        iv_logout.setOnClickListener {
            showAlertLogout()
        }

    }

    private fun initview() {
        if (sharedPreferences == null) {
            sharedPreferences = this.getSharedPreferences(
                "AcessToken",
                AppCompatActivity.MODE_PRIVATE
            )
            editor = sharedPreferences!!.edit()
            access_token = sharedPreferences!!.getString("Access_Token", "")!!
            user_id = sharedPreferences!!.getString("User_ID", "")!!
        }
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)
    }

    private fun updateLeftMenu() {
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerLayout = navigationView.getHeaderView(0)
        val tv_usernamenav = headerLayout.findViewById<TextView>(R.id.tv_usernamenav)
        tv_usernamenav.text = ""
        val tv_email = headerLayout.findViewById<TextView>(R.id.tv_email)
        if (sharedPreferences!!.getString("User_Email", "") != null) {
            val useremail = sharedPreferences!!.getString("User_Email", "")
            tv_email.text = useremail
        }

        if (sharedPreferences!!.getString("User_Name", "") != null) {
            val useremail = sharedPreferences!!.getString("User_Name", "")
            tv_usernamenav.text = useremail
        }
    }


    private fun checkGPS(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    fun readWebPageBulk(location: String?) {

        if (!Utils.isConnected(this)) {
            Toast.makeText(
                this,
                "No internet connection available. Please check your internet connection.",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(this, InternetSettingCheck::class.java))
        } else if (!checkGPS()) {
            Toast.makeText(this, "GPS is not active.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LocationPermissionCheck::class.java))
        } else {
            val service =
                RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
            val call = service.updateLatLongforUserIDBulk(
                "Bearer " + sharedPreferences!!.getString("Access_Token", ""),
                location
            )
            call.enqueue(object : retrofit2.Callback<NewLocationPojo?> {
                override fun onResponse(
                    call: Call<NewLocationPojo?>?,
                    response: Response<NewLocationPojo?>?
                ) {
                    try {
                        if (response!!.body()!!.status == "success") {
                            Log.e("ApiResponse ", response.body().toString())
                            Toast.makeText(
                                this@MainActivity,
                                "Location Updated SucessFully",
                                Toast.LENGTH_SHORT
                            ).show()
                            mService!!.removeLocationUpdates()
                            unbindService(mServiceConnection)
                            val preferences = getSharedPreferences(
                                "AcessToken",
                                Context.MODE_PRIVATE
                            )
                            val editor = preferences.edit()
                            editor.clear()
                            editor.apply()
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                            finishAffinity()
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "" + response.body().toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@MainActivity,
                            "" + e.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("ServerSam :", e.toString())
                    }

                }


                override fun onFailure(call: Call<NewLocationPojo?>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }

    }


    private fun hitApitoChecksession() {

        if (!Utils.isConnected(this)) {
            Toast.makeText(
                this,
                "No internet connection available. Please check your internet connection.",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(this, InternetSettingCheck::class.java))
        } else if (!checkGPS()) {
            Toast.makeText(this, "GPS is not active.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LocationPermissionCheck::class.java))
        }

        if (editor == null) {
            sharedPreferences = this.getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE)
            editor = sharedPreferences!!.edit()
        } else {

            val service =
                RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
            val call = service.sessionCheck(sharedPreferences!!.getString("User_ID", ""))
            call.enqueue(object : retrofit2.Callback<SessionOutPojo?> {
                override fun onResponse(call: Call<SessionOutPojo?>, response: Response<SessionOutPojo?>)
                {

                     if(response.body()!!.data.equals("0")){
                         Log.d("Result :", ""+ response.body()!!.message)
                     }else{
                         val preferences = applicationContext.getSharedPreferences("AcessToken", MODE_PRIVATE)
                         if(! preferences.getString("LatLongJson", "").equals("")){
                             readWebPageBulk(preferences.getString("LatLongJson", ""))
                         }
                     }



                }

                override fun onFailure(call: Call<SessionOutPojo?>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        resources.getString(R.string.servernotrespond),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }



    private fun hitGetProfileAPI() {

        if (!Utils.isConnected(this)) {
            Toast.makeText(
                this,
                "No internet connection available. Please check your internet connection.",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(this, InternetSettingCheck::class.java))
        } else if (!checkGPS()) {
            Toast.makeText(this, "GPS is not active.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LocationPermissionCheck::class.java))
        }

        if (editor == null) {
            sharedPreferences = this.getSharedPreferences(
                "AcessToken",
                AppCompatActivity.MODE_PRIVATE
            )
            editor = sharedPreferences!!.edit()
        } else {

            val service =
                RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
            val call = service.getUserInfo(
                "Bearer " + sharedPreferences!!.getString(
                    "Access_Token",
                    ""
                ), "application/json"
            )
            call.enqueue(object : retrofit2.Callback<UpdateprofilePojo?> {
                override fun onResponse(
                    call: Call<UpdateprofilePojo?>,
                    response: Response<UpdateprofilePojo?>
                ) {

                    editor!!.putString("User_Email", response.body()!!.success.email)
                    editor!!.putString("User_Name", response.body()!!.success.name)
                    editor!!.commit()
                    updateLeftMenu()
                    hitApitoChecksession()


//                    try {
//                        editor!!.putString("User_Email", response.body()!!.success.email)
//                        editor!!.putString("User_Name", response.body()!!.success.name)
//                        editor!!.commit()
//                        updateLeftMenu()
//
//                    } catch (e: Exception) {
//                        Toast.makeText(
//                            this@MainActivity,
//                            resources.getString(R.string.servernotrespond),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
                }

                override fun onFailure(call: Call<UpdateprofilePojo?>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        resources.getString(R.string.servernotrespond),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun showAlertLogout() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.dialogTitle)
        builder.setMessage(R.string.dialogMessage)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes")

        { dialogInterface, which ->
            val preferences = applicationContext.getSharedPreferences("AcessToken", MODE_PRIVATE)
            val ewditt = preferences.edit()
            if(! preferences.getString("LatLongJson", "").equals("")){
                readWebPageBulk(preferences.getString("LatLongJson", ""))
            }
        }

        builder.setNegativeButton("No") { dialogInterface, which ->
            //  Toast.makeText(applicationContext, "clicked No", Toast.LENGTH_LONG).show()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode === Activity.RESULT_OK) when (requestCode) {
            Constants.REQUEST_GALLERY -> {
                val temp = Utils.getPathFromURI(this, data?.data)
                EventBus.getDefault().post(temp)
            }

        }
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
        Log.e("dsf", "sdf")
    }

}