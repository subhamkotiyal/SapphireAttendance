package com.webgurus.attendanceportal.pojo
data class TestPojo(
    val post_data: PostData,
    val server_message: String,
    val statusCode: Int,
    val user_data: UserData
)

data class PostData(
    val api_key: String,
    val device_type: String,
    val id: String,
    val mobile_number: String,
    val update_email: Any,
    val update_first_name: String,
    val update_last_name: String,
    val update_mobile_number: String
)

data class UserData(
    val account_status: Int,
    val email: String,
    val first_name: String,
    val id: Int,
    val invite_code: String,
    val last_name: String,
    val mobile_number: String,
    val profile_image: String
)