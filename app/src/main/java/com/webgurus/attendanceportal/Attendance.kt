package com.webgurus.attendanceportal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class Attendance : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)
        val imgBack : ImageView? = findViewById(R.id.imgBack)
        imgBack?.setOnClickListener {
            finish()
        }
    }
}