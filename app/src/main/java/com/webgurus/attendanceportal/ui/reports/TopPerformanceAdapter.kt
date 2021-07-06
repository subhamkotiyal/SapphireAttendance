package com.webgurus.attendanceportal.ui.reports

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.AgeingData
import com.webgurus.attendanceportal.pojo.data_performance


class TopPerformanceAdapter(var mtoplist: ArrayList<data_performance>) :
    RecyclerView.Adapter<TopPerformanceAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TopPerformanceAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_top_performance, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: TopPerformanceAdapter.ViewHolder, position: Int) {
        holder.bindItems(mtoplist, position)
    }

    override fun getItemCount(): Int {
        return mtoplist.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(mtoplist: ArrayList<data_performance>, position: Int) {
            val product_name = itemView.findViewById(R.id.product_name) as TextView
            val quantity_sold = itemView.findViewById(R.id.quantity_sold) as TextView
            product_name.setText(mtoplist.get(position).user)
            quantity_sold.setText(mtoplist.get(position).total_price.toString())
        }
    }
}