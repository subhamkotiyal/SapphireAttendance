package com.webgurus.attendanceportal.ui.reports

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.adapter.CustomerLisitingAdapter
import com.webgurus.attendanceportal.adapter.HotProductAdapter
import com.webgurus.attendanceportal.adapter.ItemReportQuantityAdapter
import com.webgurus.attendanceportal.adapter.ItemReportSaleAdapter
import com.webgurus.attendanceportal.pojo.*
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import retrofit2.Call
import retrofit2.Response


class ItemReprt : BaseActivity() {

    var sharedPreferences: SharedPreferences? = null
    var mList: ArrayList<Datar> = ArrayList()
    var adapter: CustomerLisitingAdapter? = null
    var mContext: Context? = null
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var rv_itembyquantity: RecyclerView
    lateinit var rv_itembyprice: RecyclerView
    lateinit var rv_itembyhotproducts: RecyclerView
    private var animShow: Animation? = null
    private var animHide: Animation? = null
    val mListItemQuantity : ArrayList<ItemQuantity> = ArrayList()
    val mListItemSale : ArrayList<ItemSale> = ArrayList()
    val mListHotproduct : ArrayList<HotProduct> = ArrayList()
    lateinit var iv_backfromitemreport : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_reprt)
        initview()
        listeners()
        hitApitoGettheItemReports()
    }

    private fun listeners() {
        iv_backfromitemreport.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun initview() {
        iv_backfromitemreport = findViewById<ImageView>(R.id.iv_backfromitemreport)
        rv_itembyquantity = findViewById<RecyclerView>(R.id.rv_itembyquantity)
        rv_itembyprice = findViewById<RecyclerView>(R.id.rv_itembyprice)
        rv_itembyhotproducts = findViewById<RecyclerView>(R.id.rv_itembyhotproducts)
        sharedPreferences = this.getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE)
    }



    fun setRecyclerView() {
        linearLayoutManager = LinearLayoutManager(this)
        rv_itembyquantity.layoutManager = linearLayoutManager
        rv_itembyquantity.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter = ItemReportQuantityAdapter(this,mListItemQuantity)
        rv_itembyquantity.adapter = adapter
    }

    fun setRecyclerView22() {
        linearLayoutManager = LinearLayoutManager(this)
        rv_itembyprice.layoutManager = linearLayoutManager
        rv_itembyprice.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter = ItemReportSaleAdapter(this,mListItemSale)
        rv_itembyprice.adapter = adapter
    }


    fun setRecyclerView33() {
        linearLayoutManager = LinearLayoutManager(this)
        rv_itembyhotproducts.layoutManager = linearLayoutManager
        rv_itembyhotproducts.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter = HotProductAdapter(this,mListHotproduct)
        rv_itembyhotproducts.adapter = adapter
    }



    private fun hitApitoGettheItemReports() {
         mListItemQuantity.clear()
         mListItemSale.clear()
         mListHotproduct.clear()
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.item_report(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            )
        )
        showLoading(true)
        call.enqueue(object : retrofit2.Callback<ItemReportPojo?> {
            override fun onResponse(
                call: Call<ItemReportPojo?>,
                response: Response<ItemReportPojo?>
            ) {
                if (response.body() != null) {
                    hideProgress()
                    mListItemQuantity.addAll(response.body()!!.item_quantity)
                    mListItemSale.addAll(response.body()!!.item_sale)
                    mListHotproduct.addAll(response.body()!!.hot_product)
                    setRecyclerView()
                    setRecyclerView22()
                    setRecyclerView33()
                }


            }

            override fun onFailure(call: Call<ItemReportPojo?>, t: Throwable) {
                val dsgfd = ""
            }

        })
    }


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_item_reprt)
//        initview()
//        initAnimation()
//        setRecyclerView()
//        hitApitoGettheItemReports()
//       // val first_view : TextView=findViewById(R.id.first_recycler_view)
//     //   val item_report_view : LinearLayout=findViewById(R.id.item_report_view)
//        first_view.setOnClickListener {
//            if (item_report_view.visibility==View.GONE){
//                item_report_view.startAnimation(animShow)
//                item_report_view.visibility=View.VISIBLE
//            }
//            else{
//                item_report_view.startAnimation(animHide)
//                item_report_view.visibility=View.GONE
//            }
//        }
//    }
//
//    private fun initAnimation() {
//        animShow = AnimationUtils.loadAnimation(this, R.anim.view_show)
//        animHide = AnimationUtils.loadAnimation(this, R.anim.view_gone)
//    }
//
//    private fun hitApitoGettheItemReports() {
//        val service =
//            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
//        val call = service.item_report(
//            "Bearer " + sharedPreferences!!.getString(
//                "Access_Token",
//                ""
//            )
//        )
//
//
//        // Log.e("cc",""+agent_id)
//        showLoading(true)
//
//        call.enqueue(object : retrofit2.Callback<ItemReportPojo?> {
//            override fun onResponse(
//                call: Call<ItemReportPojo?>,
//                response: Response<ItemReportPojo?>
//            ) {
//                if (response.body() != null) {
//                    hideProgress()
//                    setRecyclerView()
//                }
//
//
//            }
//
//            override fun onFailure(call: Call<ItemReportPojo?>, t: Throwable) {
//                val dsgfd = ""
//            }
//
//        })
//    }
//    private fun initview() {
//        sharedPreferences = this.getSharedPreferences(
//            "AcessToken",
//            AppCompatActivity.MODE_PRIVATE
//        )
//    }
//    fun setRecyclerView() {
//        linearLayoutManager = LinearLayoutManager(this)
//        recyclerView = findViewById<RecyclerView>(R.id.rv_item_report)
//        rv_item_report.layoutManager = linearLayoutManager
//        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
//        val adapter = ItemReportAdapter()
//        recyclerView.adapter = adapter
//    }
}