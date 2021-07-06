package com.webgurus.attendanceportal.pojo


data class AttendanceReportPojo(
    val `data`: AttenReportData,
    val status: Int
)

data class AttenReportData(
    val weekLabel: List<String>,
    val weekTotal: List<Int>
)