package com.webgurus.attendanceportal.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.listeners.RecyclerViewClickListeners
import com.webgurus.attendanceportal.listeners.RecyclerViewRoleClickListeners
import com.webgurus.attendanceportal.pojo.DataRoleList
import com.webgurus.attendanceportal.pojo.DataUser
import com.webgurus.attendanceportal.ui.role.AddRoleActivity

class RoleListingAdapter (var context: Activity,var mRoleList : ArrayList<DataRoleList>?,var recyclerviewListeners : RecyclerViewRoleClickListeners) : RecyclerView.Adapter<RoleListingAdapter.ViewHolder>() {


    var filterList = ArrayList<DataRoleList>()
    // exampleListFull . exampleList

    init {
        filterList = mRoleList as ArrayList<DataRoleList>
    }


    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleListingAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_role, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: RoleListingAdapter.ViewHolder, position: Int) {
        holder.bindItems(filterList!!.get(position),position,recyclerviewListeners,context)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return filterList!!.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(rolelist: DataRoleList,position: Int,recyclerviewListeners:RecyclerViewRoleClickListeners,context: Context) {
            val tv_sno = itemView.findViewById(R.id.tv_sno) as TextView
            val tv_rolename = itemView.findViewById(R.id.tv_rolename) as TextView
            val iv_delete = itemView.findViewById(R.id.iv_delete) as ImageView
            val iv_updaterole = itemView.findViewById(R.id.iv_updaterole) as ImageView
            tv_rolename.setText(rolelist.role)
            tv_sno.setText("S.No "+(position+1).toString())
            iv_delete.setOnClickListener(View.OnClickListener {
                recyclerviewListeners.onClick(position,rolelist.id )
            })
            iv_updaterole.setOnClickListener(View.OnClickListener {
                val intent = Intent(context, AddRoleActivity::class.java)
                intent.putExtra("roleID", rolelist.id)
                intent.putExtra("rolename", rolelist.role)
                context.startActivity(intent)
            })

        }
    }
    fun filterList(filteredList: ArrayList<DataRoleList>) {
        filterList = filteredList
        notifyDataSetChanged()
    }
}