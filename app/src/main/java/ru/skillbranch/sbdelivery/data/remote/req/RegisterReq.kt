package ru.skillbranch.sbdelivery.data.remote.req

data class RegisterReq(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
)