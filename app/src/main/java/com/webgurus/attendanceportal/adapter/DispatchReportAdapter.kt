package com.webgurus.attendanceportal.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.DispatchOrder


class DispatchReportAdapter(var mContext:Activity , var mDispatchOrder: ArrayList<DispatchOrder>) : RecyclerView.Adapter<DispatchReportAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_dispatch_report, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(position,mDispatchOrder)
    }

    override fun getItemCount(): Int {
        return  1
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems( position: Int, mDispatchOrder: ArrayList<DispatchOrder>) {
            val tv_dispatchorderid = itemView.findViewById(R.id.tv_dispatchorderid) as TextView
            val tv_partydispatchname = itemView.findViewById(R.id.tv_partydispatchname) as TextView
            val tv_dispatchtotal = itemView.findViewById(R.id.tv_dispatchtotal) as TextView
            val tv_dispatchpaymentreceived = itemView.findViewById(R.id.tv_dispatchpaymentreceived) as TextView
            val tv_dispatchaddress = itemView.findViewById(R.id.tv_dispatchaddress) as TextView
            val tv_dispatchorderdate = itemView.findViewById(R.id.tv_dispatchorderdate) as TextView
            val tv_dispatchlasttransactiondate = itemView.findViewById(R.id.tv_dispatchlasttransactiondate) as TextView
            val tv_dispatchremark = itemView.findViewById(R.id.tv_dispatchremark) as TextView
            tv_dispatchorderid.setText(mDispatchOrder.get(position).id.toString())
            tv_partydispatchname.setText(mDispatchOrder.get(position).organisation_name)
            tv_dispatchtotal.setText(mDispatchOrder.get(position).total_price.toString())
            tv_dispatchpaymentreceived.setText(mDispatchOrder.get(position).payment_received.toString())
            tv_dispatchaddress.setText(mDispatchOrder.get(position).address.toString())
            tv_dispatchorderdate.setText(mDispatchOrder.get(position).order_date.toString())
            tv_dispatchorderdate.setText(mDispatchOrder.get(position).order_date.toString())
            tv_dispatchlasttransactiondate.setText(mDispatchOrder.get(position).last_transaction.toString())
            tv_dispatchremark.setText(mDispatchOrder.get(position).remarks)

        }
    }
    }
