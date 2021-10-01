package ru.skillbranch.sbdelivery.viewmodels.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import ru.skillbranch.sbdelivery.data.local.entities.Dish
import ru.skillbranch.sbdelivery.data.repositories.DishesRepository
import ru.skillbranch.sbdelivery.viewmodels.BaseViewModel

class CategoryViewModel : BaseViewModel() {

    private val dishesRepository = DishesRepository

    private var dishes: LiveData<List<Dish>>? = null
    private val mediator = MediatorLiveData<List<Dish>>()
    private var _category: String? = null


    fun setSortMode(
        itemNumber: Int = 1,
        isAscending: Boolean = true,
    ) {
        when (itemNumber) {
            1 -> {
                if (isAscending) {
                    changeMediator(
                        if (_category == "") dishesRepository.getActionDishesByNameDao() else
                            dishesRepository.getDishesInCategoryByNameDao(_category)
                    )
                } else {
                    changeMediator(
                        if (_category == "") dishesRepository.getActionDishesByNameDescDao() else
                            dishesRepository.getDishesInCategoryByNameDescDao(_category)
                    )
                }
            }
            2 -> {
                if (isAscending) {
                    changeMediator(
                        if (_category == "") dishesRepository.getActionDishesByLikesDao() else
                            dishesRepository.getDishesInCategoryByLikesDao(_category)
                    )
                } else {
                    changeMediator(
                        if (_category == "") dishesRepository.getActionDishesByLikesDescDao() else
                            dishesRepository.getDishesInCategoryByLikesDescDao(_category)
                    )
                }
            }
            3 -> {
                if (isAscending) {
                    changeMediator(
                        if (_category == "") dishesRepository.getActionDishesByRatingDao() else
                            dishesRepository.getDishesInCategoryByRatingDao(_category)
                    )
                } else {
                    changeMediator(
                        if (_category == "") dishesRepository.getActionDishesByRatingDescDao() else
                            dishesRepository.getDishesInCategoryByRatingDescDao(_category)
                    )
                }
            }
        }
    }

    fun getDishesInCategory(category: String?): MediatorLiveData<List<Dish>> {
        _category = category

        changeMediator(
            if (category == "") dishesRepository.getActionDishesByNameDao() else
                dishesRepository.getDishesInCategoryByNameDao(category)
        )

        return mediator
    }

    private fun changeMediator(_dishes: LiveData<List<Dish>>) {
        if (dishes != null) {
            mediator.removeSource(dishes!!)
        }
        dishes = _dishes
        mediator.addSource(dishes!!) {
            mediator.value = it
        }
    }
}