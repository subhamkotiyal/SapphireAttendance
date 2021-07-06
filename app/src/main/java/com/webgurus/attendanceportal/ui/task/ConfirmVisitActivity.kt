package com.webgurus.attendanceportal.ui.task

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.baseproject.utils.Utils
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.ui.home.HomeFragment
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

class ConfirmVisitActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    lateinit var mLocationRequest: LocationRequest
    var mLastLocation: Location? = null
    internal var mCurrLocationMarker: Marker? = null
    internal var mFusedLocationClient: FusedLocationProviderClient? = null
    var progressBar: ProgressBar? = null
    var sharedPreferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    var battery_percentage: Int? = null
    var staus: String = ""
    var isAlreadySessioned: Boolean = false
    var isAlreadyTimeOut: Boolean = false
    var updated_lat: String = ""
    var updated_longi: String = ""
    private val PERMISSION_REQUEST_CODE = 200
    var mTracking = false
    var myContext: Context? = null

    internal var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            getUpdatedLocation(locationResult)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_one)
        initview()
    }

    private fun initview() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment = this.getSupportFragmentManager()
            .findFragmentById(R.id.map_fragforvisit) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
//        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragforvisit) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//    }


//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        myContext = context
//    }
//
//
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_one, container, false);
//    }

    override fun onMapReady(p0: GoogleMap?) {
        this.mMap = p0!!
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 120000
        mLocationRequest.fastestInterval = 120000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (!Utils.checkLocationEnable(this)) {
                    Utils.checkLocation(this)
                }
                mFusedLocationClient?.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback,
                    Looper.myLooper()
                )
                mMap.isMyLocationEnabled = true
            } else {
                checkLocationPermission()
            }
        } else {
            if (!Utils.checkLocationEnable(this)) {
                Utils.checkLocation(this)
            }
            mFusedLocationClient?.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback,
                Looper.myLooper()
            )
            mMap.isMyLocationEnabled = true
        }
    }


    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle("Location permission needed")
                    .setMessage("This app needs the location permission. Please accept to use location functionality.")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            HomeFragment.MY_PERMISSIONS_REQUEST_LOCATION
                        )
                    }
                    .create()
                    .show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    HomeFragment.MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        }
    }


    private fun getUpdatedLocation(locationResult: LocationResult) {
        val locationList = locationResult.locations
        if (locationList.isNotEmpty()) {
            //The last location in the list is the newest
            val location = locationList.last()
            Log.i(
                "MapsActivity",
                "Location: " + location.latitude + " " + location.longitude
            )
            mLastLocation = location
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker?.remove()
            }
            //Place current location marker
            val latLng = LatLng(location.latitude, location.longitude)
            updated_lat = location.latitude.toString()
            updated_longi = location.longitude.toString()
            getAddresFromLocation(location.latitude, location.longitude)
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            markerOptions.title("Current Position")
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            mCurrLocationMarker = mMap.addMarker(markerOptions)
            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0F))
            //hitAPitoUpdateLatLong()
        }
    }

    private fun getAddresFromLocation(lattitude: Double, longitude: Double) {
        val addresses: List<Address>
        val geocoder = Geocoder(this, Locale.getDefault())
        if (geocoder != null) {
            addresses = geocoder.getFromLocation(
                lattitude,
                longitude,
                1
            )

            val address: String = addresses[0].getAddressLine(0)
            if (tv_currentaddress != null) {
                tv_currentaddress.text = address
            }
        }
    }


}