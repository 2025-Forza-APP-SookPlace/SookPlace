package com.example.sookplace.ui.mypage.settings

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sookplace.databinding.ActivityNotificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationSettingsActivity : AppCompatActivity() {

    // activity_notification.xml 파일과 연결되는 바인딩 클래스입니다.
    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        // 1. 뒤로가기 버튼 (ivBack)
        binding.ivBack.setOnClickListener {
            finish() // 현재 액티비티를 닫고 마이페이지로 돌아갑니다.
        }

        // 2. 푸시 알림 스위치 (switchPush)
        binding.switchPush.setOnCheckedChangeListener { _, isChecked ->
            showToast("푸시 알림이 ${if (isChecked) "켜졌습니다" else "꺼졌습니다"}")
        }

        // 3. 댓글 알림 스위치 (switchComments)
        binding.switchComments.setOnCheckedChangeListener { _, isChecked ->
            showToast("댓글 알림이 ${if (isChecked) "켜졌습니다" else "꺼졌습니다"}")
        }

        // 4. 좋아요 알림 스위치 (switchLikes)
        binding.switchLikes.setOnCheckedChangeListener { _, isChecked ->
            showToast("좋아요 알림이 ${if (isChecked) "켜졌습니다" else "꺼졌습니다"}")
        }

        // 5. 맛집 추천 알림 스위치 (switchRecommendations)
        binding.switchRecommendations.setOnCheckedChangeListener { _, isChecked ->
            showToast("맛집 추천이 ${if (isChecked) "켜졌습니다" else "꺼졌습니다"}")
        }
    }

    // 토스트 메시지를 띄워주는 공통 함수
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}