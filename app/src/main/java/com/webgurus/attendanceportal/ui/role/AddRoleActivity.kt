package com.webgurus.attendanceportal.ui.role

import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.adapter.RoleListingAdapter
import com.webgurus.attendanceportal.adapter.UserListingAdapter
import com.webgurus.attendanceportal.pojo.AddRolePojo
import com.webgurus.attendanceportal.pojo.GetRolesPojo
import com.webgurus.attendanceportal.pojo.UpdateRolePojo
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.fragment_addrole.*
import kotlinx.android.synthetic.main.fragment_rolelisting.*
import kotlinx.android.synthetic.main.fragment_userlisitng.*
import retrofit2.Call
import retrofit2.Response

class AddRoleActivity  : BaseActivity()  {


    var ed_addrole : TextInputEditText?=null
    var sharedPreferences : SharedPreferences? =null
    var roleID : Int=0


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onResume() {
        super.onResume()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_addrole)
        initview()
        getInrtent()
        listeners()
    }

    private fun getInrtent() {
        if(intent!=null){
            if(intent.getIntExtra("roleID",0)!=0){
                roleID=intent.getIntExtra("roleID",0)
                ed_addrole!!.setText(intent.getStringExtra("rolename"))
            }
        }

    }

    private fun initview() {
        ed_addrole=findViewById(R.id.ed_addrole)
        sharedPreferences= this.getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE)
    }

    private fun listeners() {
        iv_backrole.setOnClickListener(View.OnClickListener {
            finish()
        })
        btn_addRole.setOnClickListener(View.OnClickListener {
            if(ed_addrole!!.text.toString().equals("")){
                Toast.makeText(this,"Role is mandatory to fill",Toast.LENGTH_SHORT).show()
            }else if(roleID==0){
                showLoading(true)
                hitApiToAddRole(ed_addrole!!.text.toString())
            }else{
                showLoading(true)
                hitApiToUpdateRole(roleID ,ed_addrole!!.text.toString() )
            }
        })
    }

    private fun hitApiToUpdateRole(id:Int,role : String){
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.updateRole("Bearer " + sharedPreferences!!.getString("Access_Token", ""),"application/json",
        id,role)
        call.enqueue(object : retrofit2.Callback<UpdateRolePojo?> {
            override fun onResponse(call: Call<UpdateRolePojo?>, response: Response<UpdateRolePojo?>) {
                hideProgress()
                if(response.body()!=null){
                    Toast.makeText(this@AddRoleActivity,""+response.body()!!.message.toString(),Toast.LENGTH_SHORT).show()
                    finish()
                }

            }

            override fun onFailure(call: Call<UpdateRolePojo?>, t: Throwable) {
                Toast.makeText(this@AddRoleActivity,""+ t.toString(),Toast.LENGTH_SHORT).show()
                hideProgress()
            }

        })

    }


    private fun hitApiToAddRole(role : String){
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.addRoles("Bearer " + sharedPreferences!!.getString("Access_Token", ""),"application/json",role)
        // showProgressbar()
        call.enqueue(object : retrofit2.Callback<AddRolePojo?> {
            override fun onResponse(call: Call<AddRolePojo?>, response: Response<AddRolePojo?>) {
                if(response.body()!=null){
                    if(response!!.body()!!.status==1){
                        hideProgress()
                         Toast.makeText(this@AddRoleActivity,"Added Sucessfully",Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        hideProgress()
                        Toast.makeText(this@AddRoleActivity,""+ response.body()!!.message,Toast.LENGTH_SHORT).show()
                    }
                }

            }

            override fun onFailure(call: Call<AddRolePojo?>, t: Throwable) {
                Toast.makeText(this@AddRoleActivity,""+ t.toString(),Toast.LENGTH_SHORT).show()
                hideProgress()
            }

        })

    }


}
