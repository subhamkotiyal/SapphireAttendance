package com.webgurus.attendanceportal.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.androidnetworking.AndroidNetworking
import com.example.baseproject.utils.Utils
import com.webgurus.attendanceportal.InternetSettingCheck
import com.webgurus.attendanceportal.LocationPermissionCheck
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.UpdateprofilePojo
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Response


class ProfileFragment : Fragment()  {

    var sharedPreferences : SharedPreferences? =null
    var progressBar : ProgressBar?=null
    var editor : SharedPreferences.Editor ? =null
    var mContext : Context ? =null

    fun checkGPS(): Boolean
    {
        val locationManager = requireActivity().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidNetworking.initialize(requireContext())
    }

    private fun showProgressbar() {
        progressBar!!.visibility=View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         initview()
        listeners()
        tv_phonenumber.text = ""
        tv_useremail.text = ""
        tv_username.text = ""
    }

    private fun initview()  {
        progressBar= requireView().findViewById<View>(R.id.pv_progress) as ProgressBar
        sharedPreferences= requireActivity().getSharedPreferences(
            "AcessToken",
            AppCompatActivity.MODE_PRIVATE
        )
        editor=sharedPreferences!!.edit()
    }

    override fun onResume() {
        super.onResume()
        hitGetProfileAPI()
    }

    private fun listeners() {
        rl_editprofile.setOnClickListener(View.OnClickListener {
            startActivity(Intent(activity, EditProfile::class.java))
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun  hitGetProfileAPI(){

        if(!Utils.isConnected(requireActivity())){
            Toast.makeText(
                mContext,
                "No internet connection available. Please check your internet connection.",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(requireActivity(), InternetSettingCheck::class.java))
        }
        else if (!checkGPS()) {
            Toast.makeText(mContext, "GPS is not active.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireActivity(), LocationPermissionCheck::class.java))
        }
        else {

            showProgressbar()

            val service =
                RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
            val call = service.getUserInfo(
                "Bearer " + sharedPreferences!!.getString(
                    "Access_Token",
                    ""
                ), "application/json"
            )
            call.enqueue(object : retrofit2.Callback<UpdateprofilePojo?> {
                override fun onResponse(
                    call: Call<UpdateprofilePojo?>,
                    response: Response<UpdateprofilePojo?>
                ) {
                    progressBar!!.visibility = View.INVISIBLE
                    try {
                        editor!!.putString("User_Email", response.body()!!.success.email)
                        editor!!.putString("User_Name", response.body()!!.success.name)
                        editor!!.commit()

                        if (response.body()!!.success.name != null) {
                            tv_username.text = response.body()!!.success.name
                            editor!!.putString("User_Name", response.body()!!.success.name)
                            editor!!.commit()
                        } else {
                            tv_username.text = ""
                        }

                        if (response.body()!!.success.email != null) {
                            tv_useremail.text = response.body()!!.success.email
                        } else {
                            tv_useremail.text = ""
                        }
                        if (response.body()!!.success.phone_number != null) {
                            tv_phonenumber.text = response.body()!!.success.phone_number
                        } else {
                            tv_phonenumber.text = ""
                        }

                    } catch (e: Exception) {
                        Toast.makeText(
                            mContext,
                            resources.getString(R.string.servernotrespond),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<UpdateprofilePojo?>, t: Throwable) {
                    Toast.makeText(
                        mContext,
                        resources.getString(R.string.servernotrespond),
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar!!.visibility = View.INVISIBLE

                }
            })

        }
    }
}