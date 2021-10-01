package ru.skillbranch.sbdelivery.viewmodels.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.skillbranch.sbdelivery.data.repositories.RootRepository
import ru.skillbranch.sbdelivery.extensions.isValidEmail
import ru.skillbranch.sbdelivery.extensions.isValidLettersOnly
import ru.skillbranch.sbdelivery.extensions.notifyMainShort
import ru.skillbranch.sbdelivery.viewmodels.BaseViewModel

class RegViewModel : BaseViewModel() {

    private val repository = RootRepository

    var firstName: String = ""
    var lastName: String = ""
    var email: String = ""
    var password: String = ""

    var firstNameNotValid = MutableLiveData(false)
    var lastNameNotValid = MutableLiveData(false)
    var emailNotValid = MutableLiveData(false)

    var firstNameBlank = MutableLiveData(false)
    var lastNameBlank = MutableLiveData(false)
    var emailBlank = MutableLiveData(false)
    var passwordBlank = MutableLiveData(false)

    fun isValidation(): Boolean {
        var isValid = true

        if (firstName.isNotBlank()) {
            firstNameBlank.postValue(false)
            if (firstName.isValidLettersOnly()) {
                firstNameNotValid.postValue(false)
            } else {
                firstNameNotValid.postValue(true)
                isValid = false
            }
        } else {
            firstNameBlank.postValue(true)
            isValid = false
        }

        if (lastName.isNotBlank()) {
            lastNameBlank.postValue(false)
            if (lastName.isValidLettersOnly()) {
                lastNameNotValid.postValue(false)
            } else {
                lastNameNotValid.postValue(true)
                isValid = false
            }
        } else {
            lastNameBlank.postValue(true)
            isValid = false
        }

        if (email.isNotBlank()) {
            emailBlank.postValue(false)
            if (email.isValidEmail()) {
                emailNotValid.postValue(false)
            } else {
                emailNotValid.postValue(true)
                isValid = false
            }
        } else {
            emailBlank.postValue(true)
            isValid = false
        }

        if (password.isNotBlank()) {
            passwordBlank.postValue(false)
        } else {
            passwordBlank.postValue(true)
            isValid = false
        }

        return isValid
    }

    fun registration(): LiveData<Boolean> {
        val result: MutableLiveData<Boolean> = MutableLiveData()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.registerNet(firstName, lastName, email, password)
                result.postValue(true)
            } catch (e: Throwable) {
                result.postValue(false)
                notifyMainShort(e.localizedMessage)
            }
        }

        return result
    }
}