package com.webgurus

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.ExceptionParam
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.adapter.AddPermission2Adapter
import com.webgurus.attendanceportal.listeners.CheckBoxChangedSelectedListeners
import com.webgurus.attendanceportal.listeners.RecyclerViewRoleClickListeners
import com.webgurus.attendanceportal.pojo.DataRoleList
import com.webgurus.attendanceportal.pojo.PermissionData
import com.webgurus.attendanceportal.ui.role.AddRoleActivity

class LogsAdapter (var context : Activity , var exceptionList: ArrayList<ExceptionParam>): RecyclerView.Adapter<LogsAdapter.MyLogsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyLogsViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.item_logsfragment,
            parent,
            false
        )
        return LogsAdapter.MyLogsViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyLogsViewHolder, position: Int) {
        holder.bindItems(context,exceptionList.get(position))
    }

    override fun getItemCount(): Int {
        return exceptionList.size
    }


    class MyLogsViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView)  {


        fun bindItems( context: Activity , exception : ExceptionParam   ) {
            val tv_exceptionvalue = itemView.findViewById(R.id.tv_exceptionvalue) as TextView
            val tv_time = itemView.findViewById(R.id.tv_time) as TextView
            tv_exceptionvalue.setText(exception.exception)
            tv_time.setText(exception.time)

        }
    }

}