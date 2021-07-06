package com.webgurus.attendanceportal.ui.productmanagement

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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.listeners.RecyclerViewRoleClickListeners
import com.webgurus.attendanceportal.pojo.*
import com.webgurus.attendanceportal.ui.base.BaseFragment
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.fragment_productmanagement.*
import retrofit2.Call
import retrofit2.Response

class ProductManagementFragment22 : BaseFragment() , RecyclerViewRoleClickListeners {


    var sharedPreferences: SharedPreferences? = null
    var mListCatg: ArrayList<ProductData> = ArrayList()
    var mVariantList: ArrayList<Variant> = ArrayList()
    var productManagementAdapter:ProductManagementAdapter ?=null
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_productmanagement, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniview()

    }

    override fun onResume() {
        super.onResume()
        hitApitoGetAllProducts()
    }


    private fun iniview() {

        fab_main = view!!.findViewById(R.id.fab_main_product);
        fab1_mail = view!!.findViewById(R.id.fab1_import_product);
        fab1_add = view!!.findViewById(R.id.fb_add_product);
        fb_export = view!!.findViewById(R.id.fab_export_product);
        fab_close = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_rotate_anticlock);
        textview_mail =view!!.findViewById(R.id.textview_mail)
        textview_share = view!!.findViewById(R.id.textview_share)
        sharedPreferences = requireActivity().getSharedPreferences(
            "AcessToken",
            AppCompatActivity.MODE_PRIVATE)
        search_edit_product?.addTextChangedListener(object : TextWatcher {
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
            startActivity(Intent(requireContext(), AddProductManagementActivity::class.java))
        })
    }

    private fun openAnim(){
        textview_mail!!.setVisibility(View.INVISIBLE);
        textview_share!!.setVisibility(View.INVISIBLE);
        textview_addproduct!!.setVisibility(View.INVISIBLE);
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
        textview_addproduct!!.setVisibility(View.VISIBLE);
        fb_export!!.startAnimation(fab_open);
        fab1_mail!!.startAnimation(fab_open);
        fab1_add!!.startAnimation(fab_open);
        fab_main!!.startAnimation(fab_clock);
        fb_export!!.setClickable(true);
        fab1_mail!!.setClickable(true);
        isOpen = true;
    }


    override fun onClick(position: Int, roleID: Int) {
        showAlertLogout(position,roleID)
    }

    private fun showAlertLogout(postion:Int,iD:Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Product")
        builder.setMessage("Are you sure you want to delete this product ?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes")

        { dialogInterface, which ->
            mVariantList.remove(mVariantList.get(postion))
            productManagementAdapter!!.notifyDataSetChanged()
            hitApitoDeleteProduct(iD)

        }
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }





    private fun hitApitoDeleteProduct( productID:Int){
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.delteVariant("Bearer " + sharedPreferences!!.getString("Access_Token", ""),
            productID)
        showLoading(true)
        call.enqueue(object :retrofit2.Callback<DeleteVariantPojo>{
            override fun onResponse(call: Call<DeleteVariantPojo?>,
                                    response: Response<DeleteVariantPojo?>
            ) {
                if (response.body() != null) {
                    Toast.makeText(activity,""+response.body()!!.message.toString(),Toast.LENGTH_SHORT).show()
                    hideProgress()
                }
            }

            override fun onFailure(call: Call<DeleteVariantPojo>, t: Throwable) {
                Toast.makeText(activity,""+t.toString(),Toast.LENGTH_SHORT).show()
            }

        })


    }


    private fun hitApitoGetAllProducts(){
        mVariantList.clear()
        showLoading(true)
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getProductLisitng("Bearer " + sharedPreferences!!.getString("Access_Token", ""), "application/json")

        call.enqueue(object :retrofit2.Callback<ProductListingPojo>{
            override fun onResponse(call: Call<ProductListingPojo?>,
                                    response: Response<ProductListingPojo?>
            ) {
                if (response.body() != null) {
                    hideProgress()
                    for (i in 0 until response.body()!!.data.size){
                        mVariantList.addAll(response.body()!!.data[i].variants)
                     }

                    rv_productmanagement.layoutManager =
                        LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                    productManagementAdapter =
                        ProductManagementAdapter(requireActivity(), mVariantList!!,this@ProductManagementFragment22)
                    rv_productmanagement.adapter = productManagementAdapter
                }
            }

            override fun onFailure(call: Call<ProductListingPojo>, t: Throwable) {
                val dfgdf=""
            }

        })


    }


    private fun filter(text: String) {
        val filteredList: ArrayList<Variant> = ArrayList()
        for (item in mVariantList) {
            if (item.product_name.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
//            }else if (item.product_name.toLowerCase().contains(text.toLowerCase())) {
//                filteredList.add(item)
//            }
        }

        if(text.isEmpty()){
            filteredList.clear()
            productManagementAdapter?.filterList(mVariantList)
        }else{
            productManagementAdapter?.filterList(filteredList)
        }

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
            service.getProductExport("Bearer " + sharedPreferences!!.getString("Access_Token", ""))
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

}