package com.webgurus.attendanceportal.pojo



data class TopPerformancePojo(
    val `data`: List<data_performance>,
    val data_payment: List<DataPayment>,
    val message: String,
    val status: Int
)

data class data_performance(
    val total_price: Int,
    val user: String
)

data class DataPayment(
    val count: Int,
    val user: String
)
