package com.example.data.model

data class GiftCardItem(
    val id: String,
    val brandName: String,
    val cardTitle: String,
    val valueUSD: String,
    val diamondsCost: Int,
    val brandColorHex: Long,
    val iconType: String // e.g. "google_play", "amazon", "steam", "apple", "razer"
)

data class RedeemResult(
    val isSuccess: Boolean,
    val message: String,
    val txId: String? = null,
    val deliveredAmount: Int = 0,
    val targetUid: String? = null,
    val playerNickname: String? = null,
    val voucherCode: String? = null
)
