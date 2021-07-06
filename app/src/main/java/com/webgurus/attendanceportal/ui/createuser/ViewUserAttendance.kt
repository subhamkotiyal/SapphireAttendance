package com.webgurus.attendanceportal.ui.createuser

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.adapter.AttendanceAdapter
import com.webgurus.attendanceportal.pojo.AttendanceListPojo
import com.webgurus.attendanceportal.pojo.SuccesAttendances
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_viewuserattendance.*
import kotlinx.android.synthetic.main.fragment_attendance.*
import kotlinx.android.synthetic.main.fragment_attendance.rv_attendancelisting
import kotlinx.android.synthetic.main.fragment_attendance.tv_nodataavailable
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception

class ViewUserAttendance : BaseActivity() {

    var sharedPreferences: SharedPreferences? = null
    var userID : Int =0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewuserattendance)
        initview()
        listeners()
        if(userID!=0){
            hitApitogetAllAttendance()
        }

    }

    private fun listeners() {
        iv_backfromattendance.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun hitApitogetAllAttendance() {
        showLoading(true)
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getSingleUserAttendanceList(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            ),userID
        )
        call.enqueue(object : retrofit2.Callback<AttendanceListPojo?> {
            override fun onFailure(call: Call<AttendanceListPojo?>, t: Throwable) {
                Toast.makeText(this@ViewUserAttendance, "Failed", Toast.LENGTH_SHORT).show()
                    hideProgress()
            }

            override fun onResponse(
                call: Call<AttendanceListPojo?>?,
                response: Response<AttendanceListPojo?>?
            ) {

                try {
                    hideProgress()
                    val mAttendList: ArrayList<SuccesAttendances> = ArrayList()
                    mAttendList.addAll(response!!.body()!!.success)
                    if (mAttendList.size > 0) {
                        rv_attendancelisting.layoutManager =
                            LinearLayoutManager(this@ViewUserAttendance, RecyclerView.VERTICAL, false)
                        val adapter = AttendanceAdapter(mAttendList, this@ViewUserAttendance)
                        rv_attendancelisting.adapter = adapter
                        tv_nodataavailable.visibility = View.INVISIBLE
                    } else {
                        Toast.makeText(this@ViewUserAttendance, "No Data Available", Toast.LENGTH_SHORT).show()
                        tv_nodataavailable.visibility = View.VISIBLE
                        hideProgress()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@ViewUserAttendance,
                        resources.getString(R.string.servernotrespond),
                        Toast.LENGTH_SHORT
                    ).show()
                    hideProgress()
                }
            }
        })
    }

    private fun initview() {
        sharedPreferences = this.getSharedPreferences(
            "AcessToken",
            AppCompatActivity.MODE_PRIVATE
        )
        if(intent!=null){
           userID=intent.getIntExtra("id",0)

        }
    }



}