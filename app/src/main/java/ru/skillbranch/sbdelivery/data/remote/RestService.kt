package ru.skillbranch.sbdelivery.data.remote

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import ru.skillbranch.sbdelivery.data.remote.req.*
import ru.skillbranch.sbdelivery.data.remote.res.*

interface RestService {
    // Auth
    @POST("auth/login")
    suspend fun login(@Body loginReq: LoginReq): AuthRes

    @POST("auth/register")
    suspend fun register(@Body registerReq: RegisterReq): AuthRes

    @POST("auth/refresh")
    fun refreshAccessToken(
        @Body refreshToken: RefreshReq,
    ): Call<RefreshRes>

    @POST("auth/recovery/email")
    suspend fun recoveryEmail(@Body recoveryEmailReq: RecoveryEmailReq)

    @POST("auth/recovery/code")
    suspend fun recoveryCode(@Body recoveryCodeReq: RecoveryCodeReq)

    @POST("auth/recovery/password")
    suspend fun recoveryPassword(@Body recoveryPasswordReq: RecoveryPasswordReq)

    // Profile
    @GET("/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String,
    ): ProfileRes

    @PUT("/profile")
    suspend fun editProfile(
        @Body editProfile: EditProfileReq,
        @Header("Authorization") token: String,
    ): ProfileRes

    @PUT("/profile/password")
    suspend fun changePassword(
        @Body changePassword: ChangePasswordReq,
        @Header("Authorization") token: String,
    )

    // Dishes
    @GET("/dishes")
    suspend fun getDishes(
        @Query("offset") offset: Int, @Query("limit") limit: Int = 10,
        @Header("If-Modified-Since") ifModifiedSince: String = "Wed, 21 Oct 2015 07:28:00 GMT",
    ): Response<List<DishRes>>

    @GET("/main/recommend")
    suspend fun getRecommended(
        @Header("If-Modified-Since") ifModifiedSince: String = "Wed, 21 Oct 2015 07:28:00 GMT",
    ): Response<List<String>>

    @GET("/categories")
    suspend fun getCategories(
        @Query("offset") offset: Int, @Query("limit") limit: Int = 10,
        @Header("If-Modified-Since") ifModifiedSince: String = "Wed, 21 Oct 2015 07:28:00 GMT",
    ): Response<List<CategoryRes>>

    @GET("/favorite")
    suspend fun getFavorite(
        @Query("offset") offset: Int, @Query("limit") limit: Int = 10,
        @Header("If-Modified-Since") ifModifiedSince: String = "Wed, 21 Oct 2015 07:28:00 GMT",
        @Header("Authorization") token: String,
    ): Response<List<FavoriteDishRes>>

    @PUT("/favorite/change")
    suspend fun changeFavorite(
        @Body favoriteDishChange: List<FavoriteChangeDishReq>,
        @Header("Authorization") token: String,
    )

    // Cart
    @GET("/cart")
    suspend fun getCart(
        @Header("Authorization") token: String,
    ): CartRes

    @PUT("/cart")
    suspend fun updateCart(
        @Body cart: CartRes,
        @Header("Authorization") token: String,
    ): CartRes

    // Reviews
    @GET("/reviews/{dishId}")
    suspend fun getReviews(
        @Path("dishId") dishId: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = 10,
        @Header("If-Modified-Since") ifModifiedSince: String = "Wed, 21 Oct 2015 07:28:00 GMT",
    ): Response<List<ReviewsRes>>

    @POST("/reviews/{dishId}")
    suspend fun addReview(
        @Path("dishId") dishId: String,
        @Header("Authorization") token: String,
        @Body review: ReviewReq,
    )

    // Address
    @POST("/address/input")
    suspend fun checkInput(
        @Body address: AddressReq,
    ): AddressRes

    @POST("/address/coordinates")
    suspend fun checkCoordinates(
        @Body address: CoordinatesReq,
    ): AddressRes

    // Orders
    @POST("/orders/new")
    suspend fun createOrder(
        @Header("Authorization") token: String,
        @Body order: OrderReq,
    ): OrderRes

    @GET("/orders")
    suspend fun getOrders(
        @Header("If-Modified-Since") ifModifiedSince: String = "Wed, 21 Oct 2015 07:28:00 GMT",
        @Header("Authorization") token: String,
    ): Response<List<OrderRes>>

    @GET("/orders/statuses")
    suspend fun getStatuses(
        @Header("If-Modified-Since") ifModifiedSince: String = "Wed, 21 Oct 2015 07:28:00 GMT",
    ): Response<List<StatusRes>>

    @PUT("/orders/cancel")
    suspend fun cancelOrder(
        @Header("Authorization") token: String,
        @Body orderCancelReq: OrderCancelReq,
    ): OrderRes
}