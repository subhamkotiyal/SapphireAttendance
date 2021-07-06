package com.webgurus.attendanceportal.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.ui.attendance.AttendanceFragment
import com.webgurus.attendanceportal.ui.login.LoginActivity
import com.webgurus.attendanceportal.ui.profile.ProfileFragment


class HomeActivity22 : AppCompatActivity(){

    var drawer: DrawerLayout? = null
    var navigationView: NavigationView? = null
    var frameLayout: FrameLayout? = null
    var toggle: ActionBarDrawerToggle? = null
    var imageView: ImageView? = null
    var toolbar: Toolbar? = null
    var header: View? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home22)
        setToolbar()
        listeners()
    }

    private fun listeners() {
       // imageView!!.setOnClickListener { drawer!!.closeDrawer(GravityCompat.START) }

    }

    private fun setToolbar() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        frameLayout = findViewById<View>(R.id.frame) as FrameLayout
        header = navigationView!!.getHeaderView(0)
            //  imageView = header!!.findViewById<View>(R.id.profile_image) as ImageView
      //  imageView = header!!.findViewById<View>(R.id.imageView) as ImageView
        setupDrawerContent(navigationView!!)

    }


    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            selectDrawerItem(menuItem)
            true
        }
    }

    fun selectDrawerItem(menuItem: MenuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        var fragment: Fragment? = null
        val fragmentClass: Class<*>
        fragmentClass = when (menuItem.getItemId()) {
            R.id.nav_home -> HomeFragment::class.java
            R.id.nav_gallery -> ProfileFragment::class.java
            R.id.nav_slideshow -> AttendanceFragment::class.java
            else -> HomeFragment::class.java
        }
        try {
            fragment = fragmentClass.newInstance() as Fragment
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Insert the fragment by replacing any existing fragment
        val fragmentManager: FragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.frame, fragment!!).commit()
        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true)
        // Set action bar title
        setTitle(menuItem.getTitle())
        // Close the navigation drawer
        drawer!!.closeDrawers()
    }


    private fun showAlertLogout()
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.dialogTitle)
        builder.setMessage(R.string.dialogMessage)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes")
        { dialogInterface, which ->
            val preferences = getSharedPreferences("AcessToken", Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.clear()
            editor.apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()

        }
        builder.setNegativeButton("No"){ dialogInterface, which ->
            Toast.makeText(applicationContext, "clicked No", Toast.LENGTH_LONG).show()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }



    fun loadFragment(fragment: Fragment?) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment!!)
        transaction.commit()
    }
}