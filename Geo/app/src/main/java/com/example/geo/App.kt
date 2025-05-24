package com.example.geo

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("81f0c5ac-eca7-479e-912b-b769462bff74")
        MapKitFactory.initialize(this)
    }
}