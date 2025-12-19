package com.example.sookplace.ui.search

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
import com.example.sookplace.ui.search.SearchRVAdapter

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

        //카테고리별 검색
        val category = arguments?.getString("category")

        when (category) {
            "치킨" -> binding.foodCategoryBtn1.isChecked = true
            "카페" -> binding.foodCategoryBtn2.isChecked = true
            "한식" -> binding.foodCategoryBtn3.isChecked = true
            "분식" -> binding.foodCategoryBtn4.isChecked = true
            "양식" -> binding.foodCategoryBtn5.isChecked = true
            "디저트" -> binding.foodCategoryBtn6.isChecked = true
            else -> binding.foodCategoryBtn0.isChecked = true // 전체
        }

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
            it.findNavController().navigate(R.id.action_searchFragment_to_mypageFragment)
        }

        return binding.root
    }
}