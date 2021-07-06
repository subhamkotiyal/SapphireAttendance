package com.webgurus.attendanceportal.ui.splash

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.PowerManager
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.webgurus.MainActivityFinal2
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.ui.login.LoginActivity


class SplashScreen : Activity() {

    private val SPLASH_TIME_OUT: Long = 3000
    var sharedPreferences: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent()
            val packageName = packageName
            val pm = getSystemService(POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        }
        sharedPreferences = this.getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE)
        Handler().postDelayed({
            if (sharedPreferences!!.getString("Access_Token", "").equals("")) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, MainActivityFinal2::class.java))
                finish()
            }
        }, SPLASH_TIME_OUT)
    }
}


