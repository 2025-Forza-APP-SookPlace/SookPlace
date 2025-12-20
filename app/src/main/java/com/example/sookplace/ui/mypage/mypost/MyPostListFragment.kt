package com.example.sookplace.ui.mypage.mypost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sookplace.databinding.FragmentMyPostListBinding
import com.example.sookplace.ui.mypage.MyPageViewModel

class MyPostListFragment : Fragment() {

    private lateinit var binding: FragmentMyPostListBinding
    private val viewModel: MyPageViewModel by viewModels()
    private val adapter = MyPostPreviewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyPostListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.myPostRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.myPostRecycler.adapter = adapter

        viewModel.loadMyPage()

        viewModel.myPosts.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts) // 🔥 전체 리스트
        }
    }
}

