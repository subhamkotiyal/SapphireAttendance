package com.webgurus.attendanceportal.ui.category

import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.AddCategoryPojo
import com.webgurus.attendanceportal.pojo.AddRolePojo
import com.webgurus.attendanceportal.pojo.UpdateCategoryPojo
import com.webgurus.attendanceportal.pojo.UpdateRolePojo
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_addcategory.*
import retrofit2.Call
import retrofit2.Response

class AddNewCategoryActivity   : BaseActivity()  {

    var sharedPreferences: SharedPreferences? = null
    var catID : Int?=0



    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addcategory)
        initview()
        getInrtent()
        listeners()
    }


    private fun getInrtent() {
        if(intent!=null){
            if(intent.getIntExtra("categID",0)!=0){
                catID=intent.getIntExtra("categID",0)
                ed_addcategory!!.setText(intent.getStringExtra("CatName"))
                tv_addcatheader.setText("Edit Category")
            }
        }else{
            tv_addcatheader.setText("Add Category")
        }

    }
    private fun initview() {
        sharedPreferences = this.getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE)
    }

    private fun listeners() {
        iv_backcat.setOnClickListener(View.OnClickListener {
            finish()
        })
        btn_addCategory.setOnClickListener(View.OnClickListener {
            if(ed_addcategory.text.toString().equals("")){
                Toast.makeText(this,"This Field is Mandatory to Fill",Toast.LENGTH_SHORT).show()
            }
            else if(catID==0){
                hitApiToCategory(ed_addcategory.text.toString())
            }else{
                hitApiToUpdateCategory(catID!!,ed_addcategory.text.toString())
            }
        })
    }

    private fun hitApiToUpdateCategory(id:Int,category : String) {

        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.updateCategory("Bearer " + sharedPreferences!!.getString("Access_Token", ""),
            "application/json", id,category)
        showLoading(true)
        call.enqueue(object : retrofit2.Callback<UpdateCategoryPojo?> {
            override fun onResponse(call: Call<UpdateCategoryPojo?>, response: Response<UpdateCategoryPojo?>) {
                if(response.body()!=null){
                    if(response.body()!!.status==1){
                        Toast.makeText(this@AddNewCategoryActivity,"Category Updated Sucessfully",Toast.LENGTH_SHORT).show()
                        finish()
                        hideProgress()
                    }else{
                        Toast.makeText(this@AddNewCategoryActivity,""+response.body()!!.message,Toast.LENGTH_SHORT).show()
                        finish()
                        hideProgress()
                    }

                }

            }

            override fun onFailure(call: Call<UpdateCategoryPojo?>, t: Throwable) {
                Toast.makeText(this@AddNewCategoryActivity,""+ t.toString(),Toast.LENGTH_SHORT).show()
            }

        })
    }


    private fun hitApiToCategory(catName : String){
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.addCategory("Bearer " + sharedPreferences!!.getString("Access_Token", ""),"application/json",catName)
        showLoading(true)
        call.enqueue(object : retrofit2.Callback<AddCategoryPojo?> {
            override fun onResponse(call: Call<AddCategoryPojo?>, response: Response<AddCategoryPojo?>) {
                if(response.body()!=null){
                    if(response!!.body()!!.status==1){
                        Toast.makeText(this@AddNewCategoryActivity,""+ response.body()!!.message, Toast.LENGTH_SHORT).show()
                        finish()
                        hideProgress()
                    }else{
                        Toast.makeText(this@AddNewCategoryActivity,""+ response.body()!!.message, Toast.LENGTH_SHORT).show()
                        hideProgress()
                    }
                }

            }

            override fun onFailure(call: Call<AddCategoryPojo?>, t: Throwable) {
                Toast.makeText(this@AddNewCategoryActivity,""+ t.toString(), Toast.LENGTH_SHORT).show()
            }

        })

    }



}
