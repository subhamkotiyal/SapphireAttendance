package com.webgurus.attendanceportal.pojo

data class DispatchReportPojo(
    val dispatch_order: List<DispatchOrder>,
    val message: String,
    val ready_for_dispatch: List<ReadyForDispatch>,
    val status: Int,
    val total_order: Int,
    val total_order_approved: Int,
    val total_order_dispatched: Int,
    val total_order_ready_dispatched: Int
)

data class DispatchOrder(
    val payment_received:Int,
    val address: String,
    val id: Int,
    val last_transaction: String,
    val order_date: String,
    val organisation_name: String,
    val remarks: String,
    val total_price: String
)

data class ReadyForDispatch(
    val address: String,
    val id: Int,
    val last_transaction: String,
    val order_date: String,
    val organisation_name: String,
    val remarks: String,
    val total_price: String,
    val payment_received : Int
)