package com.webgurus.attendanceportal.ui.createuser

import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_addusertarget.*
import kotlinx.android.synthetic.main.activity_target_vs_achieved.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddUserTargetActivity : BaseActivity() , AdapterView.OnItemSelectedListener {

    var sharedPreferences: SharedPreferences? = null
    var userID: Int = 0
    lateinit var btn_submit : Button
    lateinit var targetSpinner: Spinner
    var mDaysList: ArrayList<String> = ArrayList()
    lateinit var rl_calenderdates : RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addusertarget)
        initview()
        listeners()
        setSpinner()
    }

    private fun setSpinner() {
       mDaysList.clear()
        mDaysList.add("Specific Date")
        mDaysList.add("Monthly")
        var aa = ArrayAdapter(this, R.layout.simple_spinner_list, mDaysList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val ll = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        rl_monthlydates.addView(targetSpinner)
        aa = ArrayAdapter(this, R.layout.spinner_right_aligned, mDaysList)
        aa.setDropDownViewResource(R.layout.spinner_right_aligned)
        with(targetSpinner)
        {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener = this@AddUserTargetActivity
            layoutParams = ll
            prompt = "Select filed Manager"
        }

    }



    fun openCalender() {
        val myCalendar = Calendar.getInstance()
        val date =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = monthOfYear
                myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                val myFormat = "dd-MMM-yyyy" // your format
                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                tv_dateincalender.setText(sdf.format(myCalendar.time))
            }
        DatePickerDialog(
            this,
            date,
            myCalendar[Calendar.YEAR],
            myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        ).show()
    }



    private fun listeners() {
        btn_submit.setOnClickListener(View.OnClickListener {

        })
        rl_calenderdates.setOnClickListener(View.OnClickListener {
            openCalender()
        })
    }

    private fun initview() {
        targetSpinner = Spinner(this)
        rl_calenderdates=findViewById(R.id.rl_calenderdates)
        btn_submit=findViewById(R.id.btn_submit)
        sharedPreferences = this.getSharedPreferences(
            "AcessToken",
            AppCompatActivity.MODE_PRIVATE
        )
        if(intent!=null){
            userID=intent.getIntExtra("id",0)
        }
        targetSpinner.id = 1
    }



    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent!!.getId()) {
            1 -> {
                when (position) {
                    0 -> {

                    }
                }
            }

        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

}