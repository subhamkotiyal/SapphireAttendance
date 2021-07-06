package com.webgurus.attendanceportal.pojo

data class CustomerMappingPojo(
    val customer: ArrayList<CustomerDetails>,
    val status: Int
)

data class CustomerDetails(
    val address: String,
    val city: String,
    val country: Any,
    val created_at: String,
    val created_by_id: Int,
    val dob: String,
    val email: String,
    val first_name: String,
    val id: Int,
    val is_premium: Int,
    val last_name: String,
    val middle_name: String,
    val organisation_name: String,
    val phone_number: String,
    val pincode: Int,
    val secondary_address: String,
    val secondary_phone_number: Any,
    val state: String,
    val state_id: Int,
    val updated_at: String
)