package ru.skillbranch.sbdelivery.viewmodels.fragments

import androidx.lifecycle.LiveData
import ru.skillbranch.sbdelivery.data.local.entities.Dish
import ru.skillbranch.sbdelivery.data.repositories.DishesRepository
import ru.skillbranch.sbdelivery.data.repositories.RootRepository
import ru.skillbranch.sbdelivery.viewmodels.BaseViewModel

class HomeViewModel : BaseViewModel() {

    private val repository = RootRepository
    private val dishesRepository = DishesRepository

    val isDataSync = repository.isDataSync

    fun getRecommendedDishes(): LiveData<List<Dish>> = dishesRepository.getRecommendedDishesDao()

    fun getBestDishes(): LiveData<List<Dish>> = dishesRepository.getBestDishesDao()

    fun getLikesDishes(): LiveData<List<Dish>> = dishesRepository.getLikesDishesDao()
}