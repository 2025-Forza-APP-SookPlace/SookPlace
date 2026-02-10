package com.example.sookplace.remote.response

data class PlaceResponse(
    val placeId: String,
    val name: String,
    val address: String,
    val imageUrl: String? // 이미지가 없을 수도 있어서 ? 붙임
)