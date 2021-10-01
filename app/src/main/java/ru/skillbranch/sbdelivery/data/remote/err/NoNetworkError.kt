package ru.skillbranch.sbdelivery.data.remote.err

import java.io.IOException

class NoNetworkError(override val message: String = "Нет доступа к интернету. Попробуйте позже.") :
    IOException(message)