package ru.skillbranch.sbdelivery.viewmodels

import ru.skillbranch.sbdelivery.data.repositories.CartRepository
import ru.skillbranch.sbdelivery.data.repositories.NoticesRepository
import ru.skillbranch.sbdelivery.data.repositories.RootRepository

class RootViewModel : BaseViewModel() {

    private val repository = RootRepository
    private val noticesRepository = NoticesRepository

    fun getNewNoticesCount() = noticesRepository.getNewNoticesCountDao()

    fun getCartItemsCount() = CartRepository.getCartItemsCountDao()

    fun logOut() {
        repository.logOut()
    }
}