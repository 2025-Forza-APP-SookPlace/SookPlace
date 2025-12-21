package com.example.sookplace.ui.community.postWrite

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sookplace.R
import com.example.sookplace.databinding.ActivityPostWriteBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.sookplace.ui.community.postDetail.PostDetailActivity
import kotlinx.coroutines.launch
import kotlin.jvm.java

@AndroidEntryPoint
class PostWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostWriteBinding
    private val viewModel: PostWriteViewModel by viewModels()
    private val imageAdapter by lazy { WriteImageAdapter { uri -> viewModel.removeImage(uri) } }

    private var isEditMode = false //수정모드인지?
    private var editPostId: String? = null //수정할 게시물 ID

    //식당 검색 화면에서 결과를 가져오기 위한 코드
    private val selectRestaurantLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val id = result.data?.getIntExtra("restaurantId", -1) ?: -1
            val name = result.data?.getStringExtra("restaurantName") ?: ""
            if (id != -1) {
                viewModel.restaurantId.value = id
                binding.tvRestaurantName.text = name
            }
        }
    }

    //갤러리 열기
    private val getContent = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        if (uris.isNotEmpty()) viewModel.addImages(uris)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupConstraints()
        observeViewModel()

        isEditMode = intent.getBooleanExtra("IS_EDIT_MODE", false) //수정모드인지 확인
        if (isEditMode) setupEditMode()
    }

    //만약 수정 모드일 경우 활성화
    private fun setupEditMode() {
        editPostId = intent.getStringExtra("POST_ID")
        binding.etTitle.setText(intent.getStringExtra("TITLE"))
        binding.etContent.setText(intent.getStringExtra("CONTENT"))
        binding.tvRestaurantName.text = intent.getStringExtra("PLACE_NAME")

        // 변수 초기화
        viewModel.title.value = intent.getStringExtra("TITLE") ?: ""
        viewModel.content.value = intent.getStringExtra("CONTENT") ?: ""
        viewModel.restaurantId.value = intent.getIntExtra("PLACE_ID", -1)
        viewModel.rating.value = intent.getFloatExtra("RATING", 0.0f)
        binding.ratingBar.rating = viewModel.rating.value

        val existingImages = intent.getStringArrayListExtra("IMAGES")
        existingImages?.let { urls -> viewModel.setInitialImages(urls.map { Uri.parse(it) }) }
        binding.btnComplete.text = "수정 완료"
    }

    private fun setupUI() {
        // 리사이클러뷰 설정
        binding.rvImages.adapter = imageAdapter
        binding.btnAddImage.setOnClickListener {
            if (viewModel.displayImages.value.size < 10) getContent.launch("image/*")
        }


        //사진 추가 버튼 클릭
        binding.btnAddImage.setOnClickListener {
            if (viewModel.displayImages.value.size < 10) {
                getContent.launch("image/*")
            } else {
                Toast.makeText(this, "이미지는 최대 10장까지 가능합니다.", Toast.LENGTH_SHORT).show()
            }
        }

        //식당 선택 리스너
        binding.btnSelectRestaurant.setOnClickListener {
            selectRestaurantLauncher.launch(Intent(this, PostWriteRestaurantSearchActivity::class.java))
        }

        // 평점 변경 리스너
        binding.ratingBar.setOnRatingBarChangeListener { _, r, _ ->
            viewModel.rating.value = r
            binding.tvRatingValue.text = r.toString()
        }

        // 완료 버튼 클릭 리스너
        binding.btnComplete.setOnClickListener { viewModel.submitPost(isEditMode, editPostId) }

        binding.btnBack.setOnClickListener { finish() }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                //이미지 리스트 관찰 (이미지 추가/삭제 시 자동 UI 반영)
                launch {
                    viewModel.displayImages.collect { uris ->
                        Log.d("PostWrite", "현재 이미지 개수: ${uris.size}")
                        imageAdapter.submitList(uris)
                        binding.rvImages.visibility = if (uris.isEmpty()) View.GONE else View.VISIBLE
                    }
                }

                //버튼 활성화 상태
                launch {
                    viewModel.isFormValid.collect { isValid ->
                        binding.btnComplete.isEnabled = isValid
                        binding.btnComplete.setTextColor(
                            getColor(if (isValid) R.color.smblue else R.color.gray)
                        )
                    }
                }

                //서버 전송 상태 관찰 (로딩/성공/에러)
                launch {
                    viewModel.writeState.collect { state ->
                        when (state) {
                            is WriteUiState.Loading -> {
                                binding.btnComplete.isEnabled = false // 중복 클릭 방지
                            }
                            is WriteUiState.Success -> {
                                val msg = if (isEditMode) "수정되었습니다!" else "등록되었습니다!"
                                Toast.makeText(this@PostWriteActivity, msg, Toast.LENGTH_SHORT).show()

                                val intent = Intent(this@PostWriteActivity, PostDetailActivity::class.java).apply {
                                    putExtra("POST_ID", state.data.postId)
                                    putExtra("IS_LIKED", false)
                                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                }
                                startActivity(intent)
                                finish()
                            }
                            is WriteUiState.Error -> {
                                // 에러 시 다시 버튼 활성화 여부 판단 (isFormValid에 의해 결정됨)
                                Toast.makeText(this@PostWriteActivity, state.message, Toast.LENGTH_SHORT).show()
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }

    private fun setupConstraints() {
        // 제목 글자 수 0/100
        binding.etTitle.addTextChangedListener {
            viewModel.title.value = it.toString()
            binding.tvTitleCount.text = "${it?.length ?: 0}/100"
        }

        //본문 글자 수 감시 0/5000
        binding.etContent.addTextChangedListener {
            viewModel.content.value = it.toString()
            binding.tvContentCount.text = "${it?.length ?: 0}/5000"
        }
    }
}