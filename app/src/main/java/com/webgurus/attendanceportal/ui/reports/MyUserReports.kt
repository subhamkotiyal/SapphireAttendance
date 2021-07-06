package com.webgurus.attendanceportal.ui.reports

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.OrderLabelPojo
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_myorderreport.*
import retrofit2.Call
import retrofit2.Response

class MyUserReports : BaseActivity() {

    private var chart: BarChart? = null
    var x_arrayList: ArrayList<String> = ArrayList()
    var y_arrayList: ArrayList<Int> = ArrayList()
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myuser)
        initview()
        listeners()
        hitApitoGettheUserReports(3)

    }

    private fun listeners() {
        ll_daily.setOnClickListener(View.OnClickListener {
            changeStatus(1, ll_daily, ll_monthly, ll_anually)
            hitApitoGettheUserReports(3)
        })
        ll_monthly.setOnClickListener(View.OnClickListener {
            changeStatus(2, ll_daily, ll_monthly, ll_anually)
            hitApitoGettheUserReports(2)
        })
        ll_anually.setOnClickListener(View.OnClickListener {
            changeStatus(3, ll_daily, ll_monthly, ll_anually)
            hitApitoGettheUserReports(1)
        })
    }

    private fun initview() {
        chart = findViewById(R.id.bg_barchart)
        chart!!.getDescription().isEnabled = false
        chart!!.setMaxVisibleValueCount(60)
        chart!!.setPinchZoom(false)
        chart!!.setDrawBarShadow(false)
        chart!!.setDrawGridBackground(false)
        val xAxis = chart!!.getXAxis()
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        chart!!.getAxisLeft().setDrawGridLines(false)
        chart!!.animateY(1500)
        chart!!.getLegend().isEnabled = false
        sharedPreferences = this.getSharedPreferences(
            "AcessToken",
            AppCompatActivity.MODE_PRIVATE
        )
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


    private fun hitApitoGettheUserReports(filterID: Int) {
        x_arrayList.clear()
        y_arrayList.clear()
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.filterUser(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            ),
            filterID
        )
        showLoading(true)
        call.enqueue(object : retrofit2.Callback<OrderLabelPojo?> {
            override fun onResponse(
                call: Call<OrderLabelPojo?>,
                response: Response<OrderLabelPojo?>
            ) {
                if (response.body() != null) {
                    hideProgress()
                    x_arrayList.addAll(response.body()!!.data.weekLabel)
                    y_arrayList.addAll(response.body()!!.data.weekTotal)
                    setData(x_arrayList, y_arrayList,filterID)
                }

            }

            override fun onFailure(call: Call<OrderLabelPojo?>, t: Throwable) {
                val dsgfd = ""
            }

        })

    }

    private fun setData(x_AxisList: ArrayList<String>, yAxisList: ArrayList<Int>,filterID: Int) {

        val barWidth: Float
        barWidth = 0.45f
        var yValueGroup1 = ArrayList<BarEntry>()

        // draw the graph
        var barDataSet1: BarDataSet
        var barDataSet2: BarDataSet

        if (filterID==3){
            for (i in 0 until 2) {
                yValueGroup1.add(BarEntry((i ).toFloat(), floatArrayOf(yAxisList[i].toFloat())))
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
        xAxis.setValueFormatter(IndexAxisValueFormatter(x_AxisList))

        if (filterID==3){
            xAxis.setLabelCount(8)
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


}

