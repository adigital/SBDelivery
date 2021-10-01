package ru.skillbranch.sbdelivery.viewmodels.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.skillbranch.sbdelivery.data.remote.res.AddressRes
import ru.skillbranch.sbdelivery.data.repositories.AddressRepository
import ru.skillbranch.sbdelivery.viewmodels.BaseViewModel

class AddressViewModel : BaseViewModel() {

    private val addressRepository = AddressRepository

    var address: MutableLiveData<List<AddressRes>> = MutableLiveData(listOf())


    fun checkInputNet(adds: String) {
        viewModelScope.launch(Dispatchers.IO) {
            address.postValue(addressRepository.checkInputNet(adds))
        }
    }
}
