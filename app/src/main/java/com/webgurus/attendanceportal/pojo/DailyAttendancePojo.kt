package com.webgurus.attendanceportal.pojo

data class DailyAttendancePojo(
    val location: List<Locationesss>
)

data class Locationesss(
    val attendance_id: Int,
    val battery: String,
    val created_at: String,
    val id: Int,
    val lat: String,
    val long: String,
    val updated_at: String
)