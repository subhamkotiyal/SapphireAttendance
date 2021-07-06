package com.webgurus.attendanceportal.ui.bills

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.utils.Utils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.webgurus.attendanceportal.InternetSettingCheck
import com.webgurus.attendanceportal.LocationPermissionCheck
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.adapter.AddPermission2Adapter
import com.webgurus.attendanceportal.adapter.CustomerBillsAdapter
import com.webgurus.attendanceportal.listeners.RecyclerViewRoleClickListeners
import com.webgurus.attendanceportal.pojo.DeleteBillsPojo
import com.webgurus.attendanceportal.pojo.GetAllBillsPojo
import com.webgurus.attendanceportal.pojo.GetBillData
import com.webgurus.attendanceportal.pojo.UpdateprofilePojo
import com.webgurus.attendanceportal.ui.base.BaseFragment
import com.webgurus.attendanceportal.ui.productmanagement.AddProductManagementActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.fragment_bills.*
import retrofit2.Call
import retrofit2.Response

class BillsFragments  : BaseFragment() , RecyclerViewRoleClickListeners {

    var editor : SharedPreferences.Editor ? =null
    var sharedPreferences : SharedPreferences? =null
    var mBillList : ArrayList<GetBillData> = ArrayList()
    var adapter : CustomerBillsAdapter ? = null

    private  var fb_add_bill: FloatingActionButton? = null
    private  var fab1_import_bill: FloatingActionButton? = null
    private  var fab_export_bill: FloatingActionButton? = null


    private  var fab_main_addbill: FloatingActionButton? = null


    private var fab_open: Animation? = null
    private  var fab_close: Animation? = null
    private  var fab_clock: Animation? = null
    private  var fab_anticlock: Animation? = null

    private  var textview_mail: TextView? = null
    private  var textview_share: TextView? = null
    private  var textview_addproduct: TextView? = null

    var isOpen = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bills, container, false)
    }

    override fun onClick(position: Int, roleID: Int) {
        showAlertLogout(position,roleID)
    }


    private fun showAlertLogout(position: Int,billid: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Bill")
        builder.setMessage("Are you sure you want to delete this Bill ?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes")

        { dialogInterface, which ->
            hitApitoDeleteBills(billid)
            mBillList.removeAt(position)
            adapter!!.notifyItemChanged(position)
        }

        builder.setNegativeButton("No") { dialogInterface, which ->
            //  Toast.makeText(applicationContext, "clicked No", Toast.LENGTH_LONG).show()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }


    private fun hitApitoDeleteBills(billid: Int){
        showLoading(true)
        if(!Utils.isConnected(requireActivity())){
            Toast.makeText(
                requireContext(),
                "No internet connection available. Please check your internet connection.",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(requireActivity(), InternetSettingCheck::class.java))
        }
        else {
            val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
            val call = service.deleteBills("Bearer " + sharedPreferences!!.getString("Access_Token", ""),billid )
            call.enqueue(object : retrofit2.Callback<DeleteBillsPojo?> {
                override fun onResponse(
                    call: Call<DeleteBillsPojo?>,
                    response: Response<DeleteBillsPojo?>
                ) {
                    try {

                     Toast.makeText(requireContext(),""+response.body()!!.message,Toast.LENGTH_SHORT).show()

                    } catch (e: Exception) {
                        Toast.makeText(
                            requireContext(),
                           ""+e.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    hideProgress()
                }

                override fun onFailure(call: Call<DeleteBillsPojo?>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        t.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    hideProgress()

                }
            })

        }
    }

    override fun onResume() {
        super.onResume()
        getAllBills()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initview()
        setRecyclerview()
    }


    private fun setRecyclerview() {
        rv_billslisting.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        adapter = CustomerBillsAdapter(requireContext(),mBillList,this)
        rv_billslisting.adapter = adapter
    }

    private fun getAllBills() {
        showLoading(true)
        if(!Utils.isConnected(requireActivity())){
            Toast.makeText(
                requireContext(),
                "No internet connection available. Please check your internet connection.",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(requireActivity(), InternetSettingCheck::class.java))
        }
        else {
            mBillList.clear()
            val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
            val call = service.getBills("Bearer " + sharedPreferences!!.getString("Access_Token", ""), "application/json")
            call.enqueue(object : retrofit2.Callback<GetAllBillsPojo?> {
                override fun onResponse(
                    call: Call<GetAllBillsPojo?>,
                    response: Response<GetAllBillsPojo?>
                ) {
                    try {
                        mBillList.addAll(response.body()!!.data)
                        adapter!!.notifyDataSetChanged()
                    } catch (e: Exception) {
                        Toast.makeText(
                            requireContext(),
                            resources.getString(R.string.servernotrespond),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    hideProgress()
                }

                override fun onFailure(call: Call<GetAllBillsPojo?>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.servernotrespond),
                        Toast.LENGTH_SHORT
                    ).show()
                    hideProgress()

                }
            })

        }
    }

    private fun initview() {
        sharedPreferences= requireActivity().getSharedPreferences(
            "AcessToken",
            AppCompatActivity.MODE_PRIVATE
        )

        editor=sharedPreferences!!.edit()
    }
}