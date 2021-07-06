/*
 * Copyright (c) 2019. This code has been developed by Fabio Ciravegna,
 * The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */
package com.webgurus.mitendlessservice

import android.app.job.JobParameters
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class JobService : android.app.job.JobService() {
    override fun onStartJob(jobParameters: JobParameters): Boolean {
        val bck = ProcessMainClass()
        bck.launchService(this)
        registerRestarterReceiver()
        instance = this
        Companion.jobParameters = jobParameters
        return false
    }

    private fun registerRestarterReceiver() {
        if (restartSensorServiceReceiver == null) restartSensorServiceReceiver =
            RestartServiceBroadcastReceiver() else try {
            unregisterReceiver(restartSensorServiceReceiver)
        } catch (e: Exception) {

        }
        Handler().postDelayed({
            val filter = IntentFilter()
            filter.addAction(Globals.RESTART_INTENT)
            try {
                registerReceiver(restartSensorServiceReceiver, filter)
            } catch (e: Exception) {
                try {
                    applicationContext.registerReceiver(restartSensorServiceReceiver, filter)
                } catch (ex: Exception) {
                }
            }
        }, 1000)
    }

    override fun onStopJob(jobParameters: JobParameters): Boolean {
        Log.i(TAG, "Stopping job")
        val broadcastIntent = Intent(Globals.RESTART_INTENT)
        sendBroadcast(broadcastIntent)
        Handler().postDelayed({ unregisterReceiver(restartSensorServiceReceiver) }, 1000)
        return false
    }

    companion object {
        private val TAG = JobService::class.java.simpleName
        private var restartSensorServiceReceiver: RestartServiceBroadcastReceiver? = null
        private var instance: JobService? = null
        private var jobParameters: JobParameters? = null

        fun stopJob(context: Context?) {
            if (instance != null && jobParameters != null) {
                try {
                    instance!!.unregisterReceiver(restartSensorServiceReceiver)
                } catch (e: Exception) {
                    // not registered
                }
                Log.i(TAG, "Finishing job")
                instance!!.jobFinished(jobParameters, true)
            }
        }
    }
}