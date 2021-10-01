package ru.skillbranch.sbdelivery.viewmodels.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.skillbranch.sbdelivery.data.local.entities.CartItem
import ru.skillbranch.sbdelivery.data.repositories.*
import ru.skillbranch.sbdelivery.data.toCategoriesList
import ru.skillbranch.sbdelivery.data.toDishesList
import ru.skillbranch.sbdelivery.viewmodels.BaseViewModel
import ru.skillbranch.sbdelivery.viewmodels.SyncMode

class SplashViewModel : BaseViewModel() {

    private val repository = RootRepository
    private val dishesRepository = DishesRepository
    private val categoriesRepository = CategoriesRepository
    private val cartRepository = CartRepository
    private val profileRepository = ProfileRepository
    private val statusesRepository = StatusesRepository
    private val ordersRepository = OrdersRepository

    private val isDataSync = repository.isDataSync

    fun resetRecommendedDishes() {
        viewModelScope.launch(Dispatchers.IO) {
            dishesRepository.resetRecommendedDishes()
            isDataSync.postValue(true)
        }
    }

    fun loadData(): LiveData<Boolean> {
        val isFinished: MutableLiveData<Boolean> = MutableLiveData()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                dishesRepository.resetRecommendedDishes()

                categoriesRepository.insertCategoriesDao(
                    categoriesRepository.getCategoriesNet()
                        .toCategoriesList()
                )

                dishesRepository.insertDishesDao(dishesRepository.getDishesNet().toDishesList())

                dishesRepository.setRecommendedDishesDao(dishesRepository.getRecommendedNet())

                repository.isAuth().value?.let { isAuth ->
                    if (isAuth) {
                        profileRepository.getProfileNet()

                        val cart = cartRepository.getCartNet()
                        cartRepository.clearCartDao()
                        cart?.also {
                            val cartItems = mutableListOf<CartItem>()
                            it.items.forEach { cartItemRes ->
                                cartItems.add(
                                    CartItem(
                                        itemId = 1,
                                        id = cartItemRes.id,
                                        amount = cartItemRes.amount,
                                        price = cartItemRes.price
                                    )
                                )
                            }
                            cartRepository.updateCartDao(
                                it.promocode,
                                it.promotext,
                                it.total,
                                cartItems
                            )
                        }

                        statusesRepository.updateStatuses()
                        ordersRepository.updateOrders()

//                        syncFavoriteDishes(SyncMode.NET_TO_DAO)
                        syncFavoriteDishes(SyncMode.NET_TO_DAO_CLEAR)
                    }
                }

                isFinished.postValue(true)
            } finally {
                isDataSync.postValue(true)
            }

        }
        return isFinished
    }
}