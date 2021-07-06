package com.webgurus.attendanceportal.pojo

data class CreateCustomerPojo(
    val `data`: Dataess,
    val message: String,
    val status: Int
)

data class Dataess(
    val address: String,
    val city: String,
    val country: Any,
    val created_at: String,
    val created_by_id: Int,
    val dob: String,
    val email: String,
    val first_name: String,
    val id: Int,
    val last_name: String,
    val middle_name: String,
    val phone_number: String,
    val pincode: String,
    val secondary_address: String,
    val state: String,
    val state_id: Int,
    val updated_at: String
)