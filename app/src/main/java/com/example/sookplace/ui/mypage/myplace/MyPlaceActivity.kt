package com.example.sookplace.ui.mypage.myplace

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sookplace.R
import com.example.sookplace.databinding.ActivityMyPlaceBinding
import com.example.sookplace.ui.mypage.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPlaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyPlaceBinding
    private val viewModel: MyPageViewModel by viewModels()
    private lateinit var adapter: MyPlacePreviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // XML의 <layout> 태그를 사용하므로 DataBindingUtil로 세팅
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_place)

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
        adapter = MyPlacePreviewAdapter { place ->
            Toast.makeText(this, "${place.name} 클릭됨", Toast.LENGTH_SHORT).show()
        }

        // 리사이클러뷰 세팅 (XML의 rvMyPlace ID 참조)
        binding.rvMyPlace.apply {
            // 마이플레이스는 2열 격자 구조가 보기 좋습니다.
            layoutManager = GridLayoutManager(this@MyPlaceActivity, 2)
            adapter = this@MyPlaceActivity.adapter
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.myPlaces.collect { places ->
                    adapter.submitList(places)
                }
            }
        }
    }
}