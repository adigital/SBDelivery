package ru.skillbranch.sbdelivery.viewmodels.fragments

import ru.skillbranch.sbdelivery.data.remote.req.OrderReq
import ru.skillbranch.sbdelivery.data.repositories.OrdersRepository
import ru.skillbranch.sbdelivery.viewmodels.BaseViewModel

class OrderViewModel : BaseViewModel() {

    private val ordersRepository = OrdersRepository

    fun createOrder(orderReq: OrderReq) = ordersRepository.createOrder(orderReq)
}