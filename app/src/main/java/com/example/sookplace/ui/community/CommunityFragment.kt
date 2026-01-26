package com.example.sookplace.ui.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sookplace.R
import com.example.sookplace.ui.community.CommunityRVAdapter
import com.example.sookplace.databinding.FragmentCommunityBinding

class CommunityFragment : Fragment() {

    private lateinit var binding: FragmentCommunityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_community, container, false)

        //RecyclerView
        val rv : RecyclerView = binding.communityRv
        val items = ArrayList<String>()
        items.add("a")
        items.add("b")
        items.add("a")
        items.add("b")

        val rvAdapter = CommunityRVAdapter(items)
        rv.adapter = rvAdapter
        rv.layoutManager = LinearLayoutManager(requireContext())


        ///하단바 프래그먼트 간의 이동 구현
        binding.searchTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_communityFragment_to_searchFragment)
        }

        binding.mapTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_communityFragment_to_mapFragment)
        }

        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_communityFragment_to_homeFragment)
        }

        binding.mypageTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_communityFragment_to_mypageFragment)
        }

        return binding.root
    }

}