package com.webgurus.attendanceportal.pojo

data class FiledManagersPojo(
    val `data`: List<FieldData>,
    val message: String,
    val status: Int
)

data class FieldData(
    val id: Int,
    val name: String
)