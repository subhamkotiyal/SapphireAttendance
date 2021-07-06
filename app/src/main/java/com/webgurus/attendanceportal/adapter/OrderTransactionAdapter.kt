package com.webgurus.attendanceportal.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.OrderTransaction

class OrderTransactionAdapter(
    var context: Activity,
    var mTransaction: ArrayList<OrderTransaction>
) : RecyclerView.Adapter<OrderTransactionAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderTransactionAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ordertransaction, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: OrderTransactionAdapter.ViewHolder, position: Int) {
        holder.bindItems(position,mTransaction.get(position))
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return mTransaction.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(position: Int ,   mTransaction : OrderTransaction) {
            val tv_transactionorderid =
                itemView.findViewById(R.id.tv_transactionorderid) as TextView
            val tv_transactionordervalue =
                itemView.findViewById(R.id.tv_transactionordervalue) as TextView
            tv_transactionorderid.setText(mTransaction.order_id.toString())
            tv_transactionordervalue.setText(mTransaction.amount.toString())

        }
    }

}