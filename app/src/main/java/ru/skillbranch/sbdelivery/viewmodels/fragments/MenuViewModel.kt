package ru.skillbranch.sbdelivery.viewmodels.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.skillbranch.sbdelivery.App.Companion.context
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.local.entities.Category
import ru.skillbranch.sbdelivery.data.local.entities.Dish
import ru.skillbranch.sbdelivery.data.local.entities.Search
import ru.skillbranch.sbdelivery.data.repositories.CategoriesRepository
import ru.skillbranch.sbdelivery.data.repositories.DishesRepository
import ru.skillbranch.sbdelivery.data.repositories.SearchRepository
import ru.skillbranch.sbdelivery.viewmodels.BaseViewModel

class MenuViewModel : BaseViewModel() {

    private val dishesRepository = DishesRepository
    private val categoriesRepository = CategoriesRepository
    private val searchRepositories = SearchRepository

    private var dishes: LiveData<List<Dish>>? = null
    private var category: LiveData<List<Category>>? = null
    private val mediatorCategories = MediatorLiveData<List<Category>>()
    private val mediatorDishes = MediatorLiveData<List<Dish>>()


    fun getRootCategories(): LiveData<List<Category>> {
        val result = MutableLiveData<List<Category>>()

        viewModelScope.launch(Dispatchers.IO) {
            val categories = mutableListOf<Category>()

            if (dishesRepository.getActionDishesCountDao() > 0) {
                categories.add(Category("", context.resources.getString(R.string.menu_action)))
            }

            categories.addAll(categoriesRepository.getRootCategoriesDao())

            result.postValue(categories)
        }

        return result
    }

    fun updateSearchQuery(name: String) {
        searchRepositories.updateSearchQueryDao(name)
    }

    fun getSearchQueries(): LiveData<List<Search>> = searchRepositories.getSearchQueriesDao()

    fun deleteSearchQuery(name: String) {
        searchRepositories.deleteSearchQueryDao(name)
    }

    fun getSearchResultCategories(query: String): LiveData<List<Category>> {
        if (category != null) {
            mediatorCategories.removeSource(category!!)
        }
        category = categoriesRepository.getSearchResultCategoriesDao(query)
        mediatorCategories.addSource(category!!) {
            mediatorCategories.value = it
        }

        return mediatorCategories
    }

    fun getSearchResultDishes(query: String): LiveData<List<Dish>> {
        if (dishes != null) {
            mediatorDishes.removeSource(dishes!!)
        }
        dishes = dishesRepository.getSearchResultDishesDao(query)
        mediatorDishes.addSource(dishes!!) {
            mediatorDishes.value = it
        }

        return mediatorDishes
    }
}