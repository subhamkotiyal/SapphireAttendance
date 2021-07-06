package com.webgurus.attendanceportal

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class TaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        val imgBack : ImageView? = findViewById(R.id.imgBackTask)
        imgBack?.setOnClickListener {
            finish()
        }
    }
}