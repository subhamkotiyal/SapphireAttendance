package com.webgurus.attendanceportal.ui.permission

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.adapter.AddPermission2Adapter
import com.webgurus.attendanceportal.listeners.CheckBoxChangedSelectedListeners
import com.webgurus.attendanceportal.listeners.RecyclerViewClickListenersForOrder
import com.webgurus.attendanceportal.pojo.AddPermissionPojo
import com.webgurus.attendanceportal.pojo.GetRolesPojo
import com.webgurus.attendanceportal.pojo.PermissionData
import com.webgurus.attendanceportal.pojo.PermissionPojo
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_addpermission.*
import kotlinx.android.synthetic.main.activity_addpermission.rl_listingpermission
import kotlinx.android.synthetic.main.fragment_createuser.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPermission : BaseActivity(), AdapterView.OnItemSelectedListener,
    CheckBoxChangedSelectedListeners {

    var adapter: AddPermission2Adapter? = null
    var sharedPreferences: SharedPreferences? = null
    var mModulesList: ArrayList<PermissionData> = ArrayList()
    val list: ArrayList<String> = ArrayList()
    val list_spinner: ArrayList<Int> = ArrayList()
    val NEW_SPINNER_ID = 1
    lateinit var sp_rolelisting: Spinner
    var iv_back: ImageView? = null
    var role_id: Int = 0

    var mPermissionList: ArrayList<String> = ArrayList()
    var permissionID :Int =0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addpermission)
        getIntentForUpdate()
        initview()
        listeners()
        hitApitoshowAllPermission()
        hitApitogetRoles()
    }

    private fun getIntentForUpdate() {
        if (intent.getStringExtra("permission_id") != null) {
            tv_permissionheadertittle.setText("Update Permission")
            btn_submitpermission.setText("Update")
        }else{
            tv_permissionheadertittle.setText("Add Permission")
            btn_submitpermission.setText("Submit")
        }
    }

    private fun listeners() {
        iv_back!!.setOnClickListener(View.OnClickListener {
            finish()
        })
        btn_submitpermission.setOnClickListener(View.OnClickListener {
            if (role_id == 0) {
                Toast.makeText(this, "Please select the role first.", Toast.LENGTH_SHORT).show()
            }
           else if (mPermissionList.size == 0) {
                Toast.makeText(this, "Please select atleast one permission.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                hitApitoAddPermission()
            }

        })
    }

    private fun hitApitoAddPermission() {
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.addPermission(
            "Bearer " + sharedPreferences!!.getString("Access_Token", ""),
            role_id,
            mPermissionList.toString().replace("[","").replace("]","")
        )
        showLoading(true)
        call.enqueue(object : Callback<AddPermissionPojo?> {
            override fun onResponse(
                call: Call<AddPermissionPojo?>,
                response: Response<AddPermissionPojo?>
            ) {

                hideProgress()
                if (response.body()!!.status == 1) {
                    Toast.makeText(
                        this@AddPermission,
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@AddPermission,
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<AddPermissionPojo?>, t: Throwable) {
                Toast.makeText(this@AddPermission, "" + t.toString(), Toast.LENGTH_SHORT).show()
                hideProgress()
            }

        })
    }


    private fun initview() {
        iv_back = findViewById(R.id.iv_backpermission)
        sharedPreferences = this.getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE)
        sp_rolelisting = Spinner(this)
        sp_rolelisting.id = 1
    }

    private fun setRecyclerview(mModulesList: ArrayList<PermissionData>) {
        rl_listingpermission.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter = AddPermission2Adapter(this, mModulesList, this)
        rl_listingpermission.adapter = adapter
    }


    private fun hitApitogetRoles() {
        list.clear()
        list_spinner.clear()
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getRoles("Bearer " + sharedPreferences!!.getString("Access_Token", ""))
        showLoading(true)
        call.enqueue(object : Callback<GetRolesPojo?> {
            override fun onResponse(
                call: Call<GetRolesPojo?>,
                response: Response<GetRolesPojo?>
            ) {

                Log.e("Response", response.body().toString())
                list.add("Select Role")
                for (i in 0 until response.body()!!.data.size) {
                    list.add(response.body()!!.data[i].role)
                    list_spinner.add(response.body()!!.data[i].id)
                }
                setSpinner()
            }

            override fun onFailure(call: Call<GetRolesPojo?>, t: Throwable) {
                Toast.makeText(this@AddPermission, "" + t.toString(), Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setSpinner() {
        var aa = ArrayAdapter(this@AddPermission, R.layout.simple_spinner_list, list)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_rolelisting.id = NEW_SPINNER_ID
        val ll = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        //  ll.setMargins(10, 40, 10, 10)
        rl_spinnerpermission.addView(sp_rolelisting)
        aa = ArrayAdapter(this@AddPermission, R.layout.spinner_right_aligned, list)
        aa.setDropDownViewResource(R.layout.spinner_right_aligned)
        with(sp_rolelisting)
        {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener = this@AddPermission
            layoutParams = ll
            prompt = "Select your favourite language"

        }
    }

    private fun hitApitoshowAllPermission() {
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call =
            service.getAllPermissions("Bearer " + sharedPreferences!!.getString("Access_Token", ""))
        showLoading(true)
        call.enqueue(object : retrofit2.Callback<PermissionPojo?> {
            override fun onResponse(
                call: Call<PermissionPojo?>,
                response: Response<PermissionPojo?>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.message.equals("Modules fetched")) {
                        mModulesList.addAll(response.body( )!!.data)
                        setRecyclerview(mModulesList)
                    } else {
                        Toast.makeText(
                            this@AddPermission,
                            "" + response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    hideProgress()
                }
            }

            override fun onFailure(call: Call<PermissionPojo?>, t: Throwable) {
                Toast.makeText(this@AddPermission, "" + t.toString(), Toast.LENGTH_SHORT).show()
            }

        })

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent!!.getId()) {
            1 -> {
                sp_rolelisting.setSelection(position)
                role_id = list_spinner.get(position - 1)
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onChecked(position: Int, id: Int, tittle: String) {
        mPermissionList.add(tittle)
    }

    override fun onUnChecked(position: Int, id: Int, tittle: String) {
        mPermissionList.remove(tittle)
    }

}