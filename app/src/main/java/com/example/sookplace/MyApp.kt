package com.example.sookplace

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        com.naver.maps.map.NaverMapSdk.getInstance(this).client =
            com.naver.maps.map.NaverMapSdk.NaverCloudPlatformClient("c1j6y5byty")
    }
}