package ru.skillbranch.sbdelivery.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.skillbranch.sbdelivery.data.local.DbManager.db
import ru.skillbranch.sbdelivery.data.local.PrefManager
import ru.skillbranch.sbdelivery.data.remote.NetworkManager
import ru.skillbranch.sbdelivery.data.remote.req.*

object RootRepository {

    private val preferences = PrefManager
    private var dishesDao = db.dishesDao()
    private var cartDao = db.cartDao()
    private var ordersDao = db.ordersDao()
    private var noticesDao = db.noticesDao()
    private val network = NetworkManager.api
    val isDataSync: MutableLiveData<Boolean> = MutableLiveData()

    fun isAuth(): LiveData<Boolean> = preferences.isAuthLive

    suspend fun loginNet(email: String, pass: String) {
        val auth = network.login(LoginReq(email, pass))
        preferences.id = auth.id
        preferences.firstName = auth.firstName
        preferences.lastName = auth.lastName
        preferences.email = auth.email
        preferences.accessToken = "Bearer ${auth.accessToken}"
        preferences.refreshToken = auth.refreshToken
    }

    suspend fun registerNet(firstName: String, lastName: String, email: String, pass: String) {
        val auth = network.register(RegisterReq(firstName, lastName, email, pass))
        preferences.id = auth.id
        preferences.firstName = auth.firstName
        preferences.lastName = auth.lastName
        preferences.email = auth.email
        preferences.accessToken = "Bearer ${auth.accessToken}"
        preferences.refreshToken = auth.refreshToken
    }

    suspend fun recoveryEmailNet(email: String) {
        network.recoveryEmail(RecoveryEmailReq(email))
    }

    suspend fun recoveryCodeNet(email: String, code: String) {
        network.recoveryCode(RecoveryCodeReq(email, code))
    }

    suspend fun recoveryPasswordNet(email: String, code: String, password: String) {
        network.recoveryPassword(RecoveryPasswordReq(email, code, password))
    }

    fun getUserFirstName(): LiveData<String> = preferences.firstNameLive

    fun getUserLastName(): LiveData<String> = preferences.lastNameLive

    fun getUserEmail(): LiveData<String> = preferences.emailLive

    fun logOut() {
        preferences.clearAll()

        GlobalScope.launch(Dispatchers.IO) {
            dishesDao.clearAllFavoriteDishes()
            ordersDao.clearOrders()
            cartDao.clearCart()
            noticesDao.deleteAllNotices()
        }
    }
}