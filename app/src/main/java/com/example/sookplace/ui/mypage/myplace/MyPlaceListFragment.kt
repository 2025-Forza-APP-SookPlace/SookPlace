package com.example.sookplace.ui.mypage.myplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sookplace.databinding.FragmentMyplaceListBinding
import com.example.sookplace.ui.mypage.MyPageViewModel

class MyPlaceListFragment : Fragment() {

    private lateinit var binding: FragmentMyplaceListBinding
    private val viewModel: MyPageViewModel by viewModels()
    private val adapter = MyPlacePreviewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyplaceListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.placeListRecycler.layoutManager =
            GridLayoutManager(requireContext(), 2)

        binding.placeListRecycler.adapter = adapter

        viewModel.loadMyPage()

        viewModel.myPlaces.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}