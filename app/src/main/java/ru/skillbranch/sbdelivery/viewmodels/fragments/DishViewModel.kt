package ru.skillbranch.sbdelivery.viewmodels.fragments

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.skillbranch.sbdelivery.data.repositories.DishesRepository
import ru.skillbranch.sbdelivery.data.repositories.ReviewsRepository
import ru.skillbranch.sbdelivery.data.toReviewsList
import ru.skillbranch.sbdelivery.viewmodels.BaseViewModel

class DishViewModel : BaseViewModel() {

    private val dishesRepository = DishesRepository
    private val reviewsRepository = ReviewsRepository

    fun getDishDao(id: String) = dishesRepository.getDishDao(id)

    fun updateReviews(dishId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            reviewsRepository.insertReviewsDao(
                reviewsRepository.getReviewsNet(dishId).toReviewsList()
            )
        }
    }

    fun getReviews(dishId: String) = reviewsRepository.getReviews(dishId)
}