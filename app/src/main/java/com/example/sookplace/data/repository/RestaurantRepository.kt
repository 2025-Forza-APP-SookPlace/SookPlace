package com.example.sookplace.data.repository

import com.example.sookplace.data.local.dao.FeaturedRestaurantDao
import com.example.sookplace.data.local.dao.RestaurantDao
import com.example.sookplace.data.local.entity.FeaturedRestaurantEntity
import com.example.sookplace.data.remote.api.RestaurantApi
import com.example.sookplace.data.remote.response.RestaurantItem
import com.example.sookplace.data.remote.response.FeaturedRestaurantsResponse
import com.example.sookplace.data.remote.response.GeoCoordinates
import com.example.sookplace.data.remote.response.LatestReview
import com.example.sookplace.data.remote.response.MenuItem
import com.example.sookplace.data.remote.response.OpeningHour
import com.example.sookplace.data.remote.response.Partnership
import com.example.sookplace.data.remote.response.RestaurantDetailResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestaurantRepository @Inject constructor(
    private val api: RestaurantApi,
    private val restaurantDao: RestaurantDao,
    private val featuredDao: FeaturedRestaurantDao
) {
    /**오늘의 숙플레이스*/
    val featuredRestaurants: Flow<List<FeaturedRestaurantEntity>> = featuredDao.getAllFeatured()

    suspend fun getFeaturedRestaurants(): List<RestaurantItem> { //오늘의 숙플레이스 가져오기
        val response: FeaturedRestaurantsResponse = api.getFeaturedRestaurants()
        return response.featuredRestaurants
    }

    suspend fun refreshFeaturedRestaurants() { //오늘의 숙플레이스(백엔드)에 변경사항이 있을 시 가져오기
        try {
            val response = api.getFeaturedRestaurants()
            val remoteList: List<RestaurantItem> = response.featuredRestaurants

            val entities = remoteList.map { item ->
                FeaturedRestaurantEntity(
                    name = item.name,
                    address = item.address,
                    thumbnailUrl = item.thumbnailUrl,
                    isLiked = item.isLiked
                )
            }

            featuredDao.refreshRestaurants(entities) // 기존 삭제 후 삽입
        } catch (e: Exception) {
        }
    }

    //디버깅용 함수
    suspend fun updateDummyData(list: List<FeaturedRestaurantEntity>) {
        featuredDao.refreshRestaurants(list)
    }

    /**식당 상세 페이지*/
    private val USE_DUMMY = true

    suspend fun getRestaurantDetail(id: Int): RestaurantDetailResponse {
        if (USE_DUMMY) {
            kotlinx.coroutines.delay(500)
            return RestaurantDetailResponse(
                id = id,
                name = "숙명 한식당",
                category = "한식",
                rating = 4.5,
                reviewCount = 27,
                likeCount = 89,
                thumbnailUrl = "https://picsum.photos/id/102/400/300",
                images = listOf("https://picsum.photos/id/102/800/600", "https://picsum.photos/id/103/800/600"),
                distanceMinutesFromCampus = 5,
                address = "서울 용산구 청파로 47",
                geo = GeoCoordinates(37.545, 126.97),
                naverMapUrl = "https://map.naver.com/",
                partnership = com.example.sookplace.data.remote.response.Partnership("숙명 재학생", 20, "2024-12-31"),
                openingHours = listOf(
                    OpeningHour("Mon-Fri", "11:00-21:00"),
                    OpeningHour("Sat-Sun", "12:00-20:00")
                ),
                phone = "02-123-4567",
                menus = listOf(
                    MenuItem("김치찌개", 8000),
                    MenuItem("된장찌개", 7500),
                    MenuItem("비빔밥", 9000)
                ),
                latestReviews = listOf(
                    LatestReview("https://picsum.photos/id/111/200/200", "맛집탐험가송이", "2024-12-14", "김치찌개 진짜 맛있어요!", 4.8, "정말 깔끔하고 맛있어서 자주 가는 곳이에요…")
                ),
                shareUrl = "https://sookplace.app/r/$id"
            )
        }
        return api.getRestaurantDetail(id)
    }
}