package ru.skillbranch.sbdelivery.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.skillbranch.sbdelivery.data.local.DbManager
import ru.skillbranch.sbdelivery.data.local.PrefManager
import ru.skillbranch.sbdelivery.data.local.entities.Review
import ru.skillbranch.sbdelivery.data.local.entities.ReviewExpires
import ru.skillbranch.sbdelivery.data.remote.NetworkManager
import ru.skillbranch.sbdelivery.data.remote.req.ReviewReq
import ru.skillbranch.sbdelivery.data.remote.res.ReviewsRes
import ru.skillbranch.sbdelivery.extensions.notifyMainShort

object ReviewsRepository {

    private var reviewDao = DbManager.db.reviewDao()
    private var reviewExpiresDao = DbManager.db.reviewExpiresDao()
    private val network = NetworkManager.api
    private val preferences = PrefManager

    suspend fun insertReviewsDao(reviews: List<Review>) {
        reviewDao.insertReview(reviews)
    }

    fun getReviews(dishId: String) = reviewDao.getReviews(dishId)

    suspend fun getReviewsNet(dishId: String): MutableList<ReviewsRes> {
        val reviews = mutableListOf<ReviewsRes>()
        var offset = 0
        var expires: String? = null

        try {
            val ifModifiedSince = reviewExpiresDao.getReviewExpires(dishId)

            while (true) {
                val res = if (ifModifiedSince.isNullOrBlank()) {
                    network.getReviews(dishId, offset * 10, 10)
                } else {
                    network.getReviews(dishId, offset * 10, 10, ifModifiedSince)
                }
                if (res.isSuccessful) {
                    offset++
                    reviews.addAll(res.body()!!)

                    expires = res.headers()["Expires"]
                } else break
            }

            expires?.let { reviewExpiresDao.insertReviewExpires(ReviewExpires(dishId, it)) }
        } catch (e: Throwable) {
            notifyMainShort(e.localizedMessage)
        }

        return reviews
    }

    fun addReviewNet(dishId: String, rating: Float, text: String): LiveData<Int> {
        val result: MutableLiveData<Int> = MutableLiveData(0)

        if (rating != 0f) {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    network.addReview(
                        dishId,
                        token = preferences.accessToken,
                        ReviewReq(rating, text)
                    )

                    result.postValue(1)
                } catch (e: Throwable) {
                    result.postValue(2)
                }
            }
        }

        return result
    }
}