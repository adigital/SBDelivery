package ru.skillbranch.sbdelivery.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.skillbranch.sbdelivery.data.local.DbManager
import ru.skillbranch.sbdelivery.data.local.PrefManager
import ru.skillbranch.sbdelivery.data.local.entities.Order
import ru.skillbranch.sbdelivery.data.local.entities.OrderItem
import ru.skillbranch.sbdelivery.data.remote.NetworkManager
import ru.skillbranch.sbdelivery.data.remote.req.OrderCancelReq
import ru.skillbranch.sbdelivery.data.remote.req.OrderReq
import ru.skillbranch.sbdelivery.data.remote.res.OrderRes
import ru.skillbranch.sbdelivery.data.repositories.StatusesRepository.getStatusDao
import ru.skillbranch.sbdelivery.data.repositories.StatusesRepository.updateStatuses
import ru.skillbranch.sbdelivery.extensions.notifyMainShort

object OrdersRepository {

    private var ordersDao = DbManager.db.ordersDao()
    private var ordersItemsDao = DbManager.db.ordersItemsDao()
    private val network = NetworkManager.api
    private val preferences = PrefManager

    suspend fun updateOrders() {
        val orders = mutableListOf<Order>()
        val ordersItems = mutableListOf<OrderItem>()

        val ordersRes = getOrdersNet()
        ordersRes.forEach { order ->
            orders.add(
                Order(
                    order.id,
                    order.total,
                    order.address,
                    order.statusId,
                    getStatusDao(order.statusId).name,
                    order.active,
                    order.completed,
                    order.createdAt,
                    order.updatedAt
                )
            )
            order.items.forEach { orderItem ->
                ordersItems.add(
                    OrderItem(
                        order.id,
                        orderItem.name,
                        orderItem.image,
                        orderItem.amount,
                        orderItem.price,
                        orderItem.dishId
                    )
                )
            }
        }
        if (ordersItems.isNotEmpty()) {
            updateOrdersDao(orders, ordersItems)
        }
    }

    private suspend fun updateOrdersDao(orders: List<Order>, ordersItems: List<OrderItem>) {
        ordersDao.insertOrders(orders)
        ordersItemsDao.insertOrdersItems(ordersItems)
    }

    fun getOrdersDao() = ordersDao.getOrders()

    fun getOrderWithItemsDao(orderId: String) = ordersDao.getOrderWithItems(orderId)


    fun createOrder(orderReq: OrderReq): LiveData<OrderRes> {
        val orderOut: MutableLiveData<OrderRes> = MutableLiveData()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val newOrder = network.createOrder(token = preferences.accessToken, orderReq)
                updateOrder(newOrder)

                orderOut.postValue(newOrder)
            } catch (e: Throwable) {
                orderOut.postValue(null)
                notifyMainShort(e.localizedMessage)
            }
        }

        return orderOut
    }

    private suspend fun getOrdersNet(): List<OrderRes> {
        var orders = listOf<OrderRes>()

        try {
            val res = network.getOrders(preferences.ordersExpires, preferences.accessToken)
            if (res.isSuccessful) {
                orders = res.body()!!
                res.headers()["Expires"]?.let { preferences.ordersExpires = it }
            }
        } catch (e: Throwable) {
            notifyMainShort(e.localizedMessage)
        }

        return orders
    }

    fun cancelOrder(orderId: String): LiveData<Boolean> {
        val result: MutableLiveData<Boolean> = MutableLiveData()
        val orderCancelReq = OrderCancelReq(orderId)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                updateOrder(network.cancelOrder(token = preferences.accessToken, orderCancelReq))

                result.postValue(true)
            } catch (e: Throwable) {
                notifyMainShort(e.localizedMessage)

                result.postValue(false)
            }
        }

        return result
    }

    private suspend fun updateOrder(orderRes: OrderRes) {
        val ordersItems = mutableListOf<OrderItem>()

        updateStatuses()

        val order = Order(
            orderRes.id,
            orderRes.total,
            orderRes.address,
            orderRes.statusId,
            getStatusDao(orderRes.statusId).name,
            orderRes.active,
            orderRes.completed,
            orderRes.createdAt,
            orderRes.updatedAt
        )

        orderRes.items.forEach { orderItemRes ->
            ordersItems.add(
                OrderItem(
                    order.id,
                    orderItemRes.name,
                    orderItemRes.image,
                    orderItemRes.amount,
                    orderItemRes.price,
                    orderItemRes.dishId
                )
            )
        }
        updateOrdersDao(listOf(order), ordersItems)
    }
}