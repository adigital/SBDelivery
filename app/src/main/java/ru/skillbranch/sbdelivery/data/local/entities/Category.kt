package ru.skillbranch.sbdelivery.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Categories")
data class Category(
    @PrimaryKey
    val categoryId: String, // ID категории
    val name: String, // Название категории
    val order: Int = 0, // Номер по порядку
    val icon: String? = null, // Ссылка на иконку, опционально
    val parent: String? = null, // ID родительской категории, опционально (если есть, то это подкатегория)
    val active: Boolean = false, // Доступна ли категория (нет - удалить из бд)
    val createdAt: Long = 0, // Дата создания (мс)
    val updatedAt: Long = 0, // Дата обновления (мс)
)