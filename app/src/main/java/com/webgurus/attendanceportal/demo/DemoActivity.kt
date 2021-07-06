package com.webgurus.attendanceportal.demo

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.webgurus.attendanceportal.R

class DemoActivity  : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startService(Intent(applicationContext, LocationUpdaterService66::class.java))
    }

}


//{
//
//    var sharedPreferences: SharedPreferences? = null
//    var editor: SharedPreferences.Editor? = null
//    var navView: NavigationView? = null
//    var navController: NavController? = null
//
//
//    // Location Service
//    private val TAG = "resPMain"
//    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
//
//    //   private var mService: LocationUpdatesService? = null
//    private var mService: LocationUpdaterService66? = null
//    private var mBound = false
//    var battery_percentage: Int? = null
//    var access_token = ""
//    var user_id = ""
//    var attendance_id = ""
//    var apiInterface: GetDataService? = null
//
//    // Monitors the state of the connection to the service.
//    public val mServiceConnection: ServiceConnection = object : ServiceConnection {
//        override fun onServiceConnected(name: ComponentName, service: IBinder) {
//            val binder = service as LocationUpdaterService66.LocalBinder
//            mService = binder.getService()
//            mBound = true
//        }
//
//        override fun onServiceDisconnected(name: ComponentName) {
//            mService = null
//            mBound = false
//        }
//    }
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        sharedPreferences = this.getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE)
//        checkBatteryStatus()
//        loationinit()
//        initview()
//    }
//
//
//    private fun loationinit() {
//        if (Utils.requestingLocationUpdates(this)) {
//            if (!checkPermissions()) {
//                requestPermissions()
//            }
//        }
//    }
//
//    private fun checkBatteryStatus() {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            val bm: BatteryManager? =
//                this.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
//            battery_percentage = bm!!.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
//        } else {
//            val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
//            val batteryStatus = this.registerReceiver(null, iFilter)
//            val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
//            val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
//            battery_percentage = level / scale
//        }
//    }
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
//                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                REQUEST_PERMISSIONS_REQUEST_CODE
//            )
//        }
//    }
//
//
//    /**
//     * Returns the current state of the permissions needed.
//     */
//    private fun checkPermissions(): Boolean {
//        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
//            this,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        )
//    }
//
//
//    override fun onStart() {
//        super.onStart()
//        if (sharedPreferences == null) {
//            sharedPreferences = this.getSharedPreferences(
//                "AcessToken",
//                AppCompatActivity.MODE_PRIVATE
//            )
//        }
//        PreferenceManager.getDefaultSharedPreferences(this)
//            .registerOnSharedPreferenceChangeListener(
//                this
//            )
//        if (!checkPermissions()) {
//            requestPermissions()
//        } else {
//            if (mService != null) {
//                if (sharedPreferences!!.getInt("User_ID", 0) != 0) {
//                    mService!!.requestLocationUpdates()
//                }
//            } else {
//                if (sharedPreferences!!.getInt("User_ID", 0) != 0) {
//                    bindService(
//                        Intent(this, LocationUpdaterService66::class.java), mServiceConnection,
//                        BIND_AUTO_CREATE
//                    )
//                    Handler().postDelayed({
//                        if (mService != null) {
//                            mService!!.requestLocationUpdates()
//                        }
//                    }, 5000)
//                }
//
//            }
//        }
//
//        // Bind to the service. If the service is in foreground mode, this signals to the service
//        // that since this activity is in the foreground, the service can exit foreground mode.
//        bindService(
//            Intent(this, LocationUpdaterService66::class.java), mServiceConnection,
//            BIND_AUTO_CREATE
//        )
//    }
//
//
//    override fun onResume() {
//        super.onResume()
//        if (battery_percentage == null) {
//            checkBatteryStatus()
//        }
//
//    }
//
//    override fun onPause() {
//        super.onPause()
//    }
//
//
//    override fun onStop() {
//        if (mBound) {
//            // Unbind from the service. This signals to the service that this activity is no longer
//            // in the foreground, and the service can respond by promoting itself to a foreground
//            // service.
//            if(mServiceConnection!=null){
//                unbindService(mServiceConnection)
//                mBound = false
//            }
//
//        }
//        PreferenceManager.getDefaultSharedPreferences(this)
//            .unregisterOnSharedPreferenceChangeListener(this)
//        super.onStop()
//
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        Log.i(TAG, "onRequestPermissionResult")
//        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
//            if (grantResults.size <= 0) {
//                // If user interaction was interrupted, the permission request is cancelled and you
//                // receive empty arrays.
//                Log.i(TAG, "User interaction was cancelled.")
//            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission was granted.
//                mService!!.requestLocationUpdates()
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
//    }
//
//    private fun initview() {
//        if (sharedPreferences == null) {
//            sharedPreferences = this.getSharedPreferences(
//                "AcessToken",
//                AppCompatActivity.MODE_PRIVATE
//            )
//            editor = sharedPreferences!!.edit()
//            access_token = sharedPreferences!!.getString("Access_Token", "")!!
//            user_id = sharedPreferences!!.getString("User_ID", "")!!
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode === Activity.RESULT_OK) when (requestCode) {
//            Constants.REQUEST_GALLERY -> {
//                val temp = Utils.getPathFromURI(this, data?.data)
//                EventBus.getDefault().post(temp)
//            }
//
//        }
//    }
//
//    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
//    }
//
//}