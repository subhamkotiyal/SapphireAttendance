package com.webgurus.attendanceportal.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.OrdersVariant
import com.webgurus.attendanceportal.pojo.SuccesAttendances

class ProductListAdapter( var context: Activity , var mOrderListing : ArrayList<OrdersVariant>, var customername:String) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_listproducts, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ProductListAdapter.ViewHolder, position: Int) {
        holder.bindItems(position,mOrderListing[position],customername)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return mOrderListing.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(position: Int ,mOrderListing : OrdersVariant ,customername:String) {
            val tv_orderID = itemView.findViewById(R.id.tv_orderID) as TextView
            val tv_customernames = itemView.findViewById(R.id.tv_customernames) as TextView
            val tv_productsnames = itemView.findViewById(R.id.tv_productsnames) as TextView
            val tv_variants = itemView.findViewById(R.id.tv_variants) as TextView
            val tv_quotedprice = itemView.findViewById(R.id.tv_quotedprice) as TextView
            val tv_quantity = itemView.findViewById(R.id.tv_quantity) as TextView
            val tv_totalprices = itemView.findViewById(R.id.tv_totalprices) as TextView
            tv_orderID.setText(mOrderListing.id.toString())
            tv_customernames.setText(customername)
            tv_productsnames.setText(mOrderListing.product_name.toString())
            tv_variants.setText(mOrderListing.product_unit)
            tv_quotedprice.setText(mOrderListing.quoted_price.toString())
            tv_quantity.setText(mOrderListing.quantity.toString())
            tv_totalprices.setText(mOrderListing.total_amount.toString())

        }
    }

}