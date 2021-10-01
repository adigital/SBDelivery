package ru.skillbranch.sbdelivery.data.remote.req

data class FavoriteChangeDishReq(
    val dishId: String,
    val favorite: Boolean = false,
)