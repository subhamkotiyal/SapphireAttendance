package com.webgurus.attendanceportal.pojo

data class GetCategoryListingPojo(
    val `data`: List<CategoryData>,
    val message: String,
    val status: Int
)

data class CategoryData(
    val id: Int,
    val name: String
)