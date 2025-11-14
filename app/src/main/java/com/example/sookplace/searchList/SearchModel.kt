package com.example.sookplace.searchList

class SearchModel (
    val restaurant: String = "",
    val imgUrl : String = "",
    val url : String = "",
    val location: String = "" ,
    val category: String = "",
    val score: Double = 0.0,    //별점
    val heartScore: Int = 0     //하트수(추천수)
)