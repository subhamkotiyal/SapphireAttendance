package com.webgurus.newservice

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import com.webgurus.MyCustomApplication

class ConnectivityReceiver : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        if (connectivityReceiverListener != null) {
            connectivityReceiverListener!!.onNetworkConnectionChanged(
                isConnectedOrConnecting(
                    context!!
                )
            )
        }
    }

    @SuppressLint("ServiceCast")
    private fun isConnectedOrConnecting(context: Context): Boolean {

//        val conMgr = context
//            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//        val wifi = conMgr
//            .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
//
//        val mobile = conMgr
//            .getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
//
//        if (wifi.isAvailable || mobile.isAvailable) {
//            // Do something
//            Toast.makeText(context,"Onnnnnnnn", Toast.LENGTH_LONG).show()
//        }
//        else{
//            Toast.makeText(context,"NOoooooooooooooooo", Toast.LENGTH_LONG).show()
//        }

        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }

    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    companion object {
        var connectivityReceiverListener: ConnectivityReceiverListener? = null
    }
}