package com.example.sookplace.data.remote.api

import com.example.sookplace.data.remote.request.RouletteSpinRequest
import com.example.sookplace.data.remote.response.RouletteSpinResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface RouletteSpinApi {

    @POST("/roulette/spin")
    suspend fun spinRoulette(
        @Body request: RouletteSpinRequest
    ) : RouletteSpinResponse
}