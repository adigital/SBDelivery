package ru.skillbranch.sbdelivery.data.local.entities

import androidx.room.Entity

@Entity(tableName = "OrdersItems", primaryKeys = ["itemId", "dishId"])
data class OrderItem(
    var itemId: String,
    var name: String,
    var image: String,
    var amount: Int,
    var price: Int,
    var dishId: String,
)