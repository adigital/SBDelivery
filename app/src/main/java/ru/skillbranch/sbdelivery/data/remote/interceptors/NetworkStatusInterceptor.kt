package ru.skillbranch.sbdelivery.data.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import ru.skillbranch.sbdelivery.App.Companion.context
import ru.skillbranch.sbdelivery.data.remote.err.NoNetworkError
import ru.skillbranch.sbdelivery.extensions.isOnline

class NetworkStatusInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!context.isOnline()) throw NoNetworkError()

        return chain.proceed(chain.request())
    }
}