package ru.skillbranch.sbdelivery.data.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import ru.skillbranch.sbdelivery.data.remote.err.ApiError

class ErrorStatusInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (response.isSuccessful) return response

        when (response.code) {
            304 -> return response
            400 -> throw ApiError.BadRequest(null)
            402 -> throw ApiError.Unauthorized(null)
            403 -> throw ApiError.Forbidden(null)
            404 -> throw ApiError.NotFound(null)
            500 -> throw ApiError.InternalServerError(null)
            else -> throw ApiError.UnknownError(null)
        }
    }
}