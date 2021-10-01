package ru.skillbranch.sbdelivery.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "Cart")
data class Cart(
    @PrimaryKey
    val cartId: Long,
    val promocode: String? = null, // Промокод, опционально
    val promotext: String? = null, // Текст промокода, опционально
    val total: Int = 0, // Общая цена
)

data class CartWithItems(
    @Embedded val cart: Cart?,
    @Relation(
        parentColumn = "cartId",
        entityColumn = "itemId"
    )
    val cartItems: List<CartItem>?,
)