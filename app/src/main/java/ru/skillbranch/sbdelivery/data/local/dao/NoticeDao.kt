package ru.skillbranch.sbdelivery.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.skillbranch.sbdelivery.data.local.entities.Notice

@Dao
interface NoticeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotice(notice: Notice)

    @Query("SELECT * FROM Notices")
    fun getNotices(): LiveData<List<Notice>>

    @Query("SELECT count(isNew) FROM Notices WHERE isNew = 1")
    fun getNewNoticesCount(): LiveData<Int>

    @Query("UPDATE Notices SET isNew = 0  WHERE id =:id")
    suspend fun readNotice(id: String?)

    // test
    @Query("UPDATE Notices SET isNew = 1")
    suspend fun setAllNoticesNotRead()

    @Query("DELETE FROM Notices")
    suspend fun deleteAllNotices()
}