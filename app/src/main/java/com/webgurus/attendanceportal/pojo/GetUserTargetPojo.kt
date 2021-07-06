package com.webgurus.attendanceportal.pojo

data class GetUserTargetPojo(
    val sale: Int,
    val sale_collect: Int,
    val status: Int,
    val target: Int,
    val user: User,
    val date: String
)

data class User(
    val alternate_phone_number: String,
    val api_token: Any,
    val city: String,
    val created_at: String,
    val device_token: String,
    val device_type: String,
    val dob: String,
    val email: String,
    val email_verified_at: Any,
    val id: Int,
    val image: Any,
    val is_online: Int,
    val name: String,
    val permissions: String,
    val phone_number: String,
    val role_id: Int,
    val role_name: String,
    val state: String,
    val state_id: Int,
    val updated_at: String,
    val username: Any
)