package com.webgurus.attendanceportal.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.listeners.CheckBoxChangedSelectedListeners
import com.webgurus.attendanceportal.pojo.PermissionData


class AddPermission2Adapter(var context: Activity, var mModulesList: ArrayList<PermissionData>,var mListeners: CheckBoxChangedSelectedListeners)
    : RecyclerView.Adapter<AddPermission2Adapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddPermission2Adapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.item_userlisting22,
            parent,
            false
        )
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: AddPermission2Adapter.ViewHolder, position: Int) {
        holder.bindItems(mModulesList.get(position), position,mListeners)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return mModulesList.size
    }


    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(mModulesList: PermissionData, position: Int,mCheckBoxlisteners:CheckBoxChangedSelectedListeners) {
            val cb_permission = itemView.findViewById(R.id.cb_permission) as CheckBox
            cb_permission.setText(mModulesList.module)
            cb_permission.setChecked(mModulesList.isSelected)
            cb_permission.setTag(position)
//            cb_permission.setOnCheckedChangeListener { buttonView, isChecked ->
//                if(isChecked){
//                    mModulesList.isSelected =cb_permission.isChecked
//                    mCheckBoxlisteners.onChecked(position,mModulesList.id,mModulesList.module)
//                }else{
//                    mModulesList.isSelected =cb_permission.isChecked
//                    mCheckBoxlisteners.onUnChecked(position,mModulesList.id,mModulesList.module)
//                }
//            }
            cb_permission.setOnClickListener(View.OnClickListener {
                if(cb_permission.isChecked){
                    mModulesList.isSelected = cb_permission.isChecked
                    mCheckBoxlisteners.onChecked(position,mModulesList.id,mModulesList.module)
                }else{
                    mModulesList.isSelected = cb_permission.isChecked
                    mCheckBoxlisteners.onUnChecked(position,mModulesList.id,mModulesList.module)
                }

            })
        }
    }


}