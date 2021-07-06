package com.webgurus.attendanceportal.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.ItemSale

class ItemReportSaleAdapter(var context: Activity, var mListItemSale: ArrayList<ItemSale>) :
    RecyclerView.Adapter<ItemReportSaleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.item_sales_report, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(position, mListItemSale)
    }

    override fun getItemCount(): Int {
        return mListItemSale.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(position: Int, mListItemSale: ArrayList<ItemSale>) {
            val tv_saleid = itemView.findViewById(R.id.tv_saleid) as TextView
            val tv_saleproduct = itemView.findViewById(R.id.tv_saleproduct) as TextView
            val tv_salevariant = itemView.findViewById(R.id.tv_salevariant) as TextView
            val tv_lastdatesale = itemView.findViewById(R.id.tv_lastdatesale) as TextView
            val tv_sevendaysale = itemView.findViewById(R.id.tv_sevendaysale) as TextView
            val tv_thirtydaysale = itemView.findViewById(R.id.tv_thirtydaysale) as TextView
            val tv_averageratesale = itemView.findViewById(R.id.tv_averageratesale) as TextView
            tv_saleid.setText(mListItemSale[position].id)
            tv_saleproduct.setText(mListItemSale[position].product)
            tv_salevariant.setText(mListItemSale[position].variant.toString())
            tv_lastdatesale.setText(mListItemSale[position].last_day_sale.toString())
            tv_sevendaysale.setText(mListItemSale[position].seven_day_sale.toString())
            tv_thirtydaysale.setText(mListItemSale[position].thirty_day_sale.toString())
            tv_averageratesale.setText(mListItemSale[position].avg_rate.toString())

        }
    }
}