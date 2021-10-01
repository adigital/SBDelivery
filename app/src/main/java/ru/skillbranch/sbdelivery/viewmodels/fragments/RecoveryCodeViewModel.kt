package ru.skillbranch.sbdelivery.viewmodels.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.skillbranch.sbdelivery.data.repositories.RootRepository
import ru.skillbranch.sbdelivery.extensions.notifyMainShort
import ru.skillbranch.sbdelivery.viewmodels.BaseViewModel

class RecoveryCodeViewModel : BaseViewModel() {

    private val repository = RootRepository

    var email: String = ""
    var code1: String = ""
    var code2: String = ""
    var code3: String = ""
    var code4: String = ""

    fun recovery(): LiveData<Boolean> {
        val result: MutableLiveData<Boolean> = MutableLiveData()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.recoveryCodeNet(email, code1 + code2 + code3 + code4)
                result.postValue(true)
            } catch (e: Throwable) {
                result.postValue(false)
                notifyMainShort(e.localizedMessage)
            }
        }

        return result
    }
}