package com.webgurus.utils;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.webgurus.MyService;

public class UtilsFound {

   @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
   public static void scheduleJob(Context context){
       ComponentName serviceComponent = new ComponentName(context, MyService.class);
       JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
       builder.setMinimumLatency(1 * 1000); // wait at least
       builder.setOverrideDeadline(3 * 1000); // maximum delay
       //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
       //builder.setRequiresDeviceIdle(true); // device should be idle
       //builder.setRequiresCharging(false); // we don't care if the device is charging or not
       JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
       jobScheduler.schedule(builder.build());
   }
}
