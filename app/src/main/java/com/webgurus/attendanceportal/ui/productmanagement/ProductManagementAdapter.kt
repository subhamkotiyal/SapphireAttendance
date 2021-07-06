package com.webgurus.attendanceportal.ui.productmanagement

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webgurus.attendanceportal.R
import com.webgurus.attendanceportal.listeners.RecyclerViewRoleClickListeners
import com.webgurus.attendanceportal.pojo.DataUser
import com.webgurus.attendanceportal.pojo.ProductData
import com.webgurus.attendanceportal.pojo.Variant
import com.webgurus.attendanceportal.ui.category.CategoryListingAdapter
import com.webgurus.attendanceportal.ui.createuser.CreateUserFragment

class ProductManagementAdapter (var context: Activity,var mProductData: ArrayList<Variant>,var mListeners: RecyclerViewRoleClickListeners) : RecyclerView.Adapter<ProductManagementAdapter.ViewHolder>() {


    var arrayProducatList=ArrayList<Variant>()
    init {
        arrayProducatList= mProductData as ArrayList<Variant>
    }

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_productmanage, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(context,mProductData.get(position),position,mListeners,mProductData)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return arrayProducatList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Activity,mProductData:Variant,position: Int,mListeners:RecyclerViewRoleClickListeners,mProductDataList: ArrayList<Variant>) {
            val tv_sno = itemView.findViewById(R.id.tv_sno) as TextView
            val tv_productname = itemView.findViewById(R.id.tv_productname) as TextView
            val tv_variant = itemView.findViewById(R.id.tv_variant) as TextView
            val iv_deleteproduct = itemView.findViewById(R.id.iv_deleteproduct) as ImageView
            val iv_updateproduct = itemView.findViewById(R.id.iv_updateproduct) as ImageView
            tv_sno.setText("Sno. "+(position+1))
            tv_productname.setText(mProductData.product_name)
             tv_variant.setText(mProductData.unit_name)
            iv_deleteproduct.setOnClickListener(View.OnClickListener {
                mListeners.onClick(position,mProductData.id)
            })
            iv_updateproduct.setOnClickListener(View.OnClickListener {
                var intent = Intent(context, AddProductManagementActivity::class.java)
                 intent.putParcelableArrayListExtra("productdetails",mProductDataList)
                 intent.putExtra("position",position)
                 intent.putExtra("status","forEdit")
                context.startActivity(intent)
            })
        }
    }
    fun filterList(filteredList: ArrayList<Variant>) {
        arrayProducatList = filteredList
        notifyDataSetChanged()
    }
}