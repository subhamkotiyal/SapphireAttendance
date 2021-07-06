package com.webgurus.attendanceportal.ui.reports

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.GetUserTargetPojo
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_usersalesreport.*
import retrofit2.Call
import retrofit2.Response

class UserSalesReport : BaseActivity() {

    var sharedPreferences: SharedPreferences? = null

    var mIds : Int=0
    var mIdType : String = ""
    var mIdDate : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usersalesreport)
        initview()
        hitApitogetUserTarget()
    }

    private fun initview() {
        if(intent.getIntExtra("mManagerID",0)!=0){
            mIds=intent.getIntExtra("mManagerID",0)
            mIdType=intent!!.getStringExtra("mManagertype")!!
           mIdDate=intent!!.getStringExtra("mManagerSelectedDate")!!
        }

        if(intent.getIntExtra("mFieldManagerID",0)!=0){
            mIds=intent.getIntExtra("mFieldManagerID",0)
            mIdType=intent!!.getStringExtra("mFieldManagertype")!!
            mIdDate=intent!!.getStringExtra("mFieldManagerSelectedDate")!!
        }
        iv_backfromreport.setOnClickListener(View.OnClickListener {
            finish()
        })
        sharedPreferences = getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE)
    }

    private fun hitApitogetUserTarget() {
        showLoading(true)
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getUserTraget("Bearer " + sharedPreferences!!.getString("Access_Token", ""),mIds, mIdType,mIdDate)
        call.enqueue(object : retrofit2.Callback<GetUserTargetPojo> {
            override fun onResponse(call: Call<GetUserTargetPojo?>, response: Response<GetUserTargetPojo?>) {
                hideProgress()
                if (response.body() != null) {
                    tv_targetdate.setText(response.body()!!.date)
                    tv_orderplaceamount.setText(response.body()!!.sale.toString())
                    tv_orderplaceamounts.setText(response.body()!!.sale.toString())
                    tv_target.setText(response.body()!!.target.toString())
                    tv_targetsdates.setText(response.body()!!.target.toString())
                    tv_collectionsale.setText(response.body()!!.sale_collect.toString())
                }
            }

            override fun onFailure(call: Call<GetUserTargetPojo>, t: Throwable) {
                Toast.makeText(this@UserSalesReport, "" + t.toString(), Toast.LENGTH_SHORT).show()
                hideProgress()
            }

        })

    }


}