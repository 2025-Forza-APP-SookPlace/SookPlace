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
    private val selectedImages = mutableListOf<Uri>() // 선택된 이미지 URI 저장
    private val imageAdapter by lazy {
        WriteImageAdapter { uri -> removeImage(uri) }
    }
    private var selectedRestaurantId: Int? = null // 서버로 보낼 식당 ID
    private var currentRating: Float = 0.0f      // 서버로 보낼 평점
    private val viewModel: PostWriteViewModel by viewModels()

    //식당 검색 화면에서 결과를 가져오기 위한 코드
    private val selectRestaurantLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val restaurantId = result.data?.getIntExtra("restaurantId", -1) ?: -1
            val restaurantName = result.data?.getStringExtra("restaurantName") ?: ""

            if (restaurantId != -1) {
                selectedRestaurantId = restaurantId
                binding.tvRestaurantName.text = restaurantName
                binding.tvRestaurantName.setTextColor(getColor(R.color.black))
                updateCompleteButtonState() // 식당이 선택되었으므로 완료 버튼 상태 갱신
            }
        }
    }

    //갤러리 열기
    private val getContent = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        if (uris.isNotEmpty()) {
            // 최대 10장까지만 허용
            Log.d("PhotoPicker", "선택된 이미지 개수: ${uris.size}")
            val remainingSpace = 10 - selectedImages.size
            val imagesToAdd = uris.take(remainingSpace)

            selectedImages.addAll(imagesToAdd)
            updateImageRecyclerView()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_write)

        binding = ActivityPostWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupConstraints()
    }

    private fun setupUI() {
        // 리사이클러뷰 설정
        binding.rvImages.apply {
            adapter = imageAdapter
            layoutManager = LinearLayoutManager(this@PostWriteActivity, LinearLayoutManager.HORIZONTAL, false)
        }

        //사진 추가 버튼 클릭
        binding.btnAddImage.setOnClickListener {
            Log.d("PhotoPicker", "버튼 클릭됨")
            if (selectedImages.size < 10) {
                getContent.launch("image/*") // 갤러리 실행
            } else {
                Toast.makeText(this, "이미지는 최대 10장까지 가능합니다.", Toast.LENGTH_SHORT).show()
            }
        }

        //식당 선택 리스너
        binding.btnSelectRestaurant.setOnClickListener {
            val intent = Intent(this, PostWriteRestaurantSearchActivity::class.java)
            selectRestaurantLauncher.launch(intent)

            updateCompleteButtonState()
        }

        // 평점 변경 리스너
        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            currentRating = rating
            binding.tvRatingValue.text = rating.toString()
            updateCompleteButtonState()
        }

        // 완료 버튼 클릭 리스너
        binding.btnComplete.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()
            val restaurantId = selectedRestaurantId ?: return@setOnClickListener

            viewModel.uploadPost(title, content, currentRating, restaurantId, selectedImages)
        }

        binding.btnBack.setOnClickListener { finish() }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.writeState.collect { state ->
                    when (state) {
                        is WriteUiState.Loading -> {
                            binding.btnComplete.isEnabled = false // 중복 클릭 방지
                            //로딩
                        }
                        is WriteUiState.Success -> {
                            Toast.makeText(this@PostWriteActivity, "글이 성공적으로 등록되었습니다!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@PostWriteActivity, PostDetailActivity::class.java).apply {
                                // 서버에서 받은 postId
                                putExtra("POST_ID", state.data.postId)
                                putExtra("IS_LIKED", false)
                                // 뒤로가기를 눌렀을 때 다시 작성 화면으로 오지 않도록 설정
                                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            }
                            startActivity(intent)
                            finish() // 작성 화면 종료 및 이전 화면으로 이동
                        }
                        is WriteUiState.Error -> {
                            binding.btnComplete.isEnabled = true
                            Toast.makeText(this@PostWriteActivity, state.message, Toast.LENGTH_SHORT).show()
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun updateImageRecyclerView() {
        if (selectedImages.isEmpty()) {
            binding.rvImages.visibility = View.GONE
        } else {
            binding.rvImages.visibility = View.VISIBLE
            imageAdapter.submitList(selectedImages.toList()) // 리스트 복사본 전달
        }
    }

    private fun removeImage(uri: Uri) {
        selectedImages.remove(uri)
        updateImageRecyclerView()
    }

    private fun setupConstraints() {
        // 제목 글자 수 0/100
        binding.etTitle.addTextChangedListener { text ->
            val count = text?.length ?: 0
            binding.tvTitleCount.text = "$count/100"
            updateCompleteButtonState() // 글자 바뀔 때마다 완료 버튼 상태 체크
        }

        //본문 글자 수 감시 0/5000
        binding.etContent.addTextChangedListener { text ->
            val count = text?.length ?: 0
            binding.tvContentCount.text = "$count/5000"
            updateCompleteButtonState()
        }
    }

    // 완료 버튼 활성화 조건 (제목과 내용이 모두 비어있지 않을 때)
    private fun updateCompleteButtonState() {
        val isTitleNotEmpty = binding.etTitle.text.isNotBlank()
        val isContentNotEmpty = binding.etContent.text.isNotBlank()
        val isRestaurantSelected = selectedRestaurantId != null
        val isRatingSet = currentRating > 0.0f

        val isEnabled = isTitleNotEmpty && isContentNotEmpty && isRestaurantSelected && isRatingSet

        binding.btnComplete.apply {
            this.isEnabled = isEnabled
            // 활성화 여부에 따라 색상 변경
            setTextColor(if (isEnabled) getColor(R.color.smblue) else getColor(R.color.gray))
        }
    }

}