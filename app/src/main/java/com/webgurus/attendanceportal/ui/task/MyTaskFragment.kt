package com.webgurus.attendanceportal.ui.task


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.adapter.MyTaskAdapter
import com.webgurus.attendanceportal.pojo.SuccesAttendances
import kotlinx.android.synthetic.main.fragment_task.*
import kotlin.collections.ArrayList


class MyTaskFragment  : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerview()
    }


    private fun setRecyclerview(){
        val mAttendList : ArrayList<SuccesAttendances> = ArrayList()
        rv_tasklist.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        val adapter = MyTaskAdapter(mAttendList,requireActivity())
        rv_tasklist.adapter = adapter
    }

}