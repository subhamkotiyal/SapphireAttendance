package com.webgurus.attendanceportal.ui.order

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.OrderDetail
import com.webgurus.attendanceportal.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_ordertdetails.*

class OrderDetailsFragment  : BaseFragment() {


    var orderDetails : OrderDetail ?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ordertdetails, container, false);
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initview()
    }

    private fun initview() {

        val bundle = this.arguments
        if (bundle != null) {
            orderDetails= bundle.getParcelable<Parcelable>("orderdetails") as OrderDetail?
        }
        setData()

    }

    private fun setData() {
        tv_orderid.setText(orderDetails!!.id.toString())
        tv_customername.setText(orderDetails!!.customer_name.toString())
        tv_instruction.setText(orderDetails!!.instructions.toString())
        tv_totalorderprice.setText(orderDetails!!.total_price.toString())
        tv_addresss.setText(orderDetails!!.address.toString())
        tv_paymentreceived.setText(orderDetails!!.payment_received.toString())
        tv_paymentpending.setText(orderDetails!!.payment_pending.toString())
        if(orderDetails!!.status.toString().equals("1")){
            tv_status.setText("Received")
        }else if(orderDetails!!.status.toString().equals("2")){
            tv_status.setText("Approved")
        }else if(orderDetails!!.status.toString().equals("3")){
            tv_status.setText("Dispatched")
        }else if(orderDetails!!.status.toString().equals("4")){
            tv_status.setText("Delivered")
        }else if(orderDetails!!.status.toString().equals("5")){
            tv_status.setText("Cancelled")
        }else{
            tv_status.setText("New Order")
        }
    }


}