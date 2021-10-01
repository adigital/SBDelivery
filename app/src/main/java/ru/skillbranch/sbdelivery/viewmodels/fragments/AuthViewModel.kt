package ru.skillbranch.sbdelivery.viewmodels.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.skillbranch.sbdelivery.data.repositories.DishesRepository
import ru.skillbranch.sbdelivery.data.repositories.RootRepository
import ru.skillbranch.sbdelivery.extensions.notifyMainShort
import ru.skillbranch.sbdelivery.viewmodels.BaseViewModel

class AuthViewModel : BaseViewModel() {

    private val repository = RootRepository
    private val dishesRepository = DishesRepository

    var email: String = ""
    var password: String = ""

    fun login(): LiveData<Boolean> {
        val result: MutableLiveData<Boolean> = MutableLiveData()

        if (email.isNotBlank() && password.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    repository.loginNet(email, password)
                    result.postValue(true)
                } catch (e: Throwable) {
                    result.postValue(false)
                    notifyMainShort(e.localizedMessage)
                }
            }
        } else {
            result.postValue(false)
        }

        return result
    }

    fun getFavoriteDishesCount(): LiveData<Int> {
        val count: MutableLiveData<Int> = MutableLiveData()

        viewModelScope.launch(Dispatchers.IO) {
            count.postValue(dishesRepository.getFavoriteDishesCountDao())
        }

        return count
    }
}