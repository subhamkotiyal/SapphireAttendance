package com.webgurus.attendanceportal.ui.createuser
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.baseproject.utils.Utils
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.*
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.fragment_createuser.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateUserFragment : BaseActivity(), AdapterView.OnItemSelectedListener {

    var sharedPreferences: SharedPreferences? = null
    val list: ArrayList<String> = ArrayList()
    val list_spinner: ArrayList<Int> = ArrayList()
    val list_mannnagerid: ArrayList<Int> = ArrayList()
    val mManagingList: ArrayList<String> = ArrayList()

    val NEW_SPINNER_ID = 1
    lateinit var spinner: Spinner
    lateinit var sp_manager: Spinner
    lateinit var myCalendar: Calendar
    var role_id = 0
    var mannager_id = 0
    var mListuser: ArrayList<DataUser>? = ArrayList()
    var managername: String? = ""
    var managerrole: String? = ""
    var userID: Int = 0
    var temp_roleID=0
    var temp_managerID=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_createuser)
        initview()
        listeners()
        hitApitogetRoles()
        openCalender()
    }

    @SuppressLint("ResourceType")
    private fun initview() {
        if (intent.getStringExtra("name")!= null) {
            ed_name.setText(intent.getStringExtra("name"))
            ed_emailaddress.setText(intent.getStringExtra("email"))
            ed_password.setText(intent.getStringExtra(""))
            ed_phonenumber.setText(intent.getStringExtra("phone_number"))
            ed_calender.setText(intent.getStringExtra("dob"))
            ed_state.setText(intent.getStringExtra("state"))
            ed_city.setText(intent.getStringExtra("city"))
            managername = intent.getStringExtra("manager_name")
            managerrole = intent.getStringExtra("manager_role")
            temp_roleID = intent.getIntExtra("role_id", 0)
            temp_managerID = intent.getStringExtra("manager_id")!!.toInt()
            if (intent.getIntExtra("id", 0) == 0) {
                tv_edituser.setText("Create User")
            } else {
                tv_edituser.setText("Edit User")
            }
            userID = intent.getIntExtra("id", 0)
            if (userID == 0) {
                btn_createuser.setText("Create User")
            } else {
                btn_createuser.setText("Update User")
            }
        }
        myCalendar = Calendar.getInstance()
        spinner = Spinner(this)
        sp_manager = Spinner(this)
        sp_manager.id = 2
        spinner.id = 1
        sharedPreferences = this@CreateUserFragment.getSharedPreferences(
            "AcessToken",
            AppCompatActivity.MODE_PRIVATE
        )
    }

    private fun setSpinner() {
        var aa = ArrayAdapter(this@CreateUserFragment, R.layout.simple_spinner_list, list)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.id = NEW_SPINNER_ID
        val ll = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        //  ll.setMargins(10, 40, 10, 10)
        rl_spinner.addView(spinner)
        aa = ArrayAdapter(this@CreateUserFragment, R.layout.spinner_right_aligned, list)
        aa.setDropDownViewResource(R.layout.spinner_right_aligned)
        with(spinner)
        {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener = this@CreateUserFragment
            layoutParams = ll
            prompt = "Select your favourite language"

        }

        if(userID==0){
            spinner.setSelection(0)
            role_id = list_spinner.get(0)
        }else{
            for (i in 0 until list.size){
                if(temp_roleID==list_spinner.get(i)){
                    spinner.setSelection(i)
                    role_id=temp_roleID
                }

                }

        }
    }


    private fun listeners() {

        iv_backfromuser.setOnClickListener(View.OnClickListener {
            finish()
        })

        btn_createuser.setOnClickListener(View.OnClickListener {
            if (ed_name.text.toString().equals("")) {
                ed_name.setHint("This is required field")
                ed_name.setHintTextColor(resources.getColor(R.color.red))
                Toast.makeText(this, "Name is mandatory to fill", Toast.LENGTH_SHORT).show()

            } else if (ed_emailaddress.text.toString().equals("")) {
                ed_emailaddress.setHint("This is required field")
                ed_emailaddress.setHintTextColor(resources.getColor(R.color.red))
                Toast.makeText(this, "Email is mandatory to fill", Toast.LENGTH_SHORT)
                    .show()
            } else if (!Utils.isEmailValid(ed_emailaddress.text.toString())) {
                ed_emailaddress.setHint("Email Address is not Valid.")
                ed_emailaddress.setHintTextColor(resources.getColor(R.color.red))
                Toast.makeText(this, "Email address is not valid.", Toast.LENGTH_SHORT)
                    .show()
            }  else if (ed_phonenumber.text.toString().equals("")) {
                ed_phonenumber.setHint("This is required Field")
                ed_phonenumber.setHintTextColor(resources.getColor(R.color.red))
                Toast.makeText(
                    this,
                    "Phone number is mandatory to fill.",
                    Toast.LENGTH_SHORT
                ).show()


//            } else if (ed_calender.text.toString().equals("")) {
//                ed_calender.setHint("This is required Field")
//                ed_calender.setHintTextColor(resources.getColor(R.color.red))
//                Toast.makeText(
//                    this,
//                    "Date of birth  is mandatory to fill.",
//                    Toast.LENGTH_SHORT
//                ).show()
//            } else if (ed_state.text.toString().equals("")) {

        } else if (ed_state.text.toString().equals("")) {
                ed_state.setHint("This is required Field")
                ed_state.setHintTextColor(resources.getColor(R.color.red))
                Toast.makeText(
                    this,
                    "State is mandatory to fill.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (ed_city.text.toString().equals("")) {
                ed_city.setHint("This is required Field")
                ed_city.setHintTextColor(resources.getColor(R.color.red))
                Toast.makeText(
                    this,
                    "City is mandatory to fill.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (userID != 0) {
                    showLoading(true)
                    hitApitoUpdateuser(
                        ed_name.text.toString(), ed_emailaddress.text.toString(),
                        ed_phonenumber.text.toString(), ed_password.text.toString(),
                        role_id.toString(), ed_calender.text.toString(), mannager_id.toString(),
                        ed_city.text.toString(),ed_state.text.toString(), userID

                    )
                } else {
                     if (ed_password.text.toString().equals("")) {
                        ed_password.setHint("This is required Field")
                        ed_password.setHintTextColor(resources.getColor(R.color.red))
                        Toast.makeText(
                            this,
                            "Password is mandatory to fill.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{
                         showLoading(true)
                         hitApitoCreateUser(
                             ed_name.text.toString(), ed_emailaddress.text.toString(),
                             ed_phonenumber.text.toString(), ed_password.text.toString(),
                             role_id.toString(), ed_calender.text.toString(), mannager_id.toString(),
                             ed_city.text.toString(),ed_state.text.toString()
                         )
                    }
                }

            }
        })
    }

    private fun hitApitoUpdateuser(
        name: String, email: String, phonenumber: String, password: String, roleID: String,
        dob: String, mmanager_ID: String, city: String, state:String,userID: Int
    ) {


        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.updateUser(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            ),
            name,
            email,
            phonenumber,
            password,
            roleID,
            dob,
            mmanager_ID,
            city,
            state,
            userID

        )
        call.enqueue(object : Callback<CreateuserPojo?> {
            override fun onResponse(
                call: Call<CreateuserPojo?>,
                response: Response<CreateuserPojo?>
            ) {

                if (response.body()!!.message.equals("User Updated Successfully")) {
                    hideProgress()
                    if(response.body()!!.status==1){
                        Toast.makeText(
                            this@CreateUserFragment,
                            "User Updated Successfully" ,
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }else{
                        Toast.makeText(this@CreateUserFragment,""+response.body()!!.message,Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(
                        this@CreateUserFragment,
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    hideProgress()
                }


            }

            override fun onFailure(call: Call<CreateuserPojo?>, t: Throwable) {
                Toast.makeText(this@CreateUserFragment, t.toString(), Toast.LENGTH_SHORT)
                    .show()
                hideProgress()
            }

        })


    }


    fun openCalender() {
        rl_opencalender.setOnClickListener(View.OnClickListener {
            val myCalendar = Calendar.getInstance()
            val date =
                OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
                    myCalendar[Calendar.YEAR] = year
                    myCalendar[Calendar.MONTH] = monthOfYear
                    myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                    val myFormat = "dd-MMM-yyyy" // your format
                    val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                    ed_calender.setText(sdf.format(myCalendar.time))
                }
            DatePickerDialog(
                this,
                date,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        })

    }

    private fun hitApitogetRoles() {
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getRoles(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            )
        )
        showLoading(true)
        call.enqueue(object : Callback<GetRolesPojo?> {
            override fun onResponse(
                call: Call<GetRolesPojo?>,
                response: Response<GetRolesPojo?>
            ) {

                Log.e("Response", response.body().toString())
                for (i in 0 until response.body()!!.data.size) {
                    list.add(response.body()!!.data[i].role)
                    list_spinner.add(response.body()!!.data[i].id)
                }
                setSpinner()
                hitApitousermanger()
            }

            override fun onFailure(call: Call<GetRolesPojo?>, t: Throwable) {
            }

        })
    }

    private fun hitApitousermanger() {
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getMagersList(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            )
        )
        call.enqueue(object : Callback<GetManagerListingPojo?> {
            override fun onResponse(
                call: Call<GetManagerListingPojo?>,
                response: Response<GetManagerListingPojo?>
            ) {

                for (i in 0 until response.body()!!.data.size) {
                    mManagingList.add(response.body()!!.data[i].name)
                    list_mannnagerid.add(response.body()!!.data[i].id)
                }
                hideProgress()
                setSpinner2()

            }

            override fun onFailure(call: Call<GetManagerListingPojo?>, t: Throwable) {
            }

        })
    }


    private fun hitApitoCreateUser(
        name: String, email: String, phonenumber: String, password: String, roleID: String,
        dob: String, mmanager_ID: String, city: String,state: String
    ) {
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.createUser(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            ),
            name,
            email,
            phonenumber,
            password,
            roleID,
            dob,
            mmanager_ID,
            city,
            state

        )
        call.enqueue(object : Callback<CreateuserPojo?> {
            override fun onResponse(
                call: Call<CreateuserPojo?>,
                response: Response<CreateuserPojo?>
            ) {
                hideProgress()
                if (response.body()!!.message.equals("User Created Successfully")) {
                    Toast.makeText(
                        this@CreateUserFragment,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@CreateUserFragment,
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }

            override fun onFailure(call: Call<CreateuserPojo?>, t: Throwable) {
                Toast.makeText(this@CreateUserFragment, t.toString(), Toast.LENGTH_SHORT)
                    .show()
                hideProgress()
            }

        })
    }


    private fun setSpinner2() {
        Log.e("Arraysize", mManagingList.size.toString())
        var aa =
            ArrayAdapter(
                this@CreateUserFragment,
                R.layout.simple_spinner_item2,
                mManagingList
            )
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val ll = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        //  ll.setMargins(10, 40, 10, 10)
        rl_spinnermanager.addView(sp_manager)
        aa = ArrayAdapter(
            this@CreateUserFragment,
            R.layout.spinner_right_aligned,
            mManagingList
        )
        aa.setDropDownViewResource(R.layout.spinner_right_aligned)
        with(sp_manager)
        {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener = this@CreateUserFragment
            layoutParams = ll
            prompt = "Select your favourite language"

        }
        if(userID==0){
            sp_manager.setSelection(0)
            mannager_id = list_mannnagerid.get(0)
        }else{
            for (i in 0 until list_mannnagerid.size){
                if(temp_managerID==list_mannnagerid.get(i)){
                    sp_manager.setSelection(i)
                    mannager_id = temp_managerID
                }

            }
        }

    }


    override fun onItemSelected(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ) {
        when (parent!!.getId()) {
            1 -> {
                spinner.setSelection(position)
                role_id = list_spinner.get(position)
            }
            2 -> {
                sp_manager.setSelection(position)
                mannager_id = list_mannnagerid.get(position)
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}

