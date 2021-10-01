package ru.skillbranch.sbdelivery.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ReviewExpires")
data class ReviewExpires(
    @PrimaryKey
    val dishId: String, // ID блюда

    val expires: String, // Дата/время последнего обращения
)