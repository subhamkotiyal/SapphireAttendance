package com.webgurus.attendanceportal.ui.customer

import android.content.Intent.getIntent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.adapter.ManagersAdapter
import com.webgurus.attendanceportal.listeners.RecyclerViewClickListeners
import com.webgurus.attendanceportal.pojo.FieldData
import com.webgurus.attendanceportal.pojo.FiledManagersPojo
import com.webgurus.attendanceportal.pojo.ManagerAssignPojo
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.bottom_sheet_managers.*
import retrofit2.Call
import retrofit2.Response


class ShowAllManagersBottomSheet : BaseActivity() , RecyclerViewClickListeners {

    var sharedPreferences: SharedPreferences? = null
    var mNameList:ArrayList<String> = ArrayList()
    var mManagerList: java.util.ArrayList<FieldData> = ArrayList()
    var mListId: ArrayList<Int> = ArrayList()

    override fun onClick(position: Int) {
        mManagerList.get(position).id
        showAlert(mManagerList.get(position).id.toString(), mManagerList.get(position).name)
    }

    private fun initview() {
        mListId=intent.getIntegerArrayListExtra("customerdetails")!!
        sharedPreferences =
            this.getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_sheet_managers)
        initview()
        listeners()
        hitApitogetFieldManagers()
    }

    private fun showAlert(id: String, name: String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Manager Assiging")
        builder.setMessage("Are you sure  you want to assign customer to " + name)
        builder.setIcon(R.mipmap.ic_password)
        builder.setPositiveButton("Yes"){ dialogInterface, which ->
           hitApitoAssignManager(Integer.parseInt(id))
        }
        builder.setNegativeButton("No"){ dialogInterface, which ->

        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun hitApitoAssignManager(id: Int) {

        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.assignmanager("Bearer " + sharedPreferences!!.getString("Access_Token", ""),
             id,mListId.toString().replace("[","").replace("]","")
            )
        showLoading(true)
        call.enqueue(object : retrofit2.Callback<ManagerAssignPojo?> {
            override fun onResponse(call: Call<ManagerAssignPojo?>, response: Response<ManagerAssignPojo?>) {
                if(response.body()!=null){
                    if(response.body()!!.message.equals("Successfully Assigned")){
                        Toast.makeText(this@ShowAllManagersBottomSheet,""+response.body()!!.message,Toast.LENGTH_SHORT).show()
                         finish()
                    }else{
                        Toast.makeText(this@ShowAllManagersBottomSheet,""+response.body()!!.message,Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    hideProgress()

                }

            }

            override fun onFailure(call: Call<ManagerAssignPojo?>, t: Throwable) {
                Toast.makeText(this@ShowAllManagersBottomSheet,""+t.toString(),Toast.LENGTH_SHORT).show()
            }

        })


    }


    private fun listeners() {
        iv_close.setOnClickListener(View.OnClickListener {
                 finish()
        })
    }


    private fun hitApitogetFieldManagers() {
        mNameList.clear()
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call =
            service.getFieldManager("Bearer " + sharedPreferences!!.getString("Access_Token", ""))
        call.enqueue(object : retrofit2.Callback<FiledManagersPojo> {
            override fun onResponse(
                call: Call<FiledManagersPojo?>,
                response: Response<FiledManagersPojo?>
            ) {
                if (response.body() != null) {
                    mManagerList.addAll(response.body()!!.data)
                    setUpViews()
                }
            }

            override fun onFailure(call: Call<FiledManagersPojo>, t: Throwable) {
            }

        })

    }

    private fun setUpViews() {
        rv_managers.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter = ManagersAdapter(this, mManagerList, this)
        rv_managers.adapter = adapter

    }


}