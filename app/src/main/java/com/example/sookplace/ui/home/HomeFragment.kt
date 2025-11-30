package com.example.sookplace.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.sookplace.ui.mypage.myplace.MyPlaceActivity
import com.example.sookplace.ui.mypage.mypost.MyPostActivity
import com.example.sookplace.R
import com.example.sookplace.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //데이터바인딩
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        // ViewModel 초기화
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 샘플 데이터 삽입 테스트
        viewModel.insertSampleUserData()
        viewModel.insertSampleRestaurants()

        // Coroutine으로 DB 조회
        lifecycleScope.launch(Dispatchers.IO) {
            val user = viewModel.getUser() // DAO에서 단일 사용자 반환
            withContext(Dispatchers.Main) {
                // UI에 반영
                binding.nickname.text = user.nickname
                binding.level.text = "Lv. ${user.level}"
            }
        }

        ///하단바 프래그먼트 간의 이동 구현
        binding.searchTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }

        binding.mapTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_mapFragment)
        }

        binding.communityTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_communityFragment)
        }

        binding.mypageTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_mypageFragment)
        }
    }

}