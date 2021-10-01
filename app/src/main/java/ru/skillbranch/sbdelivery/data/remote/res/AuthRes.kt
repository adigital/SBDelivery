package ru.skillbranch.sbdelivery.data.remote.res

data class AuthRes(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val accessToken: String,
    val refreshToken: String,
)