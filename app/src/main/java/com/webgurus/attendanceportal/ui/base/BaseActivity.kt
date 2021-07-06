package com.webgurus.attendanceportal.ui.base

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.webgurus.attendanceportal.R

open class BaseActivity : AppCompatActivity() {

    private var mProgressDialog: Dialog? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // pass activity's  result to the fragments
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        fragment?.onActivityResult(requestCode, resultCode, data)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onStart() {
        super.onStart()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        fragment?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    private fun showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = Dialog(this, android.R.style.Theme_Translucent)
            mProgressDialog?.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            mProgressDialog?.setContentView(R.layout.loader_half_layout)
            mProgressDialog?.setCancelable(false)

        }
        mProgressDialog!!.show()
    }

    fun hideProgress() {
        if (mProgressDialog != null && mProgressDialog?.isShowing!!) {
            mProgressDialog?.dismiss()
        }
    }

    fun showLoading(show: Boolean?) {
        if (show!!) showProgress() else hideProgress()
    }

}