package com.webgurus.attendanceportal.ui.task

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.webgurus.attendanceportal.R
import kotlinx.android.synthetic.main.activity_taskdetails.*
import com.webgurus.attendanceportal.adapter.TabAdapter as TabAdapter1

class TaskDetailsActivity : AppCompatActivity() {

    var adapter: TabAdapter1? = null
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taskdetails)
        initview()
        lsiteners()
    }

    private fun lsiteners() {
        iv_back.setOnClickListener {
            finish()
        }

    }

    private fun initview() {
        viewPager = findViewById<View>(R.id.viewPager) as ViewPager
        tabLayout = findViewById<View>(R.id.tabLayout) as TabLayout
        adapter = TabAdapter1(supportFragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, this)
        adapter!!.addFragment(LastOrderFragment(), "Last Orders")
        //adapter!!.addFragment(PendingDuesFragment(), "Pending Dues")
        viewPager!!.adapter = adapter
        tabLayout!!.setupWithViewPager(viewPager)
    }

}