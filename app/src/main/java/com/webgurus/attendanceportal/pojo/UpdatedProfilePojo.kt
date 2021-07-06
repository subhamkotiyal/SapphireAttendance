package com.webgurus.attendanceportal.pojo

data class UpdatedProfilePojo(
    val message: String,
    val success: SuccessUpdateProfile
)

data class SuccessUpdateProfile(
    val api_token: Any,
    val city: String,
    val created_at: String,
    val dob: String,
    val email: String,
    val email_verified_at: Any,
    val id: Int,
    val image: String,
    val name: String,
    val phone_number: String,
    val role_id: Int,
    val updated_at: String,
    val username: String

)
