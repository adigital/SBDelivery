package ru.skillbranch.sbdelivery.viewmodels.fragments

import androidx.lifecycle.MutableLiveData
import ru.skillbranch.sbdelivery.data.repositories.ProfileRepository
import ru.skillbranch.sbdelivery.extensions.isValidEmail
import ru.skillbranch.sbdelivery.extensions.isValidLettersOnly
import ru.skillbranch.sbdelivery.viewmodels.BaseViewModel

class ProfileViewModel : BaseViewModel() {

    private val profileRepository = ProfileRepository

    var isEdit = MutableLiveData(false)
    var isDataCorrect = MutableLiveData(true)

    var firstName = MutableLiveData(getUserFirstName().value!!)
    var lastName = MutableLiveData(getUserLastName().value!!)
    var email = MutableLiveData(getUserEmail().value!!)

    var firstNameNotValid = MutableLiveData(false)
    var lastNameNotValid = MutableLiveData(false)
    var emailNotValid = MutableLiveData(false)

    var firstNameBlank = MutableLiveData(false)
    var lastNameBlank = MutableLiveData(false)
    var emailBlank = MutableLiveData(false)


    fun getProfile() = profileRepository.getProfileNet()

    fun editProfile() =
        profileRepository.editProfileNet(firstName.value!!, lastName.value!!, email.value!!)

    fun checkData() {
        isDataCorrect.value = true

        if (firstName.value!!.isNotBlank()) {
            firstNameBlank.postValue(false)
            if (firstName.value!!.isValidLettersOnly()) {
                firstNameNotValid.postValue(false)
            } else {
                firstNameNotValid.postValue(true)
                isDataCorrect.value = false
            }
        } else {
            firstNameBlank.postValue(true)
            isDataCorrect.value = false
        }

        if (lastName.value!!.isNotBlank()) {
            lastNameBlank.postValue(false)
            if (lastName.value!!.isValidLettersOnly()) {
                lastNameNotValid.postValue(false)
            } else {
                lastNameNotValid.postValue(true)
                isDataCorrect.value = false
            }
        } else {
            lastNameBlank.postValue(true)
            isDataCorrect.value = false
        }

        if (email.value!!.isNotBlank()) {
            emailBlank.postValue(false)
            if (email.value!!.isValidEmail()) {
                emailNotValid.postValue(false)
            } else {
                emailNotValid.postValue(true)
                isDataCorrect.value = false
            }
        } else {
            emailBlank.postValue(true)
            isDataCorrect.value = false
        }
    }

    fun cancel() {
        firstName.postValue(getUserFirstName().value!!)
        lastName.postValue(getUserLastName().value!!)
        email.postValue(getUserEmail().value!!)
    }
}