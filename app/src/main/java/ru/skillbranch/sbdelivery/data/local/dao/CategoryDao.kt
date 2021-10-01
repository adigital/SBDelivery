package ru.skillbranch.sbdelivery.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.skillbranch.sbdelivery.data.local.entities.Category

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(categories: List<Category>)

    @Query("SELECT * FROM Categories WHERE parent ISNULL ORDER BY \"order\"")
    fun getRootCategories(): List<Category>

    @Query("SELECT * FROM Categories WHERE parent = :parent ORDER BY \"order\"")
    fun getCategories(parent: String): LiveData<List<Category>>

    @Query("SELECT * FROM Categories WHERE name LIKE :query ORDER BY name")
    fun getSearchResultCategories(query: String): LiveData<List<Category>>
}