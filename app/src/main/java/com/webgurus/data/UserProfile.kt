package com.webgurus.data


data class UserProfile(
    val success: Success
)

data class Success(
    val api_token: Any,
    val city: Any,
    val created_at: String,
    val dob: Any,
    val email: String,
    val email_verified_at: Any,
    val id: Int,
    val name: String,
    val phone_number: String,
    val role_id: Int,
    val updated_at: String,
    val username: Any
)