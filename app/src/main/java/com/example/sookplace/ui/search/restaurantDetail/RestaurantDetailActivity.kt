package com.example.sookplace.ui.search.restaurantDetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.sookplace.R
import com.example.sookplace.data.remote.response.LatestReview
import com.example.sookplace.data.remote.response.MenuItem
import com.example.sookplace.data.remote.response.RestaurantDetailResponse
import com.example.sookplace.databinding.ActivityRestaurantDetailBinding
import com.example.sookplace.databinding.ItemLatestReviewBinding
import com.example.sookplace.ui.search.restaurantDetail.RestaurantDetailViewModel.DetailUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RestaurantDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRestaurantDetailBinding
    private val viewModel: RestaurantDetailViewModel by viewModels()
    private val menuAdapter = MenuRVAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant_detail)
        binding.lifecycleOwner = this

        val restaurantId = intent.getIntExtra("RESTAURANT_ID", -1)

        setupListeners(restaurantId)
        observeViewModel()

        if (restaurantId != -1) {
            viewModel.fetchRestaurantDetail(restaurantId) // 식당 상세 정보 가져오기
        }
    }

    //버튼 클릭 정의
    private fun setupListeners(restaurantId: Int) {
        binding.btnBack.setOnClickListener { finish() }//뒤로가기

        binding.btnLike.setOnClickListener { //좋아요 버튼
            if (!viewModel.checkUserLoggedIn()) {
                Toast.makeText(this, "로그인 후 이용 가능합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentState = viewModel.detailState.value
            if (currentState is DetailUiState.Success) {
                viewModel.toggleLike(restaurantId)
            }
        }

        binding.btnSave.setOnClickListener { //핀 버튼
            Log.d("RestaurantDetail", "Save 버튼 눌림!")
            if (!viewModel.checkUserLoggedIn()) {
                Toast.makeText(this, "로그인 후 이용 가능합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentState = viewModel.detailState.value
            if (currentState is DetailUiState.Success) {
                viewModel.toggleSave(restaurantId)
            }
        }
    }

    private fun observeViewModel() { //viewModel
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.detailState.collect { state ->
                    when (state) {
                        is DetailUiState.Success -> bindData(state.data)
                        else -> Unit
                    }
                }
            }
        }

        //좋아요
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLiked.collect { isLiked ->
                    val iconRes = if (isLiked) R.drawable.favorite_fill else R.drawable.favorite
                    binding.btnLike.setImageResource(iconRes)
                }
            }
        }

        //마이플레이스 버튼
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isSaved.collect { isSaved ->
                    val iconRes = if (isSaved) R.drawable.location_fill else R.drawable.location
                    binding.btnSave.setImageResource(iconRes)
                }
            }
        }
    }

    private fun bindData(data: RestaurantDetailResponse) { //데이터 바인딩
        with(binding) {
            textRestaurantName.text = data.name
            textRating.text = data.rating.toString()
            textCategory.text = data.category
            textAddress.text = data.address
            textPhone.text = data.phone

            setupImageSlider(data.images.orEmpty())

            val hoursText = data.openingHours?.joinToString("\n") { "${it.day}: ${it.hours}" } ?: "영업시간 정보가 없습니다."
            textHours.text = hoursText

            setupMenuRecyclerView(data.menus.orEmpty())
            setupReviewSection(data.latestReviews.orEmpty())

            btnNaverMap.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.naverMapUrl))
                startActivity(intent)
            }
        }
    }

    private fun setupImageSlider(images: List<String>) { //헤더 이미지

        val sliderAdapter = ImageSliderAdapter(images)
        binding.imageMain.adapter = sliderAdapter
        binding.imageMain.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        //페이지 수
        val totalImages = images.size
        if (images.isEmpty()) {
            binding.tvImageIndex.isVisible = false // 이미지가 없으면 "1 / 0" 텍스트 숨기기
            return // 더 이상 슬라이더 설정을 할 필요가 없으므로 함수 종료
        } else {
            binding.tvImageIndex.isVisible = true
            binding.tvImageIndex.text = "1 / $totalImages"
        }

        binding.imageMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tvImageIndex.text = "${position + 1} / $totalImages"
            }
        })
    }

    private fun setupMenuRecyclerView(menus: List<MenuItem>) { //메뉴 RV모델
        binding.rvMenus.apply {
            adapter = menuAdapter
            layoutManager = LinearLayoutManager(this@RestaurantDetailActivity)
            isNestedScrollingEnabled = false //스크롤뷰와의 충돌 방지
        }
        menuAdapter.submitList(menus)
    }

    private fun setupReviewSection(reviews: List<LatestReview>) { //
        binding.postList.removeAllViews() // 초기화
        reviews.take(3).forEach { review ->
            val reviewBinding = ItemLatestReviewBinding.inflate(
                LayoutInflater.from(this),
                binding.postList,
                false
            )

            with(reviewBinding) {
                author.text = review.authorNickname
                date.text = review.createdAt.split("T")[0].replace("-", ".")
                title.text = review.title
                rating.text = "★ ${review.rating}"
                content.text = review.contentSnippet

                if (!review.imageUrl.isNullOrEmpty()) {
                    image.isVisible = true
                    image.load(review.imageUrl) {
                        transformations(RoundedCornersTransformation(20f)) // 둥근 모서리
                    }
                } else {
                    image.isVisible = false
                }
            }
            binding.postList.addView(reviewBinding.root)
        }
        binding.cardPosts.isVisible = reviews.isNotEmpty()
    }
}