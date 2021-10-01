package ru.skillbranch.sbdelivery.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.skillbranch.sbdelivery.data.local.entities.Cart
import ru.skillbranch.sbdelivery.data.local.entities.CartItem
import ru.skillbranch.sbdelivery.data.local.entities.CartWithItems
import ru.skillbranch.sbdelivery.data.models.CartItemModel

@Dao
interface CartDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cart: Cart)

    @Transaction
    fun clearCart() {
        deleteCartItems()
        deleteCart()
    }

    @Query("DELETE FROM Cart")
    fun deleteCart()

    @Query("DELETE FROM CartItems")
    fun deleteCartItems()


    @Transaction
    fun deleteCartItem(id: String) {
        _deleteCartItem(id)
        _getTotal().also { _updateTotal(1, it) }
    }

    @Query("DELETE FROM CartItems WHERE id = :id")
    fun _deleteCartItem(id: String)

    @Query("SELECT sum(amount * price) FROM CartItems")
    fun _getTotal(): Int

    @Query("UPDATE Cart SET total = :total WHERE cartId = :cartId")
    fun _updateTotal(cartId: Long, total: Int)


//    @Transaction
//    @Query("SELECT * FROM Cart")
//    fun getCartWithItems(): List<CartWithItems>

    @Transaction
    fun getCartWithItems(): List<CartWithItems> {
        return listOf(CartWithItems(_getCartEntity(), _getCartItemsEntities()))
    }

    @Query("SELECT * FROM Cart LIMIT 1")
    fun _getCartEntity(): Cart

    @Query("SELECT * FROM CartItems")
    fun _getCartItemsEntities(): List<CartItem>


    @Query("SELECT * FROM Cart")
    fun getCart(): LiveData<Cart>

    @Query("SELECT Dishes.name, Dishes.image, CartItems.amount, CartItems.price, CartItems.id FROM CartItems INNER JOIN Dishes ON CartItems.id = Dishes.id")
    fun getCartItems(): LiveData<List<CartItemModel>>
}