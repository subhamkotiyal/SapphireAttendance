package com.webgurus.attendanceportal.pojo

data class OrderLabelPojo(
    val `data`: OrderLabelData,
    val status: Int
)

data class OrderLabelData(
    val weekLabel: List<String>,
    val weekTotal: List<Int>
)