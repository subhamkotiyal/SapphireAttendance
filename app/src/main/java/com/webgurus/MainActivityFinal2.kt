package com.webgurus

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.app.admin.DevicePolicyManager
import android.content.*
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.baseproject.utils.Utils
import com.google.android.material.navigation.NavigationView
import com.robertohuertas.endless.*
import com.webgurus.attendanceportal.*
import com.webgurus.attendanceportal.demoservice.FService
import com.webgurus.attendanceportal.pojo.LogOutPojo
import com.webgurus.attendanceportal.pojo.NewLocationPojo
import com.webgurus.attendanceportal.pojo.SessionOutPojo
import com.webgurus.attendanceportal.pojo.UpdateprofilePojo
import com.webgurus.attendanceportal.ui.login.LoginActivity
import com.webgurus.endlessservice.ServiceAdmin
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import com.webgurus.newservice.ConnectivityReceiver
import kotlinx.android.synthetic.main.app_bar_main.*
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response
import java.util.*


class MainActivityFinal2 : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener,
    NavigationView.OnNavigationItemSelectedListener {

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
    var isFiftyClear = true

    private val mDPM: DevicePolicyManager? = null
    private var mAdminName: ComponentName? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerReceiver(
            ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        sharedPreferences = this.getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE)
        initview()
        hitGetProfileAPI()
        loationinit()
        checkBatteryStatus()
        listeners()
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

    private fun requestper() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }


    private fun requestPermissions() {


        val foreground = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (foreground) {
            val background = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            if (background) {

            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    REQUEST_PERMISSIONS_REQUEST_CODE
                )
            }
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ), REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }


    }


//
//    private fun requestPermissions() {
//        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
//            this,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        )
//
//        // Provide an additional rationale to the user. This would happen if the user denied the
//        // request previously, but didn't check the "Don't ask again" checkbox.
//        if (shouldProvideRationale) {
//            Log.i(TAG, "Displaying permission rationale to provide additional context.")
////            Snackbar.make(
////                findViewById(R.id.activity_main),
////                R.string.permission_rationale,
////                Snackbar.LENGTH_INDEFINITE
////            )
////                .setAction(R.string.ok) { // Request permission
////                    ActivityCompat.requestPermissions(
////                        this,
////                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
////                        REQUEST_PERMISSIONS_REQUEST_CODE
////                    )
////                }
////                .show()
//        } else {
//            Log.i(TAG, "Requesting permission")
//            // Request permission. It's possible this can be auto answered if device policy
//            // sets the permission in a given state or the user denied the permission
//            // previously and checked "Never ask again".
//            ActivityCompat.requestPermissions(
//                this, arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
//                ),
//                REQUEST_PERMISSIONS_REQUEST_CODE
//            )
//        }
//    }


    /**
     * Returns the current state of the permissions needed.
     */
    private fun checkPermissions(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
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
            sharedPreferences = this.getSharedPreferences(
                "AcessToken",
                AppCompatActivity.MODE_PRIVATE
            )
            editor = sharedPreferences!!.edit()
        }
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        var call = service.sessionCheck(sharedPreferences!!.getInt("User_ID", 0).toString())
        call.enqueue(object : retrofit2.Callback<SessionOutPojo?> {
            override fun onResponse(
                call: Call<SessionOutPojo?>,
                response: Response<SessionOutPojo?>
            ) {

                if (response.body()!!.status.equals("0")) {
                    Log.d("Result :", "" + response.body()!!.message)
                } else {
                    val preferences = applicationContext.getSharedPreferences(
                        "AcessToken",
                        MODE_PRIVATE
                    )
                    if (!preferences.getString("LatLongJson", "").equals("")) {
                        readWebPageBulk(preferences.getString("LatLongJson", ""))
                    }
                }


            }

            override fun onFailure(call: Call<SessionOutPojo?>, t: Throwable) {
                Toast.makeText(
                    this@MainActivityFinal2,
                    resources.getString(R.string.servernotrespond),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


    override fun onStart() {
        super.onStart()
        if (sharedPreferences == null) {
            sharedPreferences = this.getSharedPreferences(
                "AcessToken",
                AppCompatActivity.MODE_PRIVATE
            )
        }
        if (!checkPermissions()) {
            // requestper()
            requestPermissions()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (!isMyServiceRunning(FService::class.java)) {
                    actionOnService(Actions.START)
                }
            } else {
                val bck = ServiceAdmin()
                bck.launchService(applicationContext)
            }

        }
    }


    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                Log.i("Service status", "Running")
                return true
            }
        }
        Log.i("Service status", "Not running")
        return false
    }


    private fun actionOnService(action: Actions) {
        //Start service
        if (!FService.IS_RUNNING) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(Intent(applicationContext, FService::class.java))
            } else {
                startService(Intent(applicationContext, FService::class.java))
            }
        }


//        if (getServiceState(this) == ServiceState.STOPPED && action == Actions.STOP) return
//        Intent(this, FService::class.java).also {
//            it.action = action.name
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                log("Starting the service in >=26 Mode")
//                ContextCompat.startForegroundService(this, it)
//                return
//            }
//            log("Starting the service in < 26 Mode")
//            startService(it)
//        }
    }


    private fun stopService() {
        Intent(this, FService::class.java).also {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                log("Starting the service in >=26 Mode")
                stopService(it)
                return
            }
            log("Starting the service in < 26 Mode")
            stopService(it)
        }
    }


    override fun onResume() {
        super.onResume()
        if (battery_percentage == null) {
            checkBatteryStatus()
            setToolBar()
        }
        ConnectivityReceiver.connectivityReceiverListener = this
    }


    override fun onPause() {
        super.onPause()
    }


    override fun onStop() {
        super.onStop()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        if (requestCode === REQUEST_PERMISSIONS_REQUEST_CODE) {
            var foreground = false
            var background = false
            for (i in 0 until permissions.size) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    //foreground permission allowed
                    if (grantResults[i] >= 0) {
                        foreground = true

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            if (!isMyServiceRunning(FService::class.java)) {
                                actionOnService(Actions.START)
                            }
                        } else {
                            val bck = ServiceAdmin()
                            bck.launchService(applicationContext)
                        }
                        continue
                    } else {
                        break
                    }

                }
                if (permissions[i].equals(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    if (grantResults[i] >= 0) {
                        foreground = true
                        background = true
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            if (!isMyServiceRunning(FService::class.java)) {
                                actionOnService(Actions.START)
                            }
                        } else {
                            val bck = ServiceAdmin()
                            bck.launchService(applicationContext)
                        }
                    } else {
                    }
                }
            }
            if (foreground) {
                if (background) {
                }
            }
        }


//        Log.i(TAG, "onRequestPermissionResult")
//        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
//            if (grantResults.size <= 0) {
//                // If user interaction was interrupted, the permission request is cancelled and you
//                // receive empty arrays.
//                Log.i(TAG, "User interaction was cancelled.")
//            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //actionOnService(Actions.START)
//                //    mService!!.requestLocationUpdates()
//                if (!isMyServiceRunning(FService::class.java)) {
//                    actionOnService(Actions.START)
//               }
//            } else {
//                Snackbar.make(
//                    findViewById(R.id.activity_main),
//                    R.string.permission_denied_explanation,
//                    Snackbar.LENGTH_INDEFINITE
//                )
//                    .setAction(R.string.settings) { // Build intent that displays the App settings screen.
//                        val intent = Intent()
//                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                        val uri = Uri.fromParts(
//                            "package",
//                            applicationContext.packageName, null
//                        )
//                        intent.data = uri
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                        startActivity(intent)
//                    }
//                    .show()
//            }
//        }
    }

    private fun setToolBar() {
        setSupportActionBar(toolbar)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_dashboard,
                R.id.nav_gallery,
                R.id.nav_slideshow,
                R.id.nav_taskslist,
                R.id.nav_mycustomer,
                R.id.nav_userlisiting,
                R.id.nav_rolelisting,
                R.id.nav_category,
                R.id.nav_unit,
                R.id.nav_productlisting,
                R.id.nav_orderlisting,
                R.id.nav_orderdispatchready,
                R.id.nav_orderdispatched,
                R.id.nav_approvedorders,
                R.id.nav_permission,
                R.id.nav_reports,
                R.id.nav_latlongtest,
                R.id.nav_latlongreal,
                R.id.nav_logsissues,
                R.id.nav_bills,
                R.id.nav_setting,
                R.id.nav_customeroutstanding,

                ), drawerLayout

        )


        val mDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        ) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                updateLeftMenu()
                super.onDrawerSlide(drawerView, slideOffset)
            }
        }
        drawerLayout!!.setDrawerListener(mDrawerToggle)
        setupActionBarWithNavController(navController!!, appBarConfiguration)
        navView!!.setupWithNavController(navController!!)
        updateLeftMenu()

    }

    private fun listeners() {
        iv_logout.setOnClickListener {
            showAlertLogout()
        }
        iv_help.setOnClickListener {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        val broadcastIntent = Intent()
        broadcastIntent.action = "restartservice"
        broadcastIntent.setClass(this, Restarter::class.java)
        this.sendBroadcast(broadcastIntent)
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
            val username = sharedPreferences!!.getString("User_Name", "")
            tv_usernamenav.text = username
        }
    }


    private fun checkGPS(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
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
                "Bearer " + sharedPreferences!!.getString(
                    "Access_Token",
                    ""
                ), location
            )
            call.enqueue(object : retrofit2.Callback<NewLocationPojo?> {
                override fun onResponse(
                    call: Call<NewLocationPojo?>?,
                    response: Response<NewLocationPojo?>?
                ) {
                    try {
                        if (response!!.body()!!.status == "success") {
                            //    isFiftyClear=true
                            Log.e("ApiResponse ", response.body().toString())
                            Toast.makeText(
                                this@MainActivityFinal2,
                                "Location Updated SucessFully",
                                Toast.LENGTH_SHORT
                            ).show()
                            if (mService != null) {
                                mService!!.removeLocationUpdates()
                            }
                            //  unbindService(mServiceConnection)
                            val preferences = getSharedPreferences(
                                "AcessToken",
                                Context.MODE_PRIVATE
                            )
                            val editor = preferences.edit()
                            editor.clear()
                            editor.apply()
                            if (mService != null) {
                                mService!!.removeLocationUpdates()
                            }
                            startActivity(
                                Intent(
                                    this@MainActivityFinal2,
                                    LoginActivity::class.java
                                )
                            )
                            finishAffinity()
                        } else {
                            Toast.makeText(
                                this@MainActivityFinal2,
                                "" + response.body().toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@MainActivityFinal2,
                            "" + e.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("ServerSam :", e.toString())
                    }

                }


                override fun onFailure(call: Call<NewLocationPojo?>, t: Throwable) {
                    Toast.makeText(this@MainActivityFinal2, t.toString(), Toast.LENGTH_SHORT).show()
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
        }

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
                try {
                    editor!!.putString("User_Email", response.body()!!.success.email)
                    editor!!.putString("User_Name", response.body()!!.success.name)
                    editor!!.commit()
                    setToolBar()
                    updateLeftMenu()
                    hitApitoChecksession()
                } catch (e: java.lang.Exception) {
                    Log.d("Exception :", e.toString())
                }

            }

            override fun onFailure(call: Call<UpdateprofilePojo?>, t: Throwable) {
                Toast.makeText(
                    this@MainActivityFinal2,
                    resources.getString(R.string.servernotrespond),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun showAlertLogout() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.dialogTitle)
        builder.setMessage(R.string.dialogMessage)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes")
        { dialogInterface, which ->
            hitApitologout()
        }
        builder.setNegativeButton("No") { dialogInterface, which ->
            //  Toast.makeText(applicationContext, "clicked No", Toast.LENGTH_LONG).show()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun hitApitologout() {
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
        }

        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.logout("Bearer " + sharedPreferences!!.getString("Access_Token", ""))
        call.enqueue(object : retrofit2.Callback<LogOutPojo?> {
            override fun onResponse(
                call: Call<LogOutPojo?>,
                response: Response<LogOutPojo?>
            ) {
                try {
                    editor!!.clear()
                    editor!!.apply()
                    val intent = Intent(this@MainActivityFinal2, FService::class.java)
                    stopService(intent)
                    startActivity(Intent(this@MainActivityFinal2, LoginActivity::class.java))
                    finishAffinity()
                } catch (e: java.lang.Exception) {
                    Log.d("Exception :", e.toString())
                }

            }

            override fun onFailure(call: Call<LogOutPojo?>, t: Throwable) {
                Toast.makeText(
                    this@MainActivityFinal2,
                    resources.getString(R.string.servernotrespond),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
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

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (checkPermissions()) {
            showNetworkMessage(isConnected)
        }

    }

    private fun showNetworkMessage(isConnected: Boolean) {
        if (!isConnected) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (!isMyServiceRunning(FService::class.java)) {
                    actionOnService(Actions.START)
                }
            } else {
                if (!isMyServiceRunning(FService::class.java)) {
                    val bck = ServiceAdmin()
                    bck.launchService(applicationContext)
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (!isMyServiceRunning(FService::class.java)) {
                    actionOnService(Actions.START)
                }
            } else {
                if (!isMyServiceRunning(FService::class.java)) {
                    val bck = ServiceAdmin()
                    bck.launchService(applicationContext)
                }
            }

        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.home -> {
//                val homeFragment = HomeFragment()
//                show(homeFragment)
//            }
//            R.id.gallery -> {
//                val galleryFragment = GalleryFragment()
//                show(galleryFragment)
//            }
//            R.id.slide_show -> {
//                val slideShowFragment = SlideShowFragment()
//                show(slideShowFragment)
//            }
//            R.id.tools -> {
//                val toolsFragment = ToolsFragment()
//                show(toolsFragment)
//            }
//        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }




}