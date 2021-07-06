package com.webgurus.attendanceportal.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.listeners.CheckBoxSelectedListeners
import com.webgurus.attendanceportal.listeners.RecyclerViewRoleClickListeners
import com.webgurus.attendanceportal.pojo.Datar
import com.webgurus.attendanceportal.ui.bills.AddBillActivity
import com.webgurus.attendanceportal.ui.customer.AddCustomerFragment
import com.webgurus.attendanceportal.ui.customer.CustomerDetailsFragment
import java.util.*


class CustomerLisitingAdapter(var context: Context, var mList : ArrayList<Datar>,var isSelected : Boolean,var mCheckListeners:
CheckBoxSelectedListeners,var mListersClick: RecyclerViewRoleClickListeners
) :
    RecyclerView.Adapter<CustomerLisitingAdapter.ViewHolder>() {
    var customerListAry= ArrayList<Datar>()
    init {
        customerListAry=mList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerLisitingAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_customer, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(position,customerListAry.get(position),mList,isSelected,mCheckListeners,mListersClick,customerListAry)
    }

    override fun getItemCount(): Int {
        return  customerListAry.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(position: Int,customerList:Datar, mList : ArrayList<Datar>,isSelected:Boolean,
                      mCheckListeners:CheckBoxSelectedListeners,mListeners:RecyclerViewRoleClickListeners,mFilterArray:ArrayList<Datar>) {
            val ll_root = itemView.findViewById(R.id.ll_root) as LinearLayout
            val tv_assigneduser = itemView.findViewById(R.id.tv_assigneduser) as TextView
            val ll_viewcustomer = itemView.findViewById(R.id.ll_viewcustomer) as LinearLayout
            val ll_editcustomer = itemView.findViewById(R.id.ll_editcustomer) as LinearLayout
            val ll_deletecustomer = itemView.findViewById(R.id.ll_deletecustomer) as LinearLayout
            val ll_addbill = itemView.findViewById(R.id.ll_addbill) as LinearLayout
            val tv_customername = itemView.findViewById(R.id.tv_customername) as TextView
            val tv_customernum = itemView.findViewById(R.id.tv_customernum) as TextView
            val tv_address = itemView.findViewById(R.id.tv_address) as TextView
            val cb_selectcust = itemView.findViewById(R.id.cb_selectcust) as CheckBox
            tv_customername.setText(customerList.first_name+ " " + customerList.last_name)
            tv_customernum.setText(customerList.phone_number)
            tv_address.setText(customerList.address+" "+customerList.city+" "+customerList.state +"   "+ customerList.pincode)
            if(isSelected){
                cb_selectcust.visibility=View.VISIBLE
            }else{
                cb_selectcust.visibility=View.GONE
            }
            tv_assigneduser.setText(customerList.assign_user_name)


            ll_addbill.setOnClickListener(View.OnClickListener {

                var intent = Intent(itemView.context, AddBillActivity::class.java)
                intent.putExtra("customerID",customerList.id.toString())
                itemView.context.startActivity(intent)
            })


            ll_viewcustomer.setOnClickListener(View.OnClickListener {
                if(!isSelected){
                    if(mFilterArray.size>0){
                        var intent = Intent(itemView.context, CustomerDetailsFragment::class.java)
                        intent.putParcelableArrayListExtra("customerdetails",mFilterArray)
                        intent.putExtra("pos",adapterPosition)
                        itemView.context.startActivity(intent)
                    }else{
                        var intent = Intent(itemView.context, CustomerDetailsFragment::class.java)
                        intent.putParcelableArrayListExtra("customerdetails",mList)
                        intent.putExtra("pos",adapterPosition)
                        itemView.context.startActivity(intent)
                    }

                }

            })


            ll_deletecustomer.setOnClickListener(View.OnClickListener {
                mListeners.onClick(position,customerList.id)
//                if(mFilterArray.size>0){
//                    mListeners.onClick(position,mFilterArray.get(position).id)
//                    mFilterArray.removeAt(position)
//                }else{
//                    mListeners.onClick(position,customerList.id)
//                }
            })

            ll_editcustomer.setOnClickListener(View.OnClickListener {
                if(mFilterArray.size>0){
                    var intent = Intent(itemView.context, AddCustomerFragment::class.java)
                    intent.putParcelableArrayListExtra("customerdetails",mFilterArray)
                    intent.putExtra("pos",adapterPosition)
                    intent.putExtra("status","forEdit")
                    itemView.context.startActivity(intent)
                }else{
                    var intent = Intent(itemView.context, AddCustomerFragment::class.java)
                    intent.putParcelableArrayListExtra("customerdetails",mList)
                    intent.putExtra("pos",adapterPosition)
                    intent.putExtra("status","forEdit")
                    itemView.context.startActivity(intent)
                }


            })
            cb_selectcust.setOnCheckedChangeListener( object:CompoundButton.OnCheckedChangeListener {

                override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                    if(isChecked){
                        mCheckListeners.onChecked(position,customerList.id)
                    }else{
                        mCheckListeners.onUnChecked(position,customerList.id)
                    }

                }

            }
            );
        }
    }
    fun filterList(filteredList: ArrayList<Datar>) {
        customerListAry = filteredList
        notifyDataSetChanged()
    }



}