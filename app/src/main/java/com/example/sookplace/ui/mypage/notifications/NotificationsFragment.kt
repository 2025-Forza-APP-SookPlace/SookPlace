package com.example.sookplace.ui.mypage.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
// [수정] 레이아웃 파일명(activity_notification.xml)에 맞춰 바인딩 클래스 import 변경
import com.example.sookplace.databinding.ActivityNotificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    // [수정] 바인딩 클래스 타입 변경
    private var _binding: ActivityNotificationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        // [수정] inflate 메서드 호출 대상 변경
        _binding = ActivityNotificationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // [주의] activity_notification.xml 안에 'textNotifications'라는 ID를 가진 TextView가 있어야 오류가 나지 않습니다.
        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}