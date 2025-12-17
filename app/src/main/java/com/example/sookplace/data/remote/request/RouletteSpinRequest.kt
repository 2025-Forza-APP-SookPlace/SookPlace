package com.example.sookplace.data.remote.request

data class RouletteSpinRequest(
    val mode: String,
    val excludeIds: List<Int>
)