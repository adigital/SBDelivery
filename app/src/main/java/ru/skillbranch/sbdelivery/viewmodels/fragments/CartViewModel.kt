package ru.skillbranch.sbdelivery.viewmodels.fragments

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.skillbranch.sbdelivery.data.models.CartItemModel
import ru.skillbranch.sbdelivery.data.repositories.CartRepository
import ru.skillbranch.sbdelivery.viewmodels.BaseViewModel

class CartViewModel : BaseViewModel() {

    private val cartRepository = CartRepository


    fun getCart() = cartRepository.getCartDao()

    fun getCartItems() = cartRepository.getCartItemsDao()

    fun updateCart(item: CartItemModel) {
        viewModelScope.launch(Dispatchers.IO) {
            if (item.amount == 0) {
                cartRepository.deleteCartItemDao(item.id)
            } else {
                cartRepository.updateCartItemDao(item.id, item.amount, item.price)
            }
        }
    }

    fun updateCartNet() = cartRepository.updateCartNet()
}