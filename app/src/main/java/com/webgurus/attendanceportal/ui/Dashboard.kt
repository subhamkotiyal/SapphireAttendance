package com.webgurus.attendanceportal.ui

import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.baseproject.utils.Utils
import com.webgurus.attendanceportal.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class Dashboard : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    var btnAttendanceMark : TextView? =null
    var btn_tasks : TextView? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    fun checkGPS(): Boolean
    {
        val locationManager = requireActivity().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_dashboard, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initview()
        listeners()
    }

    private fun listeners() {
        btnAttendanceMark?.setOnClickListener {
            if(!Utils.isConnected(requireActivity())){
                Toast.makeText(requireActivity(), "No internet connection available. Please check your internet connection.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireActivity(), InternetSettingCheck::class.java))
            }
            else if (!checkGPS()) {
                Toast.makeText(requireActivity(), "GPS is not active.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireActivity(), LocationPermissionCheck::class.java))
            }
            else
            {
                startActivity(Intent(activity, Attendance::class.java))
            }
        }
        btn_tasks?.setOnClickListener {
            if(!Utils.isConnected(requireActivity())){
                Toast.makeText(requireActivity(), "No internet connection available. Please check your internet connection.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireActivity(), InternetSettingCheck::class.java))
            }
            else if (!checkGPS()) {
                Toast.makeText(requireActivity(), "GPS is not active.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireActivity(), LocationPermissionCheck::class.java))
            }
            else
            {
                startActivity(Intent(activity, TaskActivity::class.java))
            }
        }

    }

    private fun initview() {
         btnAttendanceMark = requireView().findViewById(R.id.btnAttendanceMark)
        btn_tasks = requireView().findViewById(R.id.btn_tasks)
    }
}