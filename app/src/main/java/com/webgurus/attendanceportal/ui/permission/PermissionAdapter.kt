package com.webgurus.attendanceportal.ui.permission

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.listeners.RecyclerViewClickListenersForOrder
import com.webgurus.attendanceportal.listeners.RecyclerViewRoleClickListeners
import com.webgurus.attendanceportal.pojo.AllListPermissionData
import com.webgurus.attendanceportal.pojo.OrderData
import com.webgurus.attendanceportal.ui.order.CustomerOrderListingActivity
import com.webgurus.attendanceportal.ui.order.OrderListingAdapter
import java.util.ArrayList

class PermissionAdapter (val context: Activity, var mListPermissions:ArrayList<AllListPermissionData>) : RecyclerView.Adapter<PermissionAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PermissionAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_permissionadapter, parent, false)
        return ViewHolder(v)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bindItems(mListPermissions.get(position))
    }

    override fun getItemCount(): Int {
       return mListPermissions.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(permissionList: AllListPermissionData) {
            val tv_rolename = itemView.findViewById(R.id.tv_rolename) as TextView
            val tv_permissionname = itemView.findViewById(R.id.tv_permissionname) as TextView
            val ll_editpermission = itemView.findViewById(R.id.ll_editpermission) as LinearLayout
            tv_rolename.setText(permissionList.role)
            tv_permissionname.setText(permissionList.permission)
            ll_editpermission.setOnClickListener(View.OnClickListener {
                val intent = Intent(itemView.context, AddPermission::class.java)
                intent.putExtra("permission_role",permissionList.role )
                intent.putExtra("permission_id",permissionList.id )
                intent.putExtra("permission_list",permissionList.permission )
                itemView.context.startActivity(intent)
            })

        }
    }



}