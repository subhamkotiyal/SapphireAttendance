package com.webgurus.attendanceportal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.legacy.content.WakefulBroadcastReceiver;

import com.webgurus.attendanceportal.demoservice.FService;

public class AlarmBroadCastReceiver extends WakefulBroadcastReceiver {

    private PowerManager.WakeLock screenWakeLock;


    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onReceive(Context context, Intent intent) {
     /*   screenWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getPackageName() + ":TrackService");
        screenWakeLock.acquire();*/
            if (screenWakeLock == null) {
                Log.d("Location Updated :" , "Device is Idle");
                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                screenWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "com.android.internal.location.ALARM_WAKEUP");
                screenWakeLock.acquire(10000 /*10 minutes*/);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(new Intent(context, FService.class));
                    } else {
                    context.startService(new Intent(context, FService.class));
                    }

            }
//TODO:Do your code here related to alarm receiver.
        if (screenWakeLock != null)
            screenWakeLock.release();
          }

}
