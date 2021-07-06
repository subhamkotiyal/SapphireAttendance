package com.webgurus.attendanceportal.ui.unitmanage

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.example.baseproject.utils.Utils.getFileName
import com.example.baseproject.utils.Utils.snackbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.listeners.RecyclerViewClickListeners
import com.webgurus.attendanceportal.listeners.RecyclerViewRoleClickListeners
import com.webgurus.attendanceportal.pojo.*
import com.webgurus.attendanceportal.ui.base.BaseFragment
import com.webgurus.attendanceportal.ui.productmanagement.AddProductManagementActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.fragment_productmanagement.*
import kotlinx.android.synthetic.main.fragment_rolelisting.*
import kotlinx.android.synthetic.main.fragment_unitlisting.*
import kotlinx.android.synthetic.main.fragment_userlisitng.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.Exception


class UnitListingFragment : BaseFragment() ,RecyclerViewRoleClickListeners {

    var sharedPreferences: SharedPreferences? = null
    val mUnitList: ArrayList<DataList> = ArrayList()
    var adapter : UnitListingAdapter?=null
    private var fab_main: FloatingActionButton? = null
    private  var fab1_mail: FloatingActionButton? = null
    private  var fb_export: FloatingActionButton? = null
    private  var fab1_add: FloatingActionButton? = null
    private var fab_open: Animation? = null
    private  var fab_close: Animation? = null
    private  var fab_clock: Animation? = null
    private  var fab_anticlock: Animation? = null

    var textview_mail: TextView? = null
    var textview_share: TextView? = null
    var isOpen = false
    var selectedImageUri: Uri?=null

    override fun onClick(position: Int, roleID: Int) {
        deleteItemDialog(position,roleID)
    }

    fun deleteItemDialog(index: Int,unitId : Int){
        var id= mUnitList.get(index).id
        val alertBuilder = AlertDialog.Builder(requireContext())
        alertBuilder.setTitle("Delete")
        alertBuilder.setMessage("Do you want to delete this Unit ?")
        alertBuilder.setPositiveButton("Delete"){_,_ ->
            mUnitList.removeAt(index)
            adapter!!.notifyDataSetChanged()
            hitApitoDeleteUnitlist(unitId)
        }

        alertBuilder.setNegativeButton("No"){_,_ ->

        }
        alertBuilder.show()
    }





    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_unitlisting, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniview()

    }


    private fun iniview() {
        fab_main = view!!.findViewById(R.id.fab_main_unit);
        fab1_mail = view!!.findViewById(R.id.fab1_import_unit);
        fab1_add = view!!.findViewById(R.id.fb_add_unit);
        fb_export = view!!.findViewById(R.id.fab_export_unit);
        fab_close = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_rotate_anticlock);

        textview_mail =view!!.findViewById(R.id.textview_mail)
        textview_share = view!!.findViewById(R.id.textview_share)

        sharedPreferences = requireActivity().getSharedPreferences(
            "AcessToken",
            AppCompatActivity.MODE_PRIVATE
        )

        search_edit_unit?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                filter(s.toString())
            }
        })
        fab_main?.setOnClickListener(View.OnClickListener {

            if (isOpen) {

                openAnim()
            } else {
                closeAnim()
            }


        })


        fb_export?.setOnClickListener(View.OnClickListener {
            hitApitoGetLink()
        })

        fab1_mail?.setOnClickListener(View.OnClickListener {
            selectCSVFile()

        })
        fab1_add?.setOnClickListener(View.OnClickListener {
            openAnim()
            startActivity(Intent(requireContext(), AddNewUnitActivity::class.java)
                .putExtra("Unit","")
                .putExtra("id","")
            )
        })
    }

    private fun openAnim(){
        textview_mail!!.setVisibility(View.INVISIBLE);
        textview_share!!.setVisibility(View.INVISIBLE);
        textview_addunit!!.setVisibility(View.INVISIBLE);
        fb_export!!.startAnimation(fab_close);
        fab1_mail!!.startAnimation(fab_close);
        fab1_add!!.startAnimation(fab_close);
        fab_main!!.startAnimation(fab_anticlock);
        fb_export!!.setClickable(false);
        fab1_mail!!.setClickable(false);
        isOpen = false;
    }

    private fun closeAnim(){
        textview_mail!!.setVisibility(View.VISIBLE);
        textview_share!!.setVisibility(View.VISIBLE);
        textview_addunit!!.setVisibility(View.VISIBLE);
        fb_export!!.startAnimation(fab_open);
        fab1_mail!!.startAnimation(fab_open);
        fab1_add!!.startAnimation(fab_open);
        fab_main!!.startAnimation(fab_clock);
        fb_export!!.setClickable(true);
        fab1_mail!!.setClickable(true);
        isOpen = true;
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === 11) {


            if (resultCode==Activity.RESULT_OK) {


                selectedImageUri = data?.data
                val parcelFileDescriptor =

                    requireActivity().contentResolver.openFileDescriptor(
                        selectedImageUri!!,
                        "r", null
                    ) ?: return

                val inputStream =
                    FileInputStream(parcelFileDescriptor.fileDescriptor)
                val file = File(
                    requireActivity().cacheDir,
                    requireActivity().contentResolver.getFileName(selectedImageUri!!)
                )
                val outputStream = FileOutputStream(file)
                inputStream.copyTo(outputStream)
                val requestFile: RequestBody = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    data?.getData()?.getPath()
                )

                val multipartBody: MultipartBody.Part =
                    MultipartBody.Part.createFormData("import", file.getName(), requestFile)

                hitApitoPostCSV(multipartBody)
            }else{

            }

        }
    }
    private fun hitApitoPostCSV(multipartBody: MultipartBody.Part) {

        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call =
            service.addImportUnit("Bearer " + sharedPreferences!!.getString("Access_Token", ""),
                multipartBody)
        showLoading(true)
        call.enqueue(object : retrofit2.Callback<ImportCustomer?> {
            override fun onResponse(call: Call<ImportCustomer?>, response: Response<ImportCustomer?>) {
                if (response.body() != null) {
                    rl_rootunit.snackbar(response.body()!!.message.toString())
                    hideProgress()
                }

            }

            override fun onFailure(call: Call<ImportCustomer?>, t: Throwable) {
                hideProgress()
            }

        })

    }
    private fun selectCSVFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        startActivityForResult(Intent.createChooser(intent, "Open CSV"), 11)
    }

    private fun hitApitoGetLink() {

        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call =
            service.getExportUnit("Bearer " + sharedPreferences!!.getString("Access_Token", ""))
        call.enqueue(object : retrofit2.Callback<ImportCustomer?> {
            override fun onResponse(call: Call<ImportCustomer?>, response: Response<ImportCustomer?>) {
                if (response.body() != null) {
                    val url = response.body()!!.message
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
            }
            override fun onFailure(call: Call<ImportCustomer?>, t: Throwable) {
                hideProgress()
            }
        })
    }

    //API for Get all list of Unit
    private fun hitApitogetUnitlist() {
        showLoading(true)
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getUnitList(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            )
        )
        call.enqueue(object : retrofit2.Callback<UnitListPojo?> {
            override fun onFailure(call: Call<UnitListPojo?>, t: Throwable) {
                Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_SHORT).show()
                hideProgress()

            }

            override fun onResponse(
                call: Call<UnitListPojo?>?,
                response: Response<UnitListPojo?>?
            ) {
                Log.e("Response", response?.body().toString())
                try {
                    hideProgress()
                    mUnitList.clear()
                    mUnitList.addAll(response!!.body()!!.data)
                    if (mUnitList.size > 0) {
                        rv_unitlisting.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                        adapter = UnitListingAdapter( requireActivity(),mUnitList,this@UnitListingFragment)
                        rv_unitlisting.adapter = adapter
                    } else {
                        Toast.makeText(activity, "No Data Available", Toast.LENGTH_SHORT).show()

                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.servernotrespond),
                        Toast.LENGTH_SHORT
                    ).show()
                    hideProgress()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        hitApitogetUnitlist()
    }

    private fun hitApitoDeleteUnitlist(id:Int) {
        showLoading(true)
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getDeleteUnit(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            ), id
        )
        call.enqueue(object : retrofit2.Callback<AddUnitPojo?> {
            override fun onFailure(call: Call<AddUnitPojo?>, t: Throwable) {
                Toast.makeText(requireActivity(), ""+t.toString(), Toast.LENGTH_SHORT).show()
                hideProgress()
            }

            override fun onResponse(
                call: Call<AddUnitPojo?>?,
                response: Response<AddUnitPojo?>?
            ) {
                hideProgress()
                Log.e("Response", response?.body().toString())
                try {
                    Toast.makeText(requireActivity(), "Deleted SucessFully", Toast.LENGTH_SHORT).show()
                    //       progressBar!!.visibility = View.INVISIBLE


                } catch (e: Exception) {
                    Toast.makeText(requireActivity(), ""+e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun filter(text: String) {
        val filteredList: ArrayList<DataList> = ArrayList()
        for (item in mUnitList) {
            if (item.unit.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
        }
        if(text.isEmpty()){
            filteredList.clear()
            adapter?.filterList(mUnitList)
        }else{
            adapter?.filterList(filteredList)
        }

    }


}