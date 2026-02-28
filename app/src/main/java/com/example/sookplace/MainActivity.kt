package com.example.sookplace

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.sookplace.databinding.ActivityMainBinding
import com.example.sookplace.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

        // 네비게이션 컨트롤러 찾기
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.searchFragment) {
                // 탐색 화면이면 상단바 숨기기
                binding.mainHeader.visibility = View.GONE
            } else {
                // 다른 화면(홈, 커뮤니티 등)이면 다시 보여주기
                binding.mainHeader.visibility = View.VISIBLE
            }
        }

    }
}