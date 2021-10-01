package ru.skillbranch.sbdelivery.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skillbranch.sbdelivery.data.repositories.ProfileRepository

class ChangePasswordDialogViewModel : ViewModel() {

    private val repository = ProfileRepository

    var oldPassword = ""
    var newPassword = ""

    var isSamePasswords = MutableLiveData(false)

    fun checkPasswords() {
        isSamePasswords.postValue(oldPassword.isNotBlank() && newPassword.isNotBlank())
    }

    fun changePassword() =
        repository.changePasswordNet(oldPassword, newPassword)
}