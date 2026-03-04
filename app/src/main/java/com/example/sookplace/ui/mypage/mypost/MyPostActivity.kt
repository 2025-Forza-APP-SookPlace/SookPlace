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
    private val viewModel: MyPageViewModel by viewModels()
    private lateinit var adapter: MyPostPreviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // XML의 <layout> 태그를 사용하므로 DataBindingUtil로 세팅
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_post)

        setupUI()
        observeViewModel()

        // 데이터 불러오기
        viewModel.loadMyPage()
    }

    private fun setupUI() {
        // 뒤로가기 버튼 기능 (XML의 ivBack ID 참조)
        binding.ivBack.setOnClickListener {
            finish()
        }

        // 어댑터 생성 (클릭 리스너 포함)
        adapter = MyPostPreviewAdapter { post ->
            Toast.makeText(this, "${post.title} 클릭됨", Toast.LENGTH_SHORT).show()
        }

        // 리사이클러뷰 세팅 (XML의 rvMyPost ID 참조)
        binding.rvMyPost.apply {
            layoutManager = LinearLayoutManager(this@MyPostActivity)
            adapter = this@MyPostActivity.adapter
        }
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