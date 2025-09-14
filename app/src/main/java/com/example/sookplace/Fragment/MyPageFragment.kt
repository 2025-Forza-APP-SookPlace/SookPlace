package com.example.sookplace.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.sookplace.R
import com.example.sookplace.databinding.FragmentMyPageBinding


class MyPageFragment : Fragment() {

    private lateinit var binding: FragmentMyPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        ///하단바 프래그먼트 간의 이동 구현
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_page, container, false)

        binding.searchTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_myPageFragment_to_searchFragment)
        }

        binding.mapTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_myPageFragment_to_mapFragment)
        }

        binding.communityTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_myPageFragment_to_communityFragment)
        }

        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_myPageFragment_to_homeFragment)
        }

        return binding.root
    }

}