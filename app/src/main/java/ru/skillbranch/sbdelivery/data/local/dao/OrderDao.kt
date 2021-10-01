package ru.skillbranch.sbdelivery.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.skillbranch.sbdelivery.data.local.entities.Order
import ru.skillbranch.sbdelivery.data.local.entities.OrderWithItems

@Dao
interface OrderDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(orders: List<Order>)

    @Transaction
    fun clearOrders() {
        deleteOrdersItems()
        deleteOrders()
    }

    @Query("DELETE FROM Orders")
    fun deleteOrders()

    @Query("DELETE FROM OrdersItems")
    fun deleteOrdersItems()

    @Transaction
    @Query("SELECT * FROM Orders WHERE id = :orderId")
    fun getOrderWithItems(orderId: String): LiveData<OrderWithItems>

    @Query("SELECT * FROM Orders")
    fun getOrders(): LiveData<List<Order>>
}