package com.webgurus.attendanceportal.pojo

data class AddCategoryPojo(
    val `data`: CategoryDatas,
    val message: String,
    val status: Int
)

data class CategoryDatas(
    val id: Int,
    val name: String
)