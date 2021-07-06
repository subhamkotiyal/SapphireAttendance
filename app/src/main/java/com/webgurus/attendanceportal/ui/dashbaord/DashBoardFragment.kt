package com.webgurus.attendanceportal.ui.dashbaord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.UserInforDatum
import com.webgurus.attendanceportal.ui.base.BaseFragment
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception

class DashBoardFragment : BaseFragment() {

      var tv_totalusercount : TextView ? =null
      var tv_totalattendance : TextView ? =null
      var tv_totalproducts : TextView ? =null
      var tv_totalorders : TextView ? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_userdashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initview()
        hitApitogettheDashBoardValues()
    }

    private fun initview() {
        tv_totalusercount=view!!.findViewById(R.id.tv_totalusercount)
        tv_totalattendance=view!!.findViewById(R.id.tv_totalattendance)
        tv_totalproducts=view!!.findViewById(R.id.tv_totalproducts)
        tv_totalorders=view!!.findViewById(R.id.tv_totalorders)
    }

    fun hitApitogettheDashBoardValues(){
        showLoading(true)
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val settings = requireActivity().getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE)
        val access_token = settings.getString("Access_Token", "")
        val call = service.getAllUserInfo("Bearer " + access_token)
        call.enqueue(object : retrofit2.Callback<UserInforDatum?> {
            override fun onFailure(call: Call<UserInforDatum?>, t: Throwable) {
                Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_SHORT).show()
                hideProgress()
            }

            override fun onResponse(
                call: Call<UserInforDatum?>?,
                response: Response<UserInforDatum?>?
            ) {
                hideProgress()
                try {
                    tv_totalusercount!!.setText(response!!.body()!!.data.total_users.toString())
                    tv_totalattendance!!.setText(response.body()!!.data.total_attendance.toString())
                    tv_totalproducts!!.setText(response.body()!!.data.total_products.toString())
                    tv_totalorders!!.setText(response.body()!!.data.total_orders.toString())

                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.servernotrespond),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

}