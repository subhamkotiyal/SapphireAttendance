package com.webgurus.attendanceportal.pojo

data class UserCurrentLocationPojo(
    val `data`: List<UserLocationData>,
    val success: String
)

data class UserLocationData(
    val address: String,
    val battery: Int,
    val gps: String,
    val latitude: String,
    val longitude: String,
    val name: String,
    val time: String
)