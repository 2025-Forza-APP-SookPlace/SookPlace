package com.example.sookplace.ui.mypage.settings

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sookplace.databinding.ActivityNicknameChangeBinding

class NicknameChangeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNicknameChangeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNicknameChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener { finish() }

        // 글자 수 세기 및 에러 메시지 초기화
        binding.etNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val length = s?.length ?: 0
                binding.tvCount.text = "$length/10"
                binding.tvError.visibility = View.GONE
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnSave.setOnClickListener {
            val nickname = binding.etNickname.text.toString()

            if (nickname.length < 2) {
                showError("닉네임은 최소 2자 이상이어야 합니다.")
                return@setOnClickListener
            }

            // 피그마 요구사항: 중복 체크 시뮬레이션
            if (nickname == "맛집킬러") {
                showError("이미 사용 중인 닉네임입니다.")
                return@setOnClickListener
            }

            // 성공 처리
            Toast.makeText(this, "닉네임이 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show()

            // [추가됨] 계정 관리 화면으로 확실하게 화면이 넘어가도록 처리
            val intent = Intent(this@NicknameChangeActivity, AccountManagementActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }

    // 안전한 색상 코드로 에러 메시지 띄우기
    private fun showError(message: String) {
        binding.tvError.text = message
        binding.tvError.setTextColor(Color.parseColor("#EC5655")) // 빨간색
        binding.tvError.visibility = View.VISIBLE
    }
}