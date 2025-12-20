package com.example.sookplace.ui.home


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import coil.load
import com.example.sookplace.R
import com.example.sookplace.data.remote.response.RouletteSpinResponse
import com.example.sookplace.databinding.FragmentHomeBinding
import com.example.sookplace.ui.home.roulette.RouletteFragment
import com.example.sookplace.ui.home.roulette.RouletteLoadingFragment
import com.example.sookplace.ui.home.roulette.RouletteResultFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    private var loadingDialog: RouletteLoadingFragment? = null
    private var currentType: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //데이터바인딩
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        // ViewModel
        val adapter = FeaturedRestaurantAdapter()
        binding.featuredViewPager.adapter = adapter

        binding.featuredViewPager.apply {
            offscreenPageLimit = 3
            setPageTransformer { page, position ->
                page.scaleY = 0.85f + (1 - kotlin.math.abs(position)) * 0.15f
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                //프로필
                launch {
                    viewModel.userProfile.collect { user ->
                        if (user != null) {
                            binding.userprofile.visibility = View.VISIBLE
                            binding.nickname.text = user.nickname
                            binding.profileImg.load(user.avatarUrl)
                            binding.level.text = user.levelTitle
                        } else {
                            binding.userprofile.visibility = View.GONE
                        }
                    }
                }
                //오늘의 숙플레이스
                launch {
                    viewModel.featuredRestaurants.collect { list ->
                        Log.d("HOME", "featured size = ${list.size}")
                        adapter.submitList(list)
                    }
                }
                //룰렛 돌리기
                launch {
                    viewModel.rouletteState.collect { state ->
                        when (state) {
                            is RouletteUiState.Loading -> {
                                // 로딩 다이얼로그 띄우기
                                if (loadingDialog == null) {
                                    loadingDialog = RouletteLoadingFragment()
                                    loadingDialog?.show(parentFragmentManager, "Loading")
                                }
                            }
                            is RouletteUiState.Success -> {
                                // 서버 응답 성공 시 결과 다이얼로그 띄우기
                                loadingDialog?.dismiss()
                                loadingDialog = null

                                showRouletteResultDialog(state.data)
                                viewModel.resetRouletteState()
                            }
                            is RouletteUiState.Error -> {
                                loadingDialog?.dismiss()
                                loadingDialog = null

                                Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                                viewModel.resetRouletteState()
                            }
                            else -> {}
                        }
                    }
                }
            }
        }

        // Fragment onCreateView 또는 onViewCreated에서 호출
        viewModel.loadFeaturedRestaurants()


//        // 로그아웃 버튼 예시
//        binding.logoutBtn.setOnClickListener {
//            viewModel.logout()
//            startActivity(Intent(requireContext(), LoginActivity::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            })
//        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //음식 카테고리 버튼 클릭
        binding.foodCategoryBtn1.setOnClickListener {
            val bundle = Bundle().apply {
                putString("category", "치킨")
            }
            it.findNavController().navigate(
                R.id.action_homeFragment_to_searchFragment,
                bundle
            )
        }
        binding.foodCategoryBtn2.setOnClickListener {
            val bundle = Bundle().apply {
                putString("category", "카페")
            }
            it.findNavController().navigate(
                R.id.action_homeFragment_to_searchFragment,
                bundle
            )
        }
        binding.foodCategoryBtn3.setOnClickListener {
            val bundle = Bundle().apply {
                putString("category", "한식")
            }
            it.findNavController().navigate(
                R.id.action_homeFragment_to_searchFragment,
                bundle
            )
        }
        binding.foodCategoryBtn4.setOnClickListener {
            val bundle = Bundle().apply {
                putString("category", "분식")
            }
            it.findNavController().navigate(
                R.id.action_homeFragment_to_searchFragment,
                bundle
            )
        }
        binding.foodCategoryBtn5.setOnClickListener {
            val bundle = Bundle().apply {
                putString("category", "양식")
            }
            it.findNavController().navigate(
                R.id.action_homeFragment_to_searchFragment,
                bundle
            )
        }
        binding.foodCategoryBtn6.setOnClickListener {
            val bundle = Bundle().apply {
                putString("category", "디저트")
            }
            it.findNavController().navigate(
                R.id.action_homeFragment_to_searchFragment,
                bundle
            )
        }

        //룰렛 버튼 클릭
        binding.rouletteBtn.setOnClickListener {
            val currentUser = viewModel.userProfile.value

            if (currentUser == null) {
                //비로그인 상태: 토스트 메시지
                android.widget.Toast.makeText(
                    requireContext(),
                    "로그인 후 이용 가능합니다.",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            } else {
                //로그인 상태: 룰렛 다이얼로그 호출
                showRouletteDialog()
            }
        }


        ///하단바 프래그먼트 간의 이동 구현
        binding.searchTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }

        binding.mapTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_mapFragment)
        }

        binding.communityTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_communityFragment)
        }

        binding.mypageTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_mypageFragment)
        }
    }

    // 옵션 다이얼로그 띄우기
    private fun showRouletteDialog() {
        val roulette = RouletteFragment()

        // 사용자가 옵션을 선택했을 때 호출될 리스너 (RouletteFragment에 이 함수를 만들어야 함)
        roulette.setOptionClickListener { type ->
            currentType = type

            val mode = when(type) {
                1 -> "myplace"
                2 -> "category"
                else -> "top20"
            }
            // ViewModel을 통해 서버에 요청 보냄
            viewModel.spinRoulette(mode)
        }

        roulette.show(parentFragmentManager, "RouletteOptions")
    }

    // 결과 다이얼로그 띄우기
    private fun showRouletteResultDialog(data: RouletteSpinResponse) {
        val resultDialog = RouletteResultFragment().apply {
            this.resultData = data
            this.optionType = currentType

            // 다시 돌리기 콜백
            this.onRetry = {
                val mode = when(currentType) {
                    1 -> "myplace"
                    2 -> "category"
                    else -> "top20"
                }
                viewModel.spinRoulette(mode)
            }

            // 다른 옵션 선택 콜백
            this.onReset = {
                viewModel.clearExcludedIds()
                showRouletteDialog()
            }
        }
        resultDialog.show(parentFragmentManager, "RouletteResult")
    }

}

