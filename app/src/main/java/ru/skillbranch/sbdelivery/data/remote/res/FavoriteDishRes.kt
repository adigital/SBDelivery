package ru.skillbranch.sbdelivery.data.remote.res

data class FavoriteDishRes(
    val dishId: String,
    val favorite: Boolean = false,
    val updatedAt: Long = 0,
)