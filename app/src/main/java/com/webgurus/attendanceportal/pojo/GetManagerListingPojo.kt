package com.webgurus.attendanceportal.pojo

data class GetManagerListingPojo(
    val `data`: List<Datasses>,
    val message: String,
    val status: Int
)

data class Datasses(
    val id: Int,
    val name: String
)