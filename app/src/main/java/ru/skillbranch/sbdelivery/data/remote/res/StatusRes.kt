package ru.skillbranch.sbdelivery.data.remote.res

data class StatusRes(
    val id: String,
    val name: String,
    val cancelable: Boolean = true,
    val active: Boolean = true,
    val createdAt: Long,
    val updatedAt: Long,
)