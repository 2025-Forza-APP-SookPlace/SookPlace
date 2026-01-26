package com.example.sookplace.ui.map

import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {

    // 나중에 서버에서 받아올 지도 중심 좌표
    val defaultLatitude = 37.5450
    val defaultLongitude = 126.9647

    // 줌 레벨
    val defaultZoom = 15.0
}