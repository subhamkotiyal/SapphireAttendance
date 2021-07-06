package com.webgurus.attendanceportal.ui.reports

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.GetUserByRole
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_customermapping.*
import kotlinx.android.synthetic.main.activity_target_vs_acheived_second.*
import kotlinx.android.synthetic.main.activity_target_vs_achieved.*
import kotlinx.android.synthetic.main.activity_target_vs_achieved.rl_fieldmanager
import kotlinx.android.synthetic.main.activity_target_vs_achieved.rl_manager

class CustomerMappingActivity : BaseActivity(), AdapterView.OnItemSelectedListener {


    var mFieldManager: ArrayList<String> = ArrayList()
    var mManager: ArrayList<String> = ArrayList()


    var mIdFieldManagerList: ArrayList<Int> = ArrayList()
    var mIdManagerList: ArrayList<Int> = ArrayList()

    var sharedPreferences: SharedPreferences? = null

    lateinit var user_spinner: Spinner
    lateinit var manager_spinner: Spinner
    var search_executed: TextView? = null
    var mSelectedFieldManagerID = 0
    var mSelectedManagerID = 0
    var iv_backfromreport : ImageView ? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customermapping)
        initview()
        listeners()
        hitApitogetUser()
    }

    private fun listeners() {

        iv_backfromreport!!.setOnClickListener( {
            finish()
        })


        search_executed!!.setOnClickListener( {
            val intent = Intent(this, CustomerMappingDetailsActivity::class.java)
            intent.putExtra("mFieldManagerID", mSelectedFieldManagerID)
            startActivity(intent)
        })

        search_manager!!.setOnClickListener( {
            val intent = Intent(this, CustomerMappingDetailsActivity::class.java)
            intent.putExtra("mManagerID", mSelectedManagerID)
            startActivity(intent)
        })

    }

    private fun initview() {
        iv_backfromreport = findViewById(R.id.iv_backfromreport)
        search_executed = findViewById(R.id.search_executed)
        manager_spinner = Spinner(this)
        user_spinner = Spinner(this)
        manager_spinner.id = 1
        user_spinner.id = 2
        sharedPreferences = this.getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE)
    }


    private fun hitApitogetUser() {
        mFieldManager.clear()
        mManager.clear()
        mIdFieldManagerList.clear()
        mIdManagerList.clear()
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call =
            service.getuserByRole("Bearer " + sharedPreferences!!.getString("Access_Token", ""), 0)
        showLoading(true)
        call.enqueue(object : retrofit2.Callback<GetUserByRole?> {
            override fun onResponse(
                call: retrofit2.Call<GetUserByRole?>,
                response: retrofit2.Response<GetUserByRole?>
            ) {
                hideProgress()
                if (response.body() != null) {
                    for (i in 0 until response.body()!!.data.size) {
                        if (response.body()!!.data[i].role_id == 5) {
                            mFieldManager.add(response.body()!!.data[i].name)
                            mIdFieldManagerList.add(response.body()!!.data[i].id)

                        }
                        if (response.body()!!.data[i].role_id == 3) {
                            mManager.add(response.body()!!.data[i].name)
                            mIdManagerList.add(response.body()!!.data[i].id)
                        }
                    }
                    setSpinner()
                }

            }

            override fun onFailure(call: retrofit2.Call<GetUserByRole?>, t: Throwable) {
                hideProgress()
            }

        })


    }

    private fun setSpinner() {
        var managerAdapter = ArrayAdapter(this, R.layout.simple_spinner_list, mManager)
        managerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val managerlayout = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        rl_manager.addView(manager_spinner)
        managerAdapter = ArrayAdapter(this, R.layout.spinner_right_aligned, mManager)
        managerAdapter.setDropDownViewResource(R.layout.spinner_right_aligned)
        with(manager_spinner)
        {
            adapter = managerAdapter
            setSelection(0, false)
            onItemSelectedListener = this@CustomerMappingActivity
            layoutParams = managerlayout
            prompt = "Select filed Manager"
        }


        mSelectedFieldManagerID = mIdFieldManagerList.get(0)
        mSelectedManagerID = mIdManagerList.get(0)

        var fieldmanagerAdapter = ArrayAdapter(this, R.layout.simple_spinner_list, mFieldManager)
        fieldmanagerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val fieldmanagerlayout = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        rl_fieldmanager.addView(user_spinner)
        fieldmanagerAdapter = ArrayAdapter(this, R.layout.spinner_right_aligned, mFieldManager)
        fieldmanagerAdapter.setDropDownViewResource(R.layout.spinner_right_aligned)
        with(user_spinner)
        {
            adapter = fieldmanagerAdapter
            setSelection(0, false)
            onItemSelectedListener = this@CustomerMappingActivity
            layoutParams = fieldmanagerlayout
            prompt = "Select filed Manager"
        }


    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent!!.getId()) {
            1 -> {
                mSelectedManagerID = mIdManagerList.get(position)

            }
            2 -> {
                mSelectedFieldManagerID = mIdFieldManagerList.get(position)
            }

        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}