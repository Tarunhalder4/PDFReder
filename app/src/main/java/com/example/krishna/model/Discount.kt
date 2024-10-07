package com.example.krishna.model

data class Discount(
    val discount_id: String,
    val min_qty: String,
    val percentage: String,
    val post_date: String,
    val price_range_from: String,
    val price_range_to: String,
    val service_id: String,
    val shop_id: String,
    val status: String
)