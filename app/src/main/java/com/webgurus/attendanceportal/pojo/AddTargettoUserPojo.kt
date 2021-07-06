package com.webgurus.attendanceportal.pojo

data class AddTargettoUserPojo(
    val `data`: TargetData,
    val message: String,
    val status: Int
)

data class TargetData(
    val created_at: String,
    val created_by_id: Int,
    val id: Int,
    val target: String,
    val type: String,
    val updated_at: String,
    val user_id: String
)