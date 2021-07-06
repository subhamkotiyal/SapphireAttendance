package com.webgurus.attendanceportal.adapter

import android.app.Activity
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.webgurus.attendanceportal.R
import com.webgurus.data.CommentPojo

class CommentsAdapter (val commentList: ArrayList<CommentPojo>,context:Activity) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_commentview, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CommentsAdapter.ViewHolder, position: Int) {
        holder.bindItems(commentList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return commentList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(commentList: CommentPojo) {
           val  profilepiv=itemView.findViewById<ImageView>(R.id.iv_profileimage) as ImageView
           val  username=itemView.findViewById<TextView>(R.id.tv_username) as TextView
           val  content=itemView.findViewById<TextView>(R.id.tv_content) as TextView
           val  time=itemView.findViewById<TextView>(R.id.tv_time) as TextView
            username.setText(commentList.name)
            Glide.with(itemView.context).load(commentList.imageurl).into(profilepiv)
            content.setText(commentList.comment)
            time.setText(commentList.time)

        }
    }

}


