package com.webgurus.attendanceportal.pojo

data class GetLatLongBydate(
    val location: List<Locationesess>
)

data class Locationesess(
    val attendance_id: Int,
    val battery: String,
    val created_at: String,
    val id: Int,
    val lat: String,
    val long: String,
    val updated_at: String
)