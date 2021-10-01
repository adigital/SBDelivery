package ru.skillbranch.sbdelivery.data.models

data class CartItemModel(
    val name: String, //Название блюда
    val image: String, // Картинка
    val amount: Int, // Количество
    val price: Int, // Стоимость (с учетом количества)
    val id: String, // ID блюда
)
