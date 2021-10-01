package ru.skillbranch.sbdelivery.data.remote.err

import java.io.IOException

sealed class ApiError(override val message: String) : IOException(message) {
    class BadRequest(message: String?) : ApiError(message ?: "Ошибка. Попробуйте позже.")
    class Unauthorized(message: String?) : ApiError(message ?: "Необходима авторизация")
    class Forbidden(message: String?) : ApiError(message ?: "Доступ запрещён")
    class NotFound(message: String?) : ApiError(message ?: "Запрашиваемая страница не найдена")
    class InternalServerError(message: String?) : ApiError(message ?: "Внутренняя ошибка сервера")
    class UnknownError(message: String?) : ApiError(message ?: "Ошибка. Попробуйте позже.")
}