package com.webgurus.attendanceportal.pojo

data class UnitListPojo(
    val `data`: List<DataList>,
    val message: String,
    val status: Int
)


data class DataList(
    val id: Int,
    val unit: String
)