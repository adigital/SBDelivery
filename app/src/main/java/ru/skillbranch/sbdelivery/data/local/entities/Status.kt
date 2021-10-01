package ru.skillbranch.sbdelivery.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Statuses")
data class Status(
    @PrimaryKey
    var id: String,
    var name: String,
    var cancelable: Boolean = true,
    var active: Boolean = true,
    var createdAt: Long,
    var updatedAt: Long,
)