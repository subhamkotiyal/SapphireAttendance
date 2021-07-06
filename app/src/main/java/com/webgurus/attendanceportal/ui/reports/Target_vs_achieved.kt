package com.webgurus.attendanceportal.ui.reports


import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.date.dayOfMonth
import com.afollestad.date.month
import com.afollestad.date.year
import com.archit.calendardaterangepicker.customviews.CalendarListener
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.GetUserByRole
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_addorder.*
import kotlinx.android.synthetic.main.activity_target_vs_acheived_second.*
import kotlinx.android.synthetic.main.activity_target_vs_achieved.*
import kotlinx.android.synthetic.main.activity_target_vs_achieved.rl_fieldmanager
import kotlinx.android.synthetic.main.activity_target_vs_achieved.rl_fieldmanagercalender
import kotlinx.android.synthetic.main.activity_target_vs_achieved.rl_manager
import kotlinx.android.synthetic.main.activity_target_vs_achieved.rl_managercalender
import kotlinx.android.synthetic.main.activity_target_vs_achieved.rl_managermonth
import kotlinx.android.synthetic.main.activity_target_vs_achieved.rl_managertarget
import kotlinx.android.synthetic.main.activity_target_vs_achieved.rl_monthly
import kotlinx.android.synthetic.main.activity_target_vs_achieved.rl_target
import kotlinx.android.synthetic.main.activity_target_vs_achieved.rl_year
import kotlinx.android.synthetic.main.activity_target_vs_achieved.rl_yearmanager
import kotlinx.android.synthetic.main.activity_target_vs_achieved.tv_fieldmanagerdate
import kotlinx.android.synthetic.main.activity_target_vs_achieved.tv_managerdate
import kotlinx.android.synthetic.main.fragment_createuser.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Target_vs_achieved : BaseActivity(), AdapterView.OnItemSelectedListener {

    var sharedPreferences: SharedPreferences? = null
    var mFieldManager: ArrayList<String> = ArrayList()
    var mManager: ArrayList<String> = ArrayList()
    var mTarget: ArrayList<String> = ArrayList()


    var mFieldManagerIDs: ArrayList<Int> = ArrayList()
    var mManagerIDs: ArrayList<Int> = ArrayList()

    var mTargetManager: ArrayList<String> = ArrayList()
    var mMonths: ArrayList<String> = ArrayList()
    var mYears: ArrayList<String> = ArrayList()


    lateinit var manager_spinner: Spinner
    lateinit var fieldmanager_spinner: Spinner
    lateinit var target_spinner: Spinner
    lateinit var target_manager_spinner: Spinner
    lateinit var month_spinner: Spinner
    lateinit var month_manager_spinner: Spinner
    lateinit var sp_year: Spinner
    lateinit var sp_year_manager: Spinner
    lateinit var search_executed: TextView

    lateinit var iv_backfromreport: ImageView

    private var calendar: DateRangeCalendarView? = null

    private var mManagerID: Int = 0
    private var mFieldManagerID: Int = 0

    private var mManagerType: String = ""
    private var mFieldManagerType: String = ""

    private var mManagerDateTime: String = ""
    private var mFiedlManagerDateTime: String = ""


    var datePickerDialog: Dialog? = null
    var datePickerDialogforManager: Dialog? = null


    private fun setSpinner() {
        var aa = ArrayAdapter(this, R.layout.simple_spinner_list, mFieldManager)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val ll = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        rl_fieldmanager.addView(fieldmanager_spinner)
        aa = ArrayAdapter(this, R.layout.spinner_right_aligned, mFieldManager)
        aa.setDropDownViewResource(R.layout.spinner_right_aligned)
        with(fieldmanager_spinner)
        {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener = this@Target_vs_achieved
            layoutParams = ll
            prompt = "Select filed Manager"
        }


        var aaa = ArrayAdapter(this, R.layout.simple_spinner_list, mManager)
        aaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val lll = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        rl_manager.addView(manager_spinner)
        aaa = ArrayAdapter(this, R.layout.spinner_right_aligned, mManager)
        aaa.setDropDownViewResource(R.layout.spinner_right_aligned)
        with(manager_spinner)
        {
            adapter = aaa
            setSelection(0, false)
            onItemSelectedListener = this@Target_vs_achieved
            layoutParams = lll
            prompt = "Select  Manager"
        }
        mTarget.add("Daily")
        mTarget.add("Weekly")
        mTarget.add("Monthly")
        mTarget.add("Yearly")

        var tagetAdapter = ArrayAdapter(this, R.layout.simple_spinner_list, mTarget)
        tagetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val targetlayout = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        rl_target.addView(target_spinner)
        tagetAdapter = ArrayAdapter(this, R.layout.spinner_right_aligned, mTarget)
        tagetAdapter.setDropDownViewResource(R.layout.spinner_right_aligned)
        with(target_spinner)
        {
            adapter = tagetAdapter
            setSelection(0, false)
            onItemSelectedListener = this@Target_vs_achieved
            layoutParams = targetlayout
            prompt = "Select  Target"
        }

        var tagetManagerAdapter = ArrayAdapter(this, R.layout.simple_spinner_list, mTarget)
        tagetManagerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val targetmanagerlayout = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        rl_managertarget.addView(target_manager_spinner)
        tagetManagerAdapter = ArrayAdapter(this, R.layout.spinner_right_aligned, mTarget)
        tagetManagerAdapter.setDropDownViewResource(R.layout.spinner_right_aligned)
        with(target_manager_spinner)
        {
            adapter = tagetManagerAdapter
            setSelection(0, false)
            onItemSelectedListener = this@Target_vs_achieved
            layoutParams = targetmanagerlayout
            prompt = "Select  Target"
        }


        mManagerID = mManagerIDs[0]
        mFieldManagerID = mFieldManagerIDs[0]
        mManagerType = mTarget[0]
        mFieldManagerType = mTarget[0]

        mMonths.add("January")
        mMonths.add("Febuary")
        mMonths.add("March")
        mMonths.add("April")
        mMonths.add("May")
        mMonths.add("June")
        mMonths.add("July")
        mMonths.add("August")
        mMonths.add("September")
        mMonths.add("October")
        mMonths.add("November")
        mMonths.add("December")

        var monthAdapter = ArrayAdapter(this, R.layout.simple_spinner_list, mMonths)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val monthslayout = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        rl_monthly.addView(month_spinner)
        monthAdapter = ArrayAdapter(this, R.layout.spinner_right_aligned, mMonths)
        monthAdapter.setDropDownViewResource(R.layout.spinner_right_aligned)
        with(month_spinner)
        {
            adapter = monthAdapter
            setSelection(0, false)
            onItemSelectedListener = this@Target_vs_achieved
            layoutParams = monthslayout
            prompt = "Select  Month"
        }


        var monthManagerAdapter = ArrayAdapter(this, R.layout.simple_spinner_list, mMonths)
        monthManagerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val monthsManagerlayout = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        rl_managermonth.addView(month_manager_spinner)
        monthManagerAdapter = ArrayAdapter(this, R.layout.spinner_right_aligned, mMonths)
        monthManagerAdapter.setDropDownViewResource(R.layout.spinner_right_aligned)
        with(month_manager_spinner)
        {
            adapter = monthManagerAdapter
            setSelection(0, false)
            onItemSelectedListener = this@Target_vs_achieved
            layoutParams = monthsManagerlayout
            prompt = "Select  Month"
        }


        mYears.add("2000")
        mYears.add("2001")
        mYears.add("2002")
        mYears.add("2003")
        mYears.add("2004")
        mYears.add("2005")
        mYears.add("2006")
        mYears.add("2007")
        mYears.add("2008")
        mYears.add("2009")
        mYears.add("2010")
        mYears.add("2011")
        mYears.add("2012")
        mYears.add("2013")
        mYears.add("2014")
        mYears.add("2015")
        mYears.add("2016")
        mYears.add("2017")
        mYears.add("2018")
        mYears.add("2019")
        mYears.add("2020")
        mYears.add("2021")

        var yearAdapter = ArrayAdapter(this, R.layout.simple_spinner_list, mYears)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val yearLayout = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        rl_year.addView(sp_year)
        yearAdapter = ArrayAdapter(this, R.layout.spinner_right_aligned, mYears)
        yearAdapter.setDropDownViewResource(R.layout.spinner_right_aligned)
        with(sp_year)
        {
            adapter = yearAdapter
            setSelection(0, false)
            onItemSelectedListener = this@Target_vs_achieved
            layoutParams = yearLayout
            prompt = "Select  Year"
        }


        var yearManagerAdapter = ArrayAdapter(this, R.layout.simple_spinner_list, mYears)
        yearManagerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val yearManagerLayout = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        rl_yearmanager.addView(sp_year_manager)
        yearManagerAdapter = ArrayAdapter(this, R.layout.spinner_right_aligned, mYears)
        yearManagerAdapter.setDropDownViewResource(R.layout.spinner_right_aligned)
        with(sp_year_manager)
        {
            adapter = yearManagerAdapter
            setSelection(0, false)
            onItemSelectedListener = this@Target_vs_achieved
            layoutParams = yearManagerLayout
            prompt = "Select  Year"
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target_vs_acheived_second)
        initview()
        listeners()
        openCalender()
        hitApitogetUser()
    }

    fun showCustomCalenderAlertForManager() {
        datePickerDialogforManager= Dialog(this, R.style.AppBaseTheme)
        datePickerDialogforManager!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        datePickerDialogforManager!!.setCancelable(false)
        datePickerDialogforManager!!.getWindow()!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        //   setCalender()
        datePickerDialogforManager!!.getWindow()!!.getAttributes().windowAnimations = R.style.dialog_animation
        datePickerDialogforManager!!.setContentView(R.layout.custom_alert_customcalender)
        calendar = datePickerDialogforManager!!.findViewById(R.id.calendar)
        calendar!!.setWeekOffset(1)
//        calendar!!.setFixedDaysSelection(6)
        datePickerDialogforManager!!.show()
        calendar!!.setCalendarListener(object : CalendarListener {
            override fun onFirstDateSelected(startDate: Calendar) {
                val dsg=""
            }

            override fun onDateRangeSelected(startDate: Calendar, endDate: Calendar) {
                val startdayss = startDate.dayOfMonth
                val startmonth =startDate.month
                val startyear = startDate.year
                val startdatess = startyear.toString() + "-" + startmonth.toString() + "-" + startdayss.toString()

                val enddayss = endDate.dayOfMonth
                val endmonth =endDate.month
                val endyear = endDate.year
                val enddatess = endyear.toString() + "-" + endmonth.toString() + "-" + enddayss.toString()

                tv_managerweekdate.setText(startdatess + " - " + enddatess )
                mManagerDateTime=tv_managerweekdate.text.toString()
                datePickerDialogforManager!!.dismiss()
            }
        })

    }



    fun showCustomCalenderAlertForUser() {
        datePickerDialog= Dialog(this, R.style.AppBaseTheme)
        datePickerDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        datePickerDialog!!.setCancelable(false)
        datePickerDialog!!.getWindow()!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        //   setCalender()
        datePickerDialog!!.getWindow()!!.getAttributes().windowAnimations = R.style.dialog_animation
        datePickerDialog!!.setContentView(R.layout.custom_alert_customcalender)
        calendar = datePickerDialog!!.findViewById(R.id.calendar)
        calendar!!.setWeekOffset(1)
//        calendar!!.setFixedDaysSelection(6)
        datePickerDialog!!.show()

        calendar!!.setCalendarListener(object : CalendarListener {
            override fun onFirstDateSelected(startDate: Calendar) {
                val dsg=""
            }

            override fun onDateRangeSelected(startDate: Calendar, endDate: Calendar) {
                val startdayss = startDate.dayOfMonth
                val startmonth =startDate.month
                val startyear = startDate.year
                val startdatess = startyear.toString() + "-" + startmonth.toString() + "-" + startdayss.toString()

                val enddayss = endDate.dayOfMonth
                val endmonth =endDate.month
                val endyear = endDate.year
                val enddatess = endyear.toString() + "-"  + endmonth.toString() + "-" +  enddayss.toString()
                tv_weekdays.setText(startdatess + " - " + enddatess )
                mFiedlManagerDateTime=tv_weekdays.text.toString()
                datePickerDialog!!.dismiss()
            }
        })

    }


    private fun listeners() {
        search_executed.setOnClickListener({
            val intent = Intent(this, UserSalesReport::class.java)
            intent.putExtra("mFieldManagerID", mFieldManagerID)
            intent.putExtra("mFieldManagerSelectedDate", mFiedlManagerDateTime)
            intent.putExtra("mFieldManagertype", mFieldManagerType)
            startActivity(intent)
        })

        tv_search_manager.setOnClickListener({
            val intent = Intent(this, UserSalesReport::class.java)
            intent.putExtra("mManagerID", mManagerID)
            intent.putExtra("mManagerSelectedDate", mManagerDateTime)
            intent.putExtra("mManagertype", mManagerType)
            startActivity(intent)
        })
        iv_backfromreport.setOnClickListener({
            finish()
        })

        ll_fieldmanagerweeklycalender.setOnClickListener(View.OnClickListener {

        })


        rl_cal.setOnClickListener(View.OnClickListener {
            showCustomCalenderAlertForUser()
        })
        rl_managerweekcalender.setOnClickListener(View.OnClickListener {
            showCustomCalenderAlertForManager()
        })


    }


    private fun initview() {
        iv_backfromreport = findViewById(R.id.iv_backfromreport)
        search_executed = findViewById(R.id.search_executed)
        manager_spinner = Spinner(this)
        fieldmanager_spinner = Spinner(this)
        target_spinner = Spinner(this)
        month_spinner = Spinner(this)
        target_manager_spinner = Spinner(this)
        month_manager_spinner = Spinner(this)
        sp_year_manager = Spinner(this)
        sp_year = Spinner(this)
        fieldmanager_spinner.id = 1
        target_spinner.id = 2
        month_spinner.id = 3
        sp_year.id = 4
        manager_spinner.id = 5
        target_manager_spinner.id = 6
        month_manager_spinner.id = 7
        sp_year_manager.id = 8
        sharedPreferences = this.getSharedPreferences("AcessToken", AppCompatActivity.MODE_PRIVATE)
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when (parent!!.getId()) {
            1 -> {
                mFieldManagerID = mFieldManagerIDs[position]
            }
            2 -> {
                when (position) {

                    0 -> {
                        ll_fieldmanagerdate.visibility = View.VISIBLE
                        ll_fieldmanagermonth.visibility = View.GONE
                        ll_fieldmanageryear.visibility = View.GONE
                        ll_fieldmanagerweeklycalender.visibility = View.GONE
                        mFieldManagerType = mTarget[position]
                    }


                    1 -> {
                        ll_fieldmanagerdate.visibility = View.GONE
                        ll_fieldmanagermonth.visibility = View.GONE
                        ll_fieldmanageryear.visibility = View.GONE
                        ll_fieldmanagerweeklycalender.visibility = View.VISIBLE
                        mFieldManagerType = mTarget[position]

                    }


                    2 -> {
                        ll_fieldmanagermonth.visibility = View.VISIBLE
                        ll_fieldmanageryear.visibility = View.VISIBLE
                        ll_fieldmanagerdate.visibility = View.GONE
                        ll_fieldmanagerweeklycalender.visibility = View.GONE
                        mFieldManagerType = mTarget[position]
                    }


                    3 -> {

                        ll_fieldmanagermonth.visibility = View.GONE
                        ll_fieldmanageryear.visibility = View.VISIBLE
                        ll_fieldmanagerdate.visibility = View.GONE
                        ll_fieldmanagerweeklycalender.visibility = View.GONE
                        mFieldManagerType = mTarget[position]

                    }


                }

            }

            4 -> {
            }
            5 -> {
                //   mSelectedManagerID = mManagerIDs[position]
            }
            6 -> {
                when (position) {

                    0 -> {
                        ll_managerdate.visibility = View.VISIBLE
                        ll_managermonth.visibility = View.GONE
                        ll_manageryear.visibility = View.GONE
                        ll_managerweeklycalender.visibility = View.GONE
                        mManagerType = mTarget[position]
                    }

                    1 -> {
                        ll_managerdate.visibility = View.GONE
                        ll_managermonth.visibility = View.GONE
                        ll_manageryear.visibility = View.GONE
                        ll_managerweeklycalender.visibility = View.VISIBLE
                        mManagerType = mTarget[position]
                    }

                    2 -> {
                        ll_managermonth.visibility = View.VISIBLE
                        ll_manageryear.visibility = View.VISIBLE
                        ll_managerdate.visibility = View.GONE
                        ll_managerweeklycalender.visibility = View.GONE
                        mManagerType = mTarget[position]
                    }

                    3 -> {
                        ll_managermonth.visibility = View.GONE
                        ll_manageryear.visibility = View.VISIBLE
                        ll_managerdate.visibility = View.GONE
                        ll_managerweeklycalender.visibility = View.GONE
                        mManagerType = mTarget[position]
                    }

                }

            }

            5 -> {
                mManagerID = mManagerIDs[position]

            }

            7 -> {
            }
            8 -> {
            }
        }

    }


    override fun onNothingSelected(parent: AdapterView<*>?) {

    }


    fun openCalender() {
        rl_managercalender.setOnClickListener(View.OnClickListener {
            val myCalendar = Calendar.getInstance()
            val date =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
                    myCalendar[Calendar.YEAR] = year
                    myCalendar[Calendar.MONTH] = monthOfYear
                    myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                    val myFormat = "dd-MMM-yyyy" // your format
                    val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                    tv_managerdate.setText(sdf.format(myCalendar.time))
                    mManagerDateTime = sdf.format(myCalendar.time)
                }
            DatePickerDialog(
                this,
                date,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        })

        rl_fieldmanagercalender.setOnClickListener(View.OnClickListener {
            val myCalendar = Calendar.getInstance()
            val date =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
                    myCalendar[Calendar.YEAR] = year
                    myCalendar[Calendar.MONTH] = monthOfYear
                    myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                    val myFormat = "dd-MMM-yyyy" // your format
                    val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                    tv_fieldmanagerdate.setText(sdf.format(myCalendar.time))
                    mFiedlManagerDateTime = sdf.format(myCalendar.time)
                }
            DatePickerDialog(
                this,
                date,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        })

    }

    private fun hitApitogetUser() {
        mFieldManager.clear()
        mManager.clear()
        mFieldManagerIDs.clear()
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call =
            service.getuserByRole("Bearer " + sharedPreferences!!.getString("Access_Token", ""), 0)
        showLoading(true)
        call.enqueue(object : retrofit2.Callback<GetUserByRole?> {
            override fun onResponse(
                call: retrofit2.Call<GetUserByRole?>,
                response: retrofit2.Response<GetUserByRole?>
            ) {
                hideProgress()
                if (response.body() != null) {
                    for (i in 0 until response.body()!!.data.size) {
                        if (response.body()!!.data[i].role_id == 5) {
                            mFieldManager.add(response.body()!!.data[i].name)
                            mFieldManagerIDs.add(response.body()!!.data[i].id)
                        }
                        if (response.body()!!.data[i].role_id == 3) {
                            mManager.add(response.body()!!.data[i].name)
                            mManagerIDs.add(response.body()!!.data[i].id)
                        }
                    }

                }
                setSpinner()
            }

            override fun onFailure(call: retrofit2.Call<GetUserByRole?>, t: Throwable) {
                hideProgress()
            }

        })


    }


}
