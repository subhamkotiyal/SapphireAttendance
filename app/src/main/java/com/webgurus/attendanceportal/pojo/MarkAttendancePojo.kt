package com.webgurus.attendanceportal.pojo

data class MarkAttendancePojo(
    val location: Location,
    val person_in: Int,
    val success: SuccessAttend
)

data class Location(
    val attendance_id: Int,
    val battery: String,
    val created_at: String,
    val id: Int,
    val lat: String,
    val long: String,
    val updated_at: String
)

data class SuccessAttend(
    val created_at: String,
    val id: Int,
    val in_time: String,
    val server_in_time: String,
    val today_date: String,
    val updated_at: String,
    val user_id: Int
)