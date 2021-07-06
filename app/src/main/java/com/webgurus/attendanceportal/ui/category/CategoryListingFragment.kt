package com.webgurus.attendanceportal.ui.category

import android.content.Context
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
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.utils.Utils.getFileName
import com.example.baseproject.utils.Utils.snackbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.listeners.RecyclerViewRoleClickListeners
import com.webgurus.attendanceportal.pojo.CategoryData
import com.webgurus.attendanceportal.pojo.DeleteCategoryPojo
import com.webgurus.attendanceportal.pojo.GetCategoryListingPojo
import com.webgurus.attendanceportal.pojo.ImportCustomer
import com.webgurus.attendanceportal.ui.base.BaseFragment
import com.webgurus.attendanceportal.ui.unitmanage.AddNewUnitActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.fragment_categorylisting.*
import kotlinx.android.synthetic.main.fragment_productmanagement.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class CategoryListingFragment : BaseFragment() , RecyclerViewRoleClickListeners {

    var sharedPreferences: SharedPreferences? = null
    var mListCategory: ArrayList<CategoryData> = ArrayList()
    var  adapter : CategoryListingAdapter ? =null
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
        return inflater.inflate(R.layout.fragment_categorylisting, container, false);
    }

    override fun onResume() {
        super.onResume()
        hitApitoGetCategoriesListing()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniview()
    }


    private fun hitApitoDeleteCategory( cateID: Int) {
        showLoading(true)
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.deleteCategory("Bearer " + sharedPreferences!!.getString("Access_Token", ""),
            "application/json",cateID)
        // showProgressbar()
        call.enqueue(object : retrofit2.Callback<DeleteCategoryPojo?> {
            override fun onResponse(call: Call<DeleteCategoryPojo?>, response: Response<DeleteCategoryPojo?>) {
                if (response.body() != null) {
                    Log.e("CategoryDeleted :",response.body()!!.message)
                    showLoading(true)
                }

            }

            override fun onFailure(call: Call<DeleteCategoryPojo?>, t: Throwable) {
                hideProgress()
            }

        })


    }



    private fun hitApitoGetCategoriesListing(){
        mListCategory!!.clear()
        showLoading(true)
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getCategoryListing("Bearer " + sharedPreferences!!.getString("Access_Token", ""),"Accept")
        // showProgressbar()
        call.enqueue(object : retrofit2.Callback<GetCategoryListingPojo?> {
            override fun onResponse(call: Call<GetCategoryListingPojo?>, response: Response<GetCategoryListingPojo?>) {
                if (response.body() != null) {
                    showLoading(false)
                    mListCategory!!.addAll(response.body()!!.data)
                    rv_categorylisting.layoutManager =
                        LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                    adapter = CategoryListingAdapter( requireActivity(),mListCategory,this@CategoryListingFragment)
                    rv_categorylisting.adapter = adapter
                }

            }

            override fun onFailure(call: Call<GetCategoryListingPojo?>, t: Throwable) {
                showLoading(true)
            }

        })
    }


    private fun iniview() {

        fab_main = view!!.findViewById(R.id.fab_main_category);
        fab1_mail = view!!.findViewById(R.id.fab1_import_category);
        fab1_add = view!!.findViewById(R.id.fb_add_category);
        fb_export = view!!.findViewById(R.id.fab_export_category);
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
        search_edit_category?.addTextChangedListener(object : TextWatcher {
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
            startActivity(Intent(requireContext(), AddNewCategoryActivity::class.java))
        })

    }


    private fun openAnim(){
        textview_mail!!.setVisibility(View.INVISIBLE);
        textview_share!!.setVisibility(View.INVISIBLE);
        textview_addcategory!!.setVisibility(View.INVISIBLE);
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
        textview_addcategory!!.setVisibility(View.VISIBLE);
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

            selectedImageUri =data?.data
            val parcelFileDescriptor =

                requireActivity().contentResolver.openFileDescriptor(selectedImageUri!!,
                    "r", null) ?: return

            val inputStream =
                FileInputStream(parcelFileDescriptor.fileDescriptor)
            val file = File(requireActivity().cacheDir,
                requireActivity().contentResolver.getFileName(selectedImageUri!!))
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            val requestFile: RequestBody = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                data?.getData()?.getPath()
            )

            val multipartBody: MultipartBody.Part =
                MultipartBody.Part.createFormData("import", file.getName(), requestFile)

            hitApitoPostCSV(multipartBody)

        }
    }
    private fun hitApitoPostCSV(multipartBody: MultipartBody.Part) {

        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call =
            service.addCategoryImport("Bearer " + sharedPreferences!!.getString("Access_Token", ""),
                multipartBody)
        showLoading(true)
        call.enqueue(object : retrofit2.Callback<ImportCustomer?> {
            override fun onResponse(call: Call<ImportCustomer?>, response: Response<ImportCustomer?>) {
                if (response.body() != null) {
                    rl_rootcategory.snackbar(response.body()!!.message)
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
            service.getCategoryExport("Bearer " + sharedPreferences!!.getString("Access_Token", ""))
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

    fun deleteItemDialog(index: Int,ID : Int){
        val alertBuilder = AlertDialog.Builder(requireContext())
        alertBuilder.setTitle("Delete")
        alertBuilder.setMessage("Do you want to delete this Category ?")
        alertBuilder.setPositiveButton("Delete"){_,_ ->
            mListCategory!!.remove(mListCategory!!.get(index))
            adapter!!.notifyDataSetChanged()
            hitApitoDeleteCategory(ID)
        }

        alertBuilder.setNegativeButton("No"){_,_ ->

        }
        alertBuilder.show()
    }

    override fun onClick(position: Int, roleID: Int) {
        deleteItemDialog(position,roleID)
    }

    private fun filter(text: String) {
        val filteredList: ArrayList<CategoryData> = ArrayList()
        for (item in mListCategory) {
            if (item.name.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
        }
        if(text.isEmpty()){
            filteredList.clear()
            adapter?.filterList(mListCategory)
        }else{
            adapter?.filterList(filteredList)
        }

    }


}