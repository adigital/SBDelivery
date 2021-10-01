package ru.skillbranch.sbdelivery.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.skillbranch.sbdelivery.data.local.DbManager
import ru.skillbranch.sbdelivery.data.local.PrefManager
import ru.skillbranch.sbdelivery.data.local.entities.Cart
import ru.skillbranch.sbdelivery.data.local.entities.CartItem
import ru.skillbranch.sbdelivery.data.remote.NetworkManager
import ru.skillbranch.sbdelivery.data.remote.res.CartItemRes
import ru.skillbranch.sbdelivery.data.remote.res.CartRes
import ru.skillbranch.sbdelivery.extensions.notifyMainShort

object CartRepository {

    private var cartDao = DbManager.db.cartDao()
    private var cartItemDao = DbManager.db.cartItemsDao()
    private val network = NetworkManager.api
    private val preferences = PrefManager


    suspend fun updateCartDao(
        promocode: String = "",
        promotext: String = "",
        total: Int = 0,
        cartItems: List<CartItem>,
    ) {
        val cart = Cart(
            1,
            promocode,
            promotext,
            if (total == 0) cartItems.sumOf { it.price * it.amount } else total
        )

        cartDao.insertCart(cart)
        cartItemDao.insertCartItems(cartItems)
    }

    fun updateCartItemDao(id: String, amount: Int, price: Int) {
        cartItemDao.updateCartItem(id, amount, price)
    }

    fun clearCartDao() {
        cartDao.clearCart()
    }

    private fun getCartWithItemsDao() = cartDao.getCartWithItems()

    fun getCartDao() = cartDao.getCart()

    fun getCartItemsDao() = cartDao.getCartItems()

    fun deleteCartItemDao(id: String) = cartDao.deleteCartItem(id)

    fun getCartItemsCountDao() = cartItemDao.getCartItemsCount()


    suspend fun getCartNet(): CartRes? {
        var cart: CartRes? = null

        try {
            cart = network.getCart(token = preferences.accessToken)
        } catch (e: Throwable) {
            notifyMainShort(e.localizedMessage)
        }

        return cart
    }

    fun updateCartNet(): LiveData<Int> {
        val result: MutableLiveData<Int> = MutableLiveData(0)

        GlobalScope.launch(Dispatchers.IO) {
            val cartWithItems = getCartWithItemsDao()
            var cartNet: CartRes? = null

            if (cartWithItems.isNotEmpty()) {
                val cart = CartRes(cartWithItems[0].cart?.promocode.toString())

                cartWithItems[0].cartItems?.forEach { item ->
                    val cartItem = CartItemRes(item.id, item.amount)

                    cart.items.add(cartItem)
                }

                try {
                    cartNet = network.updateCart(cart, token = preferences.accessToken)
                } catch (e: Throwable) {
                    if (e.message != "End of input") {
                        notifyMainShort(e.localizedMessage)
                    }
                }

                cartNet?.also {
                    var isSameCart = (cartWithItems[0].cart!!.total == it.total) &&
                            cartWithItems[0].cartItems!!.size == it.items.size
                    if (isSameCart) {
                        for (i in cartWithItems[0].cartItems!!.indices) {
                            isSameCart = isSameCart &&
                                    (cartWithItems[0].cartItems!![i].id == it.items[i].id) &&
                                    (cartWithItems[0].cartItems!![i].amount == it.items[i].amount) &&
                                    (cartWithItems[0].cartItems!![i].price == it.items[i].price)
                        }
                    }
                    if (isSameCart) result.postValue(1) else result.postValue(2)
                }
            }
        }

        return result
    }
}