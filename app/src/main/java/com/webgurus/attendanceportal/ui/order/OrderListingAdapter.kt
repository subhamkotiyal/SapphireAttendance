package com.webgurus.attendanceportal.ui.order

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.adapter.AttendanceAdapter
import com.webgurus.attendanceportal.listeners.RecyclerViewClickListenersForOrder
import com.webgurus.attendanceportal.listeners.RecyclerViewRoleClickListeners
import com.webgurus.attendanceportal.pojo.OrderData
import com.webgurus.attendanceportal.pojo.OrderVariant
import com.webgurus.attendanceportal.pojo.SuccesAttendances
import com.webgurus.attendanceportal.ui.commets.CommentsViews
import com.webgurus.attendanceportal.ui.trackuer.TrackUserDailyActivity
import com.webgurus.utils.LocationAddress
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.ArrayList

class OrderListingAdapter  (val context: Activity,val  morderList: ArrayList<OrderData>? ,
                            val mListeners : RecyclerViewRoleClickListeners, val mOrderListeners : RecyclerViewClickListenersForOrder
) : RecyclerView.Adapter<OrderListingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListingAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.item_orderlisting,
            parent,
            false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(morderList!!.get(position),position,mListeners,morderList,mOrderListeners)
    }

    override fun getItemCount(): Int {
        return morderList!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(orderdata: OrderData,position: Int,mListeners : RecyclerViewRoleClickListeners,morderList: ArrayList<OrderData>?,  mOrderListeners:RecyclerViewClickListenersForOrder) {
            val tv_clientname = itemView.findViewById(R.id.tv_clientname) as TextView
            val tv_totalprice = itemView.findViewById(R.id.tv_totalprice) as TextView
            val tv_receivedpayment = itemView.findViewById(R.id.tv_receivedpayment) as TextView
            val tv_pendingpayment = itemView.findViewById(R.id.tv_pendingpayment) as TextView
            val tv_status = itemView.findViewById(R.id.tv_status) as TextView
            val ll_view = itemView.findViewById(R.id.ll_view) as LinearLayout
            val ll_remove = itemView.findViewById(R.id.ll_remove) as LinearLayout
            val ll_addpayment = itemView.findViewById(R.id.ll_addpayment) as LinearLayout
            val ll_status = itemView.findViewById(R.id.ll_status) as LinearLayout
            val ll_return = itemView.findViewById(R.id.ll_return) as LinearLayout
            tv_clientname.setText(orderdata.customer_name.toString())
            tv_totalprice.setText("Rs. "+orderdata.total_price.toString())
            tv_receivedpayment.setText("Rs. "+orderdata.payment_received.toString())
            tv_pendingpayment.setText("Rs. "+orderdata.payment_pending.toString())
            if(orderdata.status.toString().equals("1")){
                tv_status.setText("Received")
            }else if(orderdata.status.toString().equals("2")){
                tv_status.setText("Approved")
            }else if(orderdata.status.toString().equals("3")){
                tv_status.setText("Dispatched")
            }else if(orderdata.status.toString().equals("4")){
                tv_status.setText("Delivered")
            }else if(orderdata.status.toString().equals("5")){
                tv_status.setText("Cancelled")
            }else{
                tv_status.setText("New Order")
            }

            ll_view.setOnClickListener(View.OnClickListener {
                var intent = Intent(itemView.context, CustomerOrderListingActivity::class.java)
                 intent.putExtra("order_id",orderdata.id)
                itemView.context.startActivity(intent)
            })
            ll_remove.setOnClickListener(View.OnClickListener {
                mListeners.onClick(position,orderdata.id)
            })

            ll_addpayment.setOnClickListener(View.OnClickListener {
                mOrderListeners.onClick(position,orderdata.id,"payment")
            })
            ll_status.setOnClickListener(View.OnClickListener {
                mOrderListeners.onClick(orderdata.status,orderdata.id,"status")
            })

            ll_return.setOnClickListener(View.OnClickListener {
                mOrderListeners.onClick(orderdata.status,orderdata.id,"return")
            })

        }
    }



}