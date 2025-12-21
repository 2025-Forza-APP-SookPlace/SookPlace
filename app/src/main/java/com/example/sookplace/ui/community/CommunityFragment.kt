package com.example.sookplace.ui.community

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sookplace.R
import com.example.sookplace.ui.community.CommunityRVAdapter
import com.example.sookplace.databinding.FragmentCommunityBinding
import com.example.sookplace.ui.community.postDetail.PostDetailActivity
import com.example.sookplace.ui.search.restaurantDetail.RestaurantDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class CommunityFragment : Fragment() {

    private lateinit var binding: FragmentCommunityBinding
    private val viewModel: CommunityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_community, container, false)

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView() //리사이클러뷰
        observeViewModel()
    }

    private fun setupRecyclerView() { //RecyclerView
        binding.communityRv.apply {
            adapter = communityAdapter
            layoutManager = LinearLayoutManager(requireContext())

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    // dy > 0 은 아래로 스크롤 중임을 의미
                    if (dy > 0) {
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                        val totalItemCount = layoutManager.itemCount

                        // 마지막에서 2번째 아이템에 도달하면 추가 로드
                        if (lastVisibleItemPosition >= totalItemCount - 2) {
                            viewModel.fetchPosts(isRefresh = false)
                        }
                    }
                }
            })
        }
    }

    private fun observeViewModel() { //상태에 따라 UI
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is CommunityViewModel.CommunityUiState.Loading -> {
                            // 로딩바 보여주기 (XML에 ProgressBar가 있다면)
                        }
                        is CommunityViewModel.CommunityUiState.Success -> {
                            communityAdapter.submitList(state.posts)
                        }
                        is CommunityViewModel.CommunityUiState.Error -> {
                            // 에러 메시지 처리 (Toast 등)
                        }
                    }
                }
            }
        }
    }

    private val communityAdapter = CommunityRVAdapter(
        onPlaceClick = { placeId ->
            val intent = Intent(requireContext(), RestaurantDetailActivity::class.java).apply {
                putExtra("RESTAURANT_ID", placeId.toIntOrNull() ?: 0)
            }
            startActivity(intent)
        },
        onItemClick = { post ->
            val intent = Intent(requireContext(), PostDetailActivity::class.java).apply {
                putExtra("POST_ID",post.postId)
                putExtra("IS_LIKED", post.likedByMe)
            }
            startActivity(intent)
        },
        onLikeClick = { post ->
            viewModel.toggleLike(post.postId)
        },
        onBookmarkClick = { post ->
            viewModel.toggleBookmark(post)
        }

    )

}