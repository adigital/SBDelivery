package ru.skillbranch.sbdelivery.data.remote.res

data class ReviewsRes(
    // ID блюда
    val dishId: String,

    // Имя автора
    val author: String,

    // Дата написания отзыва (ISO 8601)
    val date: String,

    // Порядок
    val order: Long = 0,

    // Оценка
    val rating: Float = 0f,

    // Текст отзыва, опционально
    val text: String,

    // Доступно ли блюдо (нет - удалить из БД)
    val active: Boolean = false,

    // Дата создания (мс)
    val createdAt: Long = 0,

    // Дата обновления (мс)
    val updatedAt: Long = 0,
)