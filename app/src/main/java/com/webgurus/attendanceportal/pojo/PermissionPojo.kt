package com.webgurus.attendanceportal.pojo

var isSelected = false
data class PermissionPojo(
    val `data`: List<PermissionData>,
    val message: String,
    val status: Int
)

data class PermissionData(
    val id: Int,
    val module: String,
    var isSelected:Boolean
)