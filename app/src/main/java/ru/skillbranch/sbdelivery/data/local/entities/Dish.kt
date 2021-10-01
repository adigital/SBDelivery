package ru.skillbranch.sbdelivery.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Dishes")
data class Dish(
    @PrimaryKey
    val id: String, // ID блюда
    val name: String? = null, // Название блюда
    val description: String? = null, // Описание блюда
    val image: String? = null, // Ссылка на изображение
    val oldPrice: Int? = null, // Старая цена, опционально
    val price: Int = 0, // Текущая цена
    val rating: Float = 0f, // Оценка пользователей
    val commentsCount: Int? = 0, // Количество комментариев
    val likes: Int = 0, // Количество лайков
    val category: String? = null, // ID категории, к которой относится блюдо
    val active: Boolean = false, // Доступно ли блюдо (нет - удалить из БД)
    val favorite: Boolean = false, // Признак "избранного" блюда
    val recommended: Boolean = false, // Признак "рекомендуемого" блюда
    val createdAt: Long = 0, // Дата создания (мс)
    val updatedAt: Long = 0, // Дата обновления (мс)
)