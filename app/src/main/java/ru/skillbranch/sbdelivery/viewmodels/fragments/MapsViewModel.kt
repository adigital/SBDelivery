package ru.skillbranch.sbdelivery.viewmodels.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.skillbranch.sbdelivery.data.remote.res.AddressRes
import ru.skillbranch.sbdelivery.data.repositories.AddressRepository
import ru.skillbranch.sbdelivery.viewmodels.BaseViewModel

class MapsViewModel : BaseViewModel() {

    private val addressRepository = AddressRepository

    var address: MutableLiveData<AddressRes> = MutableLiveData()

    fun checkCoordinatesNet(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            address.postValue(addressRepository.checkCoordinatesNet(lat, lon))
        }
    }
}

