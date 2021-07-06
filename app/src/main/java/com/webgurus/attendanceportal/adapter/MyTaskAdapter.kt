package com.webgurus.attendanceportal.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.SuccesAttendances
import com.webgurus.attendanceportal.ui.commets.CommentsViews
import com.webgurus.attendanceportal.ui.task.TaskDetailsActivity
import com.webgurus.data.CommentPojo

class MyTaskAdapter (val commentList: ArrayList<SuccesAttendances>, context: Activity) : RecyclerView.Adapter<MyTaskAdapter.ViewHolder>() {


    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTaskAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_mytask, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: MyTaskAdapter.ViewHolder, position: Int) {
        holder.bindItems()
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return 10
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems() {
            val ll_roottask = itemView.findViewById(R.id.ll_roottask) as LinearLayout
            ll_roottask.setOnClickListener(View.OnClickListener {
                itemView.context.startActivity(Intent(itemView.context, TaskDetailsActivity::class.java))
            })
        }
    }

}
