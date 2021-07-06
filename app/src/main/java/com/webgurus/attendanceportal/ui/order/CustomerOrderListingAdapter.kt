package com.webgurus.attendanceportal.ui.order

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.listeners.RecyclerViewRoleClickListeners
import com.webgurus.attendanceportal.pojo.OrderVariant
import java.util.ArrayList

class CustomerOrderListingAdapter (val context: Activity, val mItemListing: ArrayList<OrderVariant>,
                                   val mListeners : RecyclerViewRoleClickListeners) :
                                   RecyclerView.Adapter<CustomerOrderListingAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerOrderListingAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.item_variantlisting,
            parent,
            false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(mItemListing.get(position))
    }

    override fun getItemCount(): Int {
        return mItemListing.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(ordervariant: OrderVariant) {
            val tv_unit = itemView.findViewById(R.id.tv_unit) as TextView
            val tv_productnamewithunit = itemView.findViewById(R.id.tv_productnamewithunit) as TextView
            val tv_price = itemView.findViewById(R.id.tv_price) as TextView
            val tv_quantity = itemView.findViewById(R.id.tv_quantity) as TextView
            tv_productnamewithunit.setText(ordervariant.product_name.toString())
            tv_unit.setText(ordervariant.product_unit.toString())
            tv_price.setText(ordervariant.total_amount.toString())
            tv_quantity.setText(ordervariant.quantity.toString())
        }
    }



}