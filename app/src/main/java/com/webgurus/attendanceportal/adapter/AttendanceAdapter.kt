package com.webgurus.attendanceportal.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.pojo.SuccesAttendances
import com.webgurus.attendanceportal.ui.commets.CommentsViews
import com.webgurus.attendanceportal.ui.trackuer.TrackUserDailyActivity
import com.webgurus.utils.LocationAddress.changeOneDateformattoAnother
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class AttendanceAdapter(val userList: ArrayList<SuccesAttendances>, val context: Activity) :
    RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.item_attendancelist,
            parent,
            false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: AttendanceAdapter.ViewHolder, position: Int) {
        holder.bindItems(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(user: SuccesAttendances) {
            val ll_comment = itemView.findViewById(R.id.ll_comment) as LinearLayout
          //  val ll_viewtrack = itemView.findViewById(R.id.ll_viewtrack) as LinearLayout
            val ll_root = itemView.findViewById(R.id.ll_root) as LinearLayout
            val tv_outtime = itemView.findViewById(R.id.tv_outtime) as TextView
            val tv_intime  = itemView.findViewById(R.id.tv_intime) as TextView
            val tv_date  = itemView.findViewById(R.id.tv_date) as TextView
            val tv_totaldistance  = itemView.findViewById(R.id.tv_totaldistance) as TextView
            if(user.out_time!=null){
                val datess = SimpleDateFormat("yyyy-MM-dd H:mm:ss").parse(user.out_time.toString())
                val df: DateFormat = SimpleDateFormat("H:mm a")
                val myDateStr = df.format(datess)
                tv_outtime.text = myDateStr.toString()
            }else{
                tv_outtime.text = "--"
            }

            if(user.in_time!=null){
                val datess = SimpleDateFormat("yyyy-MM-dd H:mm:ss").parse(user.in_time.toString())
                val df: DateFormat = SimpleDateFormat("H:mm a")
                val myDateStr = df.format(datess)
                tv_intime.text = myDateStr.toString()
            }else{
                tv_intime.text = "--"
            }

           tv_date.text = changeOneDateformattoAnother(user.today_date)
            tv_totaldistance.text=user.distance + " Km"
            ll_comment.setOnClickListener(View.OnClickListener {
                itemView.context.startActivity(Intent(itemView.context, CommentsViews::class.java))
            })
//            ll_root.setOnClickListener(View.OnClickListener {
//                var intent = Intent(itemView.context, TrackUserDailyActivity::class.java)
//                intent.putExtra("attendance_id", user.id.toString())
//                itemView.context.startActivity(intent)
//            })

        }
    }


}