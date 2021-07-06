package com.webgurus.attendanceportal.ui.order

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.adapter.OrderTransactionAdapter
import com.webgurus.attendanceportal.adapter.ProductListAdapter
import com.webgurus.attendanceportal.pojo.OrderTransaction
import com.webgurus.attendanceportal.ui.base.BaseFragment

class OrderTransactionFragment  : BaseFragment() {


    lateinit var ordertransAdapter: OrderTransactionAdapter
    lateinit var rv_ordertransaction: RecyclerView
    var mTransaction : ArrayList<OrderTransaction> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ordertransaction, container, false);
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            mTransaction= bundle.getParcelableArrayList("orderTransaction")!!
        }
        initview()
    }

    private fun initview() {
        rv_ordertransaction=requireView().findViewById(R.id.rv_ordertransaction)
        rv_ordertransaction.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        ordertransAdapter =
            OrderTransactionAdapter(requireActivity(),mTransaction)
        rv_ordertransaction.adapter = ordertransAdapter
    }



}