package ru.skillbranch.sbdelivery.data.remote.res

data class ProfileRes(
    // ID пользователя
    val id: String,

    // Имя
    val firstName: String,

    // Фамилия
    val lastName: String,

    // Email
    val email: String,
)