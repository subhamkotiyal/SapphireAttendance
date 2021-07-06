package com.webgurus.attendanceportal.ui.customer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.Datar
import com.webgurus.attendanceportal.pojo.VariantData
import com.webgurus.attendanceportal.ui.base.BaseFragment
import com.webgurus.attendanceportal.ui.order.AddNewOrderActivity
import kotlinx.android.synthetic.main.fragment_lastorders.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LastOrdersFragment  : BaseFragment() {

    var mVarirant: ArrayList<VariantData> = ArrayList()
    var strtext:ArrayList<Datar> = ArrayList()
    var mPositon:Int?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lastorders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initview()
        listeners()
    }

    private fun listeners() {
        fb_addorder.setOnClickListener(View.OnClickListener {
            var intent = Intent(requireContext(), AddNewOrderActivity::class.java)
            intent.putExtra(
                "username",
                strtext.get(mPositon!!).first_name + " " + strtext.get(mPositon!!).last_name
            )
            startActivity(intent)
        })
    }

    private fun getSuitbaleDateFormat(newDate:String) : String{
        val srcDf: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date: Date = srcDf.parse(newDate)
        val destDf: DateFormat = SimpleDateFormat("dd-MMM-yyyy")
        return destDf.format(date)
    }

    private fun initview() {
        mVarirant.clear()
        strtext = requireArguments().getParcelableArrayList<Datar>("lastorder") as ArrayList<Datar>
        var postion=requireArguments().getInt("pos")
        mPositon=requireArguments().getInt("pos")
        if(strtext.get(postion).orders.size>0){
            for (i in 0 until strtext.get(postion).orders.size){
                    mVarirant.addAll(strtext[postion].orders[i].variant)

            }
            ll_root.visibility=View.VISIBLE
            tv_nodatavailable.visibility=View.GONE
        }else{
            ll_productlisitng.visibility=View.GONE
            ll_root.visibility=View.GONE
            tv_nodatavailable.visibility=View.VISIBLE
        }
        if(strtext[postion].last_order_date.equals("")){
            cv_lastorder.visibility=View.GONE
        }else{
            lastorder_tv.text= getSuitbaleDateFormat(strtext[postion].payment_pending[strtext.get(postion).payment_pending.size - 1].date.toString())
            //lastorder_tv.text= strtext[postion].payment_pending[strtext.get(postion).payment_pending.size - 1].date.toString()
            tv_total_amountlastorder.text= strtext[postion].payment_pending[strtext.get(postion).payment_pending.size - 1].total_payment.toString()
            if(strtext[postion].orders[0].status==1){
                order_status_tv.setText("Received")
            }else if(strtext[postion].orders[0].status==2){
                order_status_tv.setText("Approved")
            }else if(strtext[postion].orders[0].status==3){
                order_status_tv.setText("Dispatched")
            }else if(strtext[postion].orders[0].status==4){
                order_status_tv.setText("Delivered")
            }else if(strtext[postion].orders[0].status==4){
                order_status_tv.setText("Cancelled")
            }else{
                order_status_tv.setText("New Order")
            }

        }
        rv_lastorders.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        val adapter = LastOrderAdapter(requireActivity(), mVarirant)
        rv_lastorders.adapter = adapter
    }
}