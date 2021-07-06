package com.webgurus.attendanceportal.pojo

data class SettingPojo(
    val `data`: SettingData,
    val message: String,
    val status: Int
)

data class SettingData(
    val created_at: String,
    val days: String,
    val id: Int,
    val updated_at: String
)