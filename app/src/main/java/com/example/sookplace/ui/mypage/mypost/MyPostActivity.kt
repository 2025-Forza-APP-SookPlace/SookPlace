package com.example.sookplace.ui.mypage.mypost

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sookplace.R
import com.example.sookplace.databinding.ActivityMyPostBinding
import com.example.sookplace.ui.mypage.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyPostBinding

    // 뷰모델 연결
    private val viewModel: MyPageViewModel by viewModels()
    private lateinit var adapter: MyPostPreviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 회원님의 기존 방식인 DataBindingUtil을 그대로 사용합니다!
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_post)

        setupListeners()
        setupRecyclerView()
        observeViewModel()

        // 데이터 로드
        viewModel.loadMyPage()
    }

    private fun setupListeners() {
        // [핵심] 뒤로가기 버튼 처리
        // 🚨 주의: 아래 'ivBack' 부분에 빨간 줄이 뜬다면,
        // activity_my_post.xml 파일을 열어서 뒤로가기 이미지의 진짜 ID를 확인하고 그 이름으로 바꿔주세요! (예: backButton, backBtn 등)
        binding.ivBack.setOnClickListener {
            finish() // 이 함수가 마이페이지로 확실하게 돌아가게 해줍니다.
        }
    }

    private fun setupRecyclerView() {
        adapter = MyPostPreviewAdapter { post ->
            Toast.makeText(this, "${post.title} 클릭됨", Toast.LENGTH_SHORT).show()
        }

        // 회원님의 기존 리사이클러뷰 ID인 'mypostRV'를 사용합니다!
        binding.mypostRV.layoutManager = LinearLayoutManager(this)
        binding.mypostRV.adapter = adapter
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.myPosts.collect { posts ->
                    adapter.submitList(posts)
                }
            }
        }
    }
}