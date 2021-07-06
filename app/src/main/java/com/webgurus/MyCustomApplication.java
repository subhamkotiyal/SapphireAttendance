package com.webgurus;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import androidx.core.app.ActivityCompat;
import com.webgurus.newservice.MyService6666;
public class MyCustomApplication extends Application {

	private static final String STARTFOREGROUND_ACTION = "STARTFOREGROUND_ACTION";
	private static final String STOPFOREGROUND_ACTION = "STOPFOREGROUND_ACTION";

	@Override
	public void onCreate() {
		super.onCreate();

	}


	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}




	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

}