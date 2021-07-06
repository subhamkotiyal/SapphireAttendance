package com.webgurus.attendanceportal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.listeners.RecyclerViewClickListeners
import com.webgurus.attendanceportal.pojo.FieldData
import com.webgurus.attendanceportal.pojo.SuccesAttendances

class ManagersAdapter( var context:Context,var mManagerList:ArrayList<FieldData>, var mListeners: RecyclerViewClickListeners):  RecyclerView.Adapter<ManagersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManagersAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.item_managerlist,
            parent,
            false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ManagersAdapter.ViewHolder, position: Int) {
        holder.bindItems(position,mManagerList.get(position),mListeners)
    }

    override fun getItemCount(): Int {
        return mManagerList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(pos:Int,user: FieldData,listeners: RecyclerViewClickListeners) {
            val tv_managername=itemView.findViewById<TextView>(R.id.tv_managername)
            val ll_rootmanager=itemView.findViewById<LinearLayout>(R.id.ll_rootmanager)
            tv_managername.setText(user.name)
            ll_rootmanager.setOnClickListener(View.OnClickListener {
                listeners.onClick(pos)
            })

        }
    }


}