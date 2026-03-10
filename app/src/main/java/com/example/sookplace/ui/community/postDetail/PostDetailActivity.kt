package com.example.sookplace.ui.community.postDetail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.isNestedScrollingEnabled
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import coil.load
import coil.transform.CircleCropTransformation
import com.example.sookplace.R
import com.example.sookplace.data.remote.response.PostDetailResponse
import com.example.sookplace.databinding.ActivityPostDetailBinding
import com.example.sookplace.ui.community.postWrite.PostWriteActivity
import com.google.android.material.internal.ViewUtils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostDetailBinding
    private val viewModel: PostDetailViewModel by viewModels()
    private var commentAdapter: CommentRVAdapter? = null

    private var postId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent로 postId 받기
        postId = intent.getStringExtra("POST_ID") ?: return
        val isLiked = intent.getBooleanExtra("IS_LIKED", false)

        viewModel.setInitialLikeState(isLiked)


        setupUI()
        observeViewModel(postId)
    }

    private fun setupUI() {
        // 뒤로가기 버튼
        binding.btnBack.setOnClickListener { finish() }

        // 댓글 리사이클러뷰 설정
        binding.rvComments.apply {
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(this@PostDetailActivity)
            isNestedScrollingEnabled = false // ScrollView 내부 스크롤 충돌 방지
        }

        //댓글 작성 버튼 클릭
        binding.btnCommentSubmit.setOnClickListener {
            val content = binding.etComment.text.toString().trim()
            Log.d("CommentTest", "전송 시도 - postId: $postId, content: $content")
            if (content.isNotEmpty()) {
                viewModel.submitComment(postId, content)
                binding.etComment.text.clear()
                hideSoftKeyboard()
            }
        }

        //게시물 수정 버튼 클릭
        binding.tvPostEdit.setOnClickListener {
            val currentState = viewModel.postDetail.value
            if (currentState is PostDetailViewModel.DetailUiState.Success) {
                val post = currentState.data

                val intent = Intent(this, PostWriteActivity::class.java).apply {
                    putExtra("IS_EDIT_MODE", true)
                    putExtra("POST_ID", postId)
                    putExtra("TITLE", post.title)
                    putExtra("CONTENT", post.content)
                    putExtra("PLACE_ID", post.place.placeId)
                    putExtra("PLACE_NAME", post.place.name)
                    // 이미지 리스트도 전달 (URL 리스트)
                    putStringArrayListExtra("IMAGES", ArrayList(post.images))
                }
                startActivity(intent)
            }
            Toast.makeText(this, "수정 화면으로 이동합니다.", Toast.LENGTH_SHORT).show()
        }

        //게시물 삭제 버튼 클릭
        binding.tvPostDelete.setOnClickListener {
            showDeletePostDialog()
        }

        //좋아요 버튼 클릭
        binding.heart.setOnClickListener {
            viewModel.toggleLike(postId)
        }
    }

    //게시물 삭제 다이얼로그
    private fun showDeletePostDialog() {
        AlertDialog.Builder(this)
            .setTitle("게시글 삭제")
            .setMessage("정말로 이 게시글을 삭제하시겠습니까?")
            .setPositiveButton("삭제") { _, _ ->
                viewModel.deletePost(postId)
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun hideSoftKeyboard() { //키보드 숨기기 코드
        val imm = getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        imm.hideSoftInputFromWindow(binding.etComment.windowToken, 0)
    }

    private fun observeViewModel(postId: String) {
        viewModel.fetchCurrentUserId()
        viewModel.getPostDetail(postId)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentUserId.collect { myId ->
                    if (myId != null) {
                        initAdapter(myId)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.postDetail.collect { state ->
                    when (state) {
                        is PostDetailViewModel.DetailUiState.Success -> {
                            val post = state.data
                            updatePostUI(post)
                            commentAdapter?.submitList(post.comments.toList())
                            binding.commentCount.text = post.commentCount.toString()

                            val myUserId = viewModel.currentUserId.value

                            if (myUserId != null) {
                                Log.d("AuthCheck", "게시글 작성자 ID: ${post.author.userId}")
                                Log.d("AuthCheck", "내 ID: $myUserId")

                                val isMine = (post.author.userId == myUserId)
                                Log.d("AuthCheck", "일치 여부(내 글인가?): $isMine")

                                // 일치하면 게시글 수정/삭제 메뉴 보이기
                                binding.layoutPostMenu.isVisible = isMine
                            }
                        }
                        is PostDetailViewModel.DetailUiState.Loading -> { /* 로딩 처리 */ }
                        is PostDetailViewModel.DetailUiState.Error -> { /* 에러 처리 */ }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.deleteState.collect { state ->
                    when (state) {
                        is PostDetailViewModel.DeleteUiState.Success -> {
                            Toast.makeText(this@PostDetailActivity, "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                            finish() // 삭제 성공 시 화면 종료
                        }
                        is PostDetailViewModel.DeleteUiState.Error -> {
                            Toast.makeText(this@PostDetailActivity, state.message, Toast.LENGTH_SHORT).show()
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun initAdapter(myId: String) {
        if (commentAdapter == null) {
            commentAdapter = CommentRVAdapter(
                currentUserId = myId,
                onDeleteClick = { commentId -> showDeleteDialog(commentId) }
            )
            binding.rvComments.adapter = commentAdapter
        }
    }

    private fun showDeleteDialog(commentId: String) {
        AlertDialog.Builder(this)
            .setMessage("댓글을 삭제하시겠습니까?")
            .setPositiveButton("삭제") { _, _ -> viewModel.deleteComment(commentId) }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun updatePostUI(post: PostDetailResponse) {
        binding.userName.text = post.author.nickname
        binding.time.text = post.displayTime
        binding.title.text = post.title
        binding.content.text = post.content
        binding.placeName.text = post.place.name
        binding.heartCount.text = post.likeCount.toString()
        binding.commentCount.text = post.commentCount.toString()

        // 프로필 및 메인 이미지 로드 (Coil)
        binding.profileImg.load(post.author.profileImageUrl) {
            transformations(CircleCropTransformation())
            placeholder(R.drawable.my_page_icon)
        }

        // ViewPager2 설정
        if (post.images.isNotEmpty()) {
            binding.vpPostImages.adapter = PostImageAdapter(post.images)
            binding.tvImageIndex.text = "1/${post.images.size}"

            // 페이지가 바뀔 때마다 인디케이터 업데이트
            binding.vpPostImages.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.tvImageIndex.text = "${position + 1}/${post.images.size}"
                }
            })
        } else {
            binding.vpPostImages.isVisible = false
            binding.tvImageIndex.isVisible = false
        }

        //좋아요
        binding.heartCount.text = post.likeCount.toString()
        if (post.likedByMe) {
            binding.heart.setImageResource(R.drawable.favorite_fill) // 채워진 하트
        } else {
            binding.heart.setImageResource(R.drawable.favorite)  // 빈 하트
        }
    }
}