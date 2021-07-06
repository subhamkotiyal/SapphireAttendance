package com.webgurus.attendanceportal.ui.customer

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import kotlinx.android.synthetic.main.fragment_lastorders.*
import kotlinx.android.synthetic.main.fragment_lastorders.rv_lastorders
import kotlinx.android.synthetic.main.fragment_neworder.*


class NewOrderFragment   : Fragment() {

    var autoTextView: AutoCompleteTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_neworder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initview()
        setRecyclerview()
    }

    private fun setRecyclerview() {
        tv_add.setOnClickListener(View.OnClickListener {
            if(ll_addview.visibility==View.GONE){
                ll_addview.visibility=View.VISIBLE
            }else{
                ll_addview.visibility=View.GONE
            }
        })
        tv_addorder.setOnClickListener(View.OnClickListener {
            ll_addview.visibility=View.GONE
        })
    }


    private fun showCUstomalert(){

    }

    private fun initview() {
        val ProgLanguages = arrayOf(
            "MTO Thinner 300 ML ", "MTO Thinner 300 ML", "MTO Thinner 300 ML", "MTO Thinner 300 ML", "MTO Thinner 300 ML", "MTO Thinner 300 ML",
            "MTO Thinner 300 ML", "MTO Thinner 300 ML", "MTO Thinner 300 ML", "MTO Thinner 300 ML", "MTO Thinner 300 ML", "MTO Thinner 300 ML"
        )
        autoTextView = view!!.findViewById(R.id.autocompleteEditTextView) as AutoCompleteTextView
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
         R.layout.custom_spinnerdropdown , ProgLanguages
        )
        autoTextView!!.threshold = 1
        autoTextView!!.setAdapter(arrayAdapter)
        rv_neworders.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        val adapter = NewOrderAdapter( requireActivity())
        rv_neworders.adapter = adapter
    }



}