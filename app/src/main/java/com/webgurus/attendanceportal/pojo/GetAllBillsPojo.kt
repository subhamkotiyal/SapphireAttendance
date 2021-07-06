package com.webgurus.attendanceportal.pojo

data class GetAllBillsPojo(
    val `data`: List<GetBillData>,
    val message: String,
    val status: Int
)


data class GetBillData(
    val address: String,
    val bill_no: String,
    val city: String,
    val country: Any,
    val created_at: String,
    val created_by_id: Int,
    val customer_id: Int,
    val dob: String,
    val email: String,
    val first_name: String,
    val id: Int,
    val is_premium: Int,
    val last_name: String,
    val middle_name: String,
    val organisation_name: String,
    val pending: Int,
    val phone_number: String,
    val pincode: Int,
    val received: Int,
    val secondary_address: String,
    val secondary_phone_number: Any,
    val state: String,
    val state_id: Int,
    val total_payment: Int,
    val updated_at: String
)