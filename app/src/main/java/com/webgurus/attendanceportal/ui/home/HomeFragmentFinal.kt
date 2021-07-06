package com.webgurus.attendanceportal.ui.home

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.androidnetworking.AndroidNetworking
import com.example.baseproject.utils.Utils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.webgurus.attendanceportal.InternetSettingCheck
import com.webgurus.attendanceportal.LocationPermissionCheck
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.ui.base.BaseFragment
import com.webgurus.attendanceportal.ui.database.DatabaseListenerCallback
import com.webgurus.attendanceportal.ui.database.LocationParam
import com.webgurus.network.BackgroundLocationService
import com.webgurus.network.GetDataService
import io.nlopez.smartlocation.OnLocationUpdatedListener
import io.nlopez.smartlocation.SmartLocation
import java.io.IOException
import java.util.*

class HomeFragmentFinal : BaseFragment(), OnMapReadyCallback, DatabaseListenerCallback {


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
    var gps_status = 1
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    var apiInterface: GetDataService? = null
    var isApiisInProcess: Boolean=false

    lateinit var tv_address : TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidNetworking.initialize(requireContext())
        if (!checkPermissions()) {
            requestPermissions()
        }
    }

    /**
     * Returns the current state of the permissions needed.
     */
    private fun checkPermissions(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )


        if (shouldProvideRationale) {

        } else {

            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMapView()

    }

    private fun setMapView() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_frag) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun initview() {

    }


    fun getLiveLocation(){


    }



    override fun onMapReady(p0: GoogleMap?) {
        this.mMap = p0!!
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            return
        }
    //    mMap.isMyLocationEnabled=true
//        SmartLocation.with(requireContext()).location()
//            .start(object : OnLocationUpdatedListener {
//
//                override fun onLocationUpdated(location: Location?) {
//                    val latLng = LatLng(location!!.latitude, location.longitude)
//                    if (mCurrLocationMarker != null) {
//                        mCurrLocationMarker?.remove()
//                    }
//                    updated_lat = location.latitude.toString()
//                    updated_longi = location.longitude.toString()
//                    getAddressFromLocationsssss(
//                        location.latitude,
//                        location.longitude,
//                        myContext,
//                        HomeFragmentFinal.GeocoderHandler()
//                    )
//                    val markerOptions = MarkerOptions()
//                    markerOptions.position(latLng)
//                    markerOptions.title("Current Position")
//                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
//                    mCurrLocationMarker = mMap.addMarker(markerOptions)
//                    //move map camera
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0F))
//                }
//
//            })

    }

    private class GeocoderHandler() : Handler() {

        override fun handleMessage(message: Message) {
            val locationAddress: String?
            locationAddress = when (message.what) {
                1 -> {
                    val bundle = message.data
                    bundle.getString("address")
                }
                else -> null
            }
            //  var tv_currentaddress =view?.findViewById(R.id.tv_currentaddress) as TextView
            // var tv_currentaddress =view.findViewById(R.id.tv_currentaddress) as TextView

        }
    }

    fun getAddressFromLocationsssss(latitude: Double, longitude: Double, context: Context?, handler: Handler?) {
        val thread: Thread = object : Thread() {
            override fun run() {
                val geocoder = Geocoder(context, Locale.getDefault())
                var result: String? = null
                try {
                    val addressList = geocoder.getFromLocation(
                        latitude, longitude, 1
                    )

                    if (addressList != null && addressList.size > 0) {

                        try {
                            requireActivity().runOnUiThread(java.lang.Runnable {
                                try {
                                    var address = addressList.get(0).getAddressLine(0)
                                    tv_address.text = address
                                } catch (e: Exception) {
                                    Log.e("dsf", "Unable connect to Geocoder", e)
                                }
                             //   hideProgressbar()
                            })
                        }catch (e : java.lang.Exception){

                        }

                        //   tv_address.setText(addressList[0].getAddressLine(0))
                        val address = addressList[0]
                        val sb = StringBuilder()
                        for (i in 0 until address.maxAddressLineIndex) {
                            sb.append(address.getAddressLine(i)).append("\n")
                            // tv_address.setText(address.getAddressLine(0).toString())
                        }
                        sb.append(address.locality).append("\n")
                        sb.append(address.postalCode).append("\n")
                        sb.append(address.countryName)
                        result = sb.toString()
                    }
                } catch (e: IOException) {
                    Log.e("dsf", "Unable connect to Geocoder", e)
                } finally {
                    val message = Message.obtain()
                    message.target = handler
                    if (result != null) {
                        message.what = 1
                        val bundle = Bundle()
                        result = """Latitude: $latitude Longitude: $longitude Address: $result"""
                        bundle.putString("address", result)
                        message.data = bundle
                    } else {
                        message.what = 1
                        val bundle = Bundle()
                        result = """Latitude: $latitude Longitude: $longitude Unable to get address for this lat-long."""
                        bundle.putString("address", result)
                        message.data = bundle
                    }
                    message.sendToTarget()

                }
            }
        }
        thread.start()

    }


    override fun processData(locationData: MutableList<LocationParam>?) {
        TODO("Not yet implemented")
    }
}