package com.webgurus.attendanceportal.ui.unitmanage

import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.AddUnitPojo
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_addcategory.*
import kotlinx.android.synthetic.main.activity_newunit.*
import kotlinx.android.synthetic.main.fragment_unitlisting.*
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception

class AddNewUnitActivity : BaseActivity() {

    var sharedPreferences: SharedPreferences? = null
    lateinit var ed_addunit: TextInputEditText
    var unitID=0


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newunit)
        iniview()
        listeners()
    }

    private fun iniview() {
        ed_addunit = findViewById<View>(R.id.ed_addunit) as TextInputEditText
        sharedPreferences = getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE)
        if(intent.getIntExtra("id",0)!=0){
            unitID=intent.getIntExtra("id",0)
            ed_addunit.setText(intent.getStringExtra("Unit"))
            tv_header.setText("Edit Unit")
            btn_addRole.setText("Update Unit")
        }else{
            tv_header.setText("Add Unit")
            btn_addRole.setText("Submit")
        }
    }


    private fun listeners() {
        iv_backunit.setOnClickListener(View.OnClickListener {
            finish()
        })
        btn_addRole.setOnClickListener(View.OnClickListener {
            if(ed_addunit.text.toString().equals("")){
                Toast.makeText(this,"This Field is Mandatory to Fill",Toast.LENGTH_SHORT).show()
            }
            else if(unitID==0){
                hitApitogetUnitlist()
            }else{
                hitApitoUpdateUnitlist(ed_addunit.text.toString(),unitID!!)
            }
        })
    }




    private fun hitApitogetUnitlist() {
        showLoading(true)
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getAddUnit("Bearer " + sharedPreferences!!.getString("Access_Token", ""), ed_addunit.text.toString())
        call.enqueue(object : retrofit2.Callback<AddUnitPojo?> {
            override fun onFailure(call: Call<AddUnitPojo?>, t: Throwable) {
                hideProgress()

            }

            override fun onResponse(
                call: Call<AddUnitPojo?>?,
                response: Response<AddUnitPojo?>?
            ) {
                hideProgress()
                Log.e("Response", response?.body().toString())
                try {
                    Toast.makeText(this@AddNewUnitActivity,""+response!!.body()!!.message,Toast.LENGTH_SHORT).show()
                    finish()

                } catch (e: Exception) {

                }
            }
        })
    }


    private fun hitApitoUpdateUnitlist(unitstring:String,id:Int) {
        showLoading(true)
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getUpdateUnit(
            "Bearer " + sharedPreferences!!.getString("Access_Token", ""),id,unitstring)
        call.enqueue(object : retrofit2.Callback<AddUnitPojo?> {
            override fun onFailure(call: Call<AddUnitPojo?>, t: Throwable) {
                Toast.makeText(this@AddNewUnitActivity, "Failed", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<AddUnitPojo?>?,
                response: Response<AddUnitPojo?>?
            ) {
                hideProgress()
                Log.e("Response", response?.body().toString())
                try {
                    Toast.makeText(this@AddNewUnitActivity,""+response!!.body()!!.message,Toast.LENGTH_SHORT).show()
                    finish()
                } catch (e: Exception) {

                }
            }
        })
    }

}
