package ru.skillbranch.sbdelivery.viewmodels

import androidx.lifecycle.ViewModel
import ru.skillbranch.sbdelivery.data.repositories.ReviewsRepository

class ReviewDialogViewModel : ViewModel() {

    private val reviewsRepository = ReviewsRepository

    fun addReviewNet(dishId: String, rating: Float, text: String) =
        reviewsRepository.addReviewNet(dishId, rating, text)
}