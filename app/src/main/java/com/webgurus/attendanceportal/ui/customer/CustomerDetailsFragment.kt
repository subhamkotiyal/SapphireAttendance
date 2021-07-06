package com.webgurus.attendanceportal.ui.customer

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.Datar
import kotlinx.android.synthetic.main.fragment_customersdetails.*

class CustomerDetailsFragment : AppCompatActivity() {

    //    vp_orderlisitng
    var adapter: OrderTabAdapter? = null
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    private  var coustomlist :ArrayList<Datar> = ArrayList()
    var posi:Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_customersdetails)
        getIntentss()
        initview()
        listeners()
        getIntentss()
    }

    private fun listeners() {
        iv_backfromcust.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    protected fun getIntentss() {
        coustomlist = this.intent.extras!!.getParcelableArrayList<Datar>("customerdetails")!!
        posi = this.intent.extras!!.getInt("pos")
        tv_usernamedetails.text = coustomlist[posi].first_name + " " + coustomlist[posi].last_name
        tv_contactcustomer.text = coustomlist[posi].phone_number
        tv_addresscustomer.text = coustomlist[posi].address + "  "+ coustomlist[posi].secondary_address + "  "+coustomlist[posi].city + "  " + coustomlist[posi].state + "  " + coustomlist[posi].pincode

    }

    private fun initview() {
        viewPager = findViewById<View>(R.id.vp_orderlisitng) as ViewPager
        tabLayout = findViewById<View>(R.id.tabLayoutorder) as TabLayout
        adapter = OrderTabAdapter(
            supportFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            this
        )
        val bundle = Bundle()
        bundle.putParcelableArrayList("lastorder", coustomlist)
        bundle.putInt("pos",posi)
        val fragment_lastorder = LastOrdersFragment()
        fragment_lastorder.setArguments(bundle)
        val fragment_pendingpayment = PendingAmountFragment()
        fragment_pendingpayment.setArguments(bundle)
        adapter!!.addFragment(fragment_lastorder, "Last Order")
        adapter!!.addFragment(fragment_pendingpayment, "Pending  Payment")
        tabLayout!!.setSelectedTabIndicatorColor(resources.getColor(R.color.colorPrimary))
        tabLayout!!.setSelectedTabIndicatorHeight(((2 * getResources().getDisplayMetrics().density).toInt()))
        tabLayout!!.setTabTextColors(
            resources.getColor(R.color.grey),
            resources.getColor(R.color.colorPrimary)
        )
        viewPager!!.adapter = adapter
        tabLayout!!.setupWithViewPager(viewPager)
    }

}