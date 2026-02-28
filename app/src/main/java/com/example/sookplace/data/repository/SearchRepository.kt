package com.example.sookplace.data.repository

import com.example.sookplace.data.remote.api.SearchApi
import com.example.sookplace.data.remote.response.LikeToggleResponse
import com.example.sookplace.data.remote.response.SearchResponse
import com.example.sookplace.data.remote.response.SortResponse
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val searchApi: SearchApi
) {
    //식당 검색
    suspend fun searchByKeyword(query: String, page: Int): SearchResponse {
        return searchApi.searchRestaurants(query, null, page, 10, null)
    }

    //식당 정렬
    suspend fun getFilteredList(category: String, sort: String, page: Int): SortResponse {
        return searchApi.getSortedRestaurants(category, sort, "desc", page, 10)
    }

    //식당 좋아요
    suspend fun toggleLike(restaurantId: Int): LikeToggleResponse {
        return searchApi.toggleLike(restaurantId)
    }

}