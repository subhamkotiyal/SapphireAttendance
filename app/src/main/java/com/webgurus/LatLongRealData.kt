package com.webgurus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.ui.base.BaseFragment
import com.webgurus.attendanceportal.ui.database.DatabaseListenerCallback
import com.webgurus.attendanceportal.ui.database.GetLocationData
import com.webgurus.attendanceportal.ui.database.GetLocationDataTemp
import com.webgurus.attendanceportal.ui.database.LocationParam
import com.webgurus.newservice.LatLongAdapter

class LatLongRealData : BaseFragment() , DatabaseListenerCallback {

    lateinit var latLongAdapter: LatLongAdapter
    lateinit var rv_latlong: RecyclerView
    var mLatlocation: ArrayList<LocationParam> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lalongtest, container, false);
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyckerview()
    }

    fun setRecyckerview(){
        rv_latlong=requireView().findViewById(R.id.rv_latlong)
        rv_latlong.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false
        )
        latLongAdapter = LatLongAdapter(requireActivity(), mLatlocation)
        rv_latlong.adapter = latLongAdapter
        GetLocationData(requireContext(), this).execute()
    }

    override fun processData(location: MutableList<LocationParam>?) {
        requireActivity().runOnUiThread(Runnable {
            mLatlocation.clear()
            mLatlocation.addAll(location!!)
            latLongAdapter.notifyDataSetChanged()
        })


    }


}