package com.webgurus.attendanceportal.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.adapter.CustomerLisitingAdapter
import kotlinx.android.synthetic.main.fragment_customers.*
import kotlinx.android.synthetic.main.fragment_customers.rv_curomerlisitng
import kotlinx.android.synthetic.main.fragment_lastorder.*

class LastOrderFragment  : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lastorder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniview()
    }


    private fun iniview() {
        rv_lastorder.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
//        val adapter = CustomerLisitingAdapter( requireActivity())
//        rv_lastorder.adapter = adapter
    }




}