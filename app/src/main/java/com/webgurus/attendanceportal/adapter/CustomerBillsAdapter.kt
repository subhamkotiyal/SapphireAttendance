package com.webgurus.attendanceportal.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.listeners.RecyclerViewRoleClickListeners
import com.webgurus.attendanceportal.pojo.GetBillData
import com.webgurus.attendanceportal.ui.bills.AddBillActivity
import com.webgurus.attendanceportal.ui.customer.CustomerDetailsFragment
import com.webgurus.data.CommentPojo

class CustomerBillsAdapter(var context:Context , var mBillList : ArrayList<GetBillData> , var mListeners : RecyclerViewRoleClickListeners) : RecyclerView.Adapter<CustomerBillsAdapter.MyCustomerBillViewHolder>() {

    class MyCustomerBillViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(billlist: GetBillData , listeners : RecyclerViewRoleClickListeners ) {
            val  tv_customrname=itemView.findViewById<TextView>(R.id.tv_customrname) as TextView
            val  tv_billnumber=itemView.findViewById<TextView>(R.id.tv_billnumber) as TextView
            val  tv_totalamount=itemView.findViewById<TextView>(R.id.tv_totalamount) as TextView
            val  tv_paymentreceive=itemView.findViewById<TextView>(R.id.tv_paymentreceive) as TextView
            val  tv_pendingpayment=itemView.findViewById<TextView>(R.id.tv_pendingpayment) as TextView
            val  ll_delete=itemView.findViewById<LinearLayout>(R.id.ll_delete) as LinearLayout
            val  ll_edit=itemView.findViewById<LinearLayout>(R.id.ll_edit) as LinearLayout
            tv_customrname.setText(billlist.organisation_name.toString() )
            tv_billnumber.setText(billlist.bill_no)
            tv_totalamount.setText(billlist.total_payment.toString())
            tv_paymentreceive.setText(billlist.received.toString())
            tv_pendingpayment.setText(billlist.pending.toString())
            ll_delete.setOnClickListener(View.OnClickListener {
                listeners.onClick(position,billlist.id)
            })
            ll_edit.setOnClickListener(View.OnClickListener {
                var intent = Intent(itemView.context, AddBillActivity::class.java)
                intent.putExtra("billno",billlist.bill_no.toString())
                intent.putExtra("totalamount",billlist.total_payment.toString())
                intent.putExtra("paymentreceive",billlist.received.toString())
                intent.putExtra("pendingpayment",billlist.pending.toString())
                intent.putExtra("customerIDs",billlist.customer_id.toString())
                intent.putExtra("billID",billlist.id.toString())
                itemView.context.startActivity(intent)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCustomerBillViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_billsview, parent, false)
        return MyCustomerBillViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyCustomerBillViewHolder, position: Int) {
        holder.bindItems(mBillList[position],mListeners)
    }

    override fun getItemCount(): Int {
        return  mBillList.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
}