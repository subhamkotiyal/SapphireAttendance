package com.webgurus.attendanceportal.pojo

data class BillUpdatePojo(
    val `data`: BillUpdateData,
    val message: String,
    val status: Int
)

data class BillUpdateData(
    val bill_no: String,
    val created_at: String,
    val created_by_id: Int,
    val customer_id: Int,
    val date: String,
    val due_on: String,
    val id: Int,
    val overdue_by_days: String,
    val pending: String,
    val received: String,
    val state_id: Int,
    val total_payment: String,
    val updated_at: String
)