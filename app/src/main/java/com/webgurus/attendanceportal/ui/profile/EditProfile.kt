package com.webgurus.attendanceportal.ui.profile

import android.content.Intent
import android.content.SharedPreferences
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.baseproject.utils.Utils
import com.example.baseproject.utils.Utils.isEmailValid
import com.example.baseproject.utils.Utils.isValidMobile
import com.webgurus.attendanceportal.InternetSettingCheck
import com.webgurus.attendanceportal.LocationPermissionCheck
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.UpdatedProfilePojo
import com.webgurus.attendanceportal.pojo.UpdateprofilePojo
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_editprofile.*
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Response


class EditProfile : BaseActivity() {

    var sharedPreferences : SharedPreferences? =null
    var editor : SharedPreferences.Editor ? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)

        val imgBack : ImageView? = findViewById(R.id.imgBack)
        imgBack?.setOnClickListener {
            finish()
        }

        initview()
        listeners()
        hitGetProfileAPI()

    }


    private fun listeners() {
        btn_updateprofile.setOnClickListener {
            if (ed_username.text.toString().equals("")) {
                Toast.makeText(this, "Username is mandatory to fill.", Toast.LENGTH_SHORT).show()
            } else if (ed_emailaddress.text.toString().equals("")) {
                Toast.makeText(this, "Email is mandatory to fill.", Toast.LENGTH_SHORT).show()
            } else if (!isEmailValid(ed_emailaddress.text.toString())) {
                Toast.makeText(this, "Invalid email address.", Toast.LENGTH_SHORT).show()
            } else if (ed_phonenumber.text.toString().equals("")) {
                Toast.makeText(this, "Phone number is mandatory to fill.", Toast.LENGTH_SHORT)
                    .show()
            } else if (!isValidMobile(ed_phonenumber.text.toString())) {
                Toast.makeText(this, "Invalid phone number.", Toast.LENGTH_SHORT).show()
            } else {
                val username: String = ed_username.text.toString()
                val email: String = ed_emailaddress.text.toString()
                val mobileNumber: String = ed_phonenumber.text.toString()
                hitUpdateProfileAPI(username, email, mobileNumber, "", "")
            }
        }
    }


    private fun checkGPS(): Boolean
    {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }



    private fun initview() {
        sharedPreferences= getSharedPreferences(
            "AcessToken",
            MODE_PRIVATE
        )
        editor=sharedPreferences!!.edit()
    }

    //API Call
    private fun  hitGetProfileAPI() {

        if (!Utils.isConnected(this)) {
            Toast.makeText(
                this,
                "No internet connection available. Please check your internet connection.",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(this, InternetSettingCheck::class.java))
        } else if (!checkGPS()) {
            Toast.makeText(this, "GPS is not active.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LocationPermissionCheck::class.java))
        } else {

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

                    try {
                        editor!!.putString("User_Email", response.body()!!.success.email)
                        editor!!.putString("User_Name", response.body()!!.success.name)
                        editor!!.commit()

                        if (response.body()!!.success.name != null) {
                            ed_username.setText(response.body()!!.success.name)
                        } else {
                            ed_username.setText("Full name")
                        }

                        if (response.body()!!.success.email != null) {
                            ed_emailaddress.setText(response.body()!!.success.email)
                        } else {
                            ed_emailaddress.setText("Email id")
                        }
                        if (response.body()!!.success.phone_number != null) {
                            ed_phonenumber.setText(response.body()!!.success.phone_number)
                        } else {
                            ed_phonenumber.setText("Mobile number")
                        }

                    } catch (e: Exception) {
                        Toast.makeText(
                            this@EditProfile,
                            resources.getString(R.string.servernotrespond),
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }

                override fun onFailure(call: Call<UpdateprofilePojo?>, t: Throwable) {
                    Toast.makeText(
                        this@EditProfile,
                        resources.getString(R.string.servernotrespond),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun hitUpdateProfileAPI(
        username: String,
        email: String,
        phoneNumber: String,
        address: String,
        dob: String

    ) {

        if (!Utils.isConnected(this)) {
            Toast.makeText(
                this,
                "No internet connection available. Please check your internet connection.",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(this, InternetSettingCheck::class.java))
        } else if (!checkGPS()) {
            Toast.makeText(this, "GPS is not active.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LocationPermissionCheck::class.java))
        } else {
               showLoading(true)
            val service =
                RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
            val call = service.updateprofile(
                "Bearer " + sharedPreferences!!.getString(
                    "Access_Token",
                    ""
                ),
                phoneNumber,
                dob,
                address,
                username,
                email
            )

            call.enqueue(object : retrofit2.Callback<UpdatedProfilePojo?> {
                override fun onResponse(
                    call: Call<UpdatedProfilePojo?>,
                    response: Response<UpdatedProfilePojo?>
                ) {
                    hideProgress()
                    try {

                        Toast.makeText(
                            this@EditProfile,
                            "Profile successfully updated.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        finish()

                    } catch (e: Exception) {
                        Toast.makeText(
                            this@EditProfile,
                            resources.getString(R.string.servernotrespond),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

                override fun onFailure(call: Call<UpdatedProfilePojo?>, t: Throwable) {
                    hideProgress()
                    Toast.makeText(
                        this@EditProfile,
                        resources.getString(R.string.servernotrespond),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            })
        }
    }

}