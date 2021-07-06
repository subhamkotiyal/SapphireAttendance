package com.webgurus.attendanceportal.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.SuccesAttendances
import com.webgurus.attendanceportal.ui.commets.CommentsViews
import com.webgurus.utils.LocationAddress
import java.text.DateFormat
import java.text.SimpleDateFormat

class LastOrderAdapter :  RecyclerView.Adapter<LastOrderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastOrderAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.item_lastorder,
            parent,
            false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: LastOrderAdapter.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 10
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(user: SuccesAttendances) {

        }
    }


}