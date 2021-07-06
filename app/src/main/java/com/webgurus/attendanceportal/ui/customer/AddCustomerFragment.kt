package com.webgurus.attendanceportal.ui.customer

import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.baseproject.utils.Utils
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.CreateCustomerPojo
import com.webgurus.attendanceportal.pojo.CustomerUpdatedPojo
import com.webgurus.attendanceportal.pojo.Datar
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.fragment_addcustomer.*
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddCustomerFragment: BaseActivity()  {


    var sharedPreferences : SharedPreferences? =null
    var mList : ArrayList<Datar> = ArrayList()
    var mPosition:Int?= 0
    var mUserID:Int?=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_addcustomer)
        initview()
    }

    private fun initview() {
        if(this.intent.getStringExtra("status")!=null){
            mList = this.intent.extras!!.getParcelableArrayList<Datar>("customerdetails")!!
            mPosition=intent.getIntExtra("pos",0)
            if(mList.get(mPosition!!).id!=0){
                tv_header.setText("Update Customer")
                btn_createcustomer.setText("Update Customer")
                setValues()
            }else{
                tv_header.setText("Add  Customer")
                btn_createcustomer.setText("Create Customer")
            }
        }
        sharedPreferences= this@AddCustomerFragment.getSharedPreferences(
            "AcessToken",
            AppCompatActivity.MODE_PRIVATE
        )
        listeners()
    }

    private fun setValues() {
        mUserID=mList.get(mPosition!!).id
        ed_firstnamecust.setText(mList.get(mPosition!!).first_name)
        ed_middlename.setText(mList.get(mPosition!!).middle_name)
        ed_lastnamecust.setText(mList.get(mPosition!!).last_name)
        ed_emailcust.setText(mList.get(mPosition!!).email)
        ed_phonenumbercust.setText(mList.get(mPosition!!).phone_number)
        ed_addresscust.setText(mList.get(mPosition!!).address)
        ed_secondcust.setText(mList.get(mPosition!!).secondary_address)
        tv_dobcust.setText(mList.get(mPosition!!).dob)
        ed_statecus.setText(mList.get(mPosition!!).state)
        ed_citycust.setText(mList.get(mPosition!!).city)
        ed_pincodecust.setText(mList.get(mPosition!!).pincode.toString())

    }

    private fun listeners() {
        btn_createcustomer.setOnClickListener(View.OnClickListener {
            validations()
        })
        rl_calendercustomer.setOnClickListener(View.OnClickListener {
            openCalender()
        })
        iv_backfromcustomer.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun validations() {
        if(ed_firstnamecust.text.toString().equals("")){
            ed_firstnamecust.setHint("This is required Field")
            ed_firstnamecust.setHintTextColor(resources.getColor(R.color.red))
            Toast.makeText(this@AddCustomerFragment, "First name is mandatory to fill", Toast.LENGTH_SHORT).show()
        }else if(ed_middlename.text.toString().equals("")){
            ed_middlename.setHint("This is required Field")
            ed_middlename.setHintTextColor(resources.getColor(R.color.red))
            Toast.makeText(this@AddCustomerFragment, "Middle name is mandatory to fill", Toast.LENGTH_SHORT).show()
        }else if(ed_lastnamecust.text.toString().equals("")){
            ed_lastnamecust.setHint("This is required Field")
            ed_lastnamecust.setHintTextColor(resources.getColor(R.color.red))
            Toast.makeText(this@AddCustomerFragment, "Last name is mandatory to fill", Toast.LENGTH_SHORT).show()

        }else if(ed_emailcust.text.toString().equals("")){
            ed_emailcust.setHint("This is required Field")
            ed_emailcust.setHintTextColor(resources.getColor(R.color.red))
            Toast.makeText(this@AddCustomerFragment, "Email Address is mandatory to fill", Toast.LENGTH_SHORT).show()

        }

        else if (!Utils.isEmailValid(ed_emailcust.text.toString())) {
            ed_emailcust.setHint("Email Address is not Valid.")
            ed_emailcust.setHintTextColor(resources.getColor(R.color.red))
            Toast.makeText(this@AddCustomerFragment, "Email address is not valid.", Toast.LENGTH_SHORT).show()
        }

        else if(ed_phonenumbercust.text.toString().equals("")){
            ed_phonenumbercust.setHint("This is required Field")
            ed_phonenumbercust.setHintTextColor(resources.getColor(R.color.red))
            Toast.makeText(this@AddCustomerFragment, "Phone Number is mandatory to fill", Toast.LENGTH_SHORT).show()

        }
        else if(ed_addresscust.text.toString().equals("")){
            ed_addresscust.setHint("This is required Field")
            ed_addresscust.setHintTextColor(resources.getColor(R.color.red))
            Toast.makeText(this@AddCustomerFragment, "Address is mandatory to fill", Toast.LENGTH_SHORT).show()

        }

        else if(ed_secondcust.text.toString().equals("")){
            ed_secondcust.setHint("This is required Field")
            ed_secondcust.setHintTextColor(resources.getColor(R.color.red))
            Toast.makeText(this@AddCustomerFragment, "Second Address is mandatory to fill", Toast.LENGTH_SHORT).show()

        }

        else if(tv_dobcust.text.toString().equals("")){
            tv_dobcust.setHint("This is required Field")
            tv_dobcust.setHintTextColor(resources.getColor(R.color.red))
            Toast.makeText(this@AddCustomerFragment, "Date of birth is mandatory to fill", Toast.LENGTH_SHORT).show()

        }

        else if(ed_statecus.text.toString().equals("")){
            ed_statecus.setHint("This is required Field")
            ed_statecus.setHintTextColor(resources.getColor(R.color.red))
            Toast.makeText(this@AddCustomerFragment, "State is mandatory to fill", Toast.LENGTH_SHORT).show()

        }

        else if(ed_citycust.text.toString().equals("")){
            ed_citycust.setHint("This is required Field")
            ed_citycust.setHintTextColor(resources.getColor(R.color.red))
            Toast.makeText(this@AddCustomerFragment, "City is mandatory to fill", Toast.LENGTH_SHORT).show()

        }
        else if(ed_pincodecust.text.toString().equals("")){
            ed_pincodecust.setHint("This is required Field")
            ed_pincodecust.setHintTextColor(resources.getColor(R.color.red))
            Toast.makeText(this@AddCustomerFragment, "Pincode is mandatory to fill", Toast.LENGTH_SHORT).show()

        }
        else{

            if(this.intent.getStringExtra("status")!=null){
                showLoading(true)
                hitApitoUpdateCustomer(
                    ed_firstnamecust.text.toString(),
                    ed_middlename.text.toString(),
                    ed_lastnamecust.text.toString()
                    ,ed_emailcust.text.toString(),
                    ed_phonenumbercust.text.toString(),
                    ed_addresscust.text.toString(),
                    ed_secondcust.text.toString(),
                    tv_dobcust.text.toString(),
                    ed_statecus.text.toString(),
                    ed_citycust.text.toString(),
                    ed_pincodecust.text.toString(),
                    mList.get(mPosition!!).id
                )
            }else{
                showLoading(true)
                hitApitocreatecustomer(
                    ed_firstnamecust.text.toString(),
                    ed_middlename.text.toString(),
                    ed_lastnamecust.text.toString()
                    ,ed_emailcust.text.toString(),
                    ed_phonenumbercust.text.toString(),
                    ed_addresscust.text.toString(),
                    ed_secondcust.text.toString(),
                    tv_dobcust.text.toString(),
                    ed_statecus.text.toString(),
                    ed_citycust.text.toString(),
                    ed_pincodecust.text.toString()
                )
            }


        }

    }

    private fun hitApitocreatecustomer(first_name:String,middleName:String , lastname : String , emailAddres : String
                                       , phoneNumber:String , address :String , secondAddress : String , dob :String ,
                                       state : String , city : String ,pincode:String
    ) {
        showLoading(true)
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.createCustomer(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            ),
            first_name,
            middleName,
            lastname,
            emailAddres,
            phoneNumber,
            address,
            secondAddress,
            state,
            city,
            dob,
            pincode

        )
        call.enqueue(object : retrofit2.Callback<CreateCustomerPojo?> {
            override fun onResponse(
                call: Call<CreateCustomerPojo?>,
                response: Response<CreateCustomerPojo?>

            ) {
                if(response.body()!!.status==0){
                    Toast.makeText(this@AddCustomerFragment,response.body()!!.message,Toast.LENGTH_SHORT).show()
                    hideProgress()
                }else{
                    Toast.makeText(this@AddCustomerFragment,response.body()!!.message,Toast.LENGTH_SHORT).show()
                    finish()
                    hideProgress()
                }

            }

            override fun onFailure(call: Call<CreateCustomerPojo?>, t: Throwable) {
                hideProgress()
            }

        })
    }


    private fun hitApitoUpdateCustomer(
        firstname: String,
        middleName: String,
        lastname: String,
        emailAddres:String,
        phoneNumber: String,
        firstAddress: String,
        secondAddress: String,
        dob: String,
        state: String,
        city: String,
        pincode: String,
        customerID:Int
    ) {

        showLoading(true)
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.updateCustomer(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            ),
            firstname,
            middleName,
            lastname,
            emailAddres,
            phoneNumber,
            firstAddress,
            secondAddress,
            state,
            city,
            dob,
            pincode,
            customerID
            )

        call.enqueue(object : retrofit2.Callback<CustomerUpdatedPojo?> {
            override fun onResponse(
                call: Call<CustomerUpdatedPojo?>,
                response: Response<CustomerUpdatedPojo?>

            ) {
                if(response.body()!!.status==0){
                    Toast.makeText(this@AddCustomerFragment,response.body()!!.message,Toast.LENGTH_SHORT).show()
                    hideProgress()
                }else{
                    Toast.makeText(this@AddCustomerFragment,response.body()!!.message,Toast.LENGTH_SHORT).show()
                    finish()
                    hideProgress()
                }

            }

            override fun onFailure(call: Call<CustomerUpdatedPojo?>, t: Throwable) {
                hideProgress()
            }

        })


    }


    fun openCalender() {
        rl_calendercustomer.setOnClickListener(View.OnClickListener {
            val myCalendar = Calendar.getInstance()
            val date =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
                    myCalendar[Calendar.YEAR] = year
                    myCalendar[Calendar.MONTH] = monthOfYear
                    myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                    val myFormat = "dd-MMM-yyyy" // your format
                    val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                    tv_dobcust.setText(sdf.format(myCalendar.time))
                }
            DatePickerDialog(
                this@AddCustomerFragment,
                date,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        })

    }





}