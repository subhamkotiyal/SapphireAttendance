package com.webgurus.attendanceportal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R

class ItemReportAdapter2 : RecyclerView.Adapter<ItemReportAdapter2.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_item_report, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ItemReportAdapter2.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
      return 2
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
}