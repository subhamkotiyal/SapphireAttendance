package com.webgurus.attendanceportal.ui.attendance

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.adapter.AttendanceAdapter
import com.webgurus.attendanceportal.listeners.RecyclerViewClickListeners
import com.webgurus.attendanceportal.pojo.AttendanceListPojo
import com.webgurus.attendanceportal.pojo.SuccesAttendances
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.fragment_attendance.*
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception

class AttendanceFragment : Fragment(), RecyclerViewClickListeners {

    var sharedPreferences: SharedPreferences? = null
    var progressBar: ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_attendance, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initview()
        showProgressbar()
        hitApitogetAllAttendance()

    }

    private fun initview() {
        progressBar = requireView().findViewById<View>(R.id.pv_progress) as ProgressBar
        sharedPreferences = requireActivity().getSharedPreferences(
            "AcessToken",
            AppCompatActivity.MODE_PRIVATE
        )
    }

    private fun showProgressbar() {
        progressBar!!.visibility = View.VISIBLE
    }


    private fun hitApitogetAllAttendance() {
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getAllAttendanceList(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            )
        )
        call.enqueue(object : retrofit2.Callback<AttendanceListPojo?> {
            override fun onFailure(call: Call<AttendanceListPojo?>, t: Throwable) {
                Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_SHORT).show()
                progressBar!!.visibility = View.INVISIBLE
            }

            override fun onResponse(
                call: Call<AttendanceListPojo?>?,
                response: Response<AttendanceListPojo?>?
            ) {

                try {
                    val mAttendList: ArrayList<SuccesAttendances> = ArrayList()
                    mAttendList.addAll(response!!.body()!!.success)
                    if (mAttendList.size > 0) {
                        rv_attendancelisting.layoutManager =
                            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                        val adapter = AttendanceAdapter(mAttendList, requireActivity())
                        rv_attendancelisting.adapter = adapter
                        progressBar!!.visibility = View.INVISIBLE
                        tv_nodataavailable.visibility = View.INVISIBLE
                    } else {
                        Toast.makeText(activity, "No Data Available", Toast.LENGTH_SHORT).show()
                        progressBar!!.visibility = View.INVISIBLE
                        tv_nodataavailable.visibility = View.VISIBLE
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.servernotrespond),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    override fun onClick(position: Int) {
        TODO("Not yet implemented")
    }

}