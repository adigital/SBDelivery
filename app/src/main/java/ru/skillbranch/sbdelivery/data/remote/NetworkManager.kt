package ru.skillbranch.sbdelivery.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.skillbranch.sbdelivery.AppConfig.BASE_URL
import ru.skillbranch.sbdelivery.data.JsonConverter.moshi
import ru.skillbranch.sbdelivery.data.remote.interceptors.ErrorStatusInterceptor
import ru.skillbranch.sbdelivery.data.remote.interceptors.NetworkStatusInterceptor
import ru.skillbranch.sbdelivery.data.remote.interceptors.TokenAuthenticator
import java.util.concurrent.TimeUnit

object NetworkManager {
    val api: RestService by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        //client
        val client = OkHttpClient().newBuilder()
            .readTimeout(2, TimeUnit.SECONDS)   // socket timeout (GET)
            .writeTimeout(3, TimeUnit.SECONDS)  // socket timeout (POST, PUT, etc.)
            .authenticator(TokenAuthenticator())        // refresh token if response code == 401
            .addInterceptor(NetworkStatusInterceptor()) // intercept network status
            .addInterceptor(logging)                    // log requests/results
            .addInterceptor(ErrorStatusInterceptor())   // intercept network errors
            .build()

        //retrofit
        val retrofit = Retrofit.Builder()
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()

        retrofit.create(RestService::class.java)
    }
}