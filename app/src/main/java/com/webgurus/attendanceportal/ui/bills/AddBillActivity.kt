package com.webgurus.attendanceportal.ui.bills

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.baseproject.utils.Utils
import com.webgurus.attendanceportal.InternetSettingCheck
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.AddBillPojo
import com.webgurus.attendanceportal.pojo.BillUpdatePojo
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.fragment_addbills.*
import retrofit2.Call
import retrofit2.Response


class AddBillActivity : BaseActivity() {

    var ed_billno: EditText? = null
    var ed_totalamount: EditText? = null
    var ed_paymentreceived: EditText? = null
    var ed_paymentpending: EditText? = null
    var btn_createcustomer: Button? = null
    var tv_header: TextView? = null

    var sharedPreferences: SharedPreferences? = null
    var custromerID: String = ""
    var billNo: String = ""
    var totalamount: String = ""
    var paymentreceive: String = ""
    var pendingpayment: String = ""
    var billID: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_addbills)
        initview()
        listeners()
    }

    private fun listeners() {
        btn_createcustomer!!.setOnClickListener(View.OnClickListener {
            if (ed_billno!!.text.toString().equals("")) {
                Toast.makeText(this, "Bill No. is mandatory to fill .", Toast.LENGTH_SHORT).show()
            } else if (ed_totalamount!!.text.toString().equals("")) {
                Toast.makeText(this, "Total Amount is mandatory to fill .", Toast.LENGTH_SHORT)
                    .show()
            } else if (ed_paymentreceived!!.text.toString().equals("")) {
                Toast.makeText(
                    this,
                    "Total Payment Receive is mandatory to fill .",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (ed_paymentpending!!.text.toString().equals("")) {
                Toast.makeText(this, "Pending Payment is mandatory to fill .", Toast.LENGTH_SHORT)
                    .show()
            } else  {
                if(!billID.equals("")){
                    hitApitoupdateBill()
                }else{
                    hitApitoaddBill()
                }

            }

        })

        iv_backfromcustomer.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun hitApitoupdateBill() {
        showLoading(true)
        if (!Utils.isConnected(this)) {
            Toast.makeText(
                this,
                "No internet connection available. Please check your internet connection.",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(this, InternetSettingCheck::class.java))
        } else {
            val service =
                RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
            val call = service.updateBills(
                "Bearer " + sharedPreferences!!.getString("Access_Token", ""),
                ed_billno!!.text.toString(),
                ed_totalamount!!.text.toString(),
                ed_paymentreceived!!.text.toString(),
                ed_paymentpending!!.text.toString(),
                custromerID,
                billID
            )
            call.enqueue(object : retrofit2.Callback<BillUpdatePojo?> {
                override fun onResponse(
                    call: Call<BillUpdatePojo?>,
                    response: Response<BillUpdatePojo?>
                ) {
                    try {
                        Toast.makeText(
                            this@AddBillActivity,
                            "" + response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@AddBillActivity,
                            "" + e.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    hideProgress()
                }

                override fun onFailure(call: Call<BillUpdatePojo?>, t: Throwable) {
                    Toast.makeText(
                        this@AddBillActivity,
                        t.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    hideProgress()

                }
            })

        }
    }

    private fun initview() {
        sharedPreferences = this.getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE)
        tv_header = findViewById(R.id.tv_header)
        btn_createcustomer = findViewById(R.id.btn_createcustomer)
        ed_billno = findViewById(R.id.ed_billno)
        ed_totalamount = findViewById(R.id.ed_totalamount)
        ed_paymentreceived = findViewById(R.id.ed_paymentreceived)
        ed_paymentpending = findViewById(R.id.ed_paymentpending)
        if (intent.getStringExtra("customerID") != null) {
            custromerID = intent.getStringExtra("customerID")!!
            tv_header!!.setText("Add Bill")
            btn_createcustomer!!.setText("Add Bill")
        }
        if (intent.getStringExtra("billID") != null) {
            billNo = intent.getStringExtra("billno")!!
            totalamount = intent.getStringExtra("totalamount")!!
            paymentreceive = intent.getStringExtra("paymentreceive")!!
            pendingpayment = intent.getStringExtra("pendingpayment")!!
            custromerID = intent.getStringExtra("customerIDs")!!
            billID = intent.getStringExtra("billID")!!
            tv_header!!.setText("Update Bill")
            btn_createcustomer!!.setText("Update Bill")
            setData()
        }

    }

    private fun setData(){
        if(!billID.equals("")){
            ed_billno!!.setText(billNo)
            ed_totalamount!!.setText(totalamount)
            ed_paymentreceived!!.setText(paymentreceive)
            ed_paymentpending!!.setText(pendingpayment)
        }

    }


    private fun hitApitoaddBill() {
        showLoading(true)
        if (!Utils.isConnected(this)) {
            Toast.makeText(
                this,
                "No internet connection available. Please check your internet connection.",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(this, InternetSettingCheck::class.java))
        } else {
            val service =
                RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
            val call = service.addBills(
                "Bearer " + sharedPreferences!!.getString("Access_Token", ""),
                ed_billno!!.text.toString(),
                ed_totalamount!!.text.toString(),
                ed_paymentreceived!!.text.toString(),
                ed_paymentpending!!.text.toString(),
                custromerID
            )
            call.enqueue(object : retrofit2.Callback<AddBillPojo?> {
                override fun onResponse(
                    call: Call<AddBillPojo?>,
                    response: Response<AddBillPojo?>
                ) {
                    try {
                        Toast.makeText(
                            this@AddBillActivity,
                            "" + response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@AddBillActivity,
                            "" + e.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    hideProgress()
                }

                override fun onFailure(call: Call<AddBillPojo?>, t: Throwable) {
                    Toast.makeText(
                        this@AddBillActivity,
                        t.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    hideProgress()

                }
            })

        }
    }


}