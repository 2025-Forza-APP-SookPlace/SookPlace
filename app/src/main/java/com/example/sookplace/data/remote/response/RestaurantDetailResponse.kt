package com.example.sookplace.data.remote.response

data class RestaurantDetailResponse(
    val id: Int,
    val name: String,
    val category: String,
    val rating: Double,
    val reviewCount: Int,
    val likeCount: Int,
    val thumbnailUrl: String?,
    val images: List<String>,
    val distanceMinutesFromCampus: Int, //얘는 뭘까
    val address: String,
    val naverMapUrl: String,
    val openingHours: List<OpeningHour>,
    val phone: String,
    val menus: List<MenuItem>,
    val latestReviews: List<LatestReview>,
    val shareUrl: String,
    val isLiked: Boolean,
    val isFavorited: Boolean? //true=찜등록됨상태, false=찜해제상태, null=비로그인
)

data class OpeningHour(
    val day: String, // Mon-Fri, Sat-Sun 등
    val hours: String
)

data class MenuItem(
    val name: String,
    val price: Int
)

data class LatestReview(
    val imageUrl: String?,
    val authorNickname: String,
    val createdAt: String,
    val title: String,
    val rating: Double,
    val contentSnippet: String
)

//식당 핀
data class FavoriteToggleResponse( //true=찜등록됨, false=찜해제됨
    val favorited: Boolean
)