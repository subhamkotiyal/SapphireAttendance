package com.webgurus.attendanceportal.pojo

data class GetRolesPojo(
    val `data`: ArrayList<DataRoleList>,
    val message: String,
    val status: Int
)

data class DataRoleList(
    val id: Int,
    val role: String
)