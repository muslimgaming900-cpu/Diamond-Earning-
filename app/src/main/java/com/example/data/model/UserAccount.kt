package com.example.data.model

data class UserAccount(
    val userId: String,
    val displayName: String,
    val email: String,
    val photoUrl: String? = null,
    val diamonds: Int = 50, // Starting welcome bonus
    val totalEarned: Int = 50,
    val totalRedeemed: Int = 0,
    val tasksCompletedToday: Int = 0,
    val promoCodeClaimed: Boolean = false,
    val freeFireUid: String? = null,
    val currentRank: Int = 142,
    val tier: String = "Diamond II"
)
