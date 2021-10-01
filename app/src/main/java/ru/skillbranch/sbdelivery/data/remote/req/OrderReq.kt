package ru.skillbranch.sbdelivery.data.remote.req

data class OrderReq(
    val address: String,
    val entrance: Int = 0,
    val floor: Int = 0,
    val apartment: String,
    val intercom: String,
    val comment: String,
)