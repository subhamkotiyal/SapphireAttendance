package com.webgurus.attendanceportal.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.AgeingData

class AgeingReportAdapter(var mAgeinglist: ArrayList<AgeingData>) : RecyclerView.Adapter<AgeingReportAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgeingReportAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_ageing_report, parent, false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: AgeingReportAdapter.ViewHolder, position: Int) {
        holder.bindItems(mAgeinglist, position)
        Log.e("aaaaaa", "" + mAgeinglist.get(position).id)
    }

    override fun getItemCount(): Int {
        return mAgeinglist.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(mAgeinglist: ArrayList<AgeingData>, position: Int) {
            val ag_txt_id = itemView.findViewById(R.id.ag_txt_id) as TextView
            val ag_txt_customername = itemView.findViewById(R.id.ag_txt_customername) as TextView
            val ag_txt_total_price = itemView.findViewById(R.id.ag_txt_total_price) as TextView
            val ag_txt_payentReceiving = itemView.findViewById(R.id.ag_txt_payentReceiving) as TextView
            val ag_txt_address = itemView.findViewById(R.id.ag_txt_address) as TextView
            val ag_txt_orderDate = itemView.findViewById(R.id.ag_txt_orderDate) as TextView
            val ag_txt_last_transection = itemView.findViewById(R.id.ag_txt_last_transection) as TextView
            ag_txt_id.setText(mAgeinglist.get(position).id.toString())
            ag_txt_customername.setText(mAgeinglist.get(position).customer_name)
            ag_txt_total_price.setText(mAgeinglist.get(position).total_price)
            ag_txt_payentReceiving.setText(mAgeinglist.get(position).payment_received.toString())
            ag_txt_address.setText(mAgeinglist.get(position).address)
            ag_txt_orderDate.setText(mAgeinglist.get(position).date)
            if(mAgeinglist.get(position).transaction.id!=null){
                ag_txt_last_transection.setText(mAgeinglist.get(position).id.toString())
            }
//            if (mAgeinglist.get(position).id == 1) {
//                ag_txt_id.setText(mAgeinglist.get(2).id.toString())
//                ag_txt_customername.setText(mAgeinglist.get(2).)
//                ag_txt_total_price.setText(mAgeinglist.get(2).total_price.toString())
//                ag_txt_payentReceiving.setText(mAgeinglist.get(2).payment_received.toString())
//                ag_txt_address.setText(mAgeinglist.get(2).address.toString())
//                ag_txt_orderDate.setText(mAgeinglist.get(2).date)
//                ag_txt_last_transection.setText(mAgeinglist.get(2).id.toString())
//            }
//            if (mAgeinglist.get(position).id == 2) {
//                ag_txt_id.setText(mAgeinglist.get(1).id.toString())
//                ag_txt_customername.setText(mAgeinglist.get(1).customer.first_name.toString() + " " + mAgeinglist.get(1).customer.last_name.toString())
//                ag_txt_total_price.setText(mAgeinglist.get(1).total_price.toString())
//                ag_txt_address.setText(mAgeinglist.get(1).address.toString())
//                ag_txt_orderDate.setText(mAgeinglist.get(1).date)
//                ag_txt_last_transection.setText(mAgeinglist.get(1).id.toString())
//            }
//            if (mAgeinglist.get(position).id == 3) {
//                ag_txt_id.setText(mAgeinglist.get(0).id.toString())
//                ag_txt_customername.setText(mAgeinglist.get(0).customer.first_name.toString() + " " + mAgeinglist.get(0).customer.last_name.toString())
//                ag_txt_total_price.setText(mAgeinglist.get(0).total_price.toString())
//                ag_txt_payentReceiving.setText(mAgeinglist.get(0).payment_received.toString())
//                ag_txt_address.setText(mAgeinglist.get(0).address.toString())
//                ag_txt_orderDate.setText(mAgeinglist.get(0).date)
//                ag_txt_last_transection.setText(mAgeinglist.get(0).id.toString())
//            }


        }

    }
}


