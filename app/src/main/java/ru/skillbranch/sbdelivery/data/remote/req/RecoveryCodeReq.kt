package ru.skillbranch.sbdelivery.data.remote.req

data class RecoveryCodeReq(
    val email: String,
    val code: String,
)