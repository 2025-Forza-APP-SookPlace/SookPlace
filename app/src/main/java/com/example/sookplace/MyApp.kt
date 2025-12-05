package com.example.sookplace

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // TODO: 네이버 클라이언트 ID 넣기
//        NaverMapSdk.getInstance(this).client =
//            NaverMapSdk.NaverCloudPlatformClient("mki5mzmwjj")
    }
}