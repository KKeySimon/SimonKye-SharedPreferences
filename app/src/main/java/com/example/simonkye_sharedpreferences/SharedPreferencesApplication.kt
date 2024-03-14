package com.example.simonkye_sharedpreferences

import android.app.Application

class SharedPreferencesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PreferencesRepository.initialize(this)
    }
}