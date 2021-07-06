package com.webgurus.attendanceportal.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.Customer
import com.webgurus.attendanceportal.pojo.Ordervsdelivered

class DeliveredVsReceivedAdapter(var activity: Activity , var mTopPerformerList : ArrayList<Ordervsdelivered>) : RecyclerView.Adapter<DeliveredVsReceivedAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_delivervsreceived, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(position, mTopPerformerList)
    }

    override fun getItemCount(): Int {
        return  mTopPerformerList.size
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(position: Int,  mTopPerformerList : ArrayList<Ordervsdelivered>) {
            val tv_performername = itemView.findViewById<TextView>(R.id.tv_performername)
            val tv_performertotal = itemView.findViewById<TextView>(R.id.tv_performertotal)
            val tv_receivedcount = itemView.findViewById<TextView>(R.id.tv_receivedcount)
            val tv_dispatchedcount = itemView.findViewById<TextView>(R.id.tv_dispatchedcount)
            tv_performername.setText(mTopPerformerList[position].name)
            tv_performertotal.setText(mTopPerformerList[position].countTot.toString())
            tv_receivedcount.setText(mTopPerformerList[position].receivedCount.toString())
            tv_dispatchedcount.setText(mTopPerformerList[position].dispatchedCount.toString())
        }


    }
}
