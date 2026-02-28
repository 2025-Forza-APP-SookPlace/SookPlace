package com.example.sookplace.ui.mypage.settings

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.sookplace.databinding.ActivityUniversityVerificationBinding

class UniversityVerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUniversityVerificationBinding
    private var isVerified = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUniversityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener { finish() }

        // 1. 인증 메일 보내기 버튼
        binding.btnSendEmail.setOnClickListener {
            val email = binding.etEmail.text.toString()
            binding.tvMessage.visibility = View.GONE

            if (email.isEmpty()) {
                showError("이메일을 입력해주세요.")
                return@setOnClickListener
            }

            if (!email.endsWith("@sookmyung.ac.kr")) {
                showError("숙명여대 이메일 주소를 입력해주세요. (예: example@sookmyung.ac.kr)")
                return@setOnClickListener
            }

            // 전송 성공 시 화면 전환
            binding.btnSendEmail.text = "인증 메일 발송됨"
            binding.btnSendEmail.isEnabled = false
            binding.layoutCode.visibility = View.VISIBLE
            showSuccess("인증 메일이 발송되었습니다. 메일함을 확인해주세요.")
        }

        // 2. 인증 완료 버튼
        binding.btnVerify.setOnClickListener {
            val code = binding.etCode.text.toString()
            binding.tvMessage.visibility = View.GONE

            if (code.isEmpty()) {
                showError("인증번호를 입력해주세요.")
                return@setOnClickListener
            }

            // 피그마 요구사항: 올바른 인증번호 시뮬레이션 (123456)
            if (code == "123456") {
                isVerified = true

                // 뱃지 색상 변경 시 Context 에러와 리소스 누락 에러를 방지하기 위해 Color.parseColor 사용
                binding.tvStatusBadge.apply {
                    text = "숙명인증 완료"
                    setTextColor(Color.WHITE)
                    backgroundTintList = ColorStateList.valueOf(Color.parseColor("#1A2FA0"))
                }

                showSuccess("인증이 완료되었습니다.")

                // [수정됨] 1.5초 뒤 계정 관리 화면으로 확실하게 화면이 넘어가도록 처리
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this@UniversityVerificationActivity, AccountManagementActivity::class.java)
                    // 기존에 열려있던 창들을 정리하고 깔끔하게 넘어갑니다.
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    finish()
                }, 1500)
            } else {
                showError("인증번호가 올바르지 않습니다.")
            }
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