package com.webgurus.attendanceportal.ui.unitmanage

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.listeners.RecyclerViewRoleClickListeners
import com.webgurus.attendanceportal.pojo.AddUnitPojo
import com.webgurus.attendanceportal.pojo.CategoryData
import com.webgurus.attendanceportal.pojo.DataList
import com.webgurus.attendanceportal.pojo.UnitListPojo
import com.webgurus.network.GetDataService
import com.webgurus.network.RetrofitClientInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class UnitListingAdapter(context: Activity, val mUnitList: ArrayList<DataList>,var listeners: RecyclerViewRoleClickListeners) : RecyclerView.Adapter<UnitListingAdapter.ViewHolder>() {

    val context = context
    lateinit var sharedPreferences: SharedPreferences

    var arrayListUnit=ArrayList<DataList>()
    init {
        arrayListUnit=mUnitList as ArrayList<DataList>
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitListingAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.item_unit,
            parent,
            false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: UnitListingAdapter.ViewHolder, position: Int) {
        holder.bindItems(arrayListUnit[position],position,listeners)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return arrayListUnit.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bindItems(user: DataList, postion: Int,listeners:RecyclerViewRoleClickListeners) {
            sharedPreferences = context.getSharedPreferences(
                "AcessToken",
                AppCompatActivity.MODE_PRIVATE
            )
            val ser_no = itemView.findViewById(R.id.ser_no) as TextView
            val tv_valuegram = itemView.findViewById(R.id.valuegram) as TextView
            val Img_del  = itemView.findViewById(R.id.del_im) as ImageView
            val Img_edit  = itemView.findViewById(R.id.edit_im) as ImageView

            tv_valuegram.text=user.unit
            ser_no.text= "S No."+(postion+1).toString()
            Img_del.setOnClickListener{
                listeners.onClick(postion,user.id)
            }

            Img_edit.setOnClickListener {
                var id= mUnitList.get(postion).id
                var unit_value= mUnitList.get(postion).unit
                context.startActivity(Intent(context, AddNewUnitActivity::class.java)
                    .putExtra("Unit",unit_value)
                    .putExtra("id",id)
                )

            }

        }

    }
    fun filterList(filteredList: ArrayList<DataList>) {
        arrayListUnit = filteredList
        notifyDataSetChanged()
    }

}