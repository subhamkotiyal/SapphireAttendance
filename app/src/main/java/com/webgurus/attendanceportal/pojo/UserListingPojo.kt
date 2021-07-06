package com.webgurus.attendanceportal.pojo

import android.os.Parcel
import android.os.Parcelable

data class UserListingPojo(
    val `data`: List<DataUser>,
    val message: String,
    val status: Int
)
data class DataUser(
    val address: String,
    val city: String,
    val state: String,
    val dob: String,
    val email: String,
    val id: Int,
    val image: Any,
    val manager_id: String,
    val manager_name: String,
    val manager_role: String,
    val name: String,
    val phone_number: String,
    val role_id: Int,
    val username: String
)