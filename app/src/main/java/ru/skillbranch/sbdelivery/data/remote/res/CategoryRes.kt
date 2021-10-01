package ru.skillbranch.sbdelivery.data.remote.res

data class CategoryRes(
    val categoryId: String, // ID категории
    val name: String, // Название категории
    val order: Int = 0, // Номер по порядку
    val icon: String? = null, // Ссылка на изображение
    val parent: String? = null, // ID родительской категории, опционально (если есть, то это подкатегория)
    val active: Boolean = false, // Доступно ли блюдо (нет - удалить из БД)
    val createdAt: Long = 0, // Дата создания (мс)
    val updatedAt: Long = 0, // Дата обновления (мс)
)