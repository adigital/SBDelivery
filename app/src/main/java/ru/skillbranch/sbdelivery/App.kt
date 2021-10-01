package ru.skillbranch.sbdelivery

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        context = this

        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
    }

    companion object {
        lateinit var context: Context
    }
}