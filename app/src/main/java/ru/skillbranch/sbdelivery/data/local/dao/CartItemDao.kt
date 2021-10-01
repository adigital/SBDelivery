package ru.skillbranch.sbdelivery.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.skillbranch.sbdelivery.data.local.entities.CartItem

@Dao
interface CartItemDao {

    @Transaction
    suspend fun insertCartItems(cartItems: List<CartItem>) {
        _insertCartItems(cartItems)
        getTotal().also { updateTotal(1, it) }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun _insertCartItems(cartItems: List<CartItem>)

    @Transaction
    fun updateCartItem(id: String, amount: Int, price: Int) {
        changeCartItem(id, amount, price)
        getTotal().also { updateTotal(1, it) }
    }

    @Query("UPDATE CartItems SET amount = :amount, price = :price  WHERE id = :id")
    fun changeCartItem(id: String, amount: Int, price: Int)

    @Query("SELECT sum(amount * price) FROM CartItems")
    fun getTotal(): Int

    @Query("UPDATE Cart SET total = :total WHERE cartId = :cartId")
    fun updateTotal(cartId: Long, total: Int)

    @Query("SELECT count(*) FROM CartItems")
    fun getCartItemsCount(): LiveData<Int>
}