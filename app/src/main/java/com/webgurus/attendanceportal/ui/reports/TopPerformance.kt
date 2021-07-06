package com.webgurus.attendanceportal.ui.reports

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.adapter.AgeingReportAdapter
import com.webgurus.attendanceportal.adapter.CustomerLisitingAdapter
import com.webgurus.attendanceportal.adapter.TopperformerCollectionAdapter
import com.webgurus.attendanceportal.pojo.*
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_ageing_report.*
import kotlinx.android.synthetic.main.activity_ageing_report.rv_ageingReport
import kotlinx.android.synthetic.main.activity_top_performance.*
import retrofit2.Call
import retrofit2.Response

class TopPerformance : BaseActivity() {


    var sharedPreferences : SharedPreferences? =null
    var mList : ArrayList<Datar> = ArrayList()
    var adapter : CustomerLisitingAdapter? = null
    var mContext: Context?=null
    var mAgeinglist :ArrayList<data_performance> = ArrayList()
    var mDatapayment :ArrayList<DataPayment> = ArrayList()
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_performance)
        initview()
        hitApitoGettheCustomerReports()
    }
    private fun initview() {
        sharedPreferences = this.getSharedPreferences(
            "AcessToken",
            AppCompatActivity.MODE_PRIVATE
        )
    }
    private fun hitApitoGettheCustomerReports() {

        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.top_performance_report(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            )
        )


        // Log.e("cc",""+agent_id)
        showLoading(true)

        call.enqueue(object : retrofit2.Callback<TopPerformancePojo?> {
            override fun onResponse(
                call: Call<TopPerformancePojo?>,
                response: Response<TopPerformancePojo?>
            ) {
                if (response.body() != null) {
                    hideProgress()
                    Log.e("wwwww", "" + response.body()!!.data.size)
                    mAgeinglist.addAll(response.body()!!.data)
                    mDatapayment.addAll(response.body()!!.data_payment)
                    SetRecyclerView()
                    setRecyclerviewforcollection()
                }


            }

            override fun onFailure(call: Call<TopPerformancePojo?>, t: Throwable) {
                val dsgfd = ""
             //   Toast.makeText(this@AgeingReport, "ERRRRRRRRRRRRRRRRRR", Toast.LENGTH_LONG).show()
            }

        })
    }



    private fun setRecyclerviewforcollection(){
        linearLayoutManager = LinearLayoutManager(this)
        rv_topperformercollection.layoutManager = linearLayoutManager
        val rv_topperformercollection = findViewById(R.id.rv_topperformercollection) as RecyclerView
        rv_topperformercollection.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter = TopperformerCollectionAdapter(mDatapayment)
        rv_topperformercollection.adapter = adapter
    }

    private fun SetRecyclerView() {
        linearLayoutManager = LinearLayoutManager(this)
        rv_ageingReport.layoutManager = linearLayoutManager
        val recyclerView = findViewById(R.id.rv_ageingReport) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter = TopPerformanceAdapter(mAgeinglist)

        //now adding the adapter to recyclerview
        recyclerView.adapter = adapter
    }
}