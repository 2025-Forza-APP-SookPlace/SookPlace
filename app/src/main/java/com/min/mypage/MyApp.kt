package com.min.mypage

import android.app.Application
import com.naver.maps.map.NaverMapSdk

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // TODO: 네이버 클라이언트 ID 넣기
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient("mki5mzmwjj")
    }
}