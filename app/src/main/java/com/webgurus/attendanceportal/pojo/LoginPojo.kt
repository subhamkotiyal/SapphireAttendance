package com.webgurus.attendanceportal.pojo

data class Loginpojo(
    val error : String,
    val `data`: Data,
    val second: String,
    val success: Success,

)

data class Data(
    val attendance_id: Int,
    val email: String,
    val id: Int,
    val name: String,
    val phone_number: String
)

data class Success(
    val token: String
)

