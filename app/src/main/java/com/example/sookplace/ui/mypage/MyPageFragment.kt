package com.example.sookplace.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.sookplace.R
import com.example.sookplace.databinding.FragmentMypageBinding
import com.example.sookplace.ui.login.LoginActivity
import com.example.sookplace.ui.mypage.adapter.MyPlaceAdapter
import com.example.sookplace.ui.mypage.adapter.MyPostAdapter
import com.example.sookplace.ui.mypage.auth.AuthViewModel
import com.example.sookplace.ui.mypage.myplace.MyPlaceActivity
import com.example.sookplace.ui.mypage.myplace.MyPlacePreviewAdapter
import com.example.sookplace.ui.mypage.mypost.MyPostActivity
import com.example.sookplace.ui.mypage.mypost.MyPostPreviewAdapter
import com.example.sookplace.ui.mypage.settings.AccountManagementActivity
import com.example.sookplace.ui.mypage.settings.NotificationSettingsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyPageViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels() //로그아웃api 호출을 위해 추가

    // 어댑터 (클릭 시 로그인 여부 체크 후 이동)
    private val myPlaceAdapter by lazy {
        MyPlacePreviewAdapter { item ->
            viewModel.checkLoginAndAction {
                // TODO: 장소 상세 페이지 이동 로직
                Toast.makeText(requireContext(), "${item.name} 클릭됨", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val myPostAdapter by lazy {
        MyPostPreviewAdapter { item ->
            viewModel.checkLoginAndAction {
                // TODO: 게시글 상세 페이지 이동 로직
                Toast.makeText(requireContext(), "${item.title} 클릭됨", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 1~5 레벨에 따른 칭호 매핑용 배열
    private val levelNames = arrayOf("", "알송이", "어린이송이", "청소년송이", "대학생송이", "으른송이")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        setupListeners()
        observeViewModel()

        // 뷰모델을 통해 마이페이지의 모든 데이터 로드
        viewModel.loadMyPage()
        observeAuthViewModel()
    }

    private fun setupRecyclerViews() {
        binding.newMyPlaceRecycler.apply {
            adapter = myPlaceAdapter
            layoutManager = GridLayoutManager(context, 2)
            isNestedScrollingEnabled = false
        }

        binding.newMyPostRecycler.apply {
            adapter = myPostAdapter
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
        }
    }

    private fun setupListeners() {
        // 프로필 클릭 시 비로그인이면 로그인 화면으로 이동
        val loginListener = View.OnClickListener { navigateToLoginIfGuest() }
        binding.profileImage.setOnClickListener(loginListener)
        binding.username.setOnClickListener(loginListener)
        binding.email.setOnClickListener(loginListener) // 이메일 누를 때도 로그인 화면 이동

        // 전체보기 버튼
        binding.myPlaceMoreBtn.setOnClickListener {
            viewModel.checkLoginAndAction {
                startActivity(Intent(requireContext(), MyPlaceActivity::class.java))
            }
        }

        binding.myPostMoreBtn.setOnClickListener {
            viewModel.checkLoginAndAction {
                startActivity(Intent(requireContext(), MyPostActivity::class.java))
            }
        }

        // 설정 항목
        binding.notificationRow.setOnClickListener {
            viewModel.checkLoginAndAction {
                val intent = Intent(requireContext(), NotificationSettingsActivity::class.java)
                startActivity(intent)
            }
        }

        binding.accountManageRow.setOnClickListener {
            viewModel.checkLoginAndAction {
                val intent = Intent(requireContext(), AccountManagementActivity::class.java)
                startActivity(intent)
            }
        }

        binding.logoutRow.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("로그아웃")
                .setMessage("정말 로그아웃 하시겠습니까?")
                .setPositiveButton("로그아웃") { _, _ ->
                    authViewModel.logout()
                }
                .setNegativeButton("취소", null)
                .show()

        }

        // 하단 네비게이션
        binding.homeTap.setOnClickListener { it.findNavController().navigate(R.id.action_mypageFragment_to_homeFragment) }
        binding.searchTap.setOnClickListener { it.findNavController().navigate(R.id.action_mypageFragment_to_searchFragment) }
        binding.mapTap.setOnClickListener { it.findNavController().navigate(R.id.action_mypageFragment_to_mapFragment) }
        binding.communityTap.setOnClickListener { it.findNavController().navigate(R.id.action_mypageFragment_to_communityFragment) }
    }

    private fun navigateToLoginIfGuest() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // 1. [핵심] 프로필 정보 및 서버 아바타 이미지 바인딩
                launch {
                    viewModel.userMe.collect { user ->
                        if (user != null) {
                            binding.username.text = user.nickname
                            binding.email.text = user.email
                            binding.badge.visibility = if (user.sookVerified) View.VISIBLE else View.GONE

                            // API 명세서에 맞춰 avatarUrl로 이미지를 로드합니다.
                            if (!user.avatarUrl.isNullOrEmpty()) {
                                Glide.with(requireContext())
                                    .load(user.avatarUrl)
                                    .error(R.drawable.ic_user) // 로딩 실패 시 표시할 이미지 (ic_user로 변경)
                                    .into(binding.profileImage)
                            } else {
                                binding.profileImage.setImageResource(R.drawable.ic_user) // 기본 이미지 (ic_user로 변경)
                            }
                        } else {
                            // 데이터가 없을 때 (비로그인 상태 등) 기본 문구와 이미지 표시
                            binding.username.text = "로그인이 필요합니다"
                            binding.email.text = "여기를 눌러 로그인해주세요"
                            binding.badge.visibility = View.GONE
                            binding.profileImage.setImageResource(R.drawable.ic_user) // 기본 이미지 (ic_user로 변경)
                        }
                    }
                }

                // 2. 퀘스트 정보 바인딩
                launch {
                    viewModel.userQuests.collect { questData ->
                        if (questData != null && questData.quests.isNotEmpty()) {
                            // 현재 레벨 이름
                            val currentLvlName = levelNames.getOrNull(questData.currentLevel) ?: "Lv.${questData.currentLevel}"
                            binding.tvLevelTitle.text = currentLvlName

                            // 다음 레벨 및 전체 프로그레스
                            if (questData.nextLevel != null) {
                                val nextLvlName = levelNames.getOrNull(questData.nextLevel!!) ?: "Lv.${questData.nextLevel}"
                                binding.tvLevelDescription.text = "${nextLvlName}까지 달성해 보세요!"
                                val avgProgress = questData.quests.map { it.progressPercent }.average().toInt()
                                binding.pbLevelProgress.progress = avgProgress
                            } else {
                                binding.tvLevelDescription.text = "최고 레벨입니다!"
                                binding.pbLevelProgress.progress = 100
                            }

                            // 퀘스트 1 (리뷰)
                            val q1 = questData.quests.find { it.type == "review" }
                            if (q1 != null) {
                                binding.tvQuest1Title.text = "✍ ${q1.title}"
                                binding.tvQuest1Desc.text = q1.description
                                binding.pbQuest1.progress = q1.progressPercent
                            }

                            // 퀘스트 2 (댓글)
                            val q2 = questData.quests.find { it.type == "comment" }
                            if (q2 != null) {
                                binding.tvQuest2Title.text = "💬 ${q2.title}"
                                binding.tvQuest2Desc.text = q2.description
                                binding.pbQuest2.progress = q2.progressPercent
                            }

                            // 퀘스트 3 (방문)
                            val q3 = questData.quests.find { it.type == "visit" }
                            if (q3 != null) {
                                binding.tvQuest3Title.text = "🏃 ${q3.title}"
                                binding.tvQuest3Desc.text = q3.description
                                binding.pbQuest3.progress = q3.progressPercent
                            }

                        } else {
                            // 퀘스트 데이터 없음 또는 비로그인
                            binding.tvLevelTitle.text = "레벨 정보 없음"
                            binding.tvLevelDescription.text = "로그인 후 확인하세요"
                            binding.pbLevelProgress.progress = 0
                            binding.tvQuest1Desc.text = "데이터를 불러올 수 없습니다."
                            binding.pbQuest1.progress = 0
                            binding.tvQuest2Desc.text = "데이터를 불러올 수 없습니다."
                            binding.pbQuest2.progress = 0
                            binding.tvQuest3Desc.text = "데이터를 불러올 수 없습니다."
                            binding.pbQuest3.progress = 0
                        }
                    }
                }

                // 3. 현황 리포트 (통계) 안전하게 바인딩
                launch {
                    viewModel.userStats.collect { stats ->
                        if (stats != null) {
                            binding.tvPostCount.text = "${stats.postCount}"
                            binding.tvSavedPlaceCount.text = "${stats.savedPlaceCount}"
                            binding.tvReceivedLikeCount.text = "${stats.receivedLikeCount}"
                        } else {
                            // 데이터가 없거나 비로그인 상태일 때 빈 값이 되지 않도록 방지
                            binding.tvPostCount.text = "0"
                            binding.tvSavedPlaceCount.text = "0"
                            binding.tvReceivedLikeCount.text = "0"
                        }
                    }
                }

                // 4. 내 저장 장소 리스트
                launch {
                    viewModel.myPlaces.collect { items ->
                        myPlaceAdapter.submitList(items)
                        if (items.isEmpty()) {
                            binding.tvEmptyMyPlace.visibility = View.VISIBLE
                            binding.newMyPlaceRecycler.visibility = View.GONE
                        } else {
                            binding.tvEmptyMyPlace.visibility = View.GONE
                            binding.newMyPlaceRecycler.visibility = View.VISIBLE
                        }
                    }
                }

                // 5. 내 작성 글 리스트
                launch {
                    viewModel.myPosts.collect { posts ->
                        myPostAdapter.submitList(posts)
                        if (posts.isEmpty()) {
                            binding.tvEmptyMyPost.visibility = View.VISIBLE
                            binding.newMyPostRecycler.visibility = View.GONE
                        } else {
                            binding.tvEmptyMyPost.visibility = View.GONE
                            binding.newMyPostRecycler.visibility = View.VISIBLE
                        }
                    }
                }

                // 6. 에러 메시지
                launch {
                    viewModel.errorMessage.collect { msg ->
                        msg?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }
                    }
                }
            }
        }
    }

    private fun observeAuthViewModel() { //로그아웃 결과를 관찰하는 함수
        authViewModel.logoutSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(requireContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "로그아웃에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}