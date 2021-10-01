package ru.skillbranch.sbdelivery.viewmodels.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.skillbranch.sbdelivery.data.repositories.RootRepository
import ru.skillbranch.sbdelivery.extensions.notifyMainShort
import ru.skillbranch.sbdelivery.viewmodels.BaseViewModel

class RecoveryPasswordViewModel : BaseViewModel() {

    private val repository = RootRepository

    var buttonSendShow = MutableLiveData(false)

    var email: String = ""
    var code: String = ""
    var password: String = ""
    var password2: String = ""

    fun checkPasswords() {
        buttonSendShow.postValue(password.isNotBlank() && password2.isNotBlank() && password == password2)
    }

    fun recovery(): LiveData<Boolean> {
        val result: MutableLiveData<Boolean> = MutableLiveData()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.recoveryPasswordNet(email, code, password)
                result.postValue(true)
            } catch (e: Throwable) {
                result.postValue(false)
                notifyMainShort(e.localizedMessage)
            }
        }

        return result
    }
}