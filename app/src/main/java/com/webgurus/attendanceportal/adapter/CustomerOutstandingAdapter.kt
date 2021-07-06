package com.webgurus.attendanceportal.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.listeners.CheckBoxSelectedListeners
import com.webgurus.attendanceportal.listeners.RecyclerViewRoleClickListeners
import com.webgurus.attendanceportal.pojo.Customer
import com.webgurus.attendanceportal.pojo.Datar
import com.webgurus.attendanceportal.ui.bills.AddBillActivity
import com.webgurus.attendanceportal.ui.customer.AddCustomerFragment
import com.webgurus.attendanceportal.ui.customer.CustomerDetailsFragment
import java.util.ArrayList

class CustomerOutstandingAdapter(var mContext: Activity, var mOutstanding: ArrayList<Customer>) :
    RecyclerView.Adapter<CustomerOutstandingAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_customeroutstanding, parent, false)
        return ViewHolder(v)
    }


    override fun getItemCount(): Int {
        return mOutstanding.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(position: Int, mOutstanding: ArrayList<Customer>) {
            val tv_organisationid = itemView.findViewById<TextView>(R.id.tv_organisationid)
            val tv_organisationname = itemView.findViewById<TextView>(R.id.tv_organisationname)
            val tv_order = itemView.findViewById<TextView>(R.id.tv_order)
            val tv_totatbill = itemView.findViewById<TextView>(R.id.tv_totatbill)
            val tv_totaloutstanding = itemView.findViewById<TextView>(R.id.tv_totaloutstanding)
            tv_organisationid.setText(mOutstanding.get(position).id.toString())
            tv_organisationname.setText(mOutstanding.get(position).organisation_name)
            tv_order.setText(mOutstanding.get(position).orderTotal.toString())
            tv_totatbill.setText(mOutstanding.get(position).bill_total.toString())
            tv_totaloutstanding.setText(mOutstanding.get(position).total_outstanding.toString())
        }


    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(position, mOutstanding)
    }


}