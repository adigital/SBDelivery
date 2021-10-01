package ru.skillbranch.sbdelivery.data.remote.res

data class CartRes(
    val promocode: String = "",
    val promotext: String = "",
    val total: Int = 0,
    val items: MutableList<CartItemRes> = mutableListOf(),
)

data class CartItemRes(
    val id: String, // ID блюда
    val amount: Int = 0, // Количество
    val price: Int = 0, // Стоимость (с учетом количества)
)