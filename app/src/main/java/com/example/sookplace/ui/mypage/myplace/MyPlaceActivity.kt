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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sookplace.R
import com.example.sookplace.databinding.ActivityMyPlaceBinding
import com.example.sookplace.ui.mypage.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPlaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyPlaceBinding

    // 뷰모델 연결
    private val viewModel: MyPageViewModel by viewModels()
    private lateinit var adapter: MyPlacePreviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 회원님의 기존 방식인 DataBindingUtil을 그대로 사용합니다!
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_place)

        setupListeners()
        setupRecyclerView()
        observeViewModel()

        // 데이터 로드
        viewModel.loadMyPage()
    }

    private fun setupListeners() {
        // [핵심] 뒤로가기 버튼 처리
        // 🚨 주의: 아래 'ivBack' 부분에 빨간 줄이 뜬다면,
        // activity_my_place.xml 파일을 열어서 뒤로가기 이미지의 진짜 ID를 확인하고 그 이름으로 바꿔주세요! (예: backButton, backBtn 등)
        binding.ivBack.setOnClickListener {
            finish() // 이 함수가 마이페이지로 확실하게 돌아가게 해줍니다.
        }
    }

    private fun setupRecyclerView() {
        adapter = MyPlacePreviewAdapter { place ->
            Toast.makeText(this, "${place.name} 클릭됨", Toast.LENGTH_SHORT).show()
        }

        // 회원님의 기존 리사이클러뷰 ID인 'myplaceRv'를 사용합니다!
        // 마이플레이스 미리보기 화면과 통일감을 주기 위해 2열 격자(Grid)로 설정했습니다.
        // 만약 원래 쓰시던 1열 리스트가 좋으시면 GridLayoutManager(this, 2) 부분을 LinearLayoutManager(this)로 바꾸시면 됩니다!
        binding.myplaceRv.layoutManager = GridLayoutManager(this, 2)
        binding.myplaceRv.adapter = adapter
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