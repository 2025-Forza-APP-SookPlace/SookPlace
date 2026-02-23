package com.example.sookplace.data.repository

import com.example.sookplace.data.remote.api.MapApi
import com.example.sookplace.data.remote.response.FavoriteItem
import com.example.sookplace.data.remote.response.FavoriteResponse
import com.example.sookplace.data.remote.response.Geo
import javax.inject.Inject

class MapRepository @Inject constructor(
    private val mapApi: MapApi
) {
    suspend fun getFavorites(limit: Int): Result<FavoriteResponse> {
        return kotlin.runCatching { mapApi.getFavorites(limit) }

//        return try {
//            val dummyItems = listOf(
//                FavoriteItem(
//                    id = 1,
//                    name = "청파동 돈까스",
//                    category = "일식",
//                    thumbnailUrl = "https://picsum.photos/200",
//                    rating = 4.8,
//                    distanceMinutesFromCampus = 5,
//                    hasPartnership = true,
//                    partnershipBadge = "제휴할인",
//                    geo = Geo(37.545419, 126.964649),
//                    detailUrl = "restaurant_id_1"
//                ),
//                FavoriteItem(
//                    id = 2,
//                    name = "눈송이 마라탕",
//                    category = "중식",
//                    thumbnailUrl = "https://picsum.photos/201",
//                    rating = 4.5,
//                    distanceMinutesFromCampus = 3,
//                    hasPartnership = false,
//                    partnershipBadge = null,
//                    geo = Geo(37.544000, 126.965000),
//                    detailUrl = "restaurant_id_2"
//                ),
//                FavoriteItem(
//                    id = 3,
//                    name = "숙대입구 떡볶이",
//                    category = "분식",
//                    thumbnailUrl = "https://picsum.photos/202",
//                    rating = 4.2,
//                    distanceMinutesFromCampus = 8,
//                    hasPartnership = true,
//                    partnershipBadge = "학생우대",
//                    geo = Geo(37.546000, 126.966000),
//                    detailUrl = "restaurant_id_3"
//                ),
//                FavoriteItem(
//                    id = 1,
//                    name = "청파동 돈까스",
//                    category = "일식",
//                    thumbnailUrl = "https://picsum.photos/200",
//                    rating = 4.8,
//                    distanceMinutesFromCampus = 5,
//                    hasPartnership = true,
//                    partnershipBadge = "제휴할인",
//                    geo = Geo(37.545419, 126.964649),
//                    detailUrl = "restaurant_id_1"
//                ),
//                FavoriteItem(
//                    id = 2,
//                    name = "눈송이 마라탕",
//                    category = "중식",
//                    thumbnailUrl = "https://picsum.photos/201",
//                    rating = 4.5,
//                    distanceMinutesFromCampus = 3,
//                    hasPartnership = false,
//                    partnershipBadge = null,
//                    geo = Geo(37.544000, 126.965000),
//                    detailUrl = "restaurant_id_2"
//                ),
//                FavoriteItem(
//                    id = 3,
//                    name = "숙대입구 떡볶이",
//                    category = "분식",
//                    thumbnailUrl = "https://picsum.photos/202",
//                    rating = 4.2,
//                    distanceMinutesFromCampus = 8,
//                    hasPartnership = true,
//                    partnershipBadge = "학생우대",
//                    geo = Geo(37.546000, 126.966000),
//                    detailUrl = "restaurant_id_3"
//                ),
//                FavoriteItem(
//                    id = 1,
//                    name = "청파동 돈까스",
//                    category = "일식",
//                    thumbnailUrl = "https://picsum.photos/200",
//                    rating = 4.8,
//                    distanceMinutesFromCampus = 5,
//                    hasPartnership = true,
//                    partnershipBadge = "제휴할인",
//                    geo = Geo(37.545419, 126.964649),
//                    detailUrl = "restaurant_id_1"
//                ),
//                FavoriteItem(
//                    id = 2,
//                    name = "눈송이 마라탕",
//                    category = "중식",
//                    thumbnailUrl = "https://picsum.photos/201",
//                    rating = 4.5,
//                    distanceMinutesFromCampus = 3,
//                    hasPartnership = false,
//                    partnershipBadge = null,
//                    geo = Geo(37.544000, 126.965000),
//                    detailUrl = "restaurant_id_2"
//                ),
//                FavoriteItem(
//                    id = 3,
//                    name = "숙대입구 떡볶이",
//                    category = "분식",
//                    thumbnailUrl = "https://picsum.photos/202",
//                    rating = 4.2,
//                    distanceMinutesFromCampus = 8,
//                    hasPartnership = true,
//                    partnershipBadge = "학생우대",
//                    geo = Geo(37.546000, 126.966000),
//                    detailUrl = "restaurant_id_3"
//                ),
//                FavoriteItem(
//                    id = 1,
//                    name = "청파동 돈까스",
//                    category = "일식",
//                    thumbnailUrl = "https://picsum.photos/200",
//                    rating = 4.8,
//                    distanceMinutesFromCampus = 5,
//                    hasPartnership = true,
//                    partnershipBadge = "제휴할인",
//                    geo = Geo(37.545419, 126.964649),
//                    detailUrl = "restaurant_id_1"
//                ),
//                FavoriteItem(
//                    id = 2,
//                    name = "눈송이 마라탕",
//                    category = "중식",
//                    thumbnailUrl = "https://picsum.photos/201",
//                    rating = 4.5,
//                    distanceMinutesFromCampus = 3,
//                    hasPartnership = false,
//                    partnershipBadge = null,
//                    geo = Geo(37.544000, 126.965000),
//                    detailUrl = "restaurant_id_2"
//                ),
//                FavoriteItem(
//                    id = 3,
//                    name = "숙대입구 떡볶이",
//                    category = "분식",
//                    thumbnailUrl = "https://picsum.photos/202",
//                    rating = 4.2,
//                    distanceMinutesFromCampus = 8,
//                    hasPartnership = true,
//                    partnershipBadge = "학생우대",
//                    geo = Geo(37.546000, 126.966000),
//                    detailUrl = "restaurant_id_3"
//                )
//            )
//            Result.success(FavoriteResponse(dummyItems, dummyItems.size))
//        } catch (e: Exception) {
//            Result.failure(e)
//        }

    }

}