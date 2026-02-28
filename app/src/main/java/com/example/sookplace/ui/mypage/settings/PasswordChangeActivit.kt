package com.example.sookplace.ui.mypage.settings

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.sookplace.databinding.ActivityPasswordChangeBinding

class PasswordChangeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPasswordChangeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener { finish() }

        binding.btnChange.setOnClickListener {
            val current = binding.etCurrentPassword.text.toString()
            val newPass = binding.etNewPassword.text.toString()
            val confirm = binding.etConfirmPassword.text.toString()

            binding.tvMessage.visibility = View.GONE

            if (current.isEmpty()) {
                showError("현재 비밀번호를 입력해주세요.")
                return@setOnClickListener
            }
            if (newPass.isEmpty()) {
                showError("새 비밀번호를 입력해주세요.")
                return@setOnClickListener
            }
            if (newPass.length < 8) {
                showError("비밀번호는 최소 8자 이상이어야 합니다.")
                return@setOnClickListener
            }
            if (!newPass.any { it.isLetter() } || !newPass.any { it.isDigit() }) {
                showError("영문과 숫자를 모두 포함해야 합니다.")
                return@setOnClickListener
            }
            if (newPass != confirm) {
                showError("새 비밀번호가 일치하지 않습니다.")
                return@setOnClickListener
            }

            // 피그마 요구사항: 현재 비밀번호 시뮬레이션
            if (current != "password123") {
                showError("현재 비밀번호가 올바르지 않습니다.")
                return@setOnClickListener
            }

            // 성공 처리
            showSuccess("비밀번호가 성공적으로 변경되었습니다.")

            // [추가됨] 1.5초 뒤 계정 관리 화면으로 확실하게 화면이 넘어가도록 처리
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this@PasswordChangeActivity, AccountManagementActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
            }, 1500)
        }
    }

    // Context 참조 오류를 없애고 안전한 색상 코드로 변경
    private fun showError(message: String) {
        binding.tvMessage.apply {
            text = message
            setTextColor(Color.parseColor("#EC5655")) // 빨간색 텍스트
            setBackgroundColor(Color.parseColor("#1AEC5655")) // 연한 빨간색 배경
            visibility = View.VISIBLE
        }
    }

    // Context 참조 오류를 없애고 안전한 색상 코드로 변경
    private fun showSuccess(message: String) {
        binding.tvMessage.apply {
            text = message
            setTextColor(Color.parseColor("#16A34A")) // 초록색 텍스트
            setBackgroundColor(Color.parseColor("#1A16A34A")) // 연한 초록색 배경
            visibility = View.VISIBLE
        }
    }
}