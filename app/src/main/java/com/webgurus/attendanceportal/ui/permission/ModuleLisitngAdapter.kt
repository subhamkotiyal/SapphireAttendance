package com.webgurus.attendanceportal.ui.permission

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.adapter.CommentsAdapter
import com.webgurus.attendanceportal.listeners.RecyclerViewClickListenersForOrder
import com.webgurus.attendanceportal.pojo.PermissionData
import com.webgurus.data.CommentPojo

class ModuleLisitngAdapter (context: Activity,val commentList: ArrayList<PermissionData>,var mListeners: RecyclerViewClickListenersForOrder) : RecyclerView.Adapter<ModuleLisitngAdapter.ViewHolder>() {


    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleLisitngAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_modulesnames, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ModuleLisitngAdapter.ViewHolder, position: Int) {
        holder.bindItems(commentList[position],position,mListeners)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return commentList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(permissonList: PermissionData,position: Int,listeners:RecyclerViewClickListenersForOrder) {
            val  tv_modulename=itemView.findViewById<TextView>(R.id.tv_modulename) as TextView
            val  rl_root=itemView.findViewById<RelativeLayout>(R.id.rl_root) as RelativeLayout
            tv_modulename.setText(permissonList.module)
            rl_root.setOnClickListener(View.OnClickListener {
                listeners.onClick(position,permissonList.id,permissonList.module)
            })


        }
    }

}
