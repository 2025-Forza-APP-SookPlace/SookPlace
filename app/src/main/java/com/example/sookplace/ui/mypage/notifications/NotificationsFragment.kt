package com.example.sookplace.ui.mypage.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
// [수정] 파일명이 activity_notification.xml 인 경우에 맞춤
import com.example.sookplace.databinding.ActivityNotificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    private var _binding: ActivityNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뷰가 생성된 후 리스너들을 연결합니다.
        setupListeners()
    }

    private fun setupListeners() {
        // 1. 뒤로가기 버튼
        binding.ivBack.setOnClickListener {
            // 프래그먼트 환경에 맞게 뒤로가기 처리
            requireActivity().onBackPressed()
        }

        // 2. 푸시 알림 스위치
        binding.switchPush.setOnCheckedChangeListener { _, isChecked ->
            showToast("푸시 알림이 ${if (isChecked) "켜졌습니다" else "꺼졌습니다"}")
        }

        // 3. 댓글 알림 스위치
        binding.switchComments.setOnCheckedChangeListener { _, isChecked ->
            showToast("댓글 알림이 ${if (isChecked) "켜졌습니다" else "꺼졌습니다"}")
        }

        // 4. 좋아요 알림 스위치
        binding.switchLikes.setOnCheckedChangeListener { _, isChecked ->
            showToast("좋아요 알림이 ${if (isChecked) "켜졌습니다" else "꺼졌습니다"}")
        }

        // 5. 맛집 추천 알림 스위치
        binding.switchRecommendations.setOnCheckedChangeListener { _, isChecked ->
            showToast("맛집 추천이 ${if (isChecked) "켜졌습니다" else "꺼졌습니다"}")
        }
    }

    // 프래그먼트용 공통 토스트 함수 (requireContext() 사용)
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}