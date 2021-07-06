package com.webgurus.attendanceportal.pojo

data class UpdateprofilePojo(
    val message: String,
    val success: Successes
)

data class Successes(
    val api_token: Any,
    val city: String,
    val created_at: String,
    val dob: String,
    val email: String,
    val email_verified_at: Any,
    val id: Int,
    val name: String,
    val phone_number: String,
    val role_id: Int,
    val updated_at: String,
    val username: String,
    val image: String
)

