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

    // [수정] 어댑터 생성 시 클릭 리스너(람다)를 전달해야 오류가 사라집니다.
    private val adapter = MyPostPreviewAdapter { item ->
        viewModel.checkLoginAndAction {
            // 클릭 시 실행할 동작 (예: 상세 페이지 이동)
            Toast.makeText(context, "${item.title} 선택됨", Toast.LENGTH_SHORT).show()

            // 추후 상세 페이지 이동 로직 추가
            // val intent = Intent(requireContext(), PostDetailActivity::class.java)
            // intent.putExtra("postId", item.postId) // 또는 item.id
            // startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPostListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.myPostRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.myPostRecycler.adapter = adapter

        // 데이터 로드 (이미 로드되어 있다면 생략 가능하지만, 확실하게 하기 위해 호출)
        viewModel.loadMyPage()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.myPosts.collect { posts ->
                    adapter.submitList(posts)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}