package com.example.krishna.model

data class Item(
    val service_id: String,
    val service_name: String,
    val services: List<Service>
)