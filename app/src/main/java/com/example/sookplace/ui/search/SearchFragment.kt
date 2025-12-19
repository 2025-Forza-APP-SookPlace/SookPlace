package com.example.sookplace.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sookplace.R
import com.example.sookplace.databinding.FragmentSearchBinding
import com.example.sookplace.ui.search.restaurantDetail.RestaurantDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private val searchAdapter = SearchRVAdapter(
        onItemClick = { id ->
            val intent = Intent(requireContext(), RestaurantDetailActivity::class.java).apply {
                putExtra("RESTAURANT_ID", id)
            }
            startActivity(intent)
        },
        onLikeClick = { item ->
            viewModel.toggleLike(item)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

        setupRecyclerView() //RV 구현
        setupSearchInput()  //검색창 로직 구현
        setupCategoryButtons() //카테고리 정렬 로직 구현
        observeViewModel()  //

        //카테고리별 검색
        val category = arguments?.getString("category")

        when (category) {
            "치킨" -> binding.foodCategoryBtn1.isChecked = true
            "카페" -> binding.foodCategoryBtn2.isChecked = true
            "한식" -> binding.foodCategoryBtn3.isChecked = true
            "분식" -> binding.foodCategoryBtn4.isChecked = true
            "양식" -> binding.foodCategoryBtn5.isChecked = true
            "디저트" -> binding.foodCategoryBtn6.isChecked = true
            else -> binding.foodCategoryBtn0.isChecked = true // 전체
        }

        val initialCategoryKey = when(category) {
            "치킨" -> "chicken"
            "카페" -> "cafe"
            "한식" -> "korean"
            "분식" -> "bunsik"
            "양식" -> "western"
            "디저트" -> "dessert"
            else -> "all"
        }
        viewModel.fetchSortedList(category = initialCategoryKey)

        ///하단바 프래그먼트 간의 이동 구현
        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_searchFragment_to_homeFragment)
        }

        binding.mapTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_searchFragment_to_mapFragment)
        }

        binding.communityTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_searchFragment_to_communityFragment)
        }

        binding.mypageTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_searchFragment_to_mypageFragment)
        }

        return binding.root
    }

    //키보드 숨기기 함수
    private fun hideKeyboard(view: View) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    //RV 구현 함수
    private fun setupRecyclerView() {
        binding.searchRv.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    //검색 입력 구현 함수
    private fun setupSearchInput() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ -> //검색
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.etSearch.text.toString()
                if (query.isNotBlank()) {
                    viewModel.searchByKeyword(query)
                    hideKeyboard(binding.etSearch)
                    binding.etSearch.clearFocus()
                }
                true
            } else false
        }
        binding.etSearch.doOnTextChanged { text, _, _, _ -> //EditText에 글자가 있을때만 X버튼 노출
            binding.ivClear.isVisible = !text.isNullOrEmpty()
        }
        binding.ivClear.setOnClickListener { binding.etSearch.text.clear() } //X버튼 클릭 시 텍스트 삭제
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchState.collect { state ->
                    when (state) {
                        is SearchUiState.Loading -> {
                            // 로딩 바 표시 (있다면)
                        }

                        is SearchUiState.Success -> {
                            searchAdapter.submitList(state.list)
                        }

                        is SearchUiState.Error -> {
                            Log.e("SearchFragment", state.message)
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun setupCategoryButtons() {
        val categoryMap = mapOf(
            binding.foodCategoryBtn0 to "all",
            binding.foodCategoryBtn1 to "chicken",
            binding.foodCategoryBtn2 to "cafe",
            binding.foodCategoryBtn3 to "korean",
            binding.foodCategoryBtn4 to "bunsik",
            binding.foodCategoryBtn5 to "western",
            binding.foodCategoryBtn6 to "dessert"
        )

        categoryMap.forEach { (button, categoryKey) ->
            button.setOnClickListener {
                viewModel.fetchSortedList(category = categoryKey)

                binding.etSearch.text.clear()
                binding.etSearch.clearFocus()
            }
        }
    }
}