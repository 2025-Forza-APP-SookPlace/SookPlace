package com.example.sookplace.ui.mypage.myplace

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
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sookplace.databinding.FragmentMyplaceListBinding
import com.example.sookplace.ui.mypage.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPlaceListFragment : Fragment() {

    private var _binding: FragmentMyplaceListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyPageViewModel by viewModels()

    // [수정] 어댑터 생성 시 클릭 리스너(람다)를 전달해야 합니다.
    private val adapter = MyPlacePreviewAdapter { item ->
        // 아이템 클릭 시 실행될 코드
        viewModel.checkLoginAndAction {
            // [수정] placeName -> name 으로 변경 (서버 모델 필드명 확인 필요)
            Toast.makeText(context, "${item.name} 선택됨", Toast.LENGTH_SHORT).show()
            // 추후 상세 페이지 이동 로직 추가
            // val intent = Intent(requireContext(), RestaurantDetailActivity::class.java)
            // intent.putExtra("placeId", item.id)
            // startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyplaceListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()

        // 데이터 로드
        viewModel.loadMyPage()
    }

    private fun setupRecyclerView() {
        binding.placeListRecycler.layoutManager =
            GridLayoutManager(requireContext(), 2)
        binding.placeListRecycler.adapter = adapter
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.myPlaces.collect { items ->
                    adapter.submitList(items)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}