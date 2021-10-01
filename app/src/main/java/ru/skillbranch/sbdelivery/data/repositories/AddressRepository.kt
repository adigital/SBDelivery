package ru.skillbranch.sbdelivery.data.repositories

import ru.skillbranch.sbdelivery.data.remote.NetworkManager
import ru.skillbranch.sbdelivery.data.remote.req.AddressReq
import ru.skillbranch.sbdelivery.data.remote.req.CoordinatesReq
import ru.skillbranch.sbdelivery.data.remote.res.AddressRes
import ru.skillbranch.sbdelivery.extensions.notifyMainShort

object AddressRepository {

    private val network = NetworkManager.api

    suspend fun checkInputNet(adds: String): MutableList<AddressRes> {
        val address = mutableListOf<AddressRes>()
        val addressReq = AddressReq(adds)

        try {
            address.add(network.checkInput(addressReq))
        } catch (e: Throwable) {
            notifyMainShort("Ошибка. Попробуйте позже.")
        }

        return address
    }

    suspend fun checkCoordinatesNet(lat: Double, lon: Double): AddressRes {
        var addressRes = AddressRes()
        val coordinatesReq = CoordinatesReq(lat, lon)

        try {
            addressRes = network.checkCoordinates(coordinatesReq)
        } catch (e: Throwable) {
            notifyMainShort("Ошибка. Попробуйте позже.")
        }

        return addressRes
    }
}