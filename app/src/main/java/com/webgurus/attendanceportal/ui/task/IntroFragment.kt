package com.webgurus.attendanceportal.ui.task

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.adapter.ProductsAdapter
import com.webgurus.attendanceportal.pojo.SuccesAttendances
import kotlinx.android.synthetic.main.freagment_intro.*


class IntroFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.freagment_intro, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerview()
        listeners()
    }

    private fun listeners() {
        btn_confirmvisit.setOnClickListener {
            startActivity(Intent(activity, ConfirmVisitActivity::class.java))
        }
    }

    private fun setRecyclerview(){
        val mAttendList : ArrayList<SuccesAttendances> = ArrayList()
        rv_itemlisitng.layoutManager =  GridLayoutManager(context, 4)
        val adapter = ProductsAdapter(mAttendList,requireActivity())
        rv_itemlisitng.adapter = adapter
    }


}