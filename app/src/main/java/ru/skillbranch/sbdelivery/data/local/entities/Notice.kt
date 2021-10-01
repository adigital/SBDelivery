package ru.skillbranch.sbdelivery.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Notices")
data class Notice(
    @PrimaryKey
    val id: String,
    val isNew: Boolean,
    val header: String,
    val message: String,
)