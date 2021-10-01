package ru.skillbranch.sbdelivery.data.remote.res

data class DishRes(
    val id: String, // ID блюда
    val name: String, // Название блюда
    val description: String = "", // Описание блюда
    val image: String, // Ссылка на изображение
    val oldPrice: Int? = null, // Старая цена, опционально
    val price: Int = 0, // Текущая цена
    val rating: Float = 0f, // Оценка пользователей
    val commentsCount: Int? = 0, // Количество комментариев
    val likes: Int = 0, // Количество лайков
    val category: String, // ID категории, к которой относится блюдо
    val active: Boolean = false, // Доступно ли блюдо (нет - удалить из БД)
    val createdAt: Long = 0, // Дата создания (мс)
    val updatedAt: Long = 0, // Дата обновления (мс)
)