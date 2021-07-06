package com.webgurus.attendanceportal.pojo

data class RoleLisitngPojo(
    val `data`: List<DataRole>,
    val message: String,
    val status: Int
)

data class DataRole(
    val id: Int,
    val role: String
)