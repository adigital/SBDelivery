package ru.skillbranch.sbdelivery.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.skillbranch.sbdelivery.data.local.entities.Review

@Dao
interface ReviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(reviews: List<Review>)

    @Query("SELECT * FROM Reviews WHERE dishId = :dishId ORDER BY `order`")
    fun getReviews(dishId: String): LiveData<List<Review>>
}