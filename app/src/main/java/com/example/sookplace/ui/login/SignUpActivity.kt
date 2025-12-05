package com.example.sookplace.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.sookplace.R
import com.example.sookplace.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignUpBinding
    lateinit var viewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =  DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        binding.signUpButton.setOnClickListener {
            val userId = binding.userId.text.toString()
            val nickname = binding.nickname.text.toString()
            val email = binding.email.text.toString()
            val pw = binding.password.text.toString()

            viewModel.signup(
                userId = userId,
                nickname = nickname,
                email = email,
                password = pw,
                onSuccess = { response ->
                    // 가입 성공 처리
                    Toast.makeText(this, "회원가입 완료!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()

                },
                onError = {
                    Toast.makeText(this, "회원가입 실패: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }

    }
}