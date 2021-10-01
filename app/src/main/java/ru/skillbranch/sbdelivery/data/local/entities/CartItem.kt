package ru.skillbranch.sbdelivery.data.local.entities

import androidx.room.Entity

@Entity(tableName = "CartItems", primaryKeys = ["itemId", "id"])
data class CartItem(
    val itemId: Long,
    val id: String, // ID блюда
    val amount: Int, // Количество
    val price: Int, // Стоимость (с учетом количества)
)