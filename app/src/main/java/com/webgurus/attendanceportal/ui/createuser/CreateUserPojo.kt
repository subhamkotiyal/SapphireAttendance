package com.webgurus.attendanceportal.ui.createuser

data class CreateUserPojo(
    val `data`: Dataess,
    val message: String,
    val status: Int
)

data class Dataess(
    val city: String,
    val dob: String,
    val email: String,
    val id: Int,
    val image: Any,
    val manager_id: Int,
    val manager_name: String,
    val manager_role: String,
    val name: String,
    val phone_number: String,
    val role_id: String,
    val username: Any
)