package com.webgurus.attendanceportal.pojo

data class ItemReportPojo(
    val hot_product: List<HotProduct>,
    val item_quantity: List<ItemQuantity>,
    val item_sale: List<ItemSale>,
    val status: Int
)

data class HotProduct(
    val product_name: String,
    val quantity_sold: String,
    val unit: String
)

data class ItemQuantity(
    val id: Int,
    val last_day_sale: Int,
    val product: String,
    val seven_day_sale: Int,
    val thirty_day_sale: Int,
    val variant: String
)

data class ItemSale(
    val id: String,
    val avg_rate: String,
    val last_day_sale: Int,
    val product: String,
    val seven_day_sale: Int,
    val thirty_day_sale: Int,
    val variant: String
)