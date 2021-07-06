package com.webgurus.attendanceportal.ui.reports

import android.app.Dialog
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.*
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_myorderreport.*
import kotlinx.android.synthetic.main.activity_newunit.*
import kotlinx.android.synthetic.main.custom_alert_filterorder.*
import kotlinx.android.synthetic.main.custom_alert_filterorder.view.*
import kotlinx.android.synthetic.main.fragment_createuser.*
import retrofit2.Call
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class MyOrderReportsActivity : BaseActivity(), AdapterView.OnItemSelectedListener {

    private var chart: BarChart? = null
    var mList: ArrayList<Datar> = ArrayList()
    var mListCustomer: ArrayList<String> = ArrayList()
    var sharedPreferences: SharedPreferences? = null
    val NEW_SPINNER_ID = 1
    val NEW_SPINNER_ID_FileManager = 2
    lateinit var fab: View
    var mFieldManagerList: ArrayList<FieldManagerData> = ArrayList()
    var mFieldManager: ArrayList<String> = ArrayList()
    var y_axisList: ArrayList<Int> = ArrayList()
    var x_axisList: ArrayList<String> = ArrayList()
    var customerID = 0
    var agent_ID = 0
    val DAILY__WAGES = 3
    val MONTHLY_WAGES = 2
    val YEARLY_WAGES = 1

    var spinner: Spinner? = null
    var spinner2: Spinner? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myorderreport)
        initview()
        listeners()
        hitApitoGettheOrderReports(DAILY__WAGES, customerID, agent_ID)
    }


    override fun onResume() {
        super.onResume()
        hitApitoGetCustomerisitng()
        hitApitoGetFieldmanagers()

    }

    private fun listeners() {
        fab.setOnClickListener { view ->
            showFilterAlert()
        }
        ll_daily.setOnClickListener(View.OnClickListener {
            changeStatus(1, ll_daily, ll_monthly, ll_anually)
            hitApitoGettheOrderReports(DAILY__WAGES, customerID, agent_ID)
        })
        ll_monthly.setOnClickListener(View.OnClickListener {
            changeStatus(MONTHLY_WAGES, ll_daily, ll_monthly, ll_anually)
            hitApitoGettheOrderReports(MONTHLY_WAGES, customerID, agent_ID)
        })
        ll_anually.setOnClickListener(View.OnClickListener {
            changeStatus(3, ll_daily, ll_monthly, ll_anually)
            hitApitoGettheOrderReports(YEARLY_WAGES, customerID, agent_ID)
        })
    }


    fun changeStatus(status: Int, view1: View, view2: View, view3: View) {
        when (status) {
            1 -> {
                view1.setBackgroundColor(resources.getColor(R.color.white))
                view2.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                view3.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                tv_daily.setTextColor(resources.getColor(R.color.black))
                tv_monthly.setTextColor(resources.getColor(R.color.white))
                tv_annually.setTextColor(resources.getColor(R.color.white))
            }
            2 -> {
                view1.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                view2.setBackgroundColor(resources.getColor(R.color.white))
                view3.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                tv_daily.setTextColor(resources.getColor(R.color.white))
                tv_monthly.setTextColor(resources.getColor(R.color.black))
                tv_annually.setTextColor(resources.getColor(R.color.white))
            }

            3 -> {
                view1.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                view2.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                view3.setBackgroundColor(resources.getColor(R.color.white))
                tv_daily.setTextColor(resources.getColor(R.color.white))
                tv_monthly.setTextColor(resources.getColor(R.color.white))
                tv_annually.setTextColor(resources.getColor(R.color.black))
            }
        }

    }


    fun showFilterAlert() {
        val dialog = Dialog(this, R.style.AppBaseTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.getWindow()!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.getWindow()!!.getAttributes().windowAnimations = R.style.dialog_animation
        dialog.setContentView(R.layout.custom_alert_filterorder)
        val iv_close = dialog.findViewById<ImageView>(R.id.iv_close)
        val btn_filterorder = dialog.findViewById<Button>(R.id.btn_filterorder)
        val rl_customer = dialog.findViewById<RelativeLayout>(R.id.rl_customerview)
        val rl_fieldmanager = dialog.findViewById<RelativeLayout>(R.id.rl_fieldmanager)
        spinner = Spinner(this)
        spinner2 = Spinner(this)
        iv_close.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        btn_filterorder.setOnClickListener(View.OnClickListener {
            hitApitoGettheOrderReports(DAILY__WAGES, customerID, agent_ID)
            dialog.dismiss()
        })
        // Spinner inside the custom alert
        val aa = ArrayAdapter(this, R.layout.spinner_right_aligned, mListCustomer)
        aa.setDropDownViewResource(R.layout.spinner_right_aligned)
        val ll = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        spinner!!.id = NEW_SPINNER_ID
        rl_customer.addView(spinner)
        with(spinner)
        {
            this?.adapter = aa
            this!!.setSelection(0, false)
            onItemSelectedListener = this@MyOrderReportsActivity
            layoutParams = ll
            prompt = "Select your favourite language"
        }
        spinner!!.setSelection(0)

        // 2nd spinner
        // Spinner inside the custom alert
        val aaa = ArrayAdapter(this, R.layout.spinner_right_aligned, mFieldManager)
        aaa.setDropDownViewResource(R.layout.spinner_right_aligned)
        val lll = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        spinner2!!.id = NEW_SPINNER_ID_FileManager
        rl_fieldmanager.addView(spinner2)
        with(spinner2)
        {
            this!!.adapter = aaa
            setSelection(0, false)
            onItemSelectedListener = this@MyOrderReportsActivity
            layoutParams = lll
            prompt = "Select your favourite language"
        }
        spinner2!!.setSelection(0)
        dialog.show()

    }

    private fun hitApitoGettheOrderReports(filterID: Int, customer_ID: Int, agent_ID: Int) {
        x_axisList.clear()
        y_axisList.clear()
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.filterOrder(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            ),
            filterID,
            customer_ID, agent_ID
        )
        showLoading(true)
        call.enqueue(object : retrofit2.Callback<OrderLabelPojo?> {
            override fun onResponse(
                call: Call<OrderLabelPojo?>,
                response: Response<OrderLabelPojo?>
            ) {
                if (response.body() != null) {
                    hideProgress()
                    x_axisList.addAll(response.body()!!.data.weekLabel)
                    y_axisList.addAll(response.body()!!.data.weekTotal)
                    setData(x_axisList, y_axisList,filterID)
                }

            }

            override fun onFailure(call: Call<OrderLabelPojo?>, t: Throwable) {
                val dsgfd = ""
            }

        })

    }

    private fun hitApitoGetFieldmanagers() {
        mFieldManagerList.clear()
        mFieldManager.clear()
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getFiledManager(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            )
        )
        showLoading(true)
        call.enqueue(object : retrofit2.Callback<GetFieldManagerPojo?> {
            override fun onResponse(
                call: Call<GetFieldManagerPojo?>,
                response: Response<GetFieldManagerPojo?>
            ) {
                if (response.body() != null) {
                    hideProgress()
                    mFieldManagerList.addAll(response.body()!!.data)
                    mFieldManager.add("Select Field Manager")
                    for (i in 0 until response.body()!!.data.size) {
                        mFieldManager.add(response.body()!!.data[i].name)
                    }

                }

            }

            override fun onFailure(call: Call<GetFieldManagerPojo?>, t: Throwable) {
                val dsgfd = ""
            }

        })


    }

    private fun hitApitoGetCustomerisitng() {
        mList.clear()
        mListCustomer.clear()
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getCustomerList(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            )
        )
        showLoading(true)
        call.enqueue(object : retrofit2.Callback<GetCustomerLisiting?> {
            override fun onResponse(
                call: Call<GetCustomerLisiting?>,
                response: Response<GetCustomerLisiting?>
            ) {
                if (response.body() != null) {
                    hideProgress()
                    mListCustomer.add("Select Customer")
                    mList.addAll(response.body()!!.data)
                    for (i in 0 until response.body()!!.data.size) {
                        mListCustomer.add(response.body()!!.data[i].first_name + response.body()!!.data[i].middle_name + response.body()!!.data[i].last_name)
                    }

                }

            }

            override fun onFailure(call: Call<GetCustomerLisiting?>, t: Throwable) {
                val dsgfd = ""
            }

        })
    }


    private fun setData22(mXaxisList: ArrayList<String>, yAxisList: ArrayList<Int>) {

        val barWidth = 0.15f
        val barSpace = 0.07f
        val groupSpace = 0.56f
        val groupCount = 12


        var yValueGroup1 = ArrayList<BarEntry>()
        // draw the graph
        var barDataSet1: BarDataSet

        yValueGroup1.add(BarEntry(1f, floatArrayOf(9.toFloat(), 3.toFloat())))

        yValueGroup1.add(BarEntry(2f, floatArrayOf(3.toFloat(), 3.toFloat())))

        yValueGroup1.add(BarEntry(3f, floatArrayOf(3.toFloat(), 3.toFloat())))

        yValueGroup1.add(BarEntry(4f, floatArrayOf(3.toFloat(), 3.toFloat())))


        yValueGroup1.add(BarEntry(5f, floatArrayOf(9.toFloat(), 3.toFloat())))

        yValueGroup1.add(BarEntry(6f, floatArrayOf(11.toFloat(), 1.toFloat())))


        yValueGroup1.add(BarEntry(7f, floatArrayOf(11.toFloat(), 7.toFloat())))


        yValueGroup1.add(BarEntry(8f, floatArrayOf(11.toFloat(), 9.toFloat())))

        yValueGroup1.add(BarEntry(9f, floatArrayOf(11.toFloat(), 13.toFloat())))

        yValueGroup1.add(BarEntry(10f, floatArrayOf(11.toFloat(), 2.toFloat())))

        yValueGroup1.add(BarEntry(11f, floatArrayOf(11.toFloat(), 6.toFloat())))

        yValueGroup1.add(BarEntry(12f, floatArrayOf(11.toFloat(), 2.toFloat())))


        barDataSet1 = BarDataSet(yValueGroup1, "")
        barDataSet1.setColors(Color.BLUE, Color.RED)
        barDataSet1.label = "2016"
        barDataSet1.setDrawIcons(false)
        barDataSet1.setDrawValues(false)


        var barData = BarData(barDataSet1)

        chart!!.description.isEnabled = false
        chart!!.description.textSize = 0f
        barData.setValueFormatter(LargeValueFormatter())
        chart!!.setData(barData)
        chart!!.getBarData().setBarWidth(barWidth)
        chart!!.getXAxis().setAxisMinimum(0f)
        chart!!.getXAxis().setAxisMaximum(12f)
        chart!!.groupBars(0f, groupSpace, barSpace)
        chart!!.setFitBars(true)
        chart!!.getData().setHighlightEnabled(false)
        chart!!.invalidate()

        val xAxis = chart!!.getXAxis()
        xAxis.setGranularity(1f)
        xAxis.setGranularityEnabled(true)
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 9f

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis.setValueFormatter(IndexAxisValueFormatter(mXaxisList))

        xAxis.setLabelCount(12)
        xAxis.mAxisMaximum = 12f
        xAxis.setCenterAxisLabels(true)
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.spaceMin = 4f
        xAxis.spaceMax = 4f

        chart!!.setVisibleXRangeMaximum(12f)
        chart!!.setVisibleXRangeMinimum(12f)
        chart!!.setDragEnabled(true)

        //Y-axis
        chart!!.getAxisRight().setEnabled(false)
        chart!!.setScaleEnabled(true)

        val leftAxis = chart!!.getAxisLeft()
        leftAxis.setValueFormatter(LargeValueFormatter())
        leftAxis.setDrawGridLines(false)
        leftAxis.setSpaceTop(1f)
        leftAxis.setAxisMinimum(0f)

    }


    private fun setData(mXaxisList: ArrayList<String>, yAxisList: ArrayList<Int>,filterID: Int) {
        val barWidth: Float
        barWidth = 0.45f
        var yValueGroup1 = ArrayList<BarEntry>()

        // draw the graph
        var barDataSet1: BarDataSet
        var barDataSet2: BarDataSet

        if (filterID==3){
            for (i in 0 until yAxisList.size) {
                yValueGroup1.add(BarEntry((i + 1 ).toFloat(), floatArrayOf(yAxisList[i].toFloat())))
            }
        }
        else{
            for (i in 0 until yAxisList.size) {
                yValueGroup1.add(BarEntry((i + .45).toFloat(), floatArrayOf(yAxisList[i].toFloat())))
            }
        }


        //    yValueGroup1.add(BarEntry(3f, floatArrayOf(3.toFloat())))

        barDataSet1 = BarDataSet(yValueGroup1, "")
        barDataSet1.setColors(resources.getColor(R.color.colorPrimary))
        barDataSet1.label = "2016"
        barDataSet1.setDrawIcons(false)
        barDataSet1.setDrawValues(false)

        var barData = BarData(barDataSet1)

        chart!!.description.isEnabled = false
        chart!!.description.textSize = 0f
        barData.setValueFormatter(LargeValueFormatter())
        chart!!.setData(barData)
        chart!!.getBarData().setBarWidth(barWidth)
        chart!!.getXAxis().setAxisMinimum(0f)
        chart!!.getXAxis().setAxisMaximum(12f)
        //    chart!!.groupBars(0f, groupSpace, barSpace)
        chart!!.setFitBars(true)
        chart!!.getData().setHighlightEnabled(false)
        chart!!.invalidate()

        // set bar label
        var legend = chart!!.legend
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM)
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        legend.setDrawInside(false)

        var legenedEntries = arrayListOf<LegendEntry>()

        legenedEntries.add(LegendEntry("2016", Legend.LegendForm.SQUARE, 8f, 8f, null, Color.RED))
        //    legenedEntries.add(LegendEntry("2017", Legend.LegendForm.SQUARE, 8f, 8f, null, Color.YELLOW))

        legend.setCustom(legenedEntries)

        legend.setYOffset(2f)
        legend.setXOffset(0f)
        legend.xEntrySpace = 12f
        legend.setYEntrySpace(0f)
        legend.setTextSize(5f)

        val xAxis = chart!!.getXAxis()
        xAxis.setGranularity(1f)
        xAxis.setGranularityEnabled(true)
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 9f

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis.setValueFormatter(IndexAxisValueFormatter(mXaxisList))

        if (filterID==3){
            xAxis.setLabelCount(7)
        }
        else{
            xAxis.setLabelCount(12)
        }
        xAxis.mAxisMaximum = 12f
        xAxis.setCenterAxisLabels(true)
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.spaceMin = 4f
        xAxis.spaceMax = 4f

        chart!!.setVisibleXRangeMaximum(12f)
        chart!!.setVisibleXRangeMinimum(12f)
        chart!!.setDragEnabled(true)

        //Y-axis
        chart!!.getAxisRight().setEnabled(false)
        chart!!.setScaleEnabled(true)

        val leftAxis = chart!!.getAxisLeft()
        leftAxis.setValueFormatter(LargeValueFormatter())
        leftAxis.setDrawGridLines(false)
        leftAxis.setSpaceTop(1f)
        leftAxis.setAxisMinimum(0f)


        chart!!.data = barData
        chart!!.setVisibleXRange(1f, 12f)


    }


    private fun initview() {
        fab = findViewById(R.id.fabfilter)
        chart = findViewById(R.id.bg_barchart)
        chart!!.getDescription().isEnabled = false
        chart!!.setMaxVisibleValueCount(60)
        chart!!.setPinchZoom(false)
        chart!!.setDrawBarShadow(false)
        chart!!.setDrawGridBackground(false)
        val xAxis = chart!!.getXAxis()
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        chart!!.getAxisLeft().setDrawGridLines(false)
        chart!!.animateY(1500)
        chart!!.getLegend().isEnabled = false
        sharedPreferences = this.getSharedPreferences(
            "AcessToken",
            AppCompatActivity.MODE_PRIVATE
        )
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent!!.getId()) {
            1 -> {
                if (position == 0) {
                    spinner!!.setSelection(position)
                    customerID = 0
                    Toast.makeText(this, "" + customerID.toString(), Toast.LENGTH_SHORT).show()
                    return
                }
                spinner!!.setSelection(position)
                customerID = mList.get(position - 1).id
                Toast.makeText(this, "" + customerID.toString(), Toast.LENGTH_SHORT).show()


            }
            2 -> {
                if (position == 0) {
                    spinner2!!.setSelection(position)
                    agent_ID = 0
                    Toast.makeText(this, "" + agent_ID.toString(), Toast.LENGTH_SHORT).show()
                    return
                }
                spinner2!!.setSelection(position)
                agent_ID = mFieldManagerList.get(position - 1).id
                Toast.makeText(this, "" + agent_ID.toString(), Toast.LENGTH_SHORT).show()


            }
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}