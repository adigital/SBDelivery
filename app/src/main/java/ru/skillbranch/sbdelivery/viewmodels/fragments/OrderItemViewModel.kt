package ru.skillbranch.sbdelivery.viewmodels.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.skillbranch.sbdelivery.data.local.entities.CartItem
import ru.skillbranch.sbdelivery.data.local.entities.OrderWithItems
import ru.skillbranch.sbdelivery.data.repositories.CartRepository
import ru.skillbranch.sbdelivery.data.repositories.OrdersRepository
import ru.skillbranch.sbdelivery.viewmodels.BaseViewModel

class OrderItemViewModel : BaseViewModel() {

    private val cartRepository = CartRepository
    private val ordersRepository = OrdersRepository

    lateinit var order: LiveData<OrderWithItems>

    fun getOrderWithItems(orderId: String): LiveData<OrderWithItems> {
        order = ordersRepository.getOrderWithItemsDao(orderId)

        return order
    }

    fun getCartItemsCount() = cartRepository.getCartItemsCountDao()

    fun cancelOrder(orderId: String) = ordersRepository.cancelOrder(orderId)

    fun updateCart(itemsCount: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (itemsCount > 0) {
                cartRepository.clearCartDao()
            }

            val cartItems = mutableListOf<CartItem>()

            order.value.let { orderWithItems ->
                orderWithItems?.orderItems?.forEach { orderItem ->
                    cartItems.add(
                        CartItem(
                            itemId = 1,
                            id = orderItem.dishId,
                            amount = orderItem.amount,
                            price = orderItem.price / orderItem.amount
                        )
                    )
                }

                cartRepository.updateCartDao(cartItems = cartItems)
            }
        }
    }
}