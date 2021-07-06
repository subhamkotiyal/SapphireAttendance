package com.webgurus.attendanceportal.ui.reports

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.adapter.AttendanceAdapter
import com.webgurus.attendanceportal.adapter.CustomerLisitingAdapter
import com.webgurus.attendanceportal.adapter.CustomerMappingAdapter
import com.webgurus.attendanceportal.pojo.AttendanceListPojo
import com.webgurus.attendanceportal.pojo.CustomerDetails
import com.webgurus.attendanceportal.pojo.CustomerMappingPojo
import com.webgurus.attendanceportal.pojo.SuccesAttendances
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.attendanceportal.ui.productmanagement.ProductManagementAdapter
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_customermappingdetails.*
import kotlinx.android.synthetic.main.fragment_attendance.*
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception

class CustomerMappingDetailsActivity : BaseActivity() {

    var adapter: CustomerMappingAdapter? = null
    var userSelectedHome: Int = 0
    var sharedPreferences: SharedPreferences? = null
    var mCustomerList: ArrayList<CustomerDetails> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customermappingdetails)
        initview()
        listners()
        hitApitogetCustomerMappingDetails()
    }

    private fun listners() {
        iv_backformapping.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun hitApitogetCustomerMappingDetails() {
        if (userSelectedHome == 0) {
            Toast.makeText(this, "ID is null", Toast.LENGTH_LONG).show()
            return
        }
        mCustomerList.clear()
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.get_customermapping(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            ), userSelectedHome
        )
        call.enqueue(object : retrofit2.Callback<CustomerMappingPojo?> {
            override fun onFailure(call: Call<CustomerMappingPojo?>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<CustomerMappingPojo?>?,
                response: Response<CustomerMappingPojo?>?
            ) {

                try {
                    mCustomerList.addAll(response!!.body()!!.customer)
                    if(mCustomerList.size>0){
                        adapter!!.notifyDataSetChanged()
                        tv_nodata.visibility=View.GONE
                    }else{
                        tv_nodata.visibility=View.VISIBLE
                        rv_performers.visibility=View.GONE
                    }

                } catch (e: Exception) {
                    Toast.makeText(
                        this@CustomerMappingDetailsActivity,
                        resources.getString(R.string.servernotrespond),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun initview() {
        sharedPreferences = this.getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE)
        if (intent.getIntExtra("mManagerID", 0) != 0) {
            userSelectedHome = intent.getIntExtra("mManagerID", 0)
        }
        if (intent.getIntExtra("mFieldManagerID", 0) != 0) {
            userSelectedHome = intent.getIntExtra("mFieldManagerID", 0)
        }
        rv_performers.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter = CustomerMappingAdapter(this, mCustomerList)
        rv_performers.adapter = adapter
    }


}