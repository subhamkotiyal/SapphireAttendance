package com.webgurus.attendanceportal.pojo

data class AddBillPojo(
    val `data`: BillData,
    val message: String,
    val status: Int
)

data class BillData(
    val bill_no: String,
    val created_at: String,
    val created_by_id: Int,
    val customer_id: String,
    val id: Int,
    val pending: String,
    val received: String,
    val total_payment: String,
    val updated_at: String
)