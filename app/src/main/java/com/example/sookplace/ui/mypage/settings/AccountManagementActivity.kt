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
import com.bumptech.glide.Glide
import com.example.sookplace.MainActivity
import com.example.sookplace.R
import com.example.sookplace.ui.mypage.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountManagementActivity : AppCompatActivity() {


    // 마이페이지 뷰모델을 재사용하여 프로필 정보를 가져옵니다.
    private val viewModel: MyPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_account_management)
        // 데이터 로드 (프로필 정보를 채우기 위해)
        viewModel.loadMyPage()
    }
}