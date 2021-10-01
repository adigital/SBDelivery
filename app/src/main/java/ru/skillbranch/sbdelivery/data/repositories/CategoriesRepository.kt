package ru.skillbranch.sbdelivery.data.repositories

import androidx.lifecycle.LiveData
import ru.skillbranch.sbdelivery.data.local.DbManager
import ru.skillbranch.sbdelivery.data.local.PrefManager
import ru.skillbranch.sbdelivery.data.local.entities.Category
import ru.skillbranch.sbdelivery.data.remote.NetworkManager
import ru.skillbranch.sbdelivery.data.remote.res.CategoryRes
import ru.skillbranch.sbdelivery.extensions.notifyMainShort

object CategoriesRepository {

    private var categoriesDao = DbManager.db.categoriesDao()
    private val network = NetworkManager.api
    private val preferences = PrefManager

    suspend fun insertCategoriesDao(categories: List<Category>) {
        categoriesDao.insertCategory(categories)
    }

    fun getRootCategoriesDao() = categoriesDao.getRootCategories()

    fun getCategoriesDao(parent: String): LiveData<List<Category>> =
        categoriesDao.getCategories(parent)

    fun getSearchResultCategoriesDao(query: String) =
        categoriesDao.getSearchResultCategories("%$query%")

    suspend fun getCategoriesNet(): List<CategoryRes> {
        val categories = mutableListOf<CategoryRes>()
        var offset = 0
        var expires: String? = null

        try {
            val ifModifiedSince = preferences.categoriesExpires

            while (true) {
                val res = network.getCategories(offset * 10, 10, ifModifiedSince)
                if (res.isSuccessful) {
                    offset++
                    categories.addAll(res.body()!!)

                    expires = res.headers()["Expires"]
                } else break
            }

            expires?.let { preferences.categoriesExpires = it }
        } catch (e: Throwable) {
            notifyMainShort(e.localizedMessage)
        }

        return categories
    }
}