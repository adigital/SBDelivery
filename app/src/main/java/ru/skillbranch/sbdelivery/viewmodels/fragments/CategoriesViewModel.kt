package ru.skillbranch.sbdelivery.viewmodels.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skillbranch.sbdelivery.data.local.entities.Category
import ru.skillbranch.sbdelivery.data.repositories.CategoriesRepository

class CategoriesViewModel : ViewModel() {
    private val categoriesRepository = CategoriesRepository

    class Sort(
        var itemNumber: Int = 1,
        var isAscending: Boolean = true,
    )

    val sort = MutableLiveData(Sort(1, true))

    fun getSortMode() = sort.value!!

    fun setSortMode(
        itemNumber: Int = 1,
        isAscending: Boolean = true,
    ) {
        sort.postValue(Sort(itemNumber, isAscending))
    }

    fun getCategories(parent: String): LiveData<List<Category>> =
        categoriesRepository.getCategoriesDao(parent)
}
