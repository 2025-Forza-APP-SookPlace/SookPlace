package com.example.sookplace.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.sookplace.R
import com.example.sookplace.databinding.FragmentHomeBinding
import com.example.sookplace.databinding.FragmentMapBinding


class MapFragment : Fragment() {

    private lateinit var binding : FragmentMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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
            it.findNavController().navigate(R.id.action_mapFragment_to_myPageFragment)
        }

        return binding.root
    }


}