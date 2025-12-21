package com.example.sookplace.ui.community.postWrite

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sookplace.R
import com.example.sookplace.databinding.ActivityPostWriteRestaurantSearchBinding
import com.example.sookplace.ui.search.SearchUiState
import com.example.sookplace.ui.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostWriteRestaurantSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostWriteRestaurantSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var searchAdapter: RestaurantSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostWriteRestaurantSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        viewModel.fetchSortedList(category = "all", sort = "popularity")

        searchAdapter = RestaurantSearchAdapter { item ->
            // 식당 선택 시 결과 반환
            val intent = Intent().apply {
                putExtra("restaurantId", item.id)
                putExtra("restaurantName", item.name)
            }
            setResult(RESULT_OK, intent)
            finish()
        }

        binding.rvSearchResults.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(this@PostWriteRestaurantSearchActivity)
        }

        binding.btnBack.setOnClickListener { finish() }

        // 검색창 엔터 처리
        binding.etSearch.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = v.text.toString()
                if (query.isNotBlank()) {
                    viewModel.searchByKeyword(query) // ViewModel의 검색 함수 호출
                }
                true
            } else false
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchState.collect { state ->
                    when (state) {
                        is SearchUiState.Success -> {
                            searchAdapter.submitList(state.list)
                        }
                        is SearchUiState.Loading -> {
                            // 로딩 바
                        }
                        is SearchUiState.Error -> {
                            Toast.makeText(this@PostWriteRestaurantSearchActivity, state.message, Toast.LENGTH_SHORT).show()
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}