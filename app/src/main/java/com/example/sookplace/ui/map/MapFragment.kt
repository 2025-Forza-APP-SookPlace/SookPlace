package com.example.sookplace.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.sookplace.R
import com.example.sookplace.databinding.FragmentMapBinding

class MapFragment : Fragment() {

    //    private lateinit var naverMap: NaverMap
//    private lateinit var mapView: MapView
    private lateinit var binding: FragmentMapBinding
    private val viewModel: MapViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ///하단바 프래그먼트 간의 이동 구현
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)

        binding.searchTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_searchFragment)
        }

        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_homeFragment)
        }

        binding.communityTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_communityFragment)
        }

        binding.mypageTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_mypageFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        mapView = view.findViewById(R.id.naverMapView)
//        mapView.onCreate(savedInstanceState)
//
//        mapView.getMapAsync(this)
    }

//    override fun onMapReady(map: NaverMap) {
//        naverMap = map
//
//        // 숙명여대 근처로 카메라 이동
//        val cameraPosition = CameraPosition(
//            LatLng(37.5450, 126.9647),
//            15.0
//        )
//        naverMap.cameraPosition = cameraPosition
//    }
}