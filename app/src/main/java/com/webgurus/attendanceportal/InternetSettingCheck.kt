package com.webgurus.attendanceportal

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.baseproject.utils.Utils

class InternetSettingCheck : AppCompatActivity() {

    fun checkAgain()
    {
        Handler(Looper.getMainLooper()).postDelayed({
            if (Utils.isConnected(this@InternetSettingCheck)) {
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
        setContentView(R.layout.activity_internet_setting_check)

        checkAgain()

        val btnCheckInternetStatus : Button? = findViewById(R.id.btnCheckInternetStatus)
        btnCheckInternetStatus?.setOnClickListener(View.OnClickListener {
            if (!Utils.isConnected(this)) {
                Toast.makeText(
                    this,
                    "No internet connection available. Please check your internet connection.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(this, "Internet connection available now.", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        })

    }

    override fun onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
        //Write your code here
    }
}