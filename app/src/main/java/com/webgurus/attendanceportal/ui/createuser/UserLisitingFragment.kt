package com.webgurus.attendanceportal.ui.createuser

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
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.utils.Utils.getFileName
import com.example.baseproject.utils.Utils.snackbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.webgurus.attendanceportal.LocationPermissionCheck
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.adapter.UserListingAdapter
import com.webgurus.attendanceportal.listeners.RecyclerViewRoleClickListeners
import com.webgurus.attendanceportal.pojo.*
import com.webgurus.attendanceportal.ui.base.BaseFragment
import com.webgurus.attendanceportal.ui.customer.AddCustomerFragment
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.fragment_customers.*
import kotlinx.android.synthetic.main.fragment_orderlisting.*
import kotlinx.android.synthetic.main.fragment_userlisitng.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class UserLisitingFragment : BaseFragment(), RecyclerViewRoleClickListeners {

    var mListuser: ArrayList<DataUser> = ArrayList()
    var sharedPreferences: SharedPreferences? = null
    var adapter: UserListingAdapter? = null

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_userlisitng, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniview()


    }



    override fun onResume() {
        super.onResume()
        hitApitoGetUserListing()
    }


    private fun openAnim(){
        textview_mail!!.setVisibility(View.INVISIBLE)
        textview_share!!.setVisibility(View.INVISIBLE)
        textview_adduser!!.setVisibility(View.INVISIBLE)
        fb_export!!.startAnimation(fab_close)
        fab1_mail!!.startAnimation(fab_close)
        fab1_add!!.startAnimation(fab_close)
        fab_main!!.startAnimation(fab_anticlock)
        fb_export!!.setClickable(false)
        fab1_mail!!.setClickable(false)
        isOpen = false
    }

    private fun closeAnim(){
        textview_mail!!.setVisibility(View.VISIBLE)
        textview_share!!.setVisibility(View.VISIBLE)
        textview_adduser!!.setVisibility(View.VISIBLE)
        fb_export!!.startAnimation(fab_open)
        fab1_mail!!.startAnimation(fab_open)
        fab1_add!!.startAnimation(fab_open)
        fab_main!!.startAnimation(fab_clock)
        fb_export!!.setClickable(true)
        fab1_mail!!.setClickable(true)
        isOpen = true
    }


    private fun iniview() {
        fab_main =  requireView().findViewById(R.id.fab_main_user)
        fab1_mail =  requireView().findViewById(R.id.fab1_import_user)
        fab1_add =  requireView().findViewById(R.id.fb_add_user)
        fb_export =  requireView().findViewById(R.id.fab_export_user);
        fab_close = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_close)
        fab_open = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open)
        fab_clock = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_rotate_clock)
        fab_anticlock = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_rotate_anticlock)
        textview_mail = requireView().findViewById(R.id.textview_mail)
        textview_share =  requireView().findViewById(R.id.textview_share)
        sharedPreferences = requireActivity().getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE)
        search_edit?.addTextChangedListener(object : TextWatcher {
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
            startActivity(Intent(requireContext(), CreateUserFragment::class.java))
        })
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
            service.addUserImport("Bearer " + sharedPreferences!!.getString("Access_Token", ""),
                multipartBody)
          showLoading(true)
        call.enqueue(object : retrofit2.Callback<ImportCustomer?> {
            override fun onResponse(call: Call<ImportCustomer?>, response: Response<ImportCustomer?>) {
                if (response.body() != null) {
                    rl_rootuserlisting.snackbar(response.body()!!.message.toString())
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
            service.getUserExport("Bearer " + sharedPreferences!!.getString("Access_Token", ""))
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



    private fun hitApitoGetUserListing() {
        mListuser.clear()
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call =
            service.getUserList("Bearer " + sharedPreferences!!.getString("Access_Token", ""))
        showLoading(true)
        call.enqueue(object : retrofit2.Callback<UserListingPojo?> {
            override fun onResponse(
                call: Call<UserListingPojo?>,
                response: Response<UserListingPojo?>
            ) {
                if (response.body() != null) {
                    mListuser.addAll(response.body()!!.data)
                    rv_userlsiitng.layoutManager =
                        LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                    adapter = UserListingAdapter(requireActivity(), mListuser, this@UserLisitingFragment)
                    rv_userlsiitng.adapter = adapter
                    hideProgress()
                }

            }

            override fun onFailure(call: Call<UserListingPojo?>, t: Throwable) {
                hideProgress()
            }

        })
    }

    private fun filter(text: String) {
        val filteredList: ArrayList<DataUser> = ArrayList()
        for (item in mListuser) {
            if (item.name.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }

           else if (item.email.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
            else if (item.phone_number.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
        }

        if (text.isEmpty()) {
            filteredList.clear()
            adapter?.filterList(mListuser)
        } else {
            adapter?.filterList(filteredList)
        }

    }

    override fun onClick(position: Int, roleID: Int) {
        deleteItemDialog(position,roleID)
    }

    fun deleteItemDialog(index: Int,Id : Int){
        val alertBuilder = AlertDialog.Builder(requireContext())
        alertBuilder.setTitle("Delete")
        alertBuilder.setMessage("Do you want to delete this User ?")
        alertBuilder.setPositiveButton("Delete"){_,_ ->
            mListuser.removeAt(index)
            adapter!!.notifyDataSetChanged()
            hitApitoDeleteUser(Id)
        }

        alertBuilder.setNegativeButton("No"){_,_ ->

        }
        alertBuilder.show()
    }




    private fun hitApitoDeleteUser(userID: Int) {
        showLoading(true)
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.deleteUser(
            "Bearer " + sharedPreferences!!.getString("Access_Token", ""),
            userID
        )
        call.enqueue(object : retrofit2.Callback<UserDeletePojo> {
            override fun onResponse(
                call: Call<UserDeletePojo?>,
                response: Response<UserDeletePojo?>
            ) {
                if (response.body() != null) {
                    hideProgress()
                    Toast.makeText(requireActivity(),""+response.body()!!.message,Toast.LENGTH_SHORT).show()

                } else {
                    val dsgfds=""
                    hideProgress()
                    Toast.makeText(requireActivity(),""+response.body()!!.message,Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserDeletePojo>, t: Throwable) {
                val dsgfds=""
                hideProgress()
            }

        })
    }


}

