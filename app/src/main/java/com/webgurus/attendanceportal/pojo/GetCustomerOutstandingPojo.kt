package com.webgurus.attendanceportal.pojo


data class GetCustomerOutstandingPojo(
    val customer: List<Customer>,
    val status: Int
)

data class Customer(
    val bill_total: Int,
    val first_name: String,
    val id: Int,
    val middle_name: String,
    val orderTotal: Int,
    val organisation_name: String,
    val total_outstanding: Int
)