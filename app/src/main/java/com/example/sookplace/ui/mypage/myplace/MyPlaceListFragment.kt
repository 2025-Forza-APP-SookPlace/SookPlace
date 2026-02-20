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

    private val adapter = MyPlacePreviewAdapter { item ->
        viewModel.checkLoginAndAction {
            Toast.makeText(context, "${item.name} 선택됨", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyplaceListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // [핵심 1] 뒤로가기 버튼 기능 연결
        binding.ivBack.setOnClickListener {
            requireActivity().finish()
        }

        binding.placeListRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.placeListRecycler.adapter = adapter

        // 데이터 로드
        viewModel.loadMyPage()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 1. 데이터 관찰 및 안내 문구 처리
                launch {
                    viewModel.myPlaces.collect { items ->
                        adapter.submitList(items)
                        // 데이터가 비었는지 확인해서 문구 갱신
                        updateEmptyState(items.isEmpty())
                    }
                }

                // 2. 로그인 상태 관찰 (비로그인이면 문구 변경)
                launch {
                    viewModel.isGuestMode.collect {
                        // 현재 리스트 상태와 함께 체크
                        updateEmptyState(adapter.currentList.isEmpty())
                    }
                }
            }
        }
    }

    // [핵심 2] 안내 문구 제어 함수
    private fun updateEmptyState(isListEmpty: Boolean) {
        val isGuest = viewModel.isGuestMode.value

        if (isGuest) {
            binding.tvEmptyState.text = "아직 로그인을 하지 않았습니다."
            binding.tvEmptyState.visibility = View.VISIBLE
            binding.placeListRecycler.visibility = View.GONE
        } else if (isListEmpty) {
            binding.tvEmptyState.text = "아직 저장된 장소가 없습니다."
            binding.tvEmptyState.visibility = View.VISIBLE
            binding.placeListRecycler.visibility = View.GONE
        } else {
            binding.tvEmptyState.visibility = View.GONE
            binding.placeListRecycler.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}