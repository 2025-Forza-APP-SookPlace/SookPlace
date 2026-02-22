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
import android.widget.ArrayAdapter
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
import androidx.recyclerview.widget.RecyclerView
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
        setupDropdowns() //카테고리 정렬 로직 구현
        observeViewModel()  //

        //카테고리별 검색
        val category = arguments?.getString("category")
        val initialDisplayText = category ?: "전체"
        binding.categoryDropdown.setText(initialDisplayText, false)

        val initialCategoryKey = getCategoryKey(initialDisplayText)
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
        val linearLayoutManager = LinearLayoutManager(requireContext())

        binding.searchRv.apply {
            adapter = searchAdapter
            layoutManager = linearLayoutManager

            addOnScrollListener(object : RecyclerView.OnScrollListener() { //무한 스크롤 리스너
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    // 스크롤을 아래로 내릴 때만 동작
                    if (dy > 0) {
                        val visibleItemCount = linearLayoutManager.childCount
                        val totalItemCount = linearLayoutManager.itemCount
                        val firstVisibleItemPosition =
                            linearLayoutManager.findFirstVisibleItemPosition()

                        // 바닥에 닿았는지 계산
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                            // 뷰모델에게 다음 페이지 요청
                            viewModel.loadNextPage()
                        }
                    }
                }
            })
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

    private fun setupDropdowns() {
        // 드롭다운에 보여줄 목록 리스트 생성
        val categories = arrayOf("전체", "치킨", "카페", "한식", "분식", "양식", "디저트")
        val sorts = arrayOf("인기순", "가까운순", "평점순")

        // 어댑터 연결
        val categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
        val sortAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, sorts)

        binding.categoryDropdown.setAdapter(categoryAdapter)
        binding.sortDropdown.setAdapter(sortAdapter)

        binding.categoryDropdown.setDropDownBackgroundResource(R.drawable.background_radius)
        binding.sortDropdown.setDropDownBackgroundResource(R.drawable.background_radius)

        // 카테고리 클릭 이벤트 처리
        binding.categoryDropdown.setOnItemClickListener { _, _, position, _ ->
            val selectedCategory = categories[position]
            viewModel.fetchSortedList(category = getCategoryKey(selectedCategory))
        }

        // 정렬 클릭 이벤트 처리
        binding.sortDropdown.setOnItemClickListener { _, _, position, _ ->
            val selectedSort = sorts[position]

            val sortKey = when(selectedSort) {
                "가까운순" -> "distance"
                "평점순" -> "rating"
                else -> "popularity"
            }
            viewModel.fetchSortedList(sort = sortKey)
        }
    }
    //한글 카테고리명을 서버 API용 영어 키워드로 변환
    private fun getCategoryKey(koreanCategory: String): String {
        return when (koreanCategory) {
            "치킨" -> "chicken"
            "카페" -> "cafe"
            "한식" -> "korean"
            "분식" -> "bunsik"
            "양식" -> "western"
            "디저트" -> "dessert"
            else -> "all" // "전체" 또는 그 외의 경우
        }
    }
}