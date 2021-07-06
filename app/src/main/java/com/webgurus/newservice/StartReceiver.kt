package com.robertohuertas.endless

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.webgurus.newservice.ConnectivityReceiver


class StartReceiver : BroadcastReceiver() , ConnectivityReceiver.ConnectivityReceiverListener {


    private val mContext: Context? = null

    // flag for GPS status
    var isGPSEnabled = false

    // flag for network status
    var isNetworkEnabled = false

    // flag for GPS status
    var canGetLocation = false

    var location // location
            : Location? = null
    var latitude // latitude
            = 0.0
    var longitude // longitude
            = 0.0

    // The minimum distance to change Updates in meters
    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10 // 10 meters


    // The minimum time between updates in milliseconds
    private val MIN_TIME_BW_UPDATES = (1000 * 60 * 1 // 1 minute
            ).toLong()

    // Declaring a Location Manager
    protected var locationManager: LocationManager? = null

    override fun onReceive(context: Context, intent: Intent) {
//
//        val connMgr = context
//            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//        val wifi = connMgr
//            .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
//
//        val mobile = connMgr
//            .getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
//
//        if (wifi.isAvailable || mobile.isAvailable) {
//            // Do something
//           Toast.makeText(context,"Onnnnnnnn",Toast.LENGTH_LONG).show()
//        }
//        else{
//            Toast.makeText(context,"NOoooooooooooooooo",Toast.LENGTH_LONG).show()
//        }

       // val activeNetwork: NetworkInfo = cm.getActiveNetworkInfo()
        //here, check that the network connection is available. If yes, start your service. If not, stop your service.
        //here, check that the network connection is available. If yes, start your service. If not, stop your service.
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.activeNetworkInfo
        if (info != null) {
            if (info.isConnected) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                  //  log("Starting the service in >=26 Mode")
                    ContextCompat.startForegroundService(context, intent)
                    return
                }
             //   log("Starting the service in < 26 Mode")
                context.startService(intent)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //        log("Starting the service in >=26 Mode")
                    ContextCompat.startForegroundService(context, intent)
                    return
                }
           //     log("Starting the service in < 26 Mode")
                context.startService(intent)
            }
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {

        if(isConnected){
            Toast.makeText(Activity(), "Internet Connected", Toast.LENGTH_SHORT).show();
        }else{Toast.makeText(Activity(), "Internet not Connected", Toast.LENGTH_SHORT).show();}
    }

}
