package com.webgurus.attendanceportal.ui.reports

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.adapter.*
import com.webgurus.attendanceportal.pojo.Datar
import com.webgurus.attendanceportal.pojo.DispatchOrder
import com.webgurus.attendanceportal.pojo.DispatchReportPojo
import com.webgurus.attendanceportal.pojo.ReadyForDispatch
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_dispatch_report.*
import retrofit2.Call
import retrofit2.Response

class DispatchReport : BaseActivity() {


    var sharedPreferences : SharedPreferences? =null
    var mList : ArrayList<Datar> = ArrayList()
    var adapter : CustomerLisitingAdapter? = null
    var mContext: Context?=null
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var rv_dispatchReport : RecyclerView
    lateinit var rv_dispatchedorder : RecyclerView
    var mReadyToDispatchList : ArrayList<ReadyForDispatch> = ArrayList()
    var mDispatchOrder: ArrayList<DispatchOrder> = ArrayList()
    lateinit var iv_backdispatch: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispatch_report)
        initview()
        listeners()
        hitApitoGettheCustomerReports()
    }

    private fun listeners() {
        iv_backdispatch.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun initview() {
        iv_backdispatch = findViewById(R.id.iv_backdispatch) as ImageView
        rv_dispatchReport = findViewById(R.id.rv_dispatchReport) as RecyclerView
        rv_dispatchedorder = findViewById(R.id.rv_dispatchedorder) as RecyclerView
        sharedPreferences = this.getSharedPreferences(
            "AcessToken",
            AppCompatActivity.MODE_PRIVATE
        )
    }

    fun setRecyclerView() {
        linearLayoutManager = LinearLayoutManager(this)
        rv_dispatchReport.layoutManager = linearLayoutManager
        rv_dispatchReport.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter = ReadytoDispatchAdapter(this,mReadyToDispatchList)
        rv_dispatchReport.adapter = adapter
    }

    fun setRecyclerView22() {
        linearLayoutManager = LinearLayoutManager(this)
        rv_dispatchedorder.layoutManager = linearLayoutManager
        rv_dispatchedorder.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter = DispatchReportAdapter(this,mDispatchOrder)
        rv_dispatchedorder.adapter = adapter
    }


    private fun hitApitoGettheCustomerReports() {
        mReadyToDispatchList.clear()
        mDispatchOrder.clear()
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.dispatch_report(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            )
        )
        showLoading(true)

        call.enqueue(object : retrofit2.Callback<DispatchReportPojo?> {
            override fun onResponse(
                call: Call<DispatchReportPojo?>,
                response: Response<DispatchReportPojo?>
            ) {
                if (response.body() != null) {
                    hideProgress()
                    mReadyToDispatchList.addAll(response.body()!!.ready_for_dispatch)
                    mDispatchOrder.addAll(response.body()!!.dispatch_order)
                }
                setData(response.body()!!)
                setRecyclerView()
                setRecyclerView22()

            }

            override fun onFailure(call: Call<DispatchReportPojo?>, t: Throwable) {
                val dsgfd = ""
            }

        })
    }

    private fun setData(response : DispatchReportPojo) {
        tv_totalorder.setText(response.total_order.toString())
        tv_orderapproved.setText(response.total_order_approved.toString())
        tv_orderdispatched.setText(response.total_order_dispatched.toString())
        tv_orderreadyfordispatched.setText(response.total_order_ready_dispatched.toString())
    }


}