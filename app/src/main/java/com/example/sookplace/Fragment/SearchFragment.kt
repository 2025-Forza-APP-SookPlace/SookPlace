package com.example.sookplace.Fragment

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
import com.example.sookplace.databinding.FragmentSearchBinding
import com.example.sookplace.searchList.SearchRVAdapter


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

        ///RecyclerView
        val rv : RecyclerView = binding.searchRv
        val items = ArrayList<String>()
        items.add("a")
        items.add("b")
        items.add("a")
        items.add("b")

        val rvAdapter = SearchRVAdapter(items)
        rv.adapter = rvAdapter
        rv.layoutManager = LinearLayoutManager(requireContext())


        ///하단바 프래그먼트 간의 이동 구현
        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_searchFragment_to_homeFragment)
        }

        binding.mapTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_searchFragment_to_mapFragment)
        }

        binding.communityTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_searchFragment_to_communityFragment)
        }

        binding.mypageTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_searchFragment_to_myPageFragment)
        }

        return binding.root
    }
}