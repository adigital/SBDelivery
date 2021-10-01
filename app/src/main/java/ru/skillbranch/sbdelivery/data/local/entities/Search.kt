package ru.skillbranch.sbdelivery.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Search")
data class Search(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
)