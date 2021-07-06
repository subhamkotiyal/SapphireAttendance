package com.webgurus.attendanceportal.ui.order

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.*
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.fragment_attendance.*
import kotlinx.android.synthetic.main.fragment_categorylisting.*
import retrofit2.Call
import retrofit2.Response


class CustomerOrderListingActivity : BaseActivity() {

    var mOrderID: Int = 0
    var sharedPreferences: SharedPreferences? = null
    var mRemarksList: ArrayList<Remark> = ArrayList()
    var mTransaction: ArrayList<OrderTransaction> = ArrayList()
    lateinit var mOrderDetails: OrderDetail
    var mOrderListing: ArrayList<OrdersVariant> = ArrayList()
    lateinit var iv_backfromorder : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customerorderlisting)
        initview()
        listeners()
        hitApitogetOrderDetails()
    }

    private fun listeners() {
        iv_backfromorder.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun hitApitogetOrderDetails() {
        mOrderListing.clear()
        showLoading(true)
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.viewOrder("Bearer " + sharedPreferences!!.getString("Access_Token", ""), mOrderID)
        call.enqueue(object : retrofit2.Callback<OrderDetailsPojo?> {

            override fun onFailure(call: Call<OrderDetailsPojo?>, t: Throwable) {
                showLoading(false)
            }

            override fun onResponse(call: Call<OrderDetailsPojo?>?, response: Response<OrderDetailsPojo?>?) {
                showLoading(false)
                mRemarksList.addAll(response!!.body()!!.remarks)
                mTransaction.addAll(response.body()!!.order_detail.transaction)
                mOrderDetails = response.body()!!.order_detail
                mOrderListing.addAll(response.body()!!.order_detail.variant)
                val tab_viewpager = findViewById<ViewPager>(R.id.tab_viewpager)
                val tab_tablayout = findViewById<TabLayout>(R.id.tab_tablayout)
                setupViewPager(tab_viewpager)
                tab_tablayout.setupWithViewPager(tab_viewpager)
            }
        })

    }

    private fun initview() {
        iv_backfromorder=findViewById(R.id.iv_backfromorder)
        if (intent.getIntExtra("order_id", 0) != null) {
            mOrderID = intent.getIntExtra("order_id", 0)
        }
        sharedPreferences = getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE)
    }

    private fun setupViewPager(viewpager: ViewPager) {
        var adapter = ViewPagerAdapter(supportFragmentManager)

        val orderdetailsfragment = OrderDetailsFragment()
        val orderListing = OrderListing()
        val orderRemarkFragment = OrderRemarkFragment()
        val orderTransaction = OrderTransactionFragment()

        // order details
        val bundleorderdetails = Bundle()
        bundleorderdetails.putParcelable("orderdetails", mOrderDetails)
        orderdetailsfragment.setArguments(bundleorderdetails)

        val bundleorderlisting = Bundle()
        bundleorderlisting.putString("customername", mOrderDetails.customer_name)
        bundleorderlisting.putParcelableArrayList("orderlisting", mOrderListing)
        orderListing.setArguments(bundleorderlisting)


        val bundleorderremark = Bundle()
        bundleorderremark.putParcelableArrayList("orderremark", mRemarksList)
        orderRemarkFragment.setArguments(bundleorderremark)


        val bundleordertransaction = Bundle()
        bundleordertransaction.putParcelableArrayList("orderTransaction", mTransaction)
        orderTransaction.setArguments(bundleordertransaction)


        adapter.addFragment(orderdetailsfragment, "Order details")
        adapter.addFragment(orderListing, "Order listing")
        adapter.addFragment(orderRemarkFragment, "Order Remark")
        adapter.addFragment(orderTransaction, "Order Transaction")
        viewpager.setAdapter(adapter)
    }


    class ViewPagerAdapter : FragmentPagerAdapter {
        private final var fragmentList1: ArrayList<Fragment> = ArrayList()
        private final var fragmentTitleList1: ArrayList<String> = ArrayList()

        public constructor(supportFragmentManager: FragmentManager)
                : super(supportFragmentManager)

        override fun getItem(position: Int): Fragment {
            return fragmentList1.get(position)
        }

        @Nullable
        override fun getPageTitle(position: Int): CharSequence {
            return fragmentTitleList1.get(position)
        }

        override fun getCount(): Int {
            return fragmentList1.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            fragmentList1.add(fragment)
            fragmentTitleList1.add(title)
        }
    }


}

