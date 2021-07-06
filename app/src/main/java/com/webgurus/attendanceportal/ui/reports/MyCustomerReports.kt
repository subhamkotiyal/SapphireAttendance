package com.webgurus.attendanceportal.ui.reports

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.OrderLabelPojo
import com.webgurus.attendanceportal.ui.base.BaseActivity
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_myorderreport.*
import retrofit2.Call
import retrofit2.Response

class MyCustomerReports : BaseActivity() {


    var sharedPreferences : SharedPreferences? =null
    var y_axisList : ArrayList<Int> = ArrayList()
    var x_axisList : ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mycustomer)
        initview()
        listeners()
        hitApitoGettheCustomerReports(3)
    }

    private fun initview() {
        sharedPreferences= this.getSharedPreferences(
            "AcessToken",
            AppCompatActivity.MODE_PRIVATE
        )
    }


    private fun listeners() {
        ll_daily.setOnClickListener(View.OnClickListener {
            changeStatus(1, ll_daily, ll_monthly, ll_anually)
            hitApitoGettheCustomerReports(3)
        })
        ll_monthly.setOnClickListener(View.OnClickListener {
            changeStatus(2, ll_daily, ll_monthly, ll_anually)
            hitApitoGettheCustomerReports(2)
        })
        ll_anually.setOnClickListener(View.OnClickListener {
            changeStatus(3, ll_daily, ll_monthly, ll_anually)
            hitApitoGettheCustomerReports(1)
        })
    }

    private fun hitApitoGettheCustomerReports(status: Int) {
        x_axisList.clear()
        y_axisList.clear()
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.filterCustomer(
            "Bearer " + sharedPreferences!!.getString(
                "Access_Token",
                ""
            ),status
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
                    setData(x_axisList,y_axisList,status)
                }
                if (response.body()==null){
                    Log.e("lod","Errorrrrrr")
                }

            }

            override fun onFailure(call: Call<OrderLabelPojo?>, t: Throwable) {
                val dsgfd = ""
            }

        })
    }


    private fun  setData( mXaxisList: ArrayList<String>,yAxisList : ArrayList<Int>,status: Int) {
        val barWidth: Float
        barWidth = 0.45f
        val yValueGroup1 = ArrayList<BarEntry>()

        // draw the graph
        val barDataSet1: BarDataSet
        var barDataSet2: BarDataSet

        for (i in 0 until yAxisList.size){
            yValueGroup1.add(BarEntry((i+.45).toFloat(), floatArrayOf(yAxisList[i].toFloat())))
        }

        //    yValueGroup1.add(BarEntry(3f, floatArrayOf(3.toFloat())))

        barDataSet1 = BarDataSet(yValueGroup1, "")
        barDataSet1.setColors(resources.getColor(R.color.colorPrimary))
        barDataSet1.label = "2016"
        barDataSet1.setDrawIcons(false)
        barDataSet1.setDrawValues(false)

        val barData = BarData(barDataSet1)

        bg_barchart!!.description.isEnabled = false
        bg_barchart!!.description.textSize = 0f
        barData.setValueFormatter(LargeValueFormatter())
        bg_barchart!!.setData(barData)
        bg_barchart!!.getBarData().setBarWidth(barWidth)
        bg_barchart!!.getXAxis().setAxisMinimum(0f)
        bg_barchart!!.getXAxis().setAxisMaximum(12f)

        //    chart!!.groupBars(0f, groupSpace, barSpace)
        bg_barchart!!.setFitBars(true)
        bg_barchart!!.getData().setHighlightEnabled(false)
        bg_barchart!!.invalidate()

        // set bar label
        val legend =  bg_barchart!!.legend
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM)
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        legend.setDrawInside(false)

        val legenedEntries = arrayListOf<LegendEntry>()

        legenedEntries.add(LegendEntry("2016", Legend.LegendForm.SQUARE, 8f, 8f, null, Color.RED))
        //    legenedEntries.add(LegendEntry("2017", Legend.LegendForm.SQUARE, 8f, 8f, null, Color.YELLOW))

        legend.setCustom(legenedEntries)

        legend.setYOffset(2f)
        legend.setXOffset(0f)
        legend.xEntrySpace=12f
        legend.setYEntrySpace(0f)
        legend.setTextSize(5f)

        val xAxis =  bg_barchart!!.getXAxis()
        xAxis.setGranularity(1f)
        xAxis.setGranularityEnabled(true)
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 9f

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis.setValueFormatter(IndexAxisValueFormatter(mXaxisList))

        if (status==3){
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

        bg_barchart!!.setVisibleXRangeMaximum(12f)
        bg_barchart!!.setVisibleXRangeMinimum(12f)
        bg_barchart!!.setDragEnabled(true)

        //Y-axis
        bg_barchart!!.getAxisRight().setEnabled(false)
        bg_barchart!!.setScaleEnabled(true)

        val leftAxis =  bg_barchart!!.getAxisLeft()
        leftAxis.setValueFormatter(LargeValueFormatter())
        leftAxis.setDrawGridLines(false)
        leftAxis.setSpaceTop(1f)
        leftAxis.setAxisMinimum(0f)


        bg_barchart!!.data = barData
        bg_barchart!!.setVisibleXRange(1f, 12f)


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


}