package com.webgurus.attendanceportal.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.HotProduct
import com.webgurus.attendanceportal.pojo.ItemSale

class HotProductAdapter(var mContext : Activity , var mListHotproduct : ArrayList<HotProduct> ) : RecyclerView.Adapter<HotProductAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HotProductAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_hotproduct_report, parent, false)
        return HotProductAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: HotProductAdapter.ViewHolder, position: Int) {
        holder.bindItems(position, mListHotproduct)
    }

    override fun getItemCount(): Int {
        return mListHotproduct.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {

        fun bindItems(position: Int,  mListHotproduct : ArrayList<HotProduct>) {
            val tv_hotproductname = itemView.findViewById(R.id.tv_hotproductname) as TextView
            val tv_hotproductunit = itemView.findViewById(R.id.tv_hotproductunit) as TextView
            val tv_hotquantitysold = itemView.findViewById(R.id.tv_hotquantitysold) as TextView

            tv_hotproductname.setText(mListHotproduct[position].product_name)
            tv_hotproductunit.setText(mListHotproduct[position].unit)
            tv_hotquantitysold.setText(mListHotproduct[position].quantity_sold)


        }
    }

}