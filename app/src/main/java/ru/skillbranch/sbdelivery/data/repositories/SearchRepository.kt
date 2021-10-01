package ru.skillbranch.sbdelivery.data.repositories

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.skillbranch.sbdelivery.data.local.DbManager
import ru.skillbranch.sbdelivery.data.local.entities.Search

object SearchRepository {

    private var searchDao = DbManager.db.searchDao()


    fun updateSearchQueryDao(name: String) {
        GlobalScope.launch(Dispatchers.IO) {
            searchDao.updateSearchQuery(Search(name = name))
        }
    }

    fun getSearchQueriesDao(): LiveData<List<Search>> = searchDao.getSearchQueries()

    fun deleteSearchQueryDao(name: String) {
        GlobalScope.launch(Dispatchers.IO) {
            searchDao.deleteSearchQuery(name)
        }
    }

}