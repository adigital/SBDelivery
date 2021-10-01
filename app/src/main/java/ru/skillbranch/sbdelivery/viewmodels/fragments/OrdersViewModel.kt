package ru.skillbranch.sbdelivery.viewmodels.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.skillbranch.sbdelivery.data.repositories.OrdersRepository
import ru.skillbranch.sbdelivery.data.repositories.StatusesRepository
import ru.skillbranch.sbdelivery.viewmodels.BaseViewModel

class OrdersViewModel : BaseViewModel() {

    private val statusesRepository = StatusesRepository
    private val ordersRepository = OrdersRepository

    fun updateOrders(): LiveData<Boolean> {
        val result = MutableLiveData(false)

        viewModelScope.launch(Dispatchers.IO) {
            statusesRepository.updateStatuses()
            ordersRepository.updateOrders()

            result.postValue(true)
        }

        return result
    }

    fun getOrders() = ordersRepository.getOrdersDao()
}