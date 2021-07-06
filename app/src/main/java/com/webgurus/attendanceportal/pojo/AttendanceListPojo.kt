package com.webgurus.attendanceportal.pojo

data class AttendanceListPojo(
    val success: List<SuccesAttendances>
)

data class SuccesAttendances(
    val comment: Any,
    val created_at: String,
    val id: Int,
    val in_time: String,
    val out_time: Any,
    val server_in_time: String,
    val server_out_time: Any,
    val today_date: String,
    val updated_at: String,
    val user_id: Int,
    val distance : String

)