package com.webgurus.attendanceportal.ui.customer

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.VariantData

class LastOrderAdapter(var context: Activity, var orderarray: ArrayList<VariantData>
) : RecyclerView.Adapter<LastOrderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastOrderAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_lastorderss, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(orderarray.get(position))
    }

    override fun getItemCount(): Int {
        return  orderarray.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(variant: VariantData) {

            var ordername_tv =itemView.findViewById<TextView>(R.id.ordername_tv)
            var tv_order_variant =itemView.findViewById<TextView>(R.id.tv_order_variant)
            var order_quntiy_tv =itemView.findViewById<TextView>(R.id.order_quntiy_tv)
            var order_total_price =itemView.findViewById<TextView>(R.id.order_total_price)
            ordername_tv.setText(variant.product_name)
            tv_order_variant.setText(variant.product_unit.toString())
            order_quntiy_tv.setText(variant.quantity.toString())
            order_total_price.setText(variant.total_amount.toString())
        }
    }
}