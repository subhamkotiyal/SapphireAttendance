package com.webgurus.attendanceportal.pojo

data class GpaStatusPojo(
    val `data`: GpsData,
    val message: String,
    val status: Int
)

data class GpsData(
    val attendance_id: Any,
    val battery: Any,
    val created_at: String,
    val gps_status: String,
    val id: Int,
    val lat: String,
    val long: String,
    val updated_at: String,
    val user_id: Int
)