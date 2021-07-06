package com.webgurus.attendanceportal.pojo

data class AddProductPojo(
    val `data`: DataProduct,
    val message: String,
    val status: Int
)

data class DataProduct(
    val id: Int,
    val name: String,
    val variants: List<AddVariant>
)

data class AddVariant(
    val category_id: Int,
    val created_at: String,
    val created_by_id: Int,
    val id: Int,
    val in_stock: Int,
    val max_price: String,
    val min_price: String,
    val price: String,
    val product_id: Int,
    val quantity_left: Int,
    val unit_id: Int,
    val updated_at: String
)
