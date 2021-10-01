package ru.skillbranch.sbdelivery.data.local

import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.skillbranch.sbdelivery.App.Companion.context
import ru.skillbranch.sbdelivery.data.delegates.PrefDelegate
import ru.skillbranch.sbdelivery.data.delegates.PrefLiveDelegate

object PrefManager {
    private const val FIRSTNAME = "firstName"
    private const val LASTNAME = "lastName"
    private const val EMAIL = "email"
    private const val ACCESS_TOKEN = "accessToken"
    private const val START_DATE_TIME = "Wed, 21 Oct 2015 07:28:00 GMT"

    internal val preferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    var dishesExpires by PrefDelegate(START_DATE_TIME)
    var recommendExpires by PrefDelegate(START_DATE_TIME)
    var categoriesExpires by PrefDelegate(START_DATE_TIME)
    var favoriteExpires by PrefDelegate(START_DATE_TIME)
    var ordersExpires by PrefDelegate(START_DATE_TIME)
    var statusesExpires by PrefDelegate(START_DATE_TIME)
    var id by PrefDelegate("")
    var firstName by PrefDelegate("")
    var lastName by PrefDelegate("")
    var email by PrefDelegate("")
    var accessToken by PrefDelegate("")
    var refreshToken by PrefDelegate("")
    var lastKnownLocationLatitude by PrefDelegate("")
    var lastKnownLocationLongitude by PrefDelegate("")

    val isAuthLive: LiveData<Boolean> by lazy {
        val token by PrefLiveDelegate(ACCESS_TOKEN, "", preferences)
        token.map { it.isNotEmpty() }
    }

    val firstNameLive by PrefLiveDelegate(FIRSTNAME, "", preferences)
    val lastNameLive by PrefLiveDelegate(LASTNAME, "", preferences)
    val emailLive by PrefLiveDelegate(EMAIL, "", preferences)

    fun clearAll() {
        favoriteExpires = START_DATE_TIME
        ordersExpires = START_DATE_TIME
        statusesExpires = START_DATE_TIME

        accessToken = ""
        firstName = ""
        lastName = ""
        email = ""

        preferences.edit().clear().apply()
    }
}