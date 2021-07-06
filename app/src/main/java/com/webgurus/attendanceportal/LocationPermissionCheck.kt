package com.webgurus.attendanceportal

import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.webgurus.attendanceportal.GpsUtils.onGpsListener


class LocationPermissionCheck : AppCompatActivity() {

    var isGPS: Boolean = false;

    fun checkAgain()
    {
        Handler(Looper.getMainLooper()).postDelayed({
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                finish()
            }
            else
            {
                checkAgain()
            }
        }, 1000)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_location_permission_check)
        checkAgain()
        val btnGPSEnable : Button? = findViewById(R.id.btnGPSEnable)
        btnGPSEnable?.setOnClickListener {
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                finish()
            } else {
                GpsUtils(this).turnGPSOn(object : onGpsListener {
                    override fun gpsStatus(isGPSEnable: Boolean) {
                        // turn on GPS
                        isGPS = isGPSEnable
                        if (isGPS) {
                            finish()
                        }
                    }
                })
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                isGPS = true // flag maintain before get location
                finish()
            }
        }
    }


    override fun onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
        //Write your code here
    }
}