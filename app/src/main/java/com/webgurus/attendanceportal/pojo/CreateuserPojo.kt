package com.webgurus.attendanceportal.pojo

data class CreateuserPojo(
    val `data`: Dataers,
    val message: String,
    val status: Int
)

data class Dataers(
    val city: String,
    val state: String,
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