package com.webgurus.attendanceportal.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.Datar
import com.webgurus.attendanceportal.pojo.PaymentPending
import kotlinx.android.synthetic.main.fragment_lastorders.*
import kotlinx.android.synthetic.main.fragment_lastorders.rv_lastorders
import kotlinx.android.synthetic.main.fragment_pendingamount.*

class PendingAmountFragment  : Fragment() {

    var mPendinPaymentPending: ArrayList<PaymentPending> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pendingamount, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initview()
    }


    private fun initview() {
        mPendinPaymentPending.clear()
        val strtext = requireArguments().getParcelableArrayList<Datar>("lastorder") as ArrayList<Datar>
        var postion=requireArguments().getInt("pos")
        if(strtext.get(postion).payment_pending.size>0){
            mPendinPaymentPending.addAll(strtext[postion].payment_pending)
            tv_nodataavilable.visibility=View.GONE
        }else{
            tv_nodataavilable.visibility=View.VISIBLE
        }
        rv_pendingamount.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        val adapter = PendingAmountAdapter( requireActivity(),mPendinPaymentPending)
        rv_pendingamount.adapter = adapter
    }
}