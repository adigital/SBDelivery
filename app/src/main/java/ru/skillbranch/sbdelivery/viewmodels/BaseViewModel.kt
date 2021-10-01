package ru.skillbranch.sbdelivery.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.skillbranch.sbdelivery.data.local.entities.CartItem
import ru.skillbranch.sbdelivery.data.repositories.CartRepository
import ru.skillbranch.sbdelivery.data.repositories.DishesRepository
import ru.skillbranch.sbdelivery.data.repositories.RootRepository

abstract class BaseViewModel : ViewModel() {

    private val repository = RootRepository
    private val dishesRepository = DishesRepository
    private val cartRepository = CartRepository

    val authState: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        value = false
    }

    init {
        authState.addSource(repository.isAuth()) {
            authState.value = it
        }
    }

    fun getUserFirstName() = repository.getUserFirstName()

    fun getUserLastName() = repository.getUserLastName()

    fun getUserEmail() = repository.getUserEmail()

    fun updateFavoriteDish(id: String, favorite: Boolean) {
        repository.isAuth().value?.let { isAuth ->
            if (isAuth) {
                dishesRepository.changeFavoriteDish(id, favorite)
            } else {
                dishesRepository.updateFavoriteDishDao(id, favorite)
            }
        }
    }

    fun insertItemToCart(id: String, count: Int, price: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.updateCartDao(cartItems = listOf(CartItem(1, id, count, price)))
        }
    }


    fun syncFavoriteDishes(syncMode: SyncMode) {
        GlobalScope.launch(Dispatchers.IO) {
            val favoriteDishesDao = dishesRepository.getFavoriteDishesList()
            val favoriteDishesNet = dishesRepository.getFavoriteDishesNet()
            val favoriteDishesNewDao = mutableListOf<String>()
            val favoriteDishesNewNet = mutableListOf<String>()
            var isFound: Boolean

            favoriteDishesNet.forEach { dishNet ->
                isFound = false
                favoriteDishesDao.forEach { dishDao ->
                    if (dishNet.dishId == dishDao.id) {
                        isFound = true
                    }
                }

                if (!isFound) {
                    favoriteDishesNewDao.add(dishNet.dishId)
                }
            }
            if (favoriteDishesNewDao.isNotEmpty()) {
                dishesRepository.setFavoriteDishesDao(favoriteDishesNewDao, true)
            }


            if (syncMode == SyncMode.NET_TO_DAO_TO_NET || syncMode == SyncMode.NET_TO_DAO_CLEAR) {
                favoriteDishesDao.forEach { dishDao ->
                    isFound = false
                    favoriteDishesNet.forEach { dishNet ->
                        if (dishDao.id == dishNet.dishId) {
                            isFound = true
                        }
                    }

                    if (!isFound) {
                        favoriteDishesNewNet.add(dishDao.id)
                    }
                }
                if (favoriteDishesNewNet.isNotEmpty()) {
                    if (syncMode == SyncMode.NET_TO_DAO_TO_NET) {
                        dishesRepository.changeFavoriteDishesNet(favoriteDishesNewNet, true)
                    } else {
                        dishesRepository.setFavoriteDishesDao(favoriteDishesNewNet, false)
                    }
                }
            }
        }
    }
}

enum class SyncMode {
    NET_TO_DAO, NET_TO_DAO_TO_NET, NET_TO_DAO_CLEAR
}