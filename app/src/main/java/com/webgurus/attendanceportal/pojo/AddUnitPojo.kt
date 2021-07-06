package com.webgurus.attendanceportal.pojo

data class AddUnitPojo(
    val `data`: DataAddUnit,
    val message: String,
    val status: Int
)


data class DataAddUnit(
    val id: Int,
    val unit: String
)