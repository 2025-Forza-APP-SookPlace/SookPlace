package com.example.sookplace.ui.map

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sookplace.R
import com.example.sookplace.databinding.FragmentMapBinding
import com.example.sookplace.ui.search.restaurantDetail.RestaurantDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import com.naver.maps.map.MapFragment as NaverMapFragment

@AndroidEntryPoint
class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding
    private val viewModel: MapViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)

        val adapter = FavoriteAdapter { detailUrl ->
            val intent = Intent(requireContext(), RestaurantDetailActivity::class.java).apply {
//                putExtra("RESTAURANT_URL", detailUrl) // 상세 페이지 식별자 전달
            }
            startActivity(intent)
        }

        binding.rvPins.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.favorites.observe(viewLifecycleOwner) { response ->
            adapter.submitList(response.items)
        }

        ///하단바 프래그먼트 간의 이동 구현
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

        viewModel.fetchFavorites(10)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as NaverMapFragment?

        mapFragment?.getMapAsync { naverMap ->
            // 여기에 코드를 작성하면 지도가 뜬 직후에 실행
            // 마커 찍기, 현재 위치 버튼 활성화 등
        }
    }
}

