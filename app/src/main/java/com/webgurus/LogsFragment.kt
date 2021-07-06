package com.webgurus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.DatabaseExceptionListenerCallback
import com.webgurus.attendanceportal.ExceptionParam
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.ui.base.BaseFragment

class LogsFragment  : BaseFragment() , DatabaseExceptionListenerCallback {

    lateinit var rv_logs: RecyclerView
    lateinit var logsAdapter: LogsAdapter
    var exceptionList: ArrayList<ExceptionParam> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_logsfragment, container, false);
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyckerview()
    }

    fun setRecyckerview(){
        rv_logs=view!!.findViewById(R.id.rv_logs)
        rv_logs.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false
        )
        logsAdapter = LogsAdapter(requireActivity(), exceptionList)
        rv_logs.adapter = logsAdapter
        GetExceptionData(requireContext(), this).execute()
    }

    override fun getData(locationData: MutableList<ExceptionParam>?) {
        requireActivity().runOnUiThread(Runnable {
            exceptionList.clear()
            exceptionList.addAll(locationData!!)
            logsAdapter.notifyDataSetChanged()
        })
    }


}