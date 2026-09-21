package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TransactionType {
    AD_REWARD,
    CAPTCHA_REWARD,
    PROMO_CODE,
    FREE_FIRE_REDEEM,
    GIFT_CARD_REDEEM
}

enum class TransactionStatus {
    SUCCESS,
    DELIVERED,
    PENDING,
    FAILED
}

@Entity(tableName = "transactions")
data class TransactionRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: TransactionType,
    val amount: Int, // e.g. +10, +1000, -100
    val title: String,
    val description: String,
    val targetUid: String? = null, // Free Fire UID if applicable
    val txId: String, // e.g. FF-89240182 or TX-73918239
    val status: TransactionStatus = TransactionStatus.DELIVERED,
    val timestamp: Long = System.currentTimeMillis()
)
