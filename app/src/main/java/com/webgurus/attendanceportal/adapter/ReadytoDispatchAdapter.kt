package com.webgurus.attendanceportal.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.HotProduct
import com.webgurus.attendanceportal.pojo.ReadyForDispatch

class ReadytoDispatchAdapter(var mContext : Activity, var mListReadyToDispatch  : ArrayList<ReadyForDispatch>  ) : RecyclerView.Adapter<ReadytoDispatchAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReadytoDispatchAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_readytodispatched, parent, false)
        return ReadytoDispatchAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ReadytoDispatchAdapter.ViewHolder, position: Int) {
        holder.bindItems(position, mListReadyToDispatch)
    }

    override fun getItemCount(): Int {
        return mListReadyToDispatch.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {

        fun bindItems(position: Int,  mReadyToDispatchOrder : ArrayList<ReadyForDispatch>) {
            val tv_orderid = itemView.findViewById(R.id.tv_orderid) as TextView
            val tv_partyname = itemView.findViewById(R.id.tv_partyname) as TextView
            val tv_totalprice = itemView.findViewById(R.id.tv_totalprice) as TextView
            val tv_paymentreceived = itemView.findViewById(R.id.tv_paymentreceived) as TextView
            val tv_addresssale = itemView.findViewById(R.id.tv_addresssale) as TextView
            val tv_orderdates = itemView.findViewById(R.id.tv_orderdates) as TextView
            val tv_lasttransactiondate = itemView.findViewById(R.id.tv_lasttransactiondate) as TextView
            val tv_remark = itemView.findViewById(R.id.tv_remark) as TextView
            tv_orderid.setText(mReadyToDispatchOrder[position].id.toString())
            tv_partyname.setText(mReadyToDispatchOrder[position].organisation_name)
            tv_totalprice.setText(mReadyToDispatchOrder[position].total_price.toString())
            tv_paymentreceived.setText(mReadyToDispatchOrder[position].payment_received.toString())
            tv_addresssale.setText(mReadyToDispatchOrder[position].address)
            tv_orderdates.setText(mReadyToDispatchOrder[position].order_date.toString())
            tv_lasttransactiondate.setText(mReadyToDispatchOrder[position].last_transaction.toString())
            tv_remark.setText(mReadyToDispatchOrder[position].remarks)
        }
    }

}