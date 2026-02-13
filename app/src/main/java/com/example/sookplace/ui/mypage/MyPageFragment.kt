package com.example.sookplace.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.sookplace.ui.mypage.myplace.MyPlaceActivity
import com.example.sookplace.ui.mypage.myplace.MyPlacePreviewAdapter
import com.example.sookplace.ui.mypage.mypost.MyPostActivity
import com.example.sookplace.ui.mypage.mypost.MyPostPreviewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyPageViewModel by viewModels()

    private lateinit var myPlaceAdapter: MyPlacePreviewAdapter
    private lateinit var myPostAdapter: MyPostPreviewAdapter

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
    }

    private fun setupRecyclerViews() {
        // My Places (그리드)
        myPlaceAdapter = MyPlacePreviewAdapter { item ->
            viewModel.checkLoginAndAction {
                // TODO: 식당 상세 페이지로 이동
            }
        }
        binding.newMyPlaceRecycler.apply {
            adapter = myPlaceAdapter
            layoutManager = GridLayoutManager(context, 2)
            isNestedScrollingEnabled = false
        }

        // My Posts (리스트)
        myPostAdapter = MyPostPreviewAdapter { item ->
            viewModel.checkLoginAndAction {
                // TODO: 게시글 상세 페이지로 이동
            }
        }
        binding.newMyPostRecycler.apply {
            adapter = myPostAdapter
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
        }
    }

    private fun setupListeners() {
        // 1. 프로필 클릭 시 로그인
        val loginListener = View.OnClickListener { navigateToLoginIfGuest() }
        binding.profileImage.setOnClickListener(loginListener)
        binding.username.setOnClickListener(loginListener)

        // 2. 내 장소 전체보기
        binding.myPlaceMoreBtn.setOnClickListener {
            viewModel.checkLoginAndAction {
                val intent = Intent(requireContext(), MyPlaceActivity::class.java)
                startActivity(intent)
            }
        }

        // 3. 내 글 전체보기
        binding.myPostMoreBtn.setOnClickListener {
            viewModel.checkLoginAndAction {
                val intent = Intent(requireContext(), MyPostActivity::class.java)
                startActivity(intent)
            }
        }

        // 4. 알림 설정
        binding.notificationRow.setOnClickListener {
            viewModel.checkLoginAndAction {
                Toast.makeText(requireContext(), "알림 설정 화면 준비 중입니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 5. 계정 관리
        binding.accountManageRow.setOnClickListener {
            viewModel.checkLoginAndAction {
                Toast.makeText(requireContext(), "계정 관리 화면 준비 중입니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 6. 로그아웃
        binding.logoutRow.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        // 7. 하단 네비게이션 탭 이동
        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_mypageFragment_to_homeFragment)
        }
        binding.searchTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_mypageFragment_to_searchFragment)
        }
        binding.mapTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_mypageFragment_to_mapFragment)
        }
        binding.communityTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_mypageFragment_to_communityFragment)
        }
    }

    private fun navigateToLoginIfGuest() {
        if (viewModel.isGuestMode.value) {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.userMe.collect { user ->
                        user?.let {
                            binding.username.text = it.nickname
                            binding.email.text = it.email
                            binding.badge.visibility = if (it.sookVerified) View.VISIBLE else View.GONE

                            // [수정] 에러가 발생하는 이미지 로드 부분을 잠시 주석 처리했습니다.
                            // UserMeResponse 파일(데이터 클래스)을 열어서 실제 이미지 변수명이 무엇인지 확인하세요.
                            // (예: avatarUrl, imageUrl, profileImage 등)
                            /*
                            if (!it.profileImageUrl.isNullOrEmpty()) {
                                Glide.with(this@MyPageFragment)
                                    .load(it.profileImageUrl)
                                    .circleCrop()
                                    .into(binding.profileImage)
                            } else {
                                binding.profileImage.setImageResource(R.drawable.egg_song)
                            }
                            */

                            // 일단 기본 이미지로 설정하여 앱이 실행되도록 합니다.
                            binding.profileImage.setImageResource(R.drawable.egg_song)
                        }
                    }
                }

                launch {
                    viewModel.userQuests.collect { questData ->
                        if (questData != null) {
                            binding.tvLevelTitle.text = "Level ${questData.currentLevel}"
                            binding.tvLevelDescription.text = if (questData.nextLevel != null) "다음 레벨까지" else "최고 레벨입니다!"
                            binding.pbLevelProgress.progress = 50
                        } else {
                            binding.tvLevelTitle.text = "레벨 정보 없음"
                            binding.tvLevelDescription.text = "로그인 후 확인하세요"
                            binding.pbLevelProgress.progress = 0
                        }
                    }
                }

                launch {
                    viewModel.userStats.collect { stats ->
                        stats?.let {
                            binding.tvPostCount.text = "${it.postCount}"
                            binding.tvSavedPlaceCount.text = "${it.savedPlaceCount}"
                            binding.tvReceivedLikeCount.text = "${it.receivedLikeCount}"
                        }
                    }
                }

                launch {
                    viewModel.myPlaces.collect { myPlaceAdapter.submitList(it) }
                }

                launch {
                    viewModel.myPosts.collect { myPostAdapter.submitList(it) }
                }

                launch {
                    viewModel.errorMessage.collect { msg ->
                        msg?.let {
                            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}