package com.example.krishna.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "service")
data class ServiceEntity (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "service_name")
    val serviceName: String,

    @ColumnInfo(name = "service_price")
    val servicePrice: String,

    @ColumnInfo(name = "quantity")
    val quantity: Int,

    @ColumnInfo(name = "discount")
    val discountPercentage:String = ""
)