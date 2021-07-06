package com.webgurus.attendanceportal.ui.customer

import android.R.attr.data
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.webgurus.attendanceportal.adapter.CustomerLisitingAdapter
import com.webgurus.attendanceportal.listeners.CheckBoxSelectedListeners
import com.webgurus.attendanceportal.listeners.RecyclerViewRoleClickListeners
import com.webgurus.attendanceportal.pojo.*
import com.webgurus.attendanceportal.ui.base.BaseFragment
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.fragment_customers.*
import kotlinx.android.synthetic.main.fragment_customers.textview_addprpoduct
import kotlinx.android.synthetic.main.fragment_rolelisting.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class OurCustomersFragment : BaseFragment(), CheckBoxSelectedListeners , RecyclerViewRoleClickListeners {

    var sharedPreferences : SharedPreferences? =null
    var mList : ArrayList<Datar> = ArrayList()
    var adapter : CustomerLisitingAdapter? = null
    var rv_curomerlisitng: RecyclerView ?=null
    var mContext: Context ?=null
    var mCustomerIdList  : ArrayList<Int> = ArrayList()

    private var  fab_maincustomer: FloatingActionButton? = null
    private  var fab_importcust: FloatingActionButton? = null
    private  var fab_exportcustomer: FloatingActionButton? = null
    private  var fb_addcustomer: FloatingActionButton? = null

    private var fab_open: Animation? = null
    private var fab_close: Animation? = null
    private var fab_clock: Animation? = null
    private var fab_anticlock: Animation? = null

    var textview_mail: TextView? = null
    var textview_share: TextView? = null
    var isOpen = false
    var selectedImageUri:Uri?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_customers, container, false)
        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context
    }

    override fun onClick(position: Int, roleID: Int) {
        deleteItemDialog(position, roleID)
    }

    fun deleteItemDialog(index: Int, ID: Int){
        val alertBuilder = AlertDialog.Builder(requireContext())
        alertBuilder.setTitle("Delete")
        alertBuilder.setMessage("Do you want to delete this Customer ?")
        alertBuilder.setPositiveButton("Delete"){ _, _ ->
            mList!!.remove(mList!!.get(index))
           // mList.removeAt(index)
            adapter!!.notifyDataSetChanged()
            hitApitoDeleteCustomer(ID)
        }

        alertBuilder.setNegativeButton("No"){ _, _ ->

        }
        alertBuilder.show()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniview()
        listeners()
    }

    override fun onResume() {
        super.onResume()
        btn_assign.setText("Assign Manager")
        hitApitoGetCustomerisitng()
    }


    private fun openAnim(){
        textview_mail!!.setVisibility(View.INVISIBLE);
        textview_share!!.setVisibility(View.INVISIBLE);
        textview_addprpoduct!!.setVisibility(View.INVISIBLE);
        fab_exportcustomer!!.startAnimation(fab_close);
        fab_importcust!!.startAnimation(fab_close);
        fb_addcustomer!!.startAnimation(fab_close);
        fab_maincustomer!!.startAnimation(fab_anticlock);
        fab_exportcustomer!!.setClickable(false);
        fab_importcust!!.setClickable(false);
        isOpen = false;
    }

    private fun closeAnim(){
        textview_mail!!.setVisibility(View.VISIBLE)
        textview_share!!.setVisibility(View.VISIBLE)
        textview_addprpoduct!!.setVisibility(View.VISIBLE)
        fab_exportcustomer!!.startAnimation(fab_open)
        fab_importcust!!.startAnimation(fab_open)
        fb_addcustomer!!.startAnimation(fab_open)
        fab_maincustomer!!.startAnimation(fab_clock)
        fab_exportcustomer!!.setClickable(true)
        fab_importcust!!.setClickable(true)
        isOpen = true

    }

    private fun listeners() {

        fab_maincustomer?.setOnClickListener(View.OnClickListener {
            if (isOpen) {
                openAnim()
            } else {
                closeAnim()
            }
        })
        fab_exportcustomer?.setOnClickListener(View.OnClickListener {
            hitApitoGetLink()
        })
        fab_importcust?.setOnClickListener(View.OnClickListener {
            selectCSVFile()
        })
        fb_addcustomer?.setOnClickListener(View.OnClickListener {
            openAnim()
            val intent = Intent(context, AddCustomerFragment::class.java)
            context!!.startActivity(intent)
        })
        btn_assign.setOnClickListener(View.OnClickListener {
            if (btn_assign.text.equals("Cancel")) {
                btn_assign.setText("Assign Manager")
                rv_curomerlisitng!!.layoutManager =
                    LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                adapter = CustomerLisitingAdapter(
                    mContext!!,
                    mList,
                    false,
                    this,
                    this@OurCustomersFragment
                )
                rv_curomerlisitng!!.adapter = adapter
            } else if (btn_assign.text.equals("Next")) {
                if(mCustomerIdList.size>0){
                    var intent = Intent(requireActivity(), ShowAllManagersBottomSheet::class.java)
                    intent.putIntegerArrayListExtra("customerdetails", mCustomerIdList)
                    startActivity(intent)
                }else{
                    Toast.makeText(requireContext(),"Please select the customers ",Toast.LENGTH_SHORT).show()
                }
            } else if(btn_assign.text.equals("Assign Manager")) {
                btn_assign.setText("Cancel")
                rv_curomerlisitng!!.layoutManager =
                    LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                adapter = CustomerLisitingAdapter(
                    mContext!!,
                    mList,
                    true,
                    this,
                    this@OurCustomersFragment
                )
                rv_curomerlisitng!!.adapter = adapter
            }else{
                btn_assign.setText("Cancel")
                rv_curomerlisitng!!.layoutManager =
                    LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                adapter = CustomerLisitingAdapter(
                    mContext!!,
                    mList,
                    true,
                    this,
                    this@OurCustomersFragment
                )
                rv_curomerlisitng!!.adapter = adapter
            }
        })
    }

    private fun selectCSVFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        startActivityForResult(Intent.createChooser(intent, "Open CSV"), 11)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 11) {
            if (resultCode==Activity.RESULT_OK){
            selectedImageUri =data?.data
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
            service.addcsvforcustomer(
                "Bearer " + sharedPreferences!!.getString("Access_Token", ""),
                multipartBody
            )
        call.enqueue(object : retrofit2.Callback<LinkRole?> {
            override fun onResponse(call: Call<LinkRole?>, response: Response<LinkRole?>) {
                if (response.body() != null) {
                    rl_rootforcustomer.snackbar(response.body()!!.message.toString())
                }

            }

            override fun onFailure(call: Call<LinkRole?>, t: Throwable) {
                hideProgress()
            }

        })

    }

    private fun hitApitoGetLink() {
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call =
            service.getCustomerExport("Bearer " + sharedPreferences!!.getString("Access_Token", ""))
        call.enqueue(object : retrofit2.Callback<ImportCustomer?> {
            override fun onResponse(
                call: Call<ImportCustomer?>,
                response: Response<ImportCustomer?>
            ) {
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


    private fun iniview() {
        fab_maincustomer = view!!.findViewById(R.id.fab_maincustomer)
        fab_importcust = view!!.findViewById(R.id.fab_importcust)
        fab_exportcustomer = view!!.findViewById(R.id.fab_exportcustomer)
        fb_addcustomer = view!!.findViewById(R.id.fb_addcustomer)

        fab_close = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_rotate_anticlock);

        textview_mail =view!!.findViewById(R.id.textview_mail)
        textview_share = view!!.findViewById(R.id.textview_share)


        rv_curomerlisitng=requireView().findViewById(R.id.rv_curomerlisitng)
        sharedPreferences= requireActivity().getSharedPreferences(
            "AcessToken",
            AppCompatActivity.MODE_PRIVATE
        )

        search_edit_customer?.addTextChangedListener(object : TextWatcher {
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
    }


    private fun hitApitoGetCustomerisitng(){
        mList.clear()
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getCustomerList(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            )
        )
        showLoading(true)
        call.enqueue(object : retrofit2.Callback<GetCustomerLisiting?> {
            override fun onResponse(
                call: Call<GetCustomerLisiting?>,
                response: Response<GetCustomerLisiting?>
            ) {
                if (response.body() != null) {
                    hideProgress()
                    mList.addAll(response.body()!!.data)
                    if (rv_curomerlisitng != null) {
                        rv_curomerlisitng!!.layoutManager =
                            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                        adapter = CustomerLisitingAdapter(
                            mContext!!,
                            mList,
                            false,
                            this@OurCustomersFragment,
                            this@OurCustomersFragment
                        )
                        rv_curomerlisitng!!.adapter = adapter
                    } else {
                        rv_curomerlisitng = view!!.findViewById(R.id.rv_curomerlisitng)
                    }

                }

            }

            override fun onFailure(call: Call<GetCustomerLisiting?>, t: Throwable) {
                val dsgfd = ""
            }

        })
    }

    private fun filter(text: String) {

        val filteredList: ArrayList<Datar> = ArrayList()
        for (item in mList) {
            if (item.first_name.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
            if (item.last_name.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
            if (item.phone_number.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
        }

        if(text.isEmpty()){
            filteredList.clear()
            adapter?.filterList(mList)
        }else{
            adapter?.filterList(filteredList)
        }


    }

    override fun onChecked(position: Int, id: Int) {
        mCustomerIdList.add(id)
        if(mCustomerIdList.size>0){
            btn_assign.setText("Next")
        }else{
            btn_assign.setText("Assign Manager")
        }
    }

    override fun onUnChecked(position: Int, id: Int) {
        for (i in mCustomerIdList.size - 1 downTo 0) {
            if(mCustomerIdList.get(i)==id){
                mCustomerIdList.removeAt(i)
            }
        }
        if(mCustomerIdList.size>0){
            btn_assign.setText("Next")
        }else{
            btn_assign.setText("Assign Manager")
        }
    }


    private fun hitApitoDeleteCustomer(id: Int) {
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.deleteCustomer(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            ), id
        )
        showLoading(true)
        call.enqueue(object : retrofit2.Callback<CustomerDeletePojo?> {
            override fun onResponse(
                call: Call<CustomerDeletePojo?>,
                response: Response<CustomerDeletePojo?>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.message.equals("Customer deleted")) {
                        Toast.makeText(
                            requireContext(),
                            "" + response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "" + response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    hideProgress()

                }

            }

            override fun onFailure(call: Call<CustomerDeletePojo?>, t: Throwable) {
                Toast.makeText(requireContext(), "" + t.toString(), Toast.LENGTH_SHORT).show()
                hideProgress()
            }

        })
    }


}