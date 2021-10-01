package ru.skillbranch.sbdelivery.viewmodels.fragments

import ru.skillbranch.sbdelivery.data.repositories.DishesRepository
import ru.skillbranch.sbdelivery.viewmodels.BaseViewModel

class FavoritesViewModel : BaseViewModel() {

    private val dishesRepository = DishesRepository

    fun getFavoriteDishes() = dishesRepository.getFavoriteDishesDao()
}