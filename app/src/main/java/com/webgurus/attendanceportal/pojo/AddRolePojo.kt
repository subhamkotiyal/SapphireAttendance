package com.webgurus.attendanceportal.pojo

data class AddRolePojo(
    val `data`: RoleData,
    val message: String,
    val status: Int
)

data class RoleData(
    val id: Int,
    val role: String
)