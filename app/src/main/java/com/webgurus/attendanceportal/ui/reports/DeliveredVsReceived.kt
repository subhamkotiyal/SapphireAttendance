package com.webgurus.attendanceportal.ui.reports

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.adapter.DeliveredVsReceivedAdapter
import com.webgurus.attendanceportal.adapter.MyTaskAdapter
import com.webgurus.attendanceportal.pojo.GetRolesPojo
import com.webgurus.attendanceportal.pojo.Ordervsdelivered
import com.webgurus.attendanceportal.pojo.SuccesAttendances
import com.webgurus.attendanceportal.pojo.TopPerformerPojo
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_deliver_received.*
import kotlinx.android.synthetic.main.fragment_task.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeliveredVsReceived : BaseActivity() {

    var sharedPreferences: SharedPreferences? = null
    var mTopPerformerList : ArrayList<Ordervsdelivered> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deliver_received)
        initview()
        listeners()
        hitApitogetTopPerformer()
    }

    private fun initview() {
        sharedPreferences = this.getSharedPreferences(
            "AcessToken",
            AppCompatActivity.MODE_PRIVATE
        )
    }

    private fun listeners() {
        iv_backforperformance.setOnClickListener(View.OnClickListener {
            finish()
        })
    }


    private fun hitApitogetTopPerformer(){
        mTopPerformerList.clear()
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getTopPerformer("Bearer " + sharedPreferences!!.getString("Access_Token", ""))
        showLoading(true)
        call.enqueue(object : Callback<TopPerformerPojo?> {
            override fun onResponse(
                call: Call<TopPerformerPojo?>,
                response: Response<TopPerformerPojo?>
            ) {
                hideProgress()
                mTopPerformerList.addAll(response.body()!!.ordervsdelivered)
                rv_topperformer.layoutManager = LinearLayoutManager(this@DeliveredVsReceived, RecyclerView.VERTICAL, false)
                val adapter = DeliveredVsReceivedAdapter(this@DeliveredVsReceived,mTopPerformerList)
                rv_topperformer.adapter = adapter
            }

            override fun onFailure(call: Call<TopPerformerPojo?>, t: Throwable) {
                hideProgress()
                Toast.makeText(this@DeliveredVsReceived, "" + t.toString(), Toast.LENGTH_SHORT).show()
            }

        })

    }


}