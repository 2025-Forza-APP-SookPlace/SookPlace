package com.example.sookplace.data.local.entity
import androidx.room.Entity

@Entity(tableName = "UserEntity")
data class UserEntity (
    var id: Int = 0,
    var email: String = "",
    var nickname: String = "",
    val profileImage: String = "",
    val badgeImage: String = "",
    var level: Int = 0,
    var postsCount: Int = 0,
    var likesGiven: Int = 0,
    var savesGiven: Int = 0,
    var likesReceived: Int = 0,
    var savesReceived: Int = 0
)

