package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.TransactionRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionRecord>>

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC LIMIT 5")
    fun getRecentTransactions(): Flow<List<TransactionRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionRecord): Long

    @Query("SELECT COUNT(*) FROM transactions WHERE type IN ('AD_REWARD', 'CAPTCHA_REWARD') AND timestamp >= :startOfDay")
    fun getTodayTasksCount(startOfDay: Long): Flow<Int>

    @Query("DELETE FROM transactions")
    suspend fun clearAll()
}
