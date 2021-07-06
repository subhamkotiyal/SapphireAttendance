package com.webgurus.attendanceportal.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.listeners.RecyclerViewRoleClickListeners
import com.webgurus.attendanceportal.pojo.DataUser
import com.webgurus.attendanceportal.ui.createuser.AddUserTargetActivity
import com.webgurus.attendanceportal.ui.createuser.CreateUserFragment
import com.webgurus.attendanceportal.ui.createuser.UserCurrentLocation
import com.webgurus.attendanceportal.ui.createuser.ViewUserAttendance
import com.webgurus.attendanceportal.ui.role.AddRoleActivity

class UserListingAdapter (var context: Activity, var mListuser : ArrayList<DataUser> , var  mClickListeners : RecyclerViewRoleClickListeners      )
    : RecyclerView.Adapter<UserListingAdapter.ViewHolder>(),Filterable {

    var filterList = ArrayList<DataUser>()
    // exampleListFull . exampleList

    init {
        filterList = mListuser as ArrayList<DataUser>
    }


    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListingAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_userlisting, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: UserListingAdapter.ViewHolder, position: Int) {
        holder.bindItems(context,filterList.get(position),mClickListeners,position,mListuser.get(position).id, mListuser)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return filterList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Activity,user:DataUser,clickList:RecyclerViewRoleClickListeners,position: Int,userID:Int, mListuser : ArrayList<DataUser>) {
            val tv_clientname = itemView.findViewById(R.id.tv_clientname) as TextView
            val tv_email = itemView.findViewById(R.id.tv_email) as TextView
            val tv_phonenumber = itemView.findViewById(R.id.tv_phonenumber) as TextView
            val ll_delete = itemView.findViewById(R.id.ll_delete) as LinearLayout
            val ll_edit = itemView.findViewById(R.id.ll_edit) as LinearLayout
            val ll_attendance = itemView.findViewById(R.id.ll_settarget) as LinearLayout
            val ll_settarget = itemView.findViewById(R.id.ll_settarget) as LinearLayout
            val iv_userlocation = itemView.findViewById(R.id.iv_userlocation) as ImageView
            tv_clientname.setText(user.name)
            tv_email.setText(user.email)
            tv_phonenumber.setText(user.phone_number)
            ll_delete.setOnClickListener(View.OnClickListener {
                clickList.onClick(position,userID)
            })
            iv_userlocation.setOnClickListener(View.OnClickListener {
                var intent = Intent(context, UserCurrentLocation::class.java)
                intent.putExtra("userID",user.id.toString())
                context.startActivity(intent)
            })

            ll_edit.setOnClickListener(View.OnClickListener {
                var intent = Intent(context, CreateUserFragment::class.java)
                intent.putExtra("name",user.name)
                intent.putExtra("address",user.address)
                intent.putExtra("city",user.city)
                intent.putExtra("state",user.state)
                intent.putExtra("dob",user.dob)
                intent.putExtra("email",user.email)
                intent.putExtra("id",user.id)
                intent.putExtra("manager_id",user.manager_id)
                intent.putExtra("manager_name",user.manager_name)
                intent.putExtra("manager_role",user.manager_role)
                intent.putExtra("phone_number",user.phone_number)
                intent.putExtra("role_id",user.role_id)
                context.startActivity(intent)
            })
            ll_attendance.setOnClickListener(View.OnClickListener {
                var intent = Intent(context, ViewUserAttendance::class.java)
                intent.putExtra("id",user.id)
                context.startActivity(intent)
            })


          ll_settarget.setOnClickListener( {
              var intent = Intent(context, AddUserTargetActivity::class.java)
              intent.putExtra("id",user.id)
              context.startActivity(intent)
          })

        }
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filterList = mListuser as ArrayList<DataUser>
                } else {
                    val resultList = ArrayList<DataUser>()
                    for (row in mListuser) {
                        if (row.name.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            resultList.add(row)
                        }
                    }
                    filterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterList = results?.values as ArrayList<DataUser>
                notifyDataSetChanged()
            }
        }
    }

    fun filterList(filteredList: ArrayList<DataUser>) {
        filterList = filteredList
        notifyDataSetChanged()
    }
}