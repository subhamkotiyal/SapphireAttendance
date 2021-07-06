package com.webgurus.attendanceportal.pojo

data class AddPermissionPojo(
    val `data`: PermissionDatum,
    val message: String,
    val status: Int
)

data class PermissionDatum(
    val id: Int,
    val permission: String,
    val role: String
)