package com.example.krishna.model

data class Service(
    val discount: List<Discount>,
    val extra_services: List<ExtraService>,
    val image: String,
    val item_id: String,
    val item_name: String,
    val pickup_delivery: List<Any>,
    val price: String,
    val s_no: String,
    val service_category: String,
    val service_id: String,
    val service_name: String,
    val service_subcategory: String,
    val shop_id: String,
    val subcategory_image: String
)