package com.webgurus.attendanceportal.ui.createuser

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.webgurus.attendanceportal.APIClient
import com.webgurus.attendanceportal.InternetSettingCheck
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.demo.Utils
import com.webgurus.attendanceportal.pojo.UserCurrentLocationPojo
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.network.GetDataService
import com.webgurus.utils.CustomInfoWindowForGoogleMap
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserCurrentLocation : BaseActivity(), OnMapReadyCallback {

    var apiInterface: GetDataService? = null
    var userID : String = ""
    private var mMap: GoogleMap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currentlocation)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        initview()
    }

    private fun initview() {
        if(intent.getStringExtra("userID")!=null){
            userID=intent.getStringExtra("userID")!!
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun hitApitogetCurrentLocation() {
        if (!Utils.isNetworkConnected(this)) {
            Toast.makeText(this, "No internet connection available. Please check your internet connection.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, InternetSettingCheck::class.java))
            return
        }
        val settings = applicationContext.getSharedPreferences("AcessToken", MODE_PRIVATE)
        val access_token = settings.getString("Access_Token", "")
        apiInterface = APIClient.getClient().create(GetDataService::class.java)
        val call: Call<UserCurrentLocationPojo> = apiInterface!!.user_current_location("Bearer "+access_token,userID)
        call.enqueue(object : Callback<UserCurrentLocationPojo?> {

            override fun onResponse(
                call: Call<UserCurrentLocationPojo?>,
                response: Response<UserCurrentLocationPojo?>
            ) {
                try {
                    val markerOptions = MarkerOptions()
                    val lll = LatLng(response.body()!!.data[0].latitude.toDouble(),response.body()!!.data[0].longitude.toDouble())
                    markerOptions.position(lll)
                    markerOptions.title(lll.latitude.toString() + " : " + lll.longitude)
                    mMap!!.clear()
                    mMap!!.animateCamera(CameraUpdateFactory.newLatLng(lll))
                    mMap!!.addMarker(markerOptions)
                    mMap!!.setMinZoomPreference(12.0f)
                    mMap!!.setInfoWindowAdapter(CustomInfoWindowForGoogleMap(this@UserCurrentLocation,
                        response.body()!!
                    ))
                    Toast.makeText(
                        this@UserCurrentLocation,
                        "User Current Location",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    val dfgdsdsdsfds = ""
                    Log.d("Gps sttaus 22 :", e.toString())
                }
            }

            override fun onFailure(call: Call<UserCurrentLocationPojo?>, t: Throwable) {

            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.mMap = googleMap
        googleMap!!.setMapType(GoogleMap.MAP_TYPE_NORMAL)
        mMap!!.getUiSettings().setZoomControlsEnabled(true)
        mMap!!.getUiSettings().setZoomGesturesEnabled(true)
        mMap!!.getUiSettings().setCompassEnabled(true)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            mMap!!.setMyLocationEnabled(true)
            return
        }
        hitApitogetCurrentLocation()
    }


}