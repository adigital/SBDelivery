package ru.skillbranch.sbdelivery.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.skillbranch.sbdelivery.data.local.entities.ReviewExpires

@Dao
interface ReviewExpiresDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviewExpires(reviews: ReviewExpires)

    @Query("SELECT expires FROM ReviewExpires WHERE dishId = :dishId")
    fun getReviewExpires(dishId: String): String?
}