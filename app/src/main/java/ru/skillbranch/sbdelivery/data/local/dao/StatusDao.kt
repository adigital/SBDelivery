package ru.skillbranch.sbdelivery.data.local.dao

import androidx.room.*
import ru.skillbranch.sbdelivery.data.local.entities.Status

@Dao
interface StatusDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatuses(orders: List<Status>)

    @Query("DELETE FROM Statuses")
    fun deleteStatuses()

    @Query("SELECT * FROM Statuses WHERE id =:id")
    fun getStatus(id: String): Status
}