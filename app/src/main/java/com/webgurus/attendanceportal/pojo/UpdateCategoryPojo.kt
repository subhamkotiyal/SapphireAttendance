package com.webgurus.attendanceportal.pojo

data class UpdateCategoryPojo(
    val `data`: UpdateCategoryData,
    val message: String,
    val status: Int
)

data class UpdateCategoryData(
    val id: Int,
    val name: String
)