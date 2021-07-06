package com.webgurus.attendanceportal.pojo

data class UserInforDatum(
    val `data`: UserInfoData,
    val success: String
)

data class UserInfoData(
    val total_attendance: Int,
    val total_orders: Int,
    val total_products: Int,
    val total_users: Int
)