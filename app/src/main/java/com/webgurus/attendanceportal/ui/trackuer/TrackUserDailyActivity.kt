package com.webgurus.attendanceportal.ui.trackuer

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.GetLatLongBydate
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_trackuerdaily.*
import retrofit2.Call
import retrofit2.Response


class TrackUserDailyActivity : AppCompatActivity(), OnMapReadyCallback {

    val POINT_A = LatLng(30.6884326, 76.7034333)

    val POINT_B = LatLng(30.6820574, 76.7223323)

    var mLatLongList: MutableList<LatLng>? = null

    var mMap: GoogleMap? = null

    var  attendanceID:String =""

    var sharedPreferences : SharedPreferences? =null
    internal var mCurrLocationMarker: Marker? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trackuerdaily)
        initview()
        listeners()
        sharedPreferences= this.getSharedPreferences(
            "AcessToken",
            AppCompatActivity.MODE_PRIVATE
        )
        val mapFragment = getSupportFragmentManager()
            .findFragmentById(R.id.map_view) as SupportMapFragment
        mapFragment.getMapAsync(this)
        hitApitoGetAllLatLong()
        //createRoute()
    }

//    private fun hitApitoGetAllLatLong() {
//        AndroidNetworking.post("http://116.193.160.116:4014/attendance/api/location/240")
//            .setTag(this)
//            .setPriority(Priority.HIGH)
//            .build()
//            .getAsObjectList(User::class.java, object : ParsedRequestListener<GetLatLongBydate> {
//                override fun onResponse(users: GetLatLongBydate) {
//                    // do anything with response
//                    val dsgd=""
//                    val dsgddgfdfgs=""
//                    val dssdfdsgd=""
//
//                }
//
//                override fun onError(anError: ANError) {
//                    // handle error
//                    val dsgd=""
//                    val dsgddgfdfgs=""
//                    val dssdfdsgd=""
//                }
//            })
//
//    }

//    private fun hitApitoGetAllLatLong() {
//        AndroidNetworking.get("http://116.193.160.116:4014/attendance/api/location/240")
//            .setPriority(Priority.LOW)
//            .build()
//            .getAsJSONArray(object : JSONArrayRequestListener {
//                override fun onResponse(response: JSONArray) {
//                    val dsg=""
//                    val dsdfgg=""
//                    val dsdfgdg=""
//                    // do anything with response
//                }
//
//                override fun onError(error: ANError) {
//                    // handle error
//                    val dsg=""
//                    val dsdfgg=""
//                    val dsdfgdg=""
//                }
//            })
//    }

    private fun listeners() {
        iv_back.setOnClickListener(View.OnClickListener {
            finish()
        })
    }


     fun initview() {
         attendanceID = intent.getStringExtra("attendance_id")!!

    }

     fun hitApitoGetAllLatLong() {
         val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
         val call = service.getLatLongByDate(
             "Bearer " + sharedPreferences!!.getString(
                 "Access_Token",
                 ""
             ),attendanceID
         )
         call.enqueue(object : retrofit2.Callback<GetLatLongBydate?> {
             override fun onResponse(
                 call: Call<GetLatLongBydate?>?,
                 response: Response<GetLatLongBydate?>?
             ) {

                 if (mLatLongList == null) {
                     mLatLongList = ArrayList()
                 } else {
                     mLatLongList!!.clear()
                 }

                 if (response!!.body()!!.location.size > 0) {

                     for (i in 0 until response!!.body()!!.location.size) {
                         mLatLongList!!.add(LatLng( response.body()!!.location[i].lat.toDouble(), response.body()!!.location[i].long.toDouble()))
                         val markerOptions = MarkerOptions()
                         markerOptions.position(mLatLongList!![i])
                         markerOptions.title("Current Position")
                         markerOptions.icon(
                             BitmapDescriptorFactory.defaultMarker(
                                 BitmapDescriptorFactory.HUE_GREEN))
                         mCurrLocationMarker = mMap!!.addMarker(markerOptions)
                         //move map camera
                         mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLongList!![i], 14.0F))
                     }
                     startAnim()
                 }else{
                     Toast.makeText(this@TrackUserDailyActivity,"No Data Found",Toast.LENGTH_SHORT).show()
                 }

             }


             override fun onFailure(call: Call<GetLatLongBydate?>?, t: Throwable?) {
             }


         })




     }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(map: GoogleMap?) {
        mMap = map
        mMap!!.setOnMapLoadedCallback {
            val builder = LatLngBounds.Builder()
            builder.include(POINT_A)
            builder.include(POINT_B)
            val bounds = builder.build()
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, 200)
            mMap!!.moveCamera(cu)
            mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15f), 2000, null)

        }
    }

     fun startAnim() {
        if (mMap != null&&mLatLongList!!.size>0) {
            MapAnimator.getInstance().animateRoute(mMap, mLatLongList)
        } else {
            Toast.makeText(getApplicationContext(), "Map not ready", Toast.LENGTH_LONG).show()
        }
    }

    fun resetAnimation(view: View?) {
        startAnim()
    }

}

