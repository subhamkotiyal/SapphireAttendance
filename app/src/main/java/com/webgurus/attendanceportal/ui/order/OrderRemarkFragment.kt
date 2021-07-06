package com.webgurus.attendanceportal.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.adapter.OrderRemarkAdapter
import com.webgurus.attendanceportal.pojo.Remark
import com.webgurus.attendanceportal.ui.base.BaseFragment

class OrderRemarkFragment : BaseFragment() {

    lateinit var orderRemarkAdapter: OrderRemarkAdapter
    lateinit var rv_orderremark: RecyclerView

    var mRemarksList : ArrayList<Remark> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_orderremark, container, false);
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = this.arguments
//        if (bundle != null) {
//            mRemarksList= bundle.getParcelableArrayList("mRemarksList")!!
//        }
        val dsf=""
        val fdsdsf=""
        initview()
    }

    private fun initview() {
        rv_orderremark=requireView().findViewById(R.id.rv_orderremark)
        rv_orderremark.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        orderRemarkAdapter =
            OrderRemarkAdapter(requireActivity(),)
        rv_orderremark.adapter = orderRemarkAdapter
    }


}