package com.webgurus.attendanceportal.ui.home

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.*
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.Fragment
import com.androidnetworking.AndroidNetworking
import com.example.baseproject.utils.Utils
import com.example.baseproject.utils.Utils.checkLocation
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.robertohuertas.endless.Actions
import com.robertohuertas.endless.ServiceState
import com.robertohuertas.endless.getServiceState
import com.robertohuertas.endless.log
import com.webgurus.DeleteTempLocationData
import com.webgurus.GetLocationDataTempForNow
import com.webgurus.attendanceportal.*
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.demo.LocationCustomArray
import com.webgurus.attendanceportal.pojo.MarkAttendancePojo
import com.webgurus.attendanceportal.pojo.NewLocationPojo
import com.webgurus.attendanceportal.pojo.TrackerStatus
import com.webgurus.attendanceportal.ui.database.*
import com.webgurus.network.BackgroundLocationService
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import com.webgurus.newservice.MyService6666
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(), OnMapReadyCallback, DatabaseListenerCallback {

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
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34

    lateinit var dialog: Dialog
    lateinit var remark : EditText

    var gps_status = 1
    var apiInterface: GetDataService? = null


    companion object {
        const val MY_PERMISSIONS_REQUEST_LOCATION = 99

    }

    lateinit var tv_address: TextView
    internal var mLocationCallback: LocationCallback = object : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult) {
            getUpdatedLocation(locationResult)
        }
    }


    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        myContext = context

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
            getAddressFromLocationsssss(
                location.latitude,
                location.longitude,
                myContext,
                GeocoderHandler()
            )
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            markerOptions.title("Current Position")
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            mCurrLocationMarker = mMap.addMarker(markerOptions)
            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0F))


        }
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

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
//            Snackbar.make(
//                findViewById(R.id.activity_main),
//                R.string.permission_rationale,
//                Snackbar.LENGTH_INDEFINITE
//            )
//                .setAction(R.string.ok) { // Request permission
//                    ActivityCompat.requestPermissions(
//                        this,
//                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                        REQUEST_PERMISSIONS_REQUEST_CODE
//                    )
//                }
//                .show()
        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }


    /**
     * Returns the current state of the permissions needed.
     */


    override fun onAttach(context: Context) {
        myContext = context
        super.onAttach(context)

    }

    fun getAddressFromLocationsssss(
        latitude: Double, longitude: Double,
        context: Context?, handler: Handler?
    ) {
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
                                hideProgressbar()
                            })
                        } catch (e: java.lang.Exception) {

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
                        result =
                            """Latitude: $latitude Longitude: $longitude Unable to get address for this lat-long."""
                        bundle.putString("address", result)
                        message.data = bundle
                    }
                    message.sendToTarget()

                }
            }
        }
        thread.start()

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

    fun checkGPS(): Boolean {
        val locationManager =
            requireActivity().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setUpMapIfNeeded()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_frag) as SupportMapFragment
        mapFragment.getMapAsync(this)
        initview()
        checkBatteryStatus()
        listeners()
        val intent = Intent(requireActivity(), BackgroundLocationService::class.java)
        requireActivity().startService(intent)

        if (!Utils.isConnected(requireActivity())) {
            Toast.makeText(
                myContext,
                "No internet connection available. Please check your internet connection.",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(requireActivity(), InternetSettingCheck::class.java))
        } else if (!checkGPS()) {
            Toast.makeText(myContext, "GPS is not active.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireActivity(), LocationPermissionCheck::class.java))
        }

    }

    private fun initview() {
        sharedPreferences = requireActivity().getSharedPreferences(
            "AcessToken",
            AppCompatActivity.MODE_MULTI_PROCESS
        )
        editor = sharedPreferences!!.edit()
        tv_address = view?.findViewById(R.id.tv_currentaddress)!!
        progressBar = requireView().findViewById<View>(R.id.pv_progresshome) as ProgressBar
        hitApiToGetStatusTooth()
    }

    private fun checkBatteryStatus() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val bm: BatteryManager? =
                requireActivity().getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            battery_percentage = bm!!.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } else {
            val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryStatus = requireContext().registerReceiver(null, iFilter)
            val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
            battery_percentage = level / scale
        }
    }

    private fun showProgressbar() {
        progressBar!!.visibility = View.VISIBLE
    }

    private fun hideProgressbar() {
        progressBar!!.visibility = View.INVISIBLE
    }

    @SuppressLint("SimpleDateFormat")
    private fun listeners() {
        btn_timein.setOnClickListener {
            if (!cb_confirm.isChecked) {
                Toast.makeText(myContext, "Please confirm your location.", Toast.LENGTH_SHORT)
                    .show()
            } else if (isAlreadySessioned) {
                Toast.makeText(myContext, "Already time in.", Toast.LENGTH_SHORT).show(  )
            } else {
                val currentime = Utils.getCurrentDateandTime()
                val mydates = SimpleDateFormat("yyyy-MM-dd H:mm:ss").parse(currentime)
                val df: DateFormat = SimpleDateFormat("H:mm a")
                val myDateStr = df.format(mydates!!)
                btn_timein.setText(myDateStr)
                Handler().postDelayed({
                    if (btn_timein != null) {
                        btn_timein.setText(resources.getString(R.string.timein))
                    }
                }, 5000)
                staus = "1"
                hitTimeInAPI(staus)
            }

        }
        btn_timeout.setOnClickListener {
            if (!cb_confirm.isChecked) {
                Toast.makeText(
                    myContext,
                    resources.getString(R.string.pleaseconfirmlocation),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (isAlreadyTimeOut) {
                Toast.makeText(
                    myContext,
                    resources.getString(R.string.alreadytimeout),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                staus = "2"
                val currentTime = Utils.getCurrentDateandTime()
                val datess = SimpleDateFormat("yyyy-MM-dd H:mm:ss").parse(currentTime)
                val df: DateFormat = SimpleDateFormat("H:mm a")
                val myDateStr = df.format(datess)
                btn_timeout.setText(myDateStr)
                Handler().postDelayed({
                    if (btn_timeout != null) {
                        btn_timeout.setText(resources.getString(R.string.timeout))
                    }

                }, 5000)
                showAlerttoAdd()
            }

        }
    }

    private fun showAlerttoAdd() {
        dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.getWindow()!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.setContentView(R.layout.custom_layout_remark)
        remark = dialog.findViewById<EditText>(R.id.ed_remarks)
        var btn_submitremark = dialog.findViewById<Button>(R.id.btn_submitremark)
        btn_submitremark.setOnClickListener(View.OnClickListener {
            if (remark.text.toString().equals("")) {
                Toast.makeText(myContext, "Remark is mandatory to fill.", Toast.LENGTH_LONG).show()
            } else {
                hitApitoUploadLatLong(remark.text.toString())
                dialog.dismiss()
            }
        })
        dialog.show()
    }

    private fun hitApitoUploadLatLong(remark: String) {
        showProgressbar()
        GetLocationDataTempForNow(requireContext(), this).execute()
    }

    override fun processData(locationData: MutableList<LocationParam>?) {
        if (locationData!!.size > 0) {
            hitApitoBulkEnter(locationData)
        }else{
            hitTimeOutAPI(staus, remark.text.toString())
        }
    }


    private fun hitApitoBulkEnter(location: List<LocationParam>) {
        if (!com.webgurus.attendanceportal.demo.Utils.isNetworkConnected(requireContext())) {
            Toast.makeText(
                requireContext(),
                "No internet connection available. Please check your internet connection.",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(requireContext(), InternetSettingCheck::class.java))
            return
        }
        val settings = requireContext().getSharedPreferences("AcessToken", Service.MODE_PRIVATE)
        val access_token = settings.getString("Access_Token", "")
        val user_id = settings.getInt("User_ID", 0).toString()
        var attendance_id = settings.getString("Attendance_ID", "")
        val currentDate =
            SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(Date())
        val mTempList = ArrayList<LocationCustomArray>()
        mTempList.clear()
        Log.d("Location sizess : ", location.size.toString())
        if (location.size > 0) {
            for (i in location.indices) {
                if (!location[i].synczed) {
                    val locationManager =
                        requireContext().getSystemService(Service.LOCATION_SERVICE) as LocationManager
                    gps_status =
                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            1
                        } else {
                            0
                        }
                    val currentDate =
                        SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(
                            Date()
                        )
                    Log.d("currentDate and time : ", currentDate.toString())
                    val locationCustomArray = LocationCustomArray(
                        location[i].lattitude.toString(),
                        location[i].longitude.toString(),
                        currentDate.toString(),
                        user_id,
                        attendance_id,
                        getBatteryPercentage(requireContext()).toString(),
                        "2.00",
                        gps_status
                    )
                    mTempList.add(locationCustomArray)
                }
            }
            val locationCustomArray = LocationCustomArray()
            val jsonLatLong = locationCustomArray.changeListToJson(mTempList)
            readWebPageBulkTest(jsonLatLong, location, this)
        }else{
            Toast.makeText(requireContext(),"All Uploaded Sucessfully",Toast.LENGTH_SHORT).show()
        }
    }


    private fun readWebPageBulkTest(
        location: String,
        mTemp: List<LocationParam>,
        listsners: DatabaseListenerCallback
    ) {
        if (!com.webgurus.attendanceportal.demo.Utils.isNetworkConnected(requireContext())) {
            Toast.makeText(
                requireContext(),
                "No internet connection available. Please check your internet connection.",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(requireContext(), InternetSettingCheck::class.java))
            return
        }
        val settings = requireContext().getSharedPreferences("AcessToken", Service.MODE_PRIVATE)
        val access_token = settings.getString("Access_Token", "")
        val user_id = settings.getInt("User_ID", 0).toString()
        var attendance_id = settings.getString("Attendance_ID", "")
        apiInterface = APIClient.getClient().create(GetDataService::class.java)
        if (attendance_id == "") {
            attendance_id = "0"
        }
        if (user_id != "") {
            val call: Call<NewLocationPojo> = apiInterface!!.updateLatLongforUserIDBulkTemp(
                "Bearer $access_token",
                location
            )
            Log.e("JsonLocation  :", location)
            call.enqueue(object : Callback<NewLocationPojo> {
                override fun onResponse(
                    call: Call<NewLocationPojo>,
                    response: Response<NewLocationPojo>
                ) {
                    try {
                        if (response.body()!!.status == "success") {
                            Log.e("Api response :", response.body()!!.status)
                            Toast.makeText(
                                requireActivity(),
                                "Final Location Saved",
                                Toast.LENGTH_SHORT
                            ).show()
                            updatelatlong(location, mTemp)
                            GetLocationDataTempForNow(requireContext(), listsners).execute()
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                "" + response.body()!!.status.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            LocalDatabaseForException(
                                requireActivity(),
                                "",
                                "Service Destroyed"
                            ).execute()
                        }
                    } catch (e: java.lang.Exception) {
                        Toast.makeText(requireActivity(), "" + e.toString(), Toast.LENGTH_SHORT)
                            .show()
                        Log.e("ServerSam :", e.toString())
                        LocalDatabaseForException(
                            requireActivity(),
                            "",
                            "Service Destroyed"
                        ).execute()
                    }
                }

                override fun onFailure(call: Call<NewLocationPojo>, t: Throwable) {
                    Log.e("ServerSam :", t.toString())
                    log("The service has been destroyed".toUpperCase())
                    val calendar = Calendar.getInstance()
                    val mdformat = SimpleDateFormat("HH:mm:ss")
                    val strDate = mdformat.format(calendar.time)
                    LocalDatabaseForException(
                        requireActivity(),
                        strDate,
                        "Api Error :" + t.toString()
                    ).execute()
                }
            })
        }

    }


    private fun updatelatlong(location: String, mListparam: List<LocationParam>) {
        val locationCustomArray = LocationCustomArray()
        val mTempList = locationCustomArray.changeStringintoList(location)

        class UpdateTask : AsyncTask<Void?, Void?, Void?>() {
            override fun onPostExecute(aVoid: Void?) {
                super.onPostExecute(aVoid)
                getPostFinal()
            }

            override fun doInBackground(vararg params: Void?): Void? {
                for (i in mTempList.indices) {
                    mListparam[i].synczed = true
                    DatabaseClientTemp.getInstance(requireContext()).appDatabase
                        .locationDao()
                        .update(mListparam[i])
                }
                return null
            }
        }

        val gt = UpdateTask()
        gt.execute()
    }


    private fun getPostFinal() {

        class GetTasks : AsyncTask<Void?, Void?, List<LocationParam>>() {

            override fun onPostExecute(tasks: List<LocationParam>) {
                super.onPostExecute(tasks)
                DeleteTempLocationData(requireContext()).execute()
                MyService6666.GetLocationData22().execute()
                // testDataAfterDeleteion();
            }

            override fun doInBackground(vararg params: Void?): List<LocationParam> {
                val taskList = DatabaseClient
                    .getInstance(requireContext())
                    .appDatabase
                    .locationDao()
                    .all
                Log.e("Database after updation", taskList.toString())
                return taskList
            }
        }

        val gt = GetTasks()
        gt.execute()
    }


    fun getBatteryPercentage(context: Context): Int {
        return if (Build.VERSION.SDK_INT >= 21) {
            val bm = context.getSystemService(Service.BATTERY_SERVICE) as BatteryManager
            bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } else {
            val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryStatus = context.registerReceiver(null, iFilter)
            val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
            val batteryPct = level / scale.toDouble()
            (batteryPct * 100).toInt()
        }
    }


    private fun startTracking() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mTracking = true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        this.mMap = p0!!
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 5000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (!Utils.checkLocationEnable(requireActivity())) {
                    checkLocation(requireActivity())
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
            if (!Utils.checkLocationEnable(requireActivity())) {
                checkLocation(requireActivity())
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
                requireContext(),
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Location permission needed")
                    .setMessage("This app needs the location permission. Please accept to use location functionality.")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_REQUEST_LOCATION
                        )
                    }
                    .create()
                    .show()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        mFusedLocationClient?.requestLocationUpdates(
                            mLocationRequest,
                            mLocationCallback,
                            Looper.myLooper()
                        )
                        mMap.isMyLocationEnabled = true
                    }
                } else {
                    Toast.makeText(myContext, "Permission denied.", Toast.LENGTH_LONG).show()
                }
                return
            }

            PERMISSION_REQUEST_CODE -> {
                if (requestCode == PERMISSION_REQUEST_CODE) {
                    if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        startTracking()
                    }
                }
            }
        }
    }

    /*companion object {
        const val MY_PERMISSIONS_REQUEST_LOCATION = 99
    }*/

    //API Call
    private fun hitTimeInAPI(status: String) {

        if (!Utils.isConnected(requireActivity())) {
            Toast.makeText(
                myContext,
                "No internet connection available. Please check your internet connection.",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(requireActivity(), InternetSettingCheck::class.java))
        } else if (!checkGPS()) {
            Toast.makeText(myContext, "GPS is not active.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireActivity(), LocationPermissionCheck::class.java))
        } else {

            showProgressbar()
            val service =
                RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
            val call = service.markAttendanceForTimeIn(
                "Bearer " + sharedPreferences!!.getString("Access_Token", ""),
                updated_lat,
                updated_longi,
                Utils.getCurrentDateandTime(),
                sharedPreferences!!.getInt(
                    "User_ID",
                    0
                ).toString(),
                status, battery_percentage.toString()
            )
            call.enqueue(object : retrofit2.Callback<MarkAttendancePojo?> {
                override fun onResponse(
                    call: Call<MarkAttendancePojo?>?,
                    response: Response<MarkAttendancePojo?>?
                ) {

                    hideProgressbar()
                    try {
                        Toast.makeText(
                            myContext,
                            "Time in successfully.",
                            Toast.LENGTH_SHORT
                        ).show()
                        if (response!!.body()!!.person_in == 1) {
                            btn_timein.setBackgroundColor(resources.getColor(R.color.grey))
                            btn_timeout.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                            isAlreadySessioned = true
                            isAlreadyTimeOut = false
                        } else {
                            btn_timein.setBackgroundColor(resources.getColor(R.color.grey))
                            btn_timeout.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                            isAlreadySessioned = false
                            isAlreadyTimeOut = true
                        }
                        editor!!.putString(
                            "Attendance_ID",
                            response.body()!!.location.attendance_id.toString()
                        )
                        editor!!.commit()
                        editor!!.apply()
                        staus = ""

                    } catch (e: Exception) {
                    }

                }

                override fun onFailure(call: Call<MarkAttendancePojo?>, t: Throwable) {
                    staus = ""
                    hideProgressbar()
                }
            })
        }

    }

    private fun actionOnService(action: Actions) {
        if (getServiceState(requireContext()) == ServiceState.STOPPED && action == Actions.STOP) return
        Intent(requireActivity(), MyService6666::class.java).also {
            it.action = action.name
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                log("Starting the service in >=26 Mode")
                startForegroundService(requireActivity(), it)
                return
            }
            log("Starting the service in < 26 Mode")
            requireActivity().startService(it)
        }
    }


    private fun hitTimeOutAPI(status: String, remark: String) {

        if (!Utils.isConnected(requireActivity())) {
            Toast.makeText(
                myContext,
                "No internet connection available. Please check your internet connection.",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(requireActivity(), InternetSettingCheck::class.java))
        } else if (!checkGPS()) {
            Toast.makeText(myContext, "GPS is not active.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireActivity(), LocationPermissionCheck::class.java))
        } else {
            //showProgressbar()
            val currentDateAndTime = Utils.getCurrentDateandTime()
            val service =
                RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
            val call = service.markAttendanceForTimeOut(
                "Bearer " + sharedPreferences!!.getString("Access_Token", ""),
                updated_lat, updated_longi, currentDateAndTime, sharedPreferences!!.getInt(
                    "User_ID",
                    0
                ).toString(),
                status, battery_percentage.toString(), sharedPreferences!!.getString(
                    "Attendance_ID",
                    ""
                ), remark
            )
            call.enqueue(object : retrofit2.Callback<MarkAttendancePojo?> {
                override fun onResponse(
                    call: Call<MarkAttendancePojo?>?,
                    response: Response<MarkAttendancePojo?>?
                ) {

                    hideProgressbar()
                    Log.d("Exception Out :", response!!.body().toString())
                    try {
                        editor!!.remove("Attendance_ID")
                        editor!!.commit()
                        staus = ""
                        progressBar!!.visibility = View.INVISIBLE
                        if (response!!.body()!!.person_in == 2) {
                            btn_timeout.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                            btn_timein.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                            isAlreadySessioned = false
                            isAlreadyTimeOut = true
                        } else {
                            btn_timeout.setBackgroundColor(resources.getColor(R.color.grey))
                        }
                        Toast.makeText(
                            myContext,
                            "Time Out successfully.",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                        actionOnService(Actions.STOP)


                    } catch (e: Exception) {
                        Toast.makeText(
                            myContext,
                            resources.getString(R.string.servernotrespond),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<MarkAttendancePojo?>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.servernotrespond),
                        Toast.LENGTH_SHORT
                    ).show()
                    staus = ""
                    hideProgressbar()
                }
            })
        }
    }


    private fun hitApiToGetStatusTooth() {

        if (!Utils.isConnected(requireActivity())) {
            Toast.makeText(
                myContext,
                "No internet connection available. Please check your internet connection.",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(requireActivity(), InternetSettingCheck::class.java))
        } else if (!checkGPS()) {
            Toast.makeText(myContext, "GPS is not active.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireActivity(), LocationPermissionCheck::class.java))
        } else {
            showProgressbar()
            val service =
                RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
            val call = service.getTrackerStatus(
                "Bearer " + sharedPreferences!!.getString(
                    "Access_Token",
                    ""
                )
            )
            call.enqueue(object : retrofit2.Callback<TrackerStatus?> {
                override fun onResponse(
                    call: Call<TrackerStatus?>?,
                    response: Response<TrackerStatus?>?
                ) {

                    try {
                        Log.e("Jsonsdf", response!!.body().toString())

                        if (response!!.code() == 404) {
                            Toast.makeText(
                                myContext,
                                "Please mark your today's attendance.",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            isAlreadySessioned = false
                            btn_timein.setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.colorPrimary
                                )
                            )
                            btn_timeout.setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.colorPrimary
                                )
                            )

                        } else {
                            if (response.body()!!.`in`.equals("1")) {
                                btn_timein.setBackgroundColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.grey
                                    )
                                )
                                isAlreadySessioned = true
                                editor!!.putString(
                                    "Attendance_ID",
                                    response.body()!!.id
                                )
                                editor!!.commit()

                            } else if (response!!.body()!!.`in`.equals("2")) {
                                btn_timein.setBackgroundColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.colorPrimary
                                    )
                                )
                                btn_timeout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                                isAlreadySessioned = false
                                isAlreadyTimeOut = false
                            }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(myContext, resources.getString(R.string.servernotrespond), Toast.LENGTH_SHORT).show()
                        hideProgressbar()
                    }


                }

                override fun onFailure(call: Call<TrackerStatus?>, t: Throwable) {
                    Toast.makeText(
                        myContext,
                        resources.getString(R.string.servernotrespond),
                        Toast.LENGTH_SHORT
                    ).show()
                    hideProgressbar()
                }
            })
        }
    }


}
