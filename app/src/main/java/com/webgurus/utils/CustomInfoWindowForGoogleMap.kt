package com.webgurus.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.UserCurrentLocationPojo
import com.webgurus.attendanceportal.pojo.data_performance

class CustomInfoWindowForGoogleMap(context: Context , var data : UserCurrentLocationPojo) : GoogleMap.InfoWindowAdapter {

    var mContext = context
    var mWindow = (context as Activity).layoutInflater.inflate(R.layout.custom_info_window, null)

    private fun rendowWindowText(marker: Marker, view: View){

        val tv_username = view.findViewById<TextView>(R.id.tv_username)
        val tv_gpsstatus = view.findViewById<TextView>(R.id.tv_gpsstatus)
        val tv_batteryper = view.findViewById<TextView>(R.id.tv_batteryper)
        val tv_address = view.findViewById<TextView>(R.id.tv_address)
        val tv_lasttime = view.findViewById<TextView>(R.id.tv_lasttime)
        tv_username.setText("Username  :  " +data.data[0].name)
        tv_gpsstatus.setText("Gps Status  :  "+data.data[0].gps)
        tv_batteryper.setText("Battery Status  :  "+data.data[0].battery.toString())
        tv_address.setText("Address  :  "+data.data[0].address)
        tv_lasttime.setText("Last Time  :  "+data.data[0].time)
    }

    override fun getInfoContents(marker: Marker): View {
        rendowWindowText(marker, mWindow)
        return mWindow
    }

    override fun getInfoWindow(marker: Marker): View? {
        rendowWindowText(marker, mWindow)
        return mWindow
    }
}
