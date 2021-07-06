package com.webgurus.attendanceportal.ui.permission

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.AllListPermissionData
import com.webgurus.attendanceportal.pojo.CreateuserPojo
import com.webgurus.attendanceportal.pojo.PermissionListPojo
import com.webgurus.attendanceportal.ui.base.BaseFragment
import com.webgurus.attendanceportal.ui.category.AddNewCategoryActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.fragment_orderlisting.rv_orderlisting
import kotlinx.android.synthetic.main.fragment_permission.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PermissionFragment : BaseFragment() {

    var adapter: PermissionAdapter? = null
    var fab_main_permission: FloatingActionButton?=null
    var sharedPreferences: SharedPreferences? = null
    var mListPermissions:ArrayList<AllListPermissionData> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_permission, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initview()
        listeners()
    }


    override fun onResume() {
        super.onResume()
        hitApitoGetPermissionList()
    }

    private fun listeners() {
        fab_main_permission!!.setOnClickListener {
            val intent = Intent(context, AddPermission::class.java)
            requireActivity().startActivity(intent)
        }
    }

    private fun initview() {
        sharedPreferences = requireActivity().getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE)
        fab_main_permission=requireActivity().findViewById(R.id.fab_main_permission)
    }

    private fun hitApitoGetPermissionList() {
        mListPermissions.clear()
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getPermissionList("Bearer " + sharedPreferences!!.getString("Access_Token", "")
        )
        showLoading(true)
        call.enqueue(object : Callback<PermissionListPojo?> {
            override fun onResponse(
                call: Call<PermissionListPojo?>,
                response: Response<PermissionListPojo?>
            ) {
                hideProgress()
                if (response.body()!!.message.equals("Modules fetched")) {
                    hideProgress()
                    mListPermissions.addAll(response.body()!!.data)
                    rv_permissionlisitng.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                    adapter = PermissionAdapter(requireActivity(),mListPermissions)
                    rv_permissionlisitng.adapter = adapter

                } else {
                    Toast.makeText(activity, "" + response.body()!!.message, Toast.LENGTH_SHORT).show()
                    hideProgress()
                }


            }

            override fun onFailure(call: Call<PermissionListPojo?>, t: Throwable) {
                Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show()
                hideProgress()
            }

        })


    }



}