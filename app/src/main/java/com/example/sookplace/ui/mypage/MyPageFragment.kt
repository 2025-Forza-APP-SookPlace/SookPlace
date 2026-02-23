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

        viewModel.loadMyPage()
        observeAuthViewModel()
    }

    private fun setupRecyclerViews() {
        myPlaceAdapter = MyPlacePreviewAdapter { item ->
            viewModel.checkLoginAndAction {
                // 식당 상세 페이지 이동 로직
            }
        }
        binding.newMyPlaceRecycler.apply {
            adapter = myPlaceAdapter
            layoutManager = GridLayoutManager(context, 2)
            isNestedScrollingEnabled = false
        }

        myPostAdapter = MyPostPreviewAdapter { item ->
            viewModel.checkLoginAndAction {
                // 게시글 상세 페이지 이동 로직
            }
        }
        binding.newMyPostRecycler.apply {
            adapter = myPostAdapter
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
        }
    }

    private fun setupListeners() {
        val loginListener = View.OnClickListener { navigateToLoginIfGuest() }
        binding.profileImage.setOnClickListener(loginListener)
        binding.username.setOnClickListener(loginListener)

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
        if (viewModel.isGuestMode.value) {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
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

                // [추가] 내 저장 장소 데이터 상태 관찰 및 안내 문구 띄우기
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

                // [추가] 내 작성 글 데이터 상태 관찰 및 안내 문구 띄우기
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