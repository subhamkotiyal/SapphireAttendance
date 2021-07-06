package com.webgurus.attendanceportal.ui.home

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.navigation.NavigationView
import com.webgurus.attendanceportal.R


class HomeActivity33  : AppCompatActivity(){

    private val mDrawer: DrawerLayout? = null
    private val toolbar: Toolbar? = null
    private val nvDrawer: NavigationView? = null

    // Make sure to be using androidx.appcompat.app.ActionBarDrawerToggle version.
    private val drawerToggle: ActionBarDrawerToggle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home22)
        initview()

    }

    private fun initview() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
      //  val mDrawer : DrawerLayout =findViewById(R.id.drawer_layoutss)
        // ...From section above...
        // Find our drawer view
     //   val nvDrawer : NavigationView=findViewById(R.id.nvView);
        // Setup drawer view
     //   setupDrawerContent(nvDrawer)


    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // The action bar home/up action should open or close the drawer.
        when (item.itemId) {
            android.R.id.home -> {
                mDrawer!!.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener(
            object : NavigationView.OnNavigationItemSelectedListener {

                override fun onNavigationItemSelected(item: MenuItem): Boolean {
                    selectDrawerItem(item)
                    return true
                }
            })
    }

    fun selectDrawerItem(menuItem: MenuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        var fragment: Fragment? = null
        val fragmentClass: Class<*>
//        fragmentClass = when (menuItem.itemId) {
//            R.id.nav_first_fragment -> HomeFragment::class.java
//            R.id.nav_second_fragment -> ProfileFragment::class.java
//            R.id.nav_third_fragment -> AttendanceFragment::class.java
//            else -> HomeFragment::class.java
//        }
//        try {
//            fragment = fragmentClass.newInstance() as Fragment
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

        // Insert the fragment by replacing any existing fragment
        val fragmentManager: FragmentManager = supportFragmentManager
     //   fragment?.let { fragmentManager.beginTransaction().replace(R.id.flContent, it).commit() }

        // Highlight the selected item has been done by NavigationView
        menuItem.isChecked = true
        // Set action bar title
        title = menuItem.title
        // Close the navigation drawer
        mDrawer!!.closeDrawers()
    }
}