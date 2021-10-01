package ru.skillbranch.sbdelivery.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.skillbranch.sbdelivery.data.local.PrefManager
import ru.skillbranch.sbdelivery.data.remote.NetworkManager
import ru.skillbranch.sbdelivery.data.remote.req.ChangePasswordReq
import ru.skillbranch.sbdelivery.data.remote.req.EditProfileReq
import ru.skillbranch.sbdelivery.data.remote.res.ProfileRes
import ru.skillbranch.sbdelivery.extensions.notifyMainShort

object ProfileRepository {

    private val preferences = PrefManager
    private val network = NetworkManager.api

    fun getProfileNet(): LiveData<ProfileRes> {
        val profileRes: MutableLiveData<ProfileRes> = MutableLiveData()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val profile = network.getProfile(token = preferences.accessToken)
                preferences.id = profile.id
                preferences.firstName = profile.firstName
                preferences.lastName = profile.lastName
                preferences.email = profile.email

                profileRes.postValue(profile)
            } catch (e: Throwable) {
                notifyMainShort(e.localizedMessage)
            }
        }

        return profileRes
    }

    fun editProfileNet(firstName: String, lastName: String, email: String): LiveData<ProfileRes> {
        val profileRes: MutableLiveData<ProfileRes> = MutableLiveData()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val profile = network.editProfile(
                    EditProfileReq(firstName, lastName, email),
                    token = preferences.accessToken
                )
                profileRes.postValue(profile)

                preferences.firstName = profile.firstName
                preferences.lastName = profile.lastName
                preferences.email = profile.email
            } catch (e: Throwable) {
                notifyMainShort(e.localizedMessage)
            }
        }

        return profileRes
    }

    fun changePasswordNet(oldPassword: String, newPassword: String): LiveData<Boolean> {
        val result: MutableLiveData<Boolean> = MutableLiveData()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                network.changePassword(
                    ChangePasswordReq(oldPassword, newPassword),
                    token = preferences.accessToken
                )
                result.postValue(true)
            } catch (e: Throwable) {
                result.postValue(false)
                notifyMainShort(e.localizedMessage)
            }
        }

        return result
    }
}