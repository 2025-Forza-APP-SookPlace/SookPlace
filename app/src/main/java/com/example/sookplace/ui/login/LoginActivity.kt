package com.example.sookplace.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.sookplace.MainActivity
import com.example.sookplace.R
import com.example.sookplace.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        //로그인버튼
        binding.signInButton.setOnClickListener {
            val id = binding.userId.text.toString()
            val pw = binding.password.text.toString()

            viewModel.login(id, pw)
        }

        //회원가입버튼
        binding.signUpText.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        viewModel.loginResult.observe(this) { result ->
            result.onSuccess { response ->
                Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()

                // 메인 화면 이동
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }

            result.onFailure { error ->
                Toast.makeText(this, "로그인 실패: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}