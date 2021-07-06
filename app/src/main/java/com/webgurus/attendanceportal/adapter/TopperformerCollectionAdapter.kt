package com.webgurus.attendanceportal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.DataPayment
import com.webgurus.attendanceportal.pojo.data_performance
import com.webgurus.attendanceportal.ui.reports.TopPerformanceAdapter

class TopperformerCollectionAdapter(var mtoplist: ArrayList<DataPayment>) :
    RecyclerView.Adapter<TopperformerCollectionAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopperformerCollectionAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_topperformercollection, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: TopperformerCollectionAdapter.ViewHolder, position: Int) {
        holder.bindItems(mtoplist, position)
    }

    override fun getItemCount(): Int {
        return mtoplist.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(mtoplist: ArrayList<DataPayment>, position: Int) {
            val product_name = itemView.findViewById(R.id.product_namecoll) as TextView
            val quantity_sold = itemView.findViewById(R.id.quantity_soldcoll) as TextView
            product_name.setText(mtoplist.get(position).user)
            quantity_sold.setText(mtoplist.get(position).count.toString())
        }
    }
}