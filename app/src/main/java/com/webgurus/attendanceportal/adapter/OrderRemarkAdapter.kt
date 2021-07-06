package com.webgurus.attendanceportal.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R

class OrderRemarkAdapter( var context: Activity) : RecyclerView.Adapter<OrderRemarkAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderRemarkAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_orderremark, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: OrderRemarkAdapter.ViewHolder, position: Int) {
        holder.bindItems()
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return 12
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems() {

        }
    }

}