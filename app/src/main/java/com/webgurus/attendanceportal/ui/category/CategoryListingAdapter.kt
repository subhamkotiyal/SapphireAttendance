package com.webgurus.attendanceportal.ui.category

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.listeners.RecyclerViewRoleClickListeners
import com.webgurus.attendanceportal.pojo.CategoryData
import com.webgurus.attendanceportal.pojo.DataRoleList
import com.webgurus.attendanceportal.ui.role.AddRoleActivity

class CategoryListingAdapter  (var context: Activity,var mCatList:ArrayList<CategoryData>?,var clicklisteners: RecyclerViewRoleClickListeners) : RecyclerView.Adapter<CategoryListingAdapter.ViewHolder>() {

    var categoryListAray=ArrayList<CategoryData>()
    init {
        categoryListAray=mCatList as ArrayList<CategoryData>
    }


    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(context,categoryListAray!![position].id,position,categoryListAray!![position],clicklisteners)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return categoryListAray!!.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context:Context,catID:Int,position: Int,category:CategoryData,clicklisteners:RecyclerViewRoleClickListeners) {
            val tv_sno = itemView.findViewById(R.id.tv_sno) as TextView
            val tv_categoryname = itemView.findViewById(R.id.tv_categoryname) as TextView
            val iv_catDelete = itemView.findViewById(R.id.iv_catDelete) as ImageView
            val iv_updatecategory = itemView.findViewById(R.id.iv_updatecategory) as ImageView
            tv_sno.setText("S.No "+ (position+1))
            tv_categoryname.setText(category.name)
            iv_catDelete.setOnClickListener(View.OnClickListener {
                clicklisteners.onClick(position,catID)
            })
            iv_updatecategory.setOnClickListener(View.OnClickListener {
                val intent = Intent(context, AddNewCategoryActivity::class.java)
                intent.putExtra("categID", category.id)
                intent.putExtra("CatName", category.name)
                context.startActivity(intent)
            })
        }
    }
    fun filterList(filteredList: ArrayList<CategoryData>) {
        categoryListAray = filteredList
        notifyDataSetChanged()
    }
}