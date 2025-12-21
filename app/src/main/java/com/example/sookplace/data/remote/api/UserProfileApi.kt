package com.example.sookplace.data.remote.api

import com.example.sookplace.data.remote.response.UserProfileResponse
import retrofit2.http.GET

interface UserProfileApi {
    @GET("me")
    suspend fun getUserProfile(): UserProfileResponse
}
