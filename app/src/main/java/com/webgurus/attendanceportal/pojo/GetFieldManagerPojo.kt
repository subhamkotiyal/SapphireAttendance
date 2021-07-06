package com.webgurus.attendanceportal.pojo

data class GetFieldManagerPojo(
    val `data`: List<FieldManagerData>,
    val message: String,
    val status: Int
)

data class FieldManagerData(
    val id: Int,
    val name: String
)