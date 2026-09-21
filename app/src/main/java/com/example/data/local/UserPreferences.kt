package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.UserAccount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("diamond_rewards_prefs", Context.MODE_PRIVATE)

    private val _userState = MutableStateFlow(loadUser())
    val userState: StateFlow<UserAccount> = _userState.asStateFlow()

    private fun loadUser(): UserAccount {
        val userId = prefs.getString(KEY_USER_ID, "usr_mg900") ?: "usr_mg900"
        val name = prefs.getString(KEY_NAME, "Muslim Gaming") ?: "Muslim Gaming"
        val email = prefs.getString(KEY_EMAIL, "muslimgaming900@gmail.com") ?: "muslimgaming900@gmail.com"
        val photoUrl = prefs.getString(KEY_PHOTO_URL, null)
        val diamonds = prefs.getInt(KEY_DIAMONDS, 50)
        val totalEarned = prefs.getInt(KEY_TOTAL_EARNED, 50)
        val totalRedeemed = prefs.getInt(KEY_TOTAL_REDEEMED, 0)
        val tasksToday = prefs.getInt(KEY_TASKS_TODAY, 0)
        val promoClaimed = prefs.getBoolean(KEY_PROMO_CLAIMED, false)
        val ffUid = prefs.getString(KEY_FF_UID, "")
        val rank = prefs.getInt(KEY_RANK, 142)
        val tier = prefs.getString(KEY_TIER, "Diamond II") ?: "Diamond II"

        return UserAccount(
            userId = userId,
            displayName = name,
            email = email,
            photoUrl = photoUrl,
            diamonds = diamonds,
            totalEarned = totalEarned,
            totalRedeemed = totalRedeemed,
            tasksCompletedToday = tasksToday,
            promoCodeClaimed = promoClaimed,
            freeFireUid = if (ffUid.isNullOrEmpty()) null else ffUid,
            currentRank = rank,
            tier = tier
        )
    }

    fun updateAccount(account: UserAccount) {
        prefs.edit()
            .putString(KEY_USER_ID, account.userId)
            .putString(KEY_NAME, account.displayName)
            .putString(KEY_EMAIL, account.email)
            .putString(KEY_PHOTO_URL, account.photoUrl)
            .putInt(KEY_DIAMONDS, account.diamonds)
            .putInt(KEY_TOTAL_EARNED, account.totalEarned)
            .putInt(KEY_TOTAL_REDEEMED, account.totalRedeemed)
            .putInt(KEY_TASKS_TODAY, account.tasksCompletedToday)
            .putBoolean(KEY_PROMO_CLAIMED, account.promoCodeClaimed)
            .putString(KEY_FF_UID, account.freeFireUid ?: "")
            .putInt(KEY_RANK, account.currentRank)
            .putString(KEY_TIER, account.tier)
            .apply()

        _userState.value = account
    }

    fun addDiamonds(amount: Int, isTask: Boolean = false) {
        val current = _userState.value
        val newDiamonds = current.diamonds + amount
        val newTotal = current.totalEarned + amount
        val newTasks = if (isTask) current.tasksCompletedToday + 1 else current.tasksCompletedToday

        // Upgrade rank as diamonds increase
        val newRank = when {
            newTotal >= 2000 -> 18
            newTotal >= 1000 -> 45
            newTotal >= 500 -> 82
            newTotal >= 200 -> 115
            else -> 142
        }
        val newTier = when {
            newTotal >= 2000 -> "Grandmaster"
            newTotal >= 1000 -> "Heroic"
            newTotal >= 500 -> "Diamond III"
            else -> "Diamond II"
        }

        updateAccount(
            current.copy(
                diamonds = newDiamonds,
                totalEarned = newTotal,
                tasksCompletedToday = newTasks,
                currentRank = newRank,
                tier = newTier
            )
        )
    }

    fun deductDiamonds(amount: Int, targetUid: String? = null): Boolean {
        val current = _userState.value
        if (current.diamonds < amount) return false

        updateAccount(
            current.copy(
                diamonds = current.diamonds - amount,
                totalRedeemed = current.totalRedeemed + amount,
                freeFireUid = targetUid ?: current.freeFireUid
            )
        )
        return true
    }

    fun markPromoCodeClaimed() {
        val current = _userState.value
        updateAccount(current.copy(promoCodeClaimed = true))
    }

    fun signInWithGoogle(name: String, email: String, photoUrl: String? = null) {
        val current = _userState.value
        updateAccount(
            current.copy(
                userId = "usr_" + email.hashCode().toString().replace("-", "x"),
                displayName = name,
                email = email,
                photoUrl = photoUrl
            )
        )
    }

    companion object {
        private const val KEY_USER_ID = "pref_user_id"
        private const val KEY_NAME = "pref_name"
        private const val KEY_EMAIL = "pref_email"
        private const val KEY_PHOTO_URL = "pref_photo"
        private const val KEY_DIAMONDS = "pref_diamonds"
        private const val KEY_TOTAL_EARNED = "pref_total_earned"
        private const val KEY_TOTAL_REDEEMED = "pref_total_redeemed"
        private const val KEY_TASKS_TODAY = "pref_tasks_today"
        private const val KEY_PROMO_CLAIMED = "pref_promo_claimed"
        private const val KEY_FF_UID = "pref_ff_uid"
        private const val KEY_RANK = "pref_rank"
        private const val KEY_TIER = "pref_tier"
    }
}
