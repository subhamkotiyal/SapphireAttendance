package com.webgurus.attendanceportal.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.ItemQuantity

class ItemReportQuantityAdapter(var context : Activity , var mListItemQuantity : ArrayList<ItemQuantity>) : RecyclerView.Adapter<ItemReportQuantityAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_item_report, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        holder.bindItems(position,mListItemQuantity)
    }

    override fun getItemCount(): Int {
        return mListItemQuantity.size
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems( position: Int ,  mListItemQuantity : ArrayList<ItemQuantity> ) {
            val orderid = itemView.findViewById(R.id.tv_orderid) as TextView
            val tv_productnames = itemView.findViewById(R.id.tv_productnames) as TextView
            val tv_varianvalue = itemView.findViewById(R.id.tv_varianvalue) as TextView
            val tv_lastdaysale = itemView.findViewById(R.id.tv_lastdaysale) as TextView
            val tv_sevendaysale = itemView.findViewById(R.id.tv_sevendaysale) as TextView
            val tv_thrityday = itemView.findViewById(R.id.tv_thrityday) as TextView
            orderid.setText(mListItemQuantity[position].id.toString())
            tv_productnames.setText(mListItemQuantity[position].product)
            tv_varianvalue.setText(mListItemQuantity[position].variant)
            tv_lastdaysale.setText(mListItemQuantity[position].last_day_sale.toString())
            tv_sevendaysale.setText(mListItemQuantity[position].seven_day_sale.toString())
            tv_thrityday.setText(mListItemQuantity[position].thirty_day_sale.toString())
        }
    }
}