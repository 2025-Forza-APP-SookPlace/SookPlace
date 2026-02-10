package com.example.sookplace.ui.mypage

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.sookplace.databinding.FragmentMypageBinding
import com.example.sookplace.ui.mypage.adapter.MyPlaceAdapter
import com.example.sookplace.ui.mypage.adapter.MyPostAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyPageViewModel by viewModels()

    private lateinit var myPlaceAdapter: MyPlaceAdapter
    private lateinit var myPostAdapter: MyPostAdapter

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
        myPlaceAdapter = MyPlaceAdapter { item ->
            // TODO: 식당 상세 페이지로 이동
        }
        binding.newMyPlaceRecycler.apply {
            adapter = myPlaceAdapter
            layoutManager = GridLayoutManager(context, 2)
        }

        // My Posts (리스트)
        myPostAdapter = MyPostAdapter { item ->
            // TODO: 게시글 상세 페이지로 이동
        }
        binding.newMyPostRecycler.apply {
            adapter = myPostAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupListeners() {
        // 새로고침 기능이 있다면 viewModel.fetchAllMyPageData() 호출

        binding.myPlaceMoreBtn.setOnClickListener {
            // TODO: 내 장소 전체보기 화면으로 이동
        }

        binding.myPostMoreBtn.setOnClickListener {
            // TODO: 내 글 전체보기 화면으로 이동
        }

        binding.notificationRow.setOnClickListener {
            // TODO: 알림 설정 화면 이동
        }

        // 기타 버튼 리스너...
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 1. 프로필 정보 관찰
                launch {
                    viewModel.userMe.collect { user ->
                        user?.let {
                            binding.username.text = it.nickname
                            binding.email.text = it.email
                            binding.badge.visibility = if (it.sookVerified) View.VISIBLE else View.GONE

                            // 이미지 로드 (Glide)
                            Glide.with(this@MyPageFragment)
                                .load(it.avatarUrl)
                                .circleCrop()
                                .into(binding.profileImage)
                        }
                    }
                }

                // 2. 퀘스트 정보 관찰
                launch {
                    viewModel.userQuests.collect { questData ->
                        questData?.let {
                            binding.tvLevelTitle.text = "Level ${it.currentLevel}"
                            // 다음 레벨 문구는 로직에 따라 동적으로
                            binding.tvLevelDescription.text = if (it.nextLevel != null) {
                                "다음 레벨까지"
                            } else {
                                "최고 레벨입니다!"
                            }
                            // 메인 레벨 프로그래스 (서버에서 전체 %를 주거나 계산해야 함. 일단 첫 번째 퀘스트 % 등으로 대체하거나 별도 필드 필요)
                            // 여기서는 예시로 0 처리 혹은 별도 필드 매핑
                            binding.pbLevelProgress.progress = 50

                            // 퀘스트 리스트 바인딩 (3개 고정)
                            val quests = it.quests
                            if (quests.size >= 3) {
                                // Quest 1
                                binding.tvQuest1Title.text = quests[0].title
                                binding.tvQuest1Desc.text = quests[0].description
                                binding.pbQuest1.progress = quests[0].progressPercent

                                // Quest 2
                                binding.tvQuest2Title.text = quests[1].title
                                binding.tvQuest2Desc.text = quests[1].description
                                binding.pbQuest2.progress = quests[1].progressPercent

                                // Quest 3
                                binding.tvQuest3Title.text = quests[2].title
                                binding.tvQuest3Desc.text = quests[2].description
                                binding.pbQuest3.progress = quests[2].progressPercent
                            }
                        }
                    }
                }

                // 3. 통계 관찰
                launch {
                    viewModel.userStats.collect { stats ->
                        stats?.let {
                            binding.tvPostCount.text = "${it.postCount}"
                            binding.tvSavedPlaceCount.text = "${it.savedPlaceCount}"
                            binding.tvReceivedLikeCount.text = "${it.receivedLikeCount}"
                        }
                    }
                }

                // 4. 리스트 관찰
                launch {
                    viewModel.myPlaces.collect { items ->
                        myPlaceAdapter.submitList(items)
                    }
                }

                launch {
                    viewModel.myPosts.collect { items ->
                        myPostAdapter.submitList(items)
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