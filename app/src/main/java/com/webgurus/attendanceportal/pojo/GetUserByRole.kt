package com.webgurus.attendanceportal.pojo

import android.os.Bundle
import com.webgurus.attendanceportal.ui.base.BaseFragment

data class GetUserByRole(
    val `data`: List<UserDatam>,
    val success: String
)

data class UserDatam(
    val email: String,
    val id: Int,
    val name: String,
    val role_id : Int
)