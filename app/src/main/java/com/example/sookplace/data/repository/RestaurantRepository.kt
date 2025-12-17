package com.example.sookplace.data.repository

import com.example.sookplace.data.local.dao.RestaurantDao
import com.example.sookplace.data.remote.api.RestaurantApi
import com.example.sookplace.data.remote.response.RestaurantItem
import com.example.sookplace.data.remote.response.FeaturedRestaurantsResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestaurantRepository @Inject constructor(
    private val api: RestaurantApi,
    private val dao: RestaurantDao
) {

    suspend fun getFeaturedRestaurants(): List<RestaurantItem> {
        val response: FeaturedRestaurantsResponse = api.getFeaturedRestaurants()
        return response.featuredRestaurants
    }


}