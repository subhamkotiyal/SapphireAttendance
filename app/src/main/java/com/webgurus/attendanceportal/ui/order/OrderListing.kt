package com.webgurus.attendanceportal.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.adapter.ProductListAdapter
import com.webgurus.attendanceportal.pojo.OrdersVariant
import com.webgurus.attendanceportal.ui.base.BaseFragment


class OrderListing : BaseFragment() {

    lateinit var productListAdapter: ProductListAdapter
    lateinit var rv_productlisting: RecyclerView

    var mOrderListing : ArrayList<OrdersVariant> = ArrayList()
    var customername:String=""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_orderlist, container, false);
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            mOrderListing= bundle.getParcelableArrayList("orderlisting")!!
            customername= bundle.getString("customername")!!
        }
        val dsf=""
        val fdsdsf=""
        initview()


    }

    private fun initview() {
        rv_productlisting=requireView().findViewById(R.id.rv_productlisting)
        rv_productlisting.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        productListAdapter =
            ProductListAdapter(requireActivity(),mOrderListing,customername)
        rv_productlisting.adapter = productListAdapter
    }

}