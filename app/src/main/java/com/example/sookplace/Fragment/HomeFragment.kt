package com.example.sookplace.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.sookplace.MyPlaceActivity
import com.example.sookplace.MyPostActivity
import com.example.sookplace.R
import com.example.sookplace.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        ///하단바 프래그먼트 간의 이동 구현
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        //화면캡처용 마이 포스트및 플레이스 이동
        binding.foodCategoryBtn1.setOnClickListener {
            val intent = Intent(context, MyPostActivity::class.java)
            startActivity(intent)
        }
        binding.foodCategoryBtn2.setOnClickListener {
            val intent = Intent(context, MyPlaceActivity::class.java)
            startActivity(intent)
        }

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
            it.findNavController().navigate(R.id.action_homeFragment_to_myPageFragment)
        }




        return binding.root
    }

}