package ru.skillbranch.sbdelivery.data.remote.req

data class ReviewReq(
    // Оценка
    val rating: Float = 0f,

    // Текст отзыва, опционально
    val text: String,
)