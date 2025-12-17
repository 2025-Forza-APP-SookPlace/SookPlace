package com.example.sookplace

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.sookplace.databinding.ActivityMainBinding
import com.example.sookplace.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private var wasLoggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        lifecycleScope.launchWhenStarted {
            viewModel.isLoggedIn.collect { loggedIn ->
                //헤더에 로그인 버튼 노출 여부
                binding.loginBtn.visibility =
                    if (loggedIn) View.GONE else View.VISIBLE
                //토큰 만료 시 로그인 화면으로 이동
                if (wasLoggedIn && !loggedIn) {
                    Toast.makeText(
                        this@MainActivity,
                        "토큰이 만료되었습니다. 다시 로그인 해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()

//                    startActivity(
//                        Intent(this@MainActivity, LoginActivity::class.java).apply {
//                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        }
//                    ) 로그인 창으로 이동(뒤로가기 불가)
                }

                wasLoggedIn = loggedIn
            }
        }

        binding.loginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }
}