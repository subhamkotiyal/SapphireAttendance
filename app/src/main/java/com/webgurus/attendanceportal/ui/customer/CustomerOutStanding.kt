package com.webgurus.attendanceportal.ui.customer

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.adapter.CustomerOutstandingAdapter
import com.webgurus.attendanceportal.pojo.Customer
import com.webgurus.attendanceportal.pojo.CustomerDeletePojo
import com.webgurus.attendanceportal.pojo.GetCustomerOutstandingPojo
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.fragment_customeroutstanding.*
import retrofit2.Call
import retrofit2.Response

class CustomerOutStanding : Fragment() {

    var sharedPreferences: SharedPreferences? = null
    var mOustanding : ArrayList<Customer> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_customeroutstanding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hitApitoCustomerOutstanding()
    }

    private fun hitApitoCustomerOutstanding() {
        sharedPreferences =
            requireActivity().getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE)
        mOustanding.clear()
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getCustomerOutstanding(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            )
        )
        call.enqueue(object : retrofit2.Callback<GetCustomerOutstandingPojo?> {
            override fun onResponse(
                call: Call<GetCustomerOutstandingPojo?>,
                response: Response<GetCustomerOutstandingPojo?>
            ) {
                if (response.body() != null) {
                    mOustanding.addAll(response.body()!!.customer)
                    rv_customeroutstanding.layoutManager =
                        LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                    val adapter = CustomerOutstandingAdapter(requireActivity(),mOustanding)
                    rv_customeroutstanding.adapter = adapter
                }

            }

            override fun onFailure(call: Call<GetCustomerOutstandingPojo?>, t: Throwable) {
                Toast.makeText(requireContext(), "" + t.toString(), Toast.LENGTH_SHORT).show()

            }

        })

    }


}