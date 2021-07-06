package com.webgurus.attendanceportal.pojo

data class UpdateRolePojo(
    val `data`: UpdateRoleData,
    val message: String,
    val status: Int
)

data class UpdateRoleData(
    val id: Int,
    val role: String
)