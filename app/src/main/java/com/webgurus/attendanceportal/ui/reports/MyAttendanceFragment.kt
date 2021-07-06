package com.webgurus.attendanceportal.ui.reports

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.adapter.UserListingAdapter
import com.webgurus.attendanceportal.pojo.DataUser
import com.webgurus.attendanceportal.pojo.UserListingPojo
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_myattendance.*
import kotlinx.android.synthetic.main.fragment_createuser.*
import kotlinx.android.synthetic.main.fragment_createuser.rl_spinnermanager
import kotlinx.android.synthetic.main.fragment_userlisitng.*
import retrofit2.Call
import retrofit2.Response


class MyAttendanceFragment  : BaseActivity(), AdapterView.OnItemSelectedListener {


    var sharedPreferences : SharedPreferences? =null
    var mListuser: ArrayList<DataUser> = ArrayList()
    var mListUserName: ArrayList<String> = ArrayList()
    lateinit var sp_userslisitng: Spinner
    var agent_id : Int? =null
    val NEW_SPINNER_ID = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myattendance)
        initview()
        hitApitoGetUserListing()
        val search : Button=findViewById(R.id.btn_search)
        search.setOnClickListener(View.OnClickListener {
            intent = Intent(applicationContext, MyAttendanceReport::class.java)
            Log.e("agent_id",""+agent_id)
           intent.putExtra("agent_id",agent_id)
            startActivity(intent)
        })
    }

    private fun initview() {
    sharedPreferences= this.getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE)
     sp_userslisitng = Spinner(this)
      sp_userslisitng.id=NEW_SPINNER_ID
    }



    private fun hitApitoGetUserListing() {
        mListuser.clear()
        mListUserName.clear()
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getUserList("Bearer " + sharedPreferences!!.getString("Access_Token", ""))
        showLoading(true)
        call.enqueue(object : retrofit2.Callback<UserListingPojo?> {
            override fun onResponse(call: Call<UserListingPojo?>, response: Response<UserListingPojo?>) {
                if (response.body() != null) {
                    mListuser.addAll(response.body()!!.data)
                    for (i in 0 until response.body()!!.data.size){
                        mListUserName.add(response.body()!!.data[i].name)
                    }
                    setSpinner()
                    hideProgress()
                }

            }

            override fun onFailure(call: Call<UserListingPojo?>, t: Throwable) {
                hideProgress()
            }

        })
    }


    private fun setSpinner(){
        var aa =
            ArrayAdapter(
                this,
                R.layout.simple_spinner_item2,
                mListUserName
            )
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val ll = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        //  ll.setMargins(10, 40, 10, 10)
        rl_spinnerusers.addView(sp_userslisitng)
        aa = ArrayAdapter(
            this,
            R.layout.spinner_right_aligned,
            mListUserName
        )
        aa.setDropDownViewResource(R.layout.spinner_right_aligned)
        with(sp_userslisitng)
        {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener = this@MyAttendanceFragment
            layoutParams = ll
            prompt = "Select your favourite language"

        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
      //  Toast.makeText(this,""+id,Toast.LENGTH_LONG).show()
        agent_id=id.toInt()

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }


}