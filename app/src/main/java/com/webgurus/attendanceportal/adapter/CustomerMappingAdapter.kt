package com.webgurus.attendanceportal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.CustomerDetails


class CustomerMappingAdapter(var context: Context , var mCustomerList:ArrayList<CustomerDetails>  ):  RecyclerView.Adapter<CustomerMappingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerMappingAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.item_customermapping,
            parent,
            false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CustomerMappingAdapter.ViewHolder, position: Int) {
        holder.bindItems(position , mCustomerList)
    }

    override fun getItemCount(): Int {
        return mCustomerList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(pos:Int, mCustomerList:ArrayList<CustomerDetails>) {
            val tv_organisationname=itemView.findViewById<TextView>(R.id.tv_organisationname)
            val tv_contactperson=itemView.findViewById<TextView>(R.id.tv_contactperson)
            val tv_email=itemView.findViewById<TextView>(R.id.tv_email)
            val tv_phonenumber=itemView.findViewById<TextView>(R.id.tv_phonenumber)
            tv_organisationname.setText(mCustomerList[pos].organisation_name)
            tv_contactperson.setText(mCustomerList[pos].first_name + " " + mCustomerList[pos].middle_name +  "  "  +  mCustomerList[pos].last_name)
            tv_email.setText(mCustomerList[pos].email)
            tv_phonenumber.setText(mCustomerList[pos].phone_number)

        }
    }


}