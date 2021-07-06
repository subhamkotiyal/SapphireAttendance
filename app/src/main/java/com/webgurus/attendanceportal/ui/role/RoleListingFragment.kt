package com.webgurus.attendanceportal.ui.role

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.utils.Utils.getFileName
import com.example.baseproject.utils.Utils.snackbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.adapter.RoleListingAdapter
import com.webgurus.attendanceportal.listeners.RecyclerViewClickListeners
import com.webgurus.attendanceportal.listeners.RecyclerViewRoleClickListeners
import com.webgurus.attendanceportal.pojo.*
import com.webgurus.attendanceportal.ui.base.BaseFragment
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.fragment_rolelisting.*
import kotlinx.android.synthetic.main.fragment_userlisitng.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class RoleListingFragment : BaseFragment(), RecyclerViewRoleClickListeners {

    var sharedPreferences: SharedPreferences? = null
    var mListRole: ArrayList<DataRoleList> = ArrayList()
    var adapter: RoleListingAdapter? = null
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

    fun deleteItemDialog(index: Int,ID : Int){
        val alertBuilder = AlertDialog.Builder(requireContext())
        alertBuilder.setTitle("Delete")
        alertBuilder.setMessage("Do you want to delete this Role ?")
        alertBuilder.setPositiveButton("Delete"){_,_ ->
            mListRole!!.remove(mListRole.get(index))
            adapter!!.notifyDataSetChanged()
            hitApitoDeleteRole(ID)
        }

        alertBuilder.setNegativeButton("No"){_,_ ->

        }
        alertBuilder.show()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rolelisting, container, false);
    }

    override fun onResume() {
        super.onResume()
        hitApitoListRole()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniview()

    }


    private fun hitApitoDeleteRole( role_id: Int) {
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.deleteRole("Bearer " + sharedPreferences!!.getString("Access_Token", ""),"application/json",role_id)
        // showProgressbar()
        call.enqueue(object : retrofit2.Callback<RoleDeletedPojo?> {
            override fun onResponse(call: Call<RoleDeletedPojo?>, response: Response<RoleDeletedPojo?>) {
                if (response.body() != null) {
                    Log.e("RoleDeleted :",response.body()!!.message)
                }

            }

            override fun onFailure(call: Call<RoleDeletedPojo?>, t: Throwable) {
                val sasfdfg=""
            }

        })


    }


    private fun hitApitoListRole() {
        mListRole!!.clear()
        showLoading(true)
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call =
            service.getRoleListing("Bearer " + sharedPreferences!!.getString("Access_Token", ""))
        // showProgressbar()
        call.enqueue(object : retrofit2.Callback<GetRolesPojo?> {
            override fun onResponse(call: Call<GetRolesPojo?>, response: Response<GetRolesPojo?>) {
                if (response.body() != null) {
                    hideProgress()
                    mListRole!!.addAll(response.body()!!.data)
                    rv_rolelsiitng.layoutManager =
                        LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                    adapter =
                        RoleListingAdapter(requireActivity(), mListRole, this@RoleListingFragment)
                    rv_rolelsiitng.adapter = adapter
                }

            }

            override fun onFailure(call: Call<GetRolesPojo?>, t: Throwable) {
                hideProgress()
            }

        })


    }


    private fun openAnim(){
        textview_mail!!.setVisibility(View.INVISIBLE)
        textview_share!!.setVisibility(View.INVISIBLE)
        textview_addprpoduct!!.setVisibility(View.INVISIBLE)
        fb_export!!.startAnimation(fab_close)
        fab1_mail!!.startAnimation(fab_close)
        fab1_add!!.startAnimation(fab_close)
        fab_main!!.startAnimation(fab_anticlock)
        fb_export!!.setClickable(false)
        fab1_mail!!.setClickable(false)
        isOpen = false
    }

    private fun closeAnim(){
        textview_mail!!.setVisibility(View.VISIBLE);
        textview_share!!.setVisibility(View.VISIBLE);
        textview_addprpoduct!!.setVisibility(View.VISIBLE);
        fb_export!!.startAnimation(fab_open);
        fab1_mail!!.startAnimation(fab_open);
        fab1_add!!.startAnimation(fab_open);
        fab_main!!.startAnimation(fab_clock);
        fb_export!!.setClickable(true);
        fab1_mail!!.setClickable(true);
        isOpen = true;
    }

    private fun iniview() {

        fab_main = view!!.findViewById(R.id.fab)
        fab1_mail = view!!.findViewById(R.id.fab1)
        fab1_add = view!!.findViewById(R.id.fab1_add);
        fb_export = view!!.findViewById(R.id.fab_export);
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

        search_ed_role?.addTextChangedListener(object : TextWatcher {
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


        fab.setOnClickListener(View.OnClickListener {

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
            startActivity(Intent(requireContext(), AddRoleActivity::class.java))
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === 11) {

              if (requestCode==Activity.RESULT_OK) {
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
              }else if (requestCode==Activity.RESULT_CANCELED){
                  Log.i("At the","SELECT PICTURENULL");
              }
        }
    }



    private fun selectCSVFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        startActivityForResult(Intent.createChooser(intent, "Open CSV"), 11)
    }

    private fun hitApitoGetLink() {
        mListRole!!.clear()
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call =
            service.getRolelink("Bearer " + sharedPreferences!!.getString("Access_Token", ""))
        call.enqueue(object : retrofit2.Callback<LinkRole?> {
            override fun onResponse(call: Call<LinkRole?>, response: Response<LinkRole?>) {
                if (response.body() != null) {
                    val url = response.body()!!.message
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
            }
            override fun onFailure(call: Call<LinkRole?>, t: Throwable) {
                hideProgress()
            }
        })
    }


    private fun hitApitoPostCSV(multipartBody: MultipartBody.Part) {
        mListRole!!.clear()
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call =
            service.addcsv("Bearer " + sharedPreferences!!.getString("Access_Token", ""),
                multipartBody)
        call.enqueue(object : retrofit2.Callback<LinkRole?> {
            override fun onResponse(call: Call<LinkRole?>, response: Response<LinkRole?>) {
                if (response.body() != null) {
                    rl_root.snackbar(response.body()!!.message.toString())
                }

            }

            override fun onFailure(call: Call<LinkRole?>, t: Throwable) {
                hideProgress()
            }

        })


    }







    private fun filter(text: String) {
        val filteredList: ArrayList<DataRoleList> = ArrayList()
        for (item in mListRole) {
            if (item.role.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
        }

        if(text.isEmpty()){
            filteredList.clear()
            adapter?.filterList(mListRole)
        }else{
            adapter?.filterList(filteredList)
        }
    }

}
