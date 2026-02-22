package com.example.sookplace.ui.mypage.mypost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sookplace.databinding.FragmentMyPostListBinding
import com.example.sookplace.ui.mypage.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPostListFragment : Fragment() {

    private var _binding: FragmentMyPostListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyPageViewModel by viewModels()

    private val adapter = MyPostPreviewAdapter { item ->
        viewModel.checkLoginAndAction {
            Toast.makeText(context, "${item.title} 선택됨", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPostListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // [핵심 1] 뒤로가기 버튼
        binding.ivBack.setOnClickListener {
            requireActivity().finish()
        }

        binding.myPostRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.myPostRecycler.adapter = adapter

        viewModel.loadMyPage()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.myPosts.collect { posts ->
                        adapter.submitList(posts)
                        updateEmptyState(posts.isEmpty())
                    }
                }

                launch {
                    viewModel.isGuestMode.collect {
                        updateEmptyState(adapter.currentList.isEmpty())
                    }
                }
            }
        }
    }

    // [핵심 2] 안내 문구 처리
    private fun updateEmptyState(isListEmpty: Boolean) {
        val isGuest = viewModel.isGuestMode.value

        if (isGuest) {
            binding.tvEmptyState.text = "아직 로그인을 하지 않았습니다."
            binding.tvEmptyState.visibility = View.VISIBLE
            binding.myPostRecycler.visibility = View.GONE
        } else if (isListEmpty) {
            binding.tvEmptyState.text = "아직 작성한 글이 없습니다."
            binding.tvEmptyState.visibility = View.VISIBLE
            binding.myPostRecycler.visibility = View.GONE
        } else {
            binding.tvEmptyState.visibility = View.GONE
            binding.myPostRecycler.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}