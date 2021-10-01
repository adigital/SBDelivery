package ru.skillbranch.sbdelivery.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.skillbranch.sbdelivery.data.local.entities.Search

@Dao
interface SearchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchQuery(search: Search)

    @Query("DELETE FROM Search WHERE id NOT IN (SELECT id FROM Search ORDER BY id DESC LIMIT 5)")
    fun storeOnly5LastRows()

    @Query("SELECT * FROM Search ORDER BY id DESC")
    fun getSearchQueries(): LiveData<List<Search>>

    @Transaction
    suspend fun updateSearchQuery(Search: Search) {
        deleteSearchQuery(Search.name)
        insertSearchQuery(Search)
        storeOnly5LastRows()
    }

    @Query("DELETE FROM Search WHERE name = :name")
    fun deleteSearchQuery(name: String)
}