package ru.skillbranch.sbdelivery.data.remote.req

data class RecoveryPasswordReq(
    val email: String,
    val code: String,
    val password: String,
)