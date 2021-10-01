package ru.skillbranch.sbdelivery.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable

@Entity(tableName = "Orders")
data class Order(
    @PrimaryKey
    var id: String,
    var total: Int,
    var address: String,
    var statusId: String,
    var status: String,
    var active: Boolean = false,
    var completed: Boolean = false,
    var createdAt: Long,
    var updatedAt: Long,
) : Serializable

data class OrderWithItems(
    @Embedded val order: Order,
    @Relation(
        parentColumn = "id",
        entityColumn = "itemId"
    )
    val orderItems: List<OrderItem>?,
)