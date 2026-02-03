package com.example.sookplace.data.repository

import com.example.sookplace.data.remote.api.SearchApi
import com.example.sookplace.data.remote.response.RestaurantContent
import com.example.sookplace.data.remote.response.SearchResponse
import com.example.sookplace.data.remote.response.SortRestaurantItem
import com.example.sookplace.data.remote.response.SortResponse
import kotlinx.coroutines.delay
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val searchApi: SearchApi
) {
    private val USE_DUMMY = false
    private val dummyAllList = listOf(
        SortRestaurantItem(
            id = 1,
            name = "핀치",
            category = "카페",
            thumbnailUrl = "https://picsum.photos/id/102/200/200",
            rating = 4.8,
            likeCount = 120,
            isLiked = true,
            distanceMinutesFromCampus = 5,
            locationName = "서울 용산구 청파로47길 52",
            shareUrl = "https://m.place.naver.com/restaurant/1053744318" // 실제 예시 링크
        ),
        SortRestaurantItem(
            id = 2,
            name = "구복만두",
            category = "한식",
            thumbnailUrl = "https://picsum.photos/id/103/200/200",
            rating = 4.5,
            likeCount = 300,
            isLiked = false,
            distanceMinutesFromCampus = 8,
            locationName = "서울 용산구 두텁바위로 7",
            shareUrl = "https://m.place.naver.com/restaurant/37283204"
        ),
        SortRestaurantItem(
            id = 3,
            name = "굽네치킨 숙대점",
            category = "치킨",
            thumbnailUrl = "https://picsum.photos/id/104/200/200",
            rating = 4.3,
            likeCount = 45,
            isLiked = false,
            distanceMinutesFromCampus = 10,
            locationName = "서울 용산구 청파로",
            shareUrl = "https://m.place.naver.com/restaurant/31038555"
        ),
        SortRestaurantItem(
            id = 4,
            name = "신내떡",
            category = "분식",
            thumbnailUrl = "https://picsum.photos/id/106/200/200",
            rating = 4.6,
            likeCount = 150,
            isLiked = true,
            distanceMinutesFromCampus = 3,
            locationName = "서울 용산구 청파로45길",
            shareUrl = "https://m.place.naver.com/restaurant/35438848"
        ),
        SortRestaurantItem(
            id = 5,
            name = "나폴리키친",
            category = "양식",
            thumbnailUrl = "https://picsum.photos/id/107/200/200",
            rating = 4.7,
            likeCount = 88,
            isLiked = false,
            distanceMinutesFromCampus = 6,
            locationName = "서울 용산구 청파로47길",
            shareUrl = "https://m.place.naver.com/restaurant/21018519"
        ),
        SortRestaurantItem(
            id = 6,
            name = "와플하우스",
            category = "디저트",
            thumbnailUrl = "https://picsum.photos/id/108/200/200",
            rating = 4.9,
            likeCount = 500,
            isLiked = true,
            distanceMinutesFromCampus = 4,
            locationName = "서울 용산구 청파로45길",
            shareUrl = "https://m.place.naver.com/restaurant/11613274"
        )
    )
    private val categoryMap = mapOf(
        "all" to dummyAllList,
        "chicken" to dummyAllList.filter { it.category == "치킨" },
        "cafe" to dummyAllList.filter { it.category == "카페" },
        "korean" to dummyAllList.filter { it.category == "한식" },
        "bunsik" to dummyAllList.filter { it.category == "분식" },
        "western" to dummyAllList.filter { it.category == "양식" },
        "dessert" to dummyAllList.filter { it.category == "디저트" }
    )

    suspend fun searchByKeyword(query: String, page: Int): SearchResponse {
        if (USE_DUMMY) {
            delay(500)

            val filtered = dummyAllList.filter { it.name.contains(query) }
            val contentList = filtered.map {
                RestaurantContent(it.id, it.name, it.locationName, it.rating, it.likeCount, it.thumbnailUrl)
            }
            return SearchResponse(contentList, contentList.size, 1, page, 10)
        }
        return searchApi.searchRestaurants(query, null, page, 10, null)
    }

    suspend fun getFilteredList(category: String, sort: String, page: Int): SortResponse {
        if (USE_DUMMY) {
            delay(500)

            val list = categoryMap[category] ?: dummyAllList
            return SortResponse(list.size, 1, page, list)
        }
        return searchApi.getSortedRestaurants(category, sort, "desc", page, 10)
    }
}