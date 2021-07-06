package com.webgurus.attendanceportal.ui.setting

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.CustomerUpdatedPojo
import com.webgurus.attendanceportal.pojo.SettingPojo
import com.webgurus.attendanceportal.ui.base.BaseFragment
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.fragment_setting.*
import retrofit2.Call
import retrofit2.Response


class SettingFragment: BaseFragment() {

     var btn_submit : Button ? = null
    var sharedPreferences : SharedPreferences? =null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initview()
        listeners()
    }

    private fun listeners() {
        btn_submit!!.setOnClickListener(View.OnClickListener {
            if(ed_numberdays.text.toString().isEmpty()){
                Toast.makeText(requireContext(),"Please enter the number of days",Toast.LENGTH_SHORT).show()
            }else{
                hitApitoAddNumberofDays()
            }
        })
    }

    private fun hitApitoAddNumberofDays() {
        showLoading(true)
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.addNumberofDays(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            ),ed_numberdays.text.toString()

        )

        call.enqueue(object : retrofit2.Callback<SettingPojo?> {
            override fun onFailure(call: Call<SettingPojo?>, t: Throwable) {
                hideProgress()
            }

            override fun onResponse(call: Call<SettingPojo?>, response: Response<SettingPojo?>) {
                if(response.body()!!.status==1){
                    Toast.makeText(requireContext(),response.body()!!.message,Toast.LENGTH_SHORT).show()
                    hideProgress()
                    requireActivity().finish()
                }else{
                    Toast.makeText(requireContext(),response.body()!!.message,Toast.LENGTH_SHORT).show()
                    hideProgress()
                }
            }

        })

    }


    private fun initview() {
        btn_submit=requireView().findViewById(R.id.btn_submit)
        sharedPreferences= requireActivity().getSharedPreferences(
            "AcessToken",
            AppCompatActivity.MODE_PRIVATE
        )
    }
}