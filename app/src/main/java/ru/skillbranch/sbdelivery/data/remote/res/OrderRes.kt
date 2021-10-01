package ru.skillbranch.sbdelivery.data.remote.res

data class OrderRes(
    val id: String,
    val total: Int,
    val address: String,
    val statusId: String,
    val active: Boolean = false,
    val completed: Boolean = false,
    val createdAt: Long,
    val updatedAt: Long,
    val items: MutableList<OrderItemRes> = mutableListOf(),
)

data class OrderItemRes(
    val name: String,
    val image: String,
    val amount: Int,
    val price: Int,
    val dishId: String,
)