package com.webgurus.newservice

import android.app.Activity
import android.content.Context
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.ui.database.LocationParam
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat


class LatLongAdapter(var context: Activity, var mLatLongData: ArrayList<LocationParam>): RecyclerView.Adapter<LatLongAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LatLongAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.item_lalongview,
            parent,
            false
        )
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: LatLongAdapter.ViewHolder, position: Int) {
        holder.bindItems(mLatLongData, position,context)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return mLatLongData.size
    }


    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        public fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
            val theta = lon1 - lon2
            var dist = (Math.sin(deg2rad(lat1))
                    * Math.sin(deg2rad(lat2))
                    + (Math.cos(deg2rad(lat1))
                    * Math.cos(deg2rad(lat2))
                    * Math.cos(deg2rad(theta))))
            dist = Math.acos(dist)
            dist = rad2deg(dist)
            dist = dist * 60 * 1.1515
            return dist
        }

        private fun deg2rad(deg: Double): Double {
            return deg * Math.PI / 180.0
        }

        private fun rad2deg(rad: Double): Double {
            return rad * 180.0 / Math.PI
        }


        fun bindItems(mModulesList: ArrayList<LocationParam>, position: Int,context: Context) {

            val tv_lat = itemView.findViewById(R.id.tv_lat) as TextView
            val tv_long = itemView.findViewById(R.id.tv_long) as TextView
            val tv_time = itemView.findViewById(R.id.tv_time) as TextView
            val tv_distance = itemView.findViewById(R.id.tv_distance) as TextView
            val tv_difference = itemView.findViewById(R.id.tv_difference) as TextView
            tv_lat.setText(mModulesList.get(position).lattitude)
            tv_long.setText(mModulesList.get(position).longitude)
            tv_time.setText(mModulesList.get(position).time)
            if(mModulesList.get(position).time!=null){
                if(position>0){
                    val simpleDateFormat = SimpleDateFormat("HH:mm:ss")
                    val date1 = simpleDateFormat.parse(mModulesList.get(position).time)
                    val date2 = simpleDateFormat.parse(mModulesList.get(position - 1).time)
                    val difference: Int = (date1.time-date2.time).toInt()
                    val days = (difference / (1000 * 60 * 60 * 24)).toInt()
                    val hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60)) as Int
                    val min =  (difference - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours) as Int / (1000 * 60)
                    tv_difference.setText(min.toString() + "  min")

                    val loc1 = Location("")
                    loc1.setLatitude(mModulesList.get(position).lattitude.toDouble())
                    loc1.setLongitude(mModulesList.get(position).longitude.toDouble())

                    val loc2 = Location("")
                    loc1.setLatitude(mModulesList.get(position - 1).lattitude.toDouble())
                    loc1.setLongitude(mModulesList.get(position - 1).longitude.toDouble())
                    val distanceInMeters: Float = loc1.distanceTo(loc2)
                    tv_distance.setText(distanceInMeters.toString())

                    val from = LatLng(
                        mModulesList.get(position - 1).lattitude.toDouble(), mModulesList.get(
                            position - 1
                        ).longitude.toDouble()
                    )
                    val to = LatLng(
                        mModulesList.get(position).lattitude.toDouble(), mModulesList.get(
                            position
                        ).longitude.toDouble()
                    )
                    val distance: Double = SphericalUtil.computeDistanceBetween(from, to)
                    val nf: NumberFormat = DecimalFormat("##.##")
                    tv_distance.setText(nf.format(distance).toString() + " m" )
                }

            }

        }
    }



}

