package com.example.sookplace.remote.response

data class PostResponse(
    val postId: String,
    val title: String,
    val content: String,
    val date: String
)