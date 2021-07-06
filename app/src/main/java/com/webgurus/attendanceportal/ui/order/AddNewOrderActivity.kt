package com.webgurus.attendanceportal.ui.order

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.*
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_addorder.*
import kotlinx.android.synthetic.main.activity_addproduct.*
import kotlinx.android.synthetic.main.fragment_customers.*
import kotlinx.android.synthetic.main.fragment_productmanagement.*
import kotlinx.android.synthetic.main.fragment_unitlisting.*
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception


class AddNewOrderActivity : BaseActivity() ,AdapterView.OnItemSelectedListener {

    var mList : ArrayList<Datar> = ArrayList()
    var mListCategories : ArrayList<String> = ArrayList()
    var sharedPreferences : SharedPreferences? =null
    lateinit var sp_customer : Spinner
    lateinit var sp_spinnerproduct : Spinner
    lateinit var sp_spinnerproductunit : Spinner
    var mListCatg: ArrayList<ProductData>? = ArrayList()
    var mListVariant: ArrayList<Variant>? = ArrayList()
    var mListproducts: ArrayList<String>? = ArrayList()
    val mUnitList: ArrayList<DataList> = ArrayList()
    val mUnitspinnerList: ArrayList<String> = ArrayList()
    var ed_quantity : EditText ? =null
    var ed_price : EditText ? =null
    var ed_address : EditText ? =null
    var ed_instruction : EditText ? =null
    var ed_payment : EditText ? =null
    var mProductList:ArrayList<AddOrderPojo> = ArrayList()
    var product_id : Int =0
    var customer_id : Int =0
    var unit_id : Int = 0
    var customer_name:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addorder)
        initview()
        listeners()
        hitApitoGetCustomerisitng()
    }


    private fun listeners() {
        iv_backneworder.setOnClickListener(View.OnClickListener {
            finish()
        })
        btn_addneworder.setOnClickListener(View.OnClickListener {
              if(ed_quantity!!.text.toString().equals("")){
                  Toast.makeText(this,"Quantity is mandatory to fill .",Toast.LENGTH_SHORT).show()
              }else if(ed_price!!.text.toString().equals("")){
                  Toast.makeText(this,"Price is mandatory to fill .",Toast.LENGTH_SHORT).show()
              }else if(ed_address!!.text.toString().equals("")){
                  Toast.makeText(this,"Address is mandatory to fill .",Toast.LENGTH_SHORT).show()
              }

              else if(ed_instruction!!.text.toString().equals("")){
                  Toast.makeText(this,"Instruction is mandatory to fill .",Toast.LENGTH_SHORT).show()
              }
              else{
                    hitApitoaddNewOrder()
              }
        })
    }

    private fun hitApitoaddNewOrder() {
        mProductList.clear()
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.addOrder("Bearer " + sharedPreferences!!.getString("Access_Token", ""),
             product_id,
             ed_quantity!!.text.toString(),
             ed_price!!.text.toString(),
             customer_id,
             ed_address!!.text.toString(),
             ed_instruction!!.text.toString()
            )
        Log.d("Order Details","ProductID :"+product_id+"   CustomerID: "+customer_id)
        showLoading(true)
        call.enqueue(object : retrofit2.Callback<AddOrderPojo?> {
            override fun onResponse(call: Call<AddOrderPojo?>, response: Response<AddOrderPojo?>) {
                if(response.body()!=null){
                        if(response.body()!!.status==1&&response.body()!!.message.equals("Order Added")){
                            Toast.makeText(this@AddNewOrderActivity,""+response.body()!!.message,Toast.LENGTH_SHORT).show()
                            finish()
                        }else{
                            Toast.makeText(this@AddNewOrderActivity,""+response.body()!!.message,Toast.LENGTH_SHORT).show()
                        }
                    hideProgress()
                }

            }

            override fun onFailure(call: Call<AddOrderPojo?>, t: Throwable) {
                hideProgress()
            }

        })

    }


    private fun initview() {
        if(intent!!.getStringExtra("username")!=null){
            customer_name=intent!!.getStringExtra("username")!!
        }
        ed_quantity=findViewById(R.id.ed_quantity)
        ed_price=findViewById(R.id.ed_price)
        ed_address=findViewById(R.id.ed_address)
        ed_instruction=findViewById(R.id.ed_instruction)
        ed_payment=findViewById(R.id.ed_payment)
        sharedPreferences= this.getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE)
        sp_customer = Spinner(this)
        sp_spinnerproduct = Spinner(this)
        sp_spinnerproductunit = Spinner(this)
        sp_customer.id = 1
        sp_spinnerproduct.id = 2
        sp_spinnerproductunit.id = 3
    }


    private fun setSpinner(mCustomerList:ArrayList<String>) {
        var aa = ArrayAdapter(this, R.layout.simple_spinner_list, mCustomerList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val ll = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        rl_spinnercustomer.addView(sp_customer)
        aa = ArrayAdapter(this, R.layout.spinner_right_aligned, mCustomerList)
        aa.setDropDownViewResource(R.layout.spinner_right_aligned)
        with(sp_customer)
        {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener = this@AddNewOrderActivity
            layoutParams = ll
            prompt = "Select your favourite language"

        }

        if(customer_name.equals("")){
            sp_customer.setSelection(0)
            customer_id=0
        }else{
            for (i in 0 until mListCategories.size){
                if(mListCategories.get(i).equals(customer_name)){
                    sp_customer.setSelection(i)
                    customer_id=mList.get(i-1).id
                  //  Toast.makeText(this,""+customer_id.toString(),Toast.LENGTH_SHORT).show()
                }
                sp_customer.isEnabled=false
            }

        }


    }
    private fun setSpinnerProduct(mCustomerList:ArrayList<String>) {
        var aa = ArrayAdapter(this, R.layout.simple_spinner_list, mCustomerList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val ll = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        rl_spinnerproduct.addView(sp_spinnerproduct)
        aa = ArrayAdapter(this, R.layout.spinner_right_aligned, mCustomerList)
        aa.setDropDownViewResource(R.layout.spinner_right_aligned)
        with(sp_spinnerproduct)
        {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener = this@AddNewOrderActivity
            layoutParams = ll
            prompt = "Select your favourite language"

        }
        sp_spinnerproduct.setSelection(0)
        if(mListCatg!=null){
            if(mListproducts!!.size==1){
                sp_spinnerproduct.setSelection(0)
            }else{
                product_id=mListCatg!!.get(0).id
                product_id=mListVariant!!.get(0).id
            }

        }


    }

    private fun setSpinnerProductUnit(mCustomerList:ArrayList<String>) {
        var aa = ArrayAdapter(this, R.layout.simple_spinner_list, mCustomerList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val ll = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        rl_spinnerproductunitsss.addView(sp_spinnerproductunit)
        aa = ArrayAdapter(this, R.layout.spinner_right_aligned, mCustomerList)
        aa.setDropDownViewResource(R.layout.spinner_right_aligned)
        with(sp_spinnerproductunit)
        {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener = this@AddNewOrderActivity
            layoutParams = ll
            prompt = "Select your favourite language"

        }
        sp_spinnerproductunit.setSelection(0)
        unit_id= mUnitList.get(0).id
    }

    private fun hitApitoGetCustomerisitng(){
        mList.clear()
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getCustomerList("Bearer " + sharedPreferences!!.getString("Access_Token", ""))
        showLoading(true)
        call.enqueue(object : retrofit2.Callback<GetCustomerLisiting?> {
            override fun onResponse(call: Call<GetCustomerLisiting?>, response: Response<GetCustomerLisiting?>) {
                if(response.body()!=null){
                    hideProgress()
                    mListCategories.add("Select Customers")
                    mList.addAll(response.body()!!.data)
                    for (i in 0 until response.body()!!.data.size){
                        mListCategories.add(response.body()!!.data[i].first_name+" "+response.body()!!.data[i].last_name)
                    }

                       setSpinner(mListCategories)
                }
                hitApitoGetAllProducts()

            }

            override fun onFailure(call: Call<GetCustomerLisiting?>, t: Throwable) {
                hideProgress()
            }

        })
    }


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
                Toast.makeText(this@AddNewOrderActivity, "Failed", Toast.LENGTH_SHORT).show()
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
                    mUnitspinnerList.clear()
                    mUnitspinnerList.add("Select Variant Units")
                    if (mUnitList.size > 0) {
                        for (i in 0 until response.body()!!.data.size){
                            mUnitspinnerList.add(response.body()!!.data[i].unit)
                        }
                        setSpinnerProductUnit(mUnitspinnerList)
                    } else {
                        Toast.makeText(this@AddNewOrderActivity, "No Data Available", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
//                    Toast.makeText(
//                        this@AddNewOrderActivity,
//                        resources.getString(R.string.servernotrespond),
//                        Toast.LENGTH_SHORT
//                    ).show()
                    hideProgress()
                }
            }
        })
    }

    private fun hitApitogetUnitlistByProducts(iD:Int) {
        showLoading(true)
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getproductVariant(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            ),
            iD
        )
        call.enqueue(object : retrofit2.Callback<UnitListPojo?> {
            override fun onFailure(call: Call<UnitListPojo?>, t: Throwable) {
                Toast.makeText(this@AddNewOrderActivity, "Failed", Toast.LENGTH_SHORT).show()
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
                    mUnitspinnerList.clear()
                    mUnitspinnerList.add("Select Variant Units")
                    mUnitList.addAll(response!!.body()!!.data)
                    if (mUnitList.size > 0) {

                        for (i in 0 until response.body()!!.data.size){
                            mUnitspinnerList.add(response.body()!!.data[i].unit)
                        }
                        setSpinnerProductUnit(mUnitspinnerList)
                    } else {
                        Toast.makeText(this@AddNewOrderActivity, "No Units Available for now", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
//                    Toast.makeText(
//                        this@AddNewOrderActivity,
//                        resources.getString(R.string.servernotrespond),
//                        Toast.LENGTH_SHORT
//                    ).show()
                    hideProgress()
                }
            }
        })
    }



    private fun hitApitoGetAllProducts() {
        mListCatg!!.clear()
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getProductLisitng(
            "Bearer " + sharedPreferences!!.getString("Access_Token", ""),
            "application/json"
        )
        // showProgressbar()
        call.enqueue(object : retrofit2.Callback<ProductListingPojo> {
            override fun onResponse(
                call: Call<ProductListingPojo?>,
                response: Response<ProductListingPojo?>
            ) {
                if (response.body() != null) {
                    mListCatg!!.addAll(response.body()!!.data)
                    if(mListCatg!!.size>0){
                        mListproducts!!.add("Select Product")
                        for (i in 0 until response.body()!!.data.size){
                            mListproducts!!.add(response.body()!!.data[i].name)
                            mListVariant!!.addAll(response.body()!!.data[i].variants)
                        }
                        setSpinnerProduct(mListproducts!!)
                        hitApitogetUnitlist()
                    }else{
                        mListproducts!!.add("No data Available")
                        setSpinnerProduct(mListproducts!!)
                        hitApitogetUnitlist()
                    }



                }
            }

            override fun onFailure(call: Call<ProductListingPojo>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent!!.getId()) {
            1 -> {
                if(position==0){
                    sp_customer.setSelection(position)
                    Toast.makeText(this,"Please Select the customer first",Toast.LENGTH_SHORT).show()
                    return
                }
                sp_customer.setSelection(position)
                customer_id=mList.get(position-1).id
            }
            2 ->{
                if(position==0){
                    sp_customer.setSelection(position)
                  Toast.makeText(this,"Please Select the Product first",Toast.LENGTH_SHORT).show()
                    return
                }
                sp_spinnerproduct.setSelection(position)
                product_id=mListCatg!!.get(position-1).id
                hitApitogetUnitlistByProducts(mListCatg!!.get(position-1).id)
            }
            3 ->{
                if(position==0){
                    sp_spinnerproductunit.setSelection(position)
                   Toast.makeText(this,"Please Select the Product first",Toast.LENGTH_SHORT).show()
                    return
                }
                sp_spinnerproductunit.setSelection(position)
                unit_id=mUnitList.get(position-1).id


            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}