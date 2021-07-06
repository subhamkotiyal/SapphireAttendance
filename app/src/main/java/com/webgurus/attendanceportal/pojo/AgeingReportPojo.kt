package com.webgurus.attendanceportal.pojo

data class AgeingReportPojo(
    val `data`: List<AgeingData>,
    val message: String,
    val status: Int
)

data class AgeingData(
    val address: String,
    val created_by_id: Int,
    val customer_name: String,
    val date: String,
    val id: Int,
    val instructions: String,
    val is_payment_complete: Int,
    val payment_pending: Int,
    val payment_received: Int,
    val payment_type: Int,
    val status: Int,
    val total_price: String,
    val transaction: Transaction,
    val variant: List<VariantAgeing>
)

data class Transaction(
    val amount: String,
    val created_at: String,
    val created_by_id: Int,
    val id: Int,
    val order_id: Int,
    val status: Int,
    val transaction_id: String,
    val updated_at: String
)

data class VariantAgeing(
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