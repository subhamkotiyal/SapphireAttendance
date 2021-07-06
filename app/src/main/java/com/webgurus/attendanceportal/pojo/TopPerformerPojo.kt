package com.webgurus.attendanceportal.pojo

data class TopPerformerPojo(
    val ordervsdelivered: List<Ordervsdelivered>,
    val status: Int
)

data class Ordervsdelivered(
    val countTot: Int,
    val created_by_id: Int,
    val dispatchedCount: String,
    val name: String,
    val receivedCount: String
)