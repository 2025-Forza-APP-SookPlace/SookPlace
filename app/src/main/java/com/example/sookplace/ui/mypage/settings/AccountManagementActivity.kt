package com.example.sookplace.ui.mypage.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.sookplace.MainActivity
import com.example.sookplace.R
import com.example.sookplace.databinding.ActivityAccountManagementBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountManagementBinding
    private val viewModel: AccountManagementViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        // 뒤로가기 (마이페이지로 돌아감)
        binding.ivBack.setOnClickListener { finish() }

        // [🔥 핵심 업데이트] 새로 만든 화면들로 이동하도록 Intent 연결!

        // 1. 닉네임 변경 화면으로 이동
        binding.btnNickname.setOnClickListener {
            val intent = Intent(this, NicknameChangeActivity::class.java)
            startActivity(intent)
        }

        // 2. 비밀번호 변경 화면으로 이동
        binding.btnPassword.setOnClickListener {
            val intent = Intent(this, PasswordChangeActivity::class.java)
            startActivity(intent)
        }

        // 3. 숙명인증 재확인 화면으로 이동
        binding.btnVerify.setOnClickListener {
            val intent = Intent(this, UniversityVerificationActivity::class.java)
            startActivity(intent)
        }

        // 회원 탈퇴는 아직 화면이 없으므로 토스트 메시지만 띄움
        binding.btnDelete.setOnClickListener {
            Toast.makeText(this, "회원 탈퇴 기능 준비 중입니다.", Toast.LENGTH_SHORT).show()
        }

        // 하단 네비게이션 처리
        val navListener = View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        try {
            binding.homeTap.setOnClickListener(navListener)
            binding.searchTap.setOnClickListener(navListener)
            binding.mapTap.setOnClickListener(navListener)
            binding.communityTap.setOnClickListener(navListener)
            binding.mypageTap.setOnClickListener { finish() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userMe.collect { user ->
                    user?.let {
                        binding.username.text = it.nickname
                        binding.email.text = it.email
                        binding.badge.visibility = if (it.sookVerified) View.VISIBLE else View.GONE

                        // 서버 이미지가 있으면 띄우고, 아니면 기본 유저 아이콘 표시
                        if (!it.avatarUrl.isNullOrEmpty()) {
                            com.bumptech.glide.Glide.with(this@AccountManagementActivity)
                                .load(it.avatarUrl)
                                .error(R.drawable.ic_user)
                                .into(binding.profileImage)
                        } else {
                            binding.profileImage.setImageResource(R.drawable.ic_user)
                        }
                    }
                }
            }
        }
    }
}