package ru.skillbranch.sbdelivery.data.repositories

import ru.skillbranch.sbdelivery.data.local.DbManager
import ru.skillbranch.sbdelivery.data.local.PrefManager
import ru.skillbranch.sbdelivery.data.local.entities.Status
import ru.skillbranch.sbdelivery.data.remote.NetworkManager
import ru.skillbranch.sbdelivery.data.remote.res.StatusRes
import ru.skillbranch.sbdelivery.extensions.notifyMainShort
import ru.skillbranch.sbdelivery.data.toStatusesList

object StatusesRepository {

    private var statusesDao = DbManager.db.statusesDao()
    private val network = NetworkManager.api
    private val preferences = PrefManager

    suspend fun updateStatuses() {
        updateStatusesDao(getStatusesNet().toStatusesList())
    }

    private suspend fun updateStatusesDao(statuses: List<Status>) {
        deleteStatusesDao()

        statusesDao.insertStatuses(statuses)
    }

    private fun deleteStatusesDao() {
        statusesDao.deleteStatuses()
    }

    fun getStatusDao(id: String) = statusesDao.getStatus(id)

    private suspend fun getStatusesNet(): List<StatusRes> {
        var statuses = listOf<StatusRes>()

        try {
            val res = network.getStatuses(preferences.statusesExpires)
            if (res.isSuccessful) {
                statuses = res.body()!!
                res.headers()["Expires"]?.let { preferences.statusesExpires = it }
            }
        } catch (e: Throwable) {
            notifyMainShort(e.localizedMessage)
        }

        return statuses
    }
}