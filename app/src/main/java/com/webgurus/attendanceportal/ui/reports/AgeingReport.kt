package com.webgurus.attendanceportal.ui.reports

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.adapter.AgeingReportAdapter
import com.webgurus.attendanceportal.adapter.CustomerLisitingAdapter
import com.webgurus.attendanceportal.pojo.AgeingData
import com.webgurus.attendanceportal.pojo.AgeingReportPojo
import com.webgurus.attendanceportal.pojo.AttendanceReportPojo
import com.webgurus.attendanceportal.pojo.Datar
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_ageing_report.*
import kotlinx.android.synthetic.main.item_ageing_report.*
import retrofit2.Call
import retrofit2.Response

class AgeingReport : BaseActivity() {

    var sharedPreferences: SharedPreferences? = null
    var mList: ArrayList<Datar> = ArrayList()
    var adapter: CustomerLisitingAdapter? = null
    var mContext: Context? = null
    var list_size: Int = 0
    var id_number: Int = 0
    var mAgeinglist: ArrayList<AgeingData> = ArrayList()
    lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ageing_report)
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
        val call = service.agingReport(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            )
        )


        // Log.e("cc",""+agent_id)
        showLoading(true)

        call.enqueue(object : retrofit2.Callback<AgeingReportPojo?> {
            override fun onResponse(
                call: Call<AgeingReportPojo?>,
                response: Response<AgeingReportPojo?>
            ) {
                if (response.body() != null) {
                    hideProgress()
                    Log.e("wwwww", "" + response.body()!!.data.size)
                    list_size = response.body()!!.data.size
                    mAgeinglist.addAll(response.body()!!.data)
                    setRecyclerView()
                }


            }

            override fun onFailure(call: Call<AgeingReportPojo?>, t: Throwable) {
                val dsgfd = ""
                Toast.makeText(this@AgeingReport, "ERRRRRRRRRRRRRRRRRR", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun setRecyclerView() {
        linearLayoutManager = LinearLayoutManager(this)
        rv_ageingReport.layoutManager = linearLayoutManager
        val recyclerView = findViewById(R.id.rv_ageingReport) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter = AgeingReportAdapter(mAgeinglist)
        //now adding the adapter to recyclerview
        recyclerView.adapter = adapter
    }
}