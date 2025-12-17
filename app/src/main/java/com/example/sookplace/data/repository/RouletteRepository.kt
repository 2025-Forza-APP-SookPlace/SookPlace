package com.example.sookplace.data.repository

import com.example.sookplace.data.remote.api.RouletteSpinApi
import com.example.sookplace.data.remote.request.RouletteSpinRequest
import com.example.sookplace.data.remote.response.RouletteSpinResponse
import javax.inject.Inject

class RouletteRepository @Inject constructor(
    private val api: RouletteSpinApi
) {
    suspend fun spin(mode: String, excludeIds: List<Int>): RouletteSpinResponse {
        return api.spinRoulette(RouletteSpinRequest(mode, excludeIds))
    }
}