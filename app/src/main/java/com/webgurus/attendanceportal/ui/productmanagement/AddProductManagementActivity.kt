package com.webgurus.attendanceportal.ui.productmanagement

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
import kotlinx.android.synthetic.main.activity_addproduct.*
import kotlinx.android.synthetic.main.fragment_categorylisting.*
import kotlinx.android.synthetic.main.fragment_createuser.*
import kotlinx.android.synthetic.main.fragment_unitlisting.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class AddProductManagementActivity: BaseActivity()  , AdapterView.OnItemSelectedListener {

    var mListCategoryName: ArrayList<String>? = ArrayList()
    var mListCategoryId: ArrayList<Int>? = ArrayList()
    var mCatList: ArrayList<GetCategoryListingPojo>? = ArrayList()
    var sharedPreferences: SharedPreferences? = null
    lateinit var sp_category : Spinner
    lateinit var sp_unitproduct : Spinner
    lateinit var sp_instocks : Spinner
    val mUnitList: ArrayList<String> = ArrayList()
    val mUnit: ArrayList<Int> = ArrayList()
    var mListStock: ArrayList<String>? = ArrayList()
    var button_add :Button ?=null

    var catID =0
    var unit_id =0
    var stock_id =0
    var  mProductDataList: ArrayList<Variant> = ArrayList()
    var postion=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addproduct)
        initiview()
        listeners()
        hitApitoGetCategoriesListing()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun initiview() {
        if(intent.getStringExtra("status")!=null){
            tv_header.setText("Update Product")
            btn_addprproduct.setText("Update Product")
            mProductDataList = this.intent.extras!!.getParcelableArrayList<Variant>("productdetails")!!
            postion= this.intent.getIntExtra("position",0)
            ed_productname.setText(mProductDataList.get(postion).product_name)
            ed_minprice.setText(mProductDataList.get(postion).min_price)
            ed_maxprice.setText(mProductDataList.get(postion).max_price)
            ed_price.setText(mProductDataList.get(postion).price)
            ed_quantity.setText(mProductDataList.get(postion).quantity_left.toString())
        }else{
            tv_header.setText("Create Product")
            btn_addprproduct.setText("Add Product")
        }
        button_add=findViewById(R.id.btn_addprproduct)
        sp_category = Spinner(this)
        sp_unitproduct = Spinner(this)
        sp_instocks = Spinner(this)
        sp_category.id=1
        sp_unitproduct.id=2
        sp_instocks.id=3
        sharedPreferences = this.getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE)

    }
    private fun setSpinner(mSpinnerList:ArrayList<String>) {
        var aa = ArrayAdapter(this, R.layout.simple_spinner_list, mSpinnerList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val ll = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        rl_spinnercat.addView(sp_category)
        aa = ArrayAdapter(this, R.layout.spinner_right_aligned, mSpinnerList)
        aa.setDropDownViewResource(R.layout.spinner_right_aligned)
        with(sp_category)
        {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener =this@AddProductManagementActivity
            layoutParams = ll
            prompt = "Select your favourite language"

        }

        if(mProductDataList.size>0){
            if(mProductDataList.size>0){
                var catIDs=mProductDataList.get(postion).category_id
                for (i in 0 until mListCategoryId!!.size){
                    if(catIDs== mListCategoryId!![i]){
                        sp_category.setSelection(i+1)
                        catID=mProductDataList.get(postion).category_id
                    }
                }
            }

        }else{
            sp_category.setSelection(0)
        }


    }

    private fun setSpinner2(mSpinnerList:ArrayList<String>) {
        var aa = ArrayAdapter(this, R.layout.simple_spinner_list, mSpinnerList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val ll = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        rl_unitview.addView(sp_unitproduct)
        aa = ArrayAdapter(this, R.layout.spinner_right_aligned, mSpinnerList)
        aa.setDropDownViewResource(R.layout.spinner_right_aligned)
        with(sp_unitproduct)
        {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener =this@AddProductManagementActivity
            layoutParams = ll
            prompt = "Select your favourite language"

        }


        if(mProductDataList.size>0){
            var unitID=mProductDataList.get(postion).unit_id
            for (i in 0 until mUnit.size){
                    if(unitID== mUnit[i]){
                        sp_unitproduct.setSelection(i+1)
                        unit_id=mProductDataList.get(postion).unit_id
                    }
                }


        }else{
            sp_unitproduct.setSelection(0)
        }


    }


    private fun setSpinner3() {
        mListStock!!.clear()
        mListStock!!.add("Select Stock")
        mListStock!!.add("Yes")
        mListStock!!.add("No")
        var aa = ArrayAdapter(this, R.layout.simple_spinner_list, mListStock!!)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val ll = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        rl_instock.addView(sp_instocks)
        aa = ArrayAdapter(this, R.layout.spinner_right_aligned, mListStock!!)
        aa.setDropDownViewResource(R.layout.spinner_right_aligned)
        with(sp_instocks)
        {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener =this@AddProductManagementActivity
            layoutParams = ll
            prompt = "Select your favourite language"

        }

        if(mProductDataList.size>0){
            var inStock=mProductDataList.get(postion).in_stock
            if(inStock==1){
                sp_instocks.setSelection(2)
            }else if(inStock==2){
                sp_instocks.setSelection(1)
            }

        }else{
            sp_instocks.setSelection(0)
        }
    }

    private fun hitApitoGetCategoriesListing(){
        mListCategoryName!!.clear()
        mListCategoryId!!.clear()
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getCategoryListing("Bearer " + sharedPreferences!!.getString("Access_Token", ""),
            "Accept")
        showLoading(true)
        call.enqueue(object : retrofit2.Callback<GetCategoryListingPojo?> {
            override fun onResponse(call: Call<GetCategoryListingPojo?>, response: Response<GetCategoryListingPojo?>) {
                if (response.body() != null) {
                    hideProgress()
                    mListCategoryName!!.add("Select Category")
                    for (i in 0 until response.body()!!.data.size){
                        mListCategoryName!!.add(response.body()!!.data[i].name)
                        mListCategoryId!!.add(response.body()!!.data[i].id)

                    }

                    setSpinner(mListCategoryName!!)
                    hitApitogetAllUnits()
                }

            }

            override fun onFailure(call: Call<GetCategoryListingPojo?>, t: Throwable) {

            }

        })
    }

    private fun hitApitogetAllUnits() {
        showLoading(true)
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        showLoading(true)
        val call = service.getUnitList(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            )
        )
        call.enqueue(object : retrofit2.Callback<UnitListPojo?> {
            override fun onFailure(call: Call<UnitListPojo?>, t: Throwable) {
                Toast.makeText(this@AddProductManagementActivity, "Failed", Toast.LENGTH_SHORT).show()
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
                    mUnitList.add("Select Unit")
                    for (i in 0 until response!!.body()!!.data.size){
                        mUnitList.add(response.body()!!.data[i].unit)
                        mUnit.add(response.body()!!.data[i].id)

                    }

                    setSpinner2(mUnitList)
                    setSpinner3()

                } catch (e: Exception) {
                    Toast.makeText(
                        this@AddProductManagementActivity,
                        resources.getString(R.string.servernotrespond),
                        Toast.LENGTH_SHORT
                    ).show()
                    hideProgress()
                }
            }
        })
    }


    private fun listeners() {
        ic_backforproduct.setOnClickListener(View.OnClickListener {
            finish()
        })
        button_add!!.setOnClickListener(View.OnClickListener {
           if(ed_productname.text.toString().equals("")){
               Toast.makeText(this,"Product name is mandatory to fill .",Toast.LENGTH_SHORT).show()
           }
            else if(ed_minprice.text.toString().equals("")){
                Toast.makeText(this,"Minimum price is mandatory to fill .",Toast.LENGTH_SHORT).show()
            }
           else if(ed_maxprice.text.toString().equals("")){
               Toast.makeText(this,"Maximum price is mandatory to fill .",Toast.LENGTH_SHORT).show()
           }
           else if(ed_price.text.toString().equals("")){
               Toast.makeText(this,"Price is mandatory to fill .",Toast.LENGTH_SHORT).show()
           }

           else if(ed_quantity.text.toString().equals("")){
               Toast.makeText(this,"Quantity is mandatory to fill .",Toast.LENGTH_SHORT).show()
           }

           else if(stock_id==0){
               Toast.makeText(this,"Please select the stock first .",Toast.LENGTH_SHORT).show()
           }
           else if(catID==0){
               Toast.makeText(this,"Please select the product category .",Toast.LENGTH_SHORT).show()
           }
           else if(unit_id==0){
               Toast.makeText(this,"Please select the unit .",Toast.LENGTH_SHORT).show()
           }
           else if(intent.getStringExtra("status")!=null){
                      hitApitoUpdateProduct(catID,unit_id,ed_minprice.text.toString(),ed_maxprice.text.toString(),ed_price.text.toString()
                          ,ed_quantity.text.toString(),ed_productname.text.toString(),stock_id,catID,mProductDataList.get(postion).id
                      )
            }
           else{
               hitApitoAddProduct(catID,unit_id,ed_minprice.text.toString(),ed_maxprice.text.toString(),ed_price.text.toString()
               ,ed_quantity.text.toString(),ed_productname.text.toString(),stock_id
               )
           }

        })
    }

    private fun hitApitoUpdateProduct(catID: Int,
                                      unit_id: Int,
                                      min_price: String,
                                      maxprice:String,
                                      price:String,
                                      quantity:String,
                                      name:String,
                                      in_stock:Int,
                                      productID:Int,
                                      variant_ID:Int


    ) {


        showLoading(true)
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.updateProduct(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            ),
            "application/json",
            catID,unit_id,min_price,maxprice,price,quantity,name,in_stock,variant_ID
        )
        call.enqueue(object : Callback<UpdateProductPojo?> {
            override fun onResponse(
                call: Call<UpdateProductPojo?>,
                response: Response<UpdateProductPojo?>
            ) {

                if(response.body()!!.status==1){
                    hideProgress()
                    hideProgress()
                    Toast.makeText(this@AddProductManagementActivity,""+response.body()!!.message,Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    hideProgress()
                    Toast.makeText(this@AddProductManagementActivity,""+response.body()!!.message,Toast.LENGTH_SHORT).show()
                }


            }

            override fun onFailure(call: Call<UpdateProductPojo?>, t: Throwable) {
                Toast.makeText(this@AddProductManagementActivity,t.toString(),Toast.LENGTH_SHORT).show()
            }

        })


    }

    private fun hitApitoAddProduct(
        catID: Int,
        unit_id: Int, min_price: String,
        maxprice:String,
        price:String,
        quantity:String,
        name:String,
        in_stock:Int) {

        showLoading(true)

        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.addProduct(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            ),
           "application/json",
            catID,unit_id,min_price,maxprice,price,quantity,name,in_stock
        )
        call.enqueue(object : Callback<AddProductPojo?> {
            override fun onResponse(
                call: Call<AddProductPojo?>,
                response: Response<AddProductPojo?>
            ) {

                 hideProgress()
                if(response.body()!!.status==1){
                    hideProgress()
                    Toast.makeText(this@AddProductManagementActivity,""+response.body()!!.message,Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this@AddProductManagementActivity,""+response.body()!!.message,Toast.LENGTH_SHORT).show()
                }


            }

            override fun onFailure(call: Call<AddProductPojo?>, t: Throwable) {
                Toast.makeText(this@AddProductManagementActivity,t.toString(),Toast.LENGTH_SHORT).show()
                hideProgress()
            }

        })

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent!!.getId()) {
            1 -> {
                sp_category.setSelection(position)
                catID=mListCategoryId!!.get(position-1)
            }
            2 ->{
                sp_unitproduct.setSelection(position)
                unit_id=mUnit.get(position-1)
            }
            3 ->{
                sp_instocks.setSelection(position)
                if(mListStock!![position].equals("No")){
                    stock_id=1
                }else if(mListStock!![position].equals("Yes")){
                    stock_id=2
                }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    }


