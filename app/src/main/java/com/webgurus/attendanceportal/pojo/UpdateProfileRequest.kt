package com.webgurus.attendanceportal.pojo

 data class UpdateProfileRequest(
    val city: String,
    val dob: String,
    val email: String,
    val phone_number: Int,
    val username: String
)