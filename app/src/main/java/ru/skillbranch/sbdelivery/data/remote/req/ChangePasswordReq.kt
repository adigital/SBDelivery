package ru.skillbranch.sbdelivery.data.remote.req

data class ChangePasswordReq(
    val oldPassword: String,
    val newPassword: String,
)
