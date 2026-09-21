package com.example.data.model

data class LeaderboardUser(
    val rank: Int,
    val username: String,
    val diamondsEarned: Int,
    val avatarEmoji: String,
    val badge: String,
    val isCurrentUser: Boolean = false
)
