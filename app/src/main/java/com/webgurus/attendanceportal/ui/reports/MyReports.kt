package com.webgurus.attendanceportal.ui.reports

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_reports.*



class MyReports  : BaseFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reports, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initview()
    }

    private fun initview() {
        ll_users.setOnClickListener {
            startActivity(Intent(requireActivity(), MyUserReports::class.java))
        }
        ll_orders.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireActivity(), MyOrderReportsActivity::class.java))
        })
        ll_attendance.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireActivity(), MyAttendanceFragment::class.java))
        })

        ll_customer.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireActivity(), MyCustomerReports::class.java))
        })
        ll_ageingReport.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireActivity(), AgeingReport::class.java))
        })

        ll_topPerformance.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireActivity(), TopPerformance::class.java))
        })

        ll_dispatchReport.setOnClickListener(View.OnClickListener {
           startActivity(Intent(requireActivity(), DispatchReport::class.java))
        })
        ll_itemReport.setOnClickListener(View.OnClickListener {
             startActivity(Intent(requireActivity(), ItemReprt::class.java))
        })
        ll_targetvsachieved.setOnClickListener(View.OnClickListener {

        startActivity(Intent(requireActivity(), Target_vs_achieved::class.java))
        })


        ll_deliver_vs_received.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireActivity(), DeliveredVsReceived::class.java))
        })

        ll_customermapping.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireActivity(), CustomerMappingActivity::class.java))
        })
    }


}