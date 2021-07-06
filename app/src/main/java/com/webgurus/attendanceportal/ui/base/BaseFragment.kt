package com.webgurus.attendanceportal.ui.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.webgurus.attendanceportal.R

open class BaseFragment  : Fragment() {

    private var mActivity: FragmentActivity? = null
    private var mProgressDialog: Dialog? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity
    }


    fun showLoading(show: Boolean?) {
        if (show!!) showProgress() else hideProgress()
    }

    fun hideProgress() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog?.dismiss()
        }
    }

    private fun showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = Dialog(mActivity!!, android.R.style.Theme_Translucent)
            mProgressDialog?.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            mProgressDialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ////////
            mProgressDialog?.setContentView(R.layout.loader_half_layout)
            mProgressDialog?.setCancelable(false)

        }

        mProgressDialog?.show()
    }




}