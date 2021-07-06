package com.webgurus.attendanceportal.pojo

data class UpdateLocationPojo(
    val location: Locationss
)

data class Locationss(
    val attendance_id: String,
    val created_at: String,
    val id: Int,
    val lat: String,
    val long: String,
    val user_id : String,
    val updated_at: String
)