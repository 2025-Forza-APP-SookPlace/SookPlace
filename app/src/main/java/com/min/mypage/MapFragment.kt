package com.min.mypage.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.min.mypage.R
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var naverMap: NaverMap
    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_map.xml을 연결
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = view.findViewById(R.id.naverMapView)
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync(this)
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map

        // 숙명여대 근처로 카메라 이동
        val cameraPosition = CameraPosition(
            LatLng(37.5450, 126.9647),
            15.0
        )
        naverMap.cameraPosition = cameraPosition
    }
}
