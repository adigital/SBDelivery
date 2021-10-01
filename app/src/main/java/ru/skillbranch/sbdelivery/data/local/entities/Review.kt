package ru.skillbranch.sbdelivery.data.local.entities

import androidx.room.Entity

@Entity(tableName = "Reviews", primaryKeys = ["dishId", "author", "date"])
data class Review(
    val dishId: String, // ID блюда
    val author: String, // Имя автора
    val date: String, // Дата написания отзыва (ISO 8601)
    var order: Long = 0, // Порядок
    val rating: Float = 0f, // Оценка
    val text: String? = null, // Текст отзыва, опционально
    val active: Boolean = false, // Доступно ли блюдо (нет - удалить из БД)
    val createdAt: Long = 0, // Дата создания (мс)
    val updatedAt: Long = 0, // Дата обновления (мс)
)