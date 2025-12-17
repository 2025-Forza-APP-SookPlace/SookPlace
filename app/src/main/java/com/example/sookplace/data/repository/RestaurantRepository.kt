package com.example.sookplace.data.repository

import com.example.sookplace.data.local.dao.FeaturedRestaurantDao
import com.example.sookplace.data.local.dao.RestaurantDao
import com.example.sookplace.data.local.entity.FeaturedRestaurantEntity
import com.example.sookplace.data.remote.api.RestaurantApi
import com.example.sookplace.data.remote.response.RestaurantItem
import com.example.sookplace.data.remote.response.FeaturedRestaurantsResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestaurantRepository @Inject constructor(
    private val api: RestaurantApi,
    private val restaurantDao: RestaurantDao,
    private val featuredDao: FeaturedRestaurantDao
) {
    val featuredRestaurants: Flow<List<FeaturedRestaurantEntity>> = featuredDao.getAllFeatured()

    suspend fun getFeaturedRestaurants(): List<RestaurantItem> {
        val response: FeaturedRestaurantsResponse = api.getFeaturedRestaurants()
        return response.featuredRestaurants
    }

    suspend fun refreshFeaturedRestaurants() {
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
}