package com.example.sookplace.data.repository

import com.example.sookplace.data.local.dao.FeaturedRestaurantDao
import com.example.sookplace.data.local.dao.RestaurantDao
import com.example.sookplace.data.local.entity.FeaturedRestaurantEntity
import com.example.sookplace.data.remote.api.RestaurantApi
import com.example.sookplace.data.remote.response.FavoriteToggleResponse
import com.example.sookplace.data.remote.response.RestaurantItem
import com.example.sookplace.data.remote.response.FeaturedRestaurantsResponse
import com.example.sookplace.data.remote.response.LatestReview
import com.example.sookplace.data.remote.response.LikeToggleResponse
import com.example.sookplace.data.remote.response.MenuItem
import com.example.sookplace.data.remote.response.OpeningHour
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
    suspend fun getRestaurantDetail(id: Int): RestaurantDetailResponse {
        return api.getRestaurantDetail(id)
    }

    /**식당 좋아요**/
    suspend fun toggleLike(restaurantId: Int): LikeToggleResponse {
        return api.toggleLike(restaurantId) // (앞서 만든 API 인터페이스 사용)
    }

    /**식당 핀**/
    suspend fun togglePin(restaurantId: Int): FavoriteToggleResponse{
        return api.postPin(restaurantId)
    }
}