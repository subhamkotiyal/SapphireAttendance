package com.webgurus.attendanceportal.ui.customer

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.adapter.LastOrderAdapter
import com.webgurus.attendanceportal.pojo.SuccesAttendances

class NewOrderAdapter (var context: Activity) :  RecyclerView.Adapter<NewOrderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewOrderAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.item_neworder,
            parent,
            false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: NewOrderAdapter.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 20
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(user: SuccesAttendances) {

        }
    }


}