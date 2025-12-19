package com.example.sookplace.data.remote.request

data class SearchRequest(
    val keyword: String?,
    val category: String? = "all", // chicken, cafe, korean, bunsik, western, dessert
    val page: Int = 0,
    val size: Int = 10,
    val sort: String? = "popularity", // distance, rating
)

data class SortRequest(
    val category: String? = "all", // chicken, cafe, korean, bunsik, western, dessert
    val sort: String = "popularity", // distance, rating
    val dir: String = "desc", // asc
    val page: Int = 0,
    val size: Int = 10
)
