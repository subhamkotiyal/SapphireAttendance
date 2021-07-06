package com.webgurus.attendanceportal.ui.customer

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.PaymentPending
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PendingAmountAdapter  (var context: Activity,var mListPendingAmount:ArrayList<PaymentPending> ) : RecyclerView.Adapter<PendingAmountAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingAmountAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_pendingamount, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(mListPendingAmount.get(position))
    }

    override fun getItemCount(): Int {
        return  mListPendingAmount.size
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private fun getSuitbaleDateFormat(newDate:String) : String{
            val srcDf: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val date: Date = srcDf.parse(newDate)
            val destDf: DateFormat = SimpleDateFormat("dd-MMM-yyyy")
            return destDf.format(date)
        }


        fun bindItems(mPaymentList:PaymentPending) {
            val tv_orderid = itemView.findViewById(R.id.tv_orderid) as TextView
            val tv_orderdate = itemView.findViewById(R.id.tv_orderdate) as TextView
            val tv_totalamount = itemView.findViewById(R.id.tv_totalamount) as TextView
            val tv_pendingpayment = itemView.findViewById(R.id.tv_pendingpayment) as TextView
            tv_orderid.setText(mPaymentList.order_id.toString())
            tv_orderdate.setText(getSuitbaleDateFormat(mPaymentList.date))
            tv_totalamount.setText(mPaymentList.total_payment)
            tv_pendingpayment.setText(mPaymentList.payment_pending.toString())

        }
    }
}