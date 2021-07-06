package com.webgurus.attendanceportal.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.example.baseproject.utils.Utils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.webgurus.MainActivityFinal2
import com.webgurus.attendanceportal.InternetSettingCheck
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.LoginError
import com.webgurus.attendanceportal.pojo.Loginpojo
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.bottom_sheet_login.*
import retrofit2.Call
import retrofit2.Response


class LoginActivity : BaseActivity() {

    var sharedPreferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_sheet_login)
        AndroidNetworking.initialize(this)
        initview()
        listeners()
    }

    private fun listeners() {
        btn_login.setOnClickListener(View.OnClickListener {
            validations()
        })
    }


    private fun validations() {
        if (ed_emailaddress.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter email id or mobile number.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        if (ed_password.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter password.", Toast.LENGTH_SHORT).show()
            return
        }

        if (!(Utils.isEmailValid(ed_emailaddress.text.toString()) || Utils.isValidMobile(
                ed_emailaddress.text.toString()
            ))
        ) {
            Toast.makeText(this, "Invalid email id or mobile number.", Toast.LENGTH_SHORT).show()
            return
        }

        LoginApiTooth()

    }


    @SuppressLint("CommitPrefEdits")
    private fun initview() {
        sharedPreferences = getSharedPreferences("AcessToken", MODE_PRIVATE)
        editor = sharedPreferences!!.edit()
    }

    private fun LoginApiTooth() {

        if (!Utils.isConnected(this)) {
            Toast.makeText(this, "No internet connection available. Please check your internet connection.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@LoginActivity, InternetSettingCheck::class.java))
            return
        }
        showLoading(true)
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val settings = applicationContext.getSharedPreferences("FCMToken", MODE_PRIVATE)
        val aceess_token =settings.getString("access_token", "")
        val call = service.loginAuth(
            ed_emailaddress.text.toString(),
            ed_password.text.toString(),
            aceess_token
        )
        call.enqueue(object : retrofit2.Callback<Loginpojo?> {
            override fun onResponse(call: Call<Loginpojo?>, response: Response<Loginpojo?>) {
                hideProgress()
                try {
                    if (response.code() == 401) {
                        try {
                            val gson = Gson()
                            val type = object : TypeToken<LoginError>() {}.type
                            var errorResponse: LoginError? = gson.fromJson(
                                response.errorBody()!!.charStream(), type
                            )
                            val res = errorResponse!!.error
                            Toast.makeText(this@LoginActivity, res, Toast.LENGTH_LONG).show()
                        } catch (e: java.lang.Exception) {
                            Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_LONG).show()
                        }

                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Sucessfully login .",
                            Toast.LENGTH_SHORT
                        ).show()
                        if (response.body()!!.data.attendance_id == 0) {

                        } else {
                            editor!!.putInt("Access_Token", response.body()!!.data.attendance_id)
                        }
                        editor!!.putString("Access_Token", response.body()!!.success.token)
                        editor!!.putInt("User_ID", response.body()!!.data.id)
                        editor!!.putString("User_Name", response.body()!!.data.name)
                        editor!!.putString("Service_Time", response.body()!!.second)
                        editor!!.commit()
                        startActivity(Intent(this@LoginActivity, MainActivityFinal2::class.java))
                        finish()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@LoginActivity,
                        resources.getString(R.string.servernotrespond),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Loginpojo?>, t: Throwable) {
                Toast.makeText(
                    this@LoginActivity,
                    resources.getString(R.string.servernotrespond),
                    Toast.LENGTH_SHORT
                ).show()
                hideProgress()
            }
        })
    }


}
