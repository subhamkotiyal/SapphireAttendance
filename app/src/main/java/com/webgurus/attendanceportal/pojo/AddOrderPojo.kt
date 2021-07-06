package com.webgurus.attendanceportal.pojo

data class AddOrderPojo(
    val `data`: DataOrder,
    val message: String,
    val status: Int
)

data class DataOrder(
    val address: String,
    val created_by_id: Int,
    val customer_name: String,
    val date: String,
    val id: Int,
    val instructions: Any,
    val is_payment_complete: Int,
    val payment_pending: Int,
    val payment_received: Int,
    val payment_type: Int,
    val status: Int,
    val total_price: String,
    val variant: List<DataVariant>
)

data class DataVariant(
    val id: Int,
    val max_price: String,
    val min_price: String,
    val product_name: String,
    val product_price: String,
    val product_unit: String,
    val quantity: Int,
    val quoted_price: String,
    val total_amount: String
)