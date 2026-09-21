package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.UserPreferences
import com.example.data.model.GiftCardItem
import com.example.data.model.LeaderboardUser
import com.example.data.model.RedeemResult
import com.example.data.model.TransactionRecord
import com.example.data.model.UserAccount
import com.example.data.remote.FreeFireApiService
import com.example.data.repository.RewardRepository
import com.example.service.NotificationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

data class CaptchaChallenge(
    val id: String,
    val question: String,
    val solution: String,
    val isMath: Boolean = false
)

enum class AppNavDestination(val label: String) {
    DASHBOARD("Dashboard"),
    EARN("Earn"),
    REDEEM("Redeem"),
    LEADERBOARD("Ranking")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RewardRepository

    val userAccount: StateFlow<UserAccount>
    val recentTransactions: StateFlow<List<TransactionRecord>>
    val allTransactions: StateFlow<List<TransactionRecord>>

    // Navigation Tab
    private val _currentDestination = MutableStateFlow(AppNavDestination.DASHBOARD)
    val currentDestination: StateFlow<AppNavDestination> = _currentDestination.asStateFlow()

    // Ads state
    private val _isAdPlaying = MutableStateFlow(false)
    val isAdPlaying: StateFlow<Boolean> = _isAdPlaying.asStateFlow()

    private val _adCountdown = MutableStateFlow(5)
    val adCountdown: StateFlow<Int> = _adCountdown.asStateFlow()

    private val _canClaimAdReward = MutableStateFlow(false)
    val canClaimAdReward: StateFlow<Boolean> = _canClaimAdReward.asStateFlow()

    private var adTimerJob: Job? = null

    // Captcha state
    private val _currentCaptcha = MutableStateFlow(generateCaptcha())
    val currentCaptcha: StateFlow<CaptchaChallenge> = _currentCaptcha.asStateFlow()

    private val _captchaInput = MutableStateFlow("")
    val captchaInput: StateFlow<String> = _captchaInput.asStateFlow()

    private val _captchaError = MutableStateFlow<String?>(null)
    val captchaError: StateFlow<String?> = _captchaError.asStateFlow()

    // Promo Code state
    private val _promoInput = MutableStateFlow("")
    val promoInput: StateFlow<String> = _promoInput.asStateFlow()

    private val _isClaimingPromo = MutableStateFlow(false)
    val isClaimingPromo: StateFlow<Boolean> = _isClaimingPromo.asStateFlow()

    // Free Fire Redemption state
    private val _ffUidInput = MutableStateFlow("")
    val ffUidInput: StateFlow<String> = _ffUidInput.asStateFlow()

    private val _ffAmountInput = MutableStateFlow("100")
    val ffAmountInput: StateFlow<String> = _ffAmountInput.asStateFlow()

    private val _isRedeeming = MutableStateFlow(false)
    val isRedeeming: StateFlow<Boolean> = _isRedeeming.asStateFlow()

    private val _redeemResult = MutableStateFlow<RedeemResult?>(null)
    val redeemResult: StateFlow<RedeemResult?> = _redeemResult.asStateFlow()

    // Google Sign-In Sheet state
    private val _showGoogleSignInDialog = MutableStateFlow(false)
    val showGoogleSignInDialog: StateFlow<Boolean> = _showGoogleSignInDialog.asStateFlow()

    // One-time Snackbars / Events
    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent: SharedFlow<String> = _snackbarEvent.asSharedFlow()

    init {
        val database = AppDatabase.getInstance(application)
        val userPrefs = UserPreferences(application)
        val apiService = FreeFireApiService.create()
        val notificationHelper = NotificationHelper(application)

        repository = RewardRepository(
            transactionDao = database.transactionDao(),
            userPreferences = userPrefs,
            apiService = apiService,
            notificationHelper = notificationHelper
        )

        userAccount = repository.userAccount
        recentTransactions = repository.recentTransactions.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        allTransactions = repository.transactions.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        // Pre-fill last known UID if exists
        userAccount.value.freeFireUid?.let {
            if (it.isNotEmpty()) _ffUidInput.value = it
        }
    }

    fun setDestination(dest: AppNavDestination) {
        _currentDestination.value = dest
    }

    // --- Ad Simulation Mechanics ---
    fun startWatchAd() {
        if (_isAdPlaying.value) return
        _isAdPlaying.value = true
        _canClaimAdReward.value = false
        _adCountdown.value = 5

        adTimerJob?.cancel()
        adTimerJob = viewModelScope.launch {
            for (i in 5 downTo 1) {
                _adCountdown.value = i
                delay(1000)
            }
            _adCountdown.value = 0
            _canClaimAdReward.value = true
        }
    }

    fun claimAdReward() {
        viewModelScope.launch {
            _canClaimAdReward.value = false
            _isAdPlaying.value = false
            val reward = repository.rewardAdWatched()
            _snackbarEvent.emit("🎉 +$reward Diamonds earned from Ad!")
        }
    }

    fun dismissAd() {
        adTimerJob?.cancel()
        _isAdPlaying.value = false
        _canClaimAdReward.value = false
    }

    // --- Captcha Mechanics ---
    fun onCaptchaInputChange(newVal: String) {
        _captchaInput.value = newVal
        _captchaError.value = null
    }

    fun refreshCaptcha() {
        _currentCaptcha.value = generateCaptcha()
        _captchaInput.value = ""
        _captchaError.value = null
    }

    fun submitCaptcha() {
        val current = _currentCaptcha.value
        val input = _captchaInput.value.trim()

        val isCorrect = if (current.isMath) {
            input == current.solution
        } else {
            input.equals(current.solution, ignoreCase = true)
        }

        if (isCorrect) {
            viewModelScope.launch {
                val reward = repository.rewardCaptchaSolved()
                _captchaInput.value = ""
                _captchaError.value = null
                _currentCaptcha.value = generateCaptcha()
                _snackbarEvent.emit("✓ Correct! +$reward Diamonds credited!")
            }
        } else {
            _captchaError.value = "Incorrect captcha! Try again or tap refresh."
        }
    }

    private fun generateCaptcha(): CaptchaChallenge {
        val isMath = Random.nextBoolean()
        return if (isMath) {
            val a = Random.nextInt(12, 60)
            val b = Random.nextInt(7, 45)
            CaptchaChallenge(
                id = Random.nextInt(1000, 9999).toString(),
                question = "$a + $b = ?",
                solution = (a + b).toString(),
                isMath = true
            )
        } else {
            val chars = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ"
            val text = (1..5).map { chars.random() }.joinToString("")
            CaptchaChallenge(
                id = Random.nextInt(1000, 9999).toString(),
                question = text,
                solution = text,
                isMath = false
            )
        }
    }

    // --- Promo Code 'MG69' Mechanics ---
    fun onPromoInputChange(newVal: String) {
        _promoInput.value = newVal
    }

    fun applyPromoCode() {
        val code = _promoInput.value.trim()
        if (code.isEmpty()) return

        viewModelScope.launch {
            _isClaimingPromo.value = true
            val result = repository.redeemPromoCode(code)
            _isClaimingPromo.value = false

            result.onSuccess { msg ->
                _promoInput.value = ""
                _snackbarEvent.emit(msg)
            }.onFailure { err ->
                _snackbarEvent.emit(err.message ?: "Failed to redeem code")
            }
        }
    }

    // --- Free Fire Redemption ---
    fun onFfUidChange(uid: String) {
        _ffUidInput.value = uid
    }

    fun onFfAmountChange(amount: String) {
        _ffAmountInput.value = amount
    }

    fun selectPresetAmount(amount: Int) {
        _ffAmountInput.value = amount.toString()
    }

    fun submitFreeFireRedemption() {
        val uid = _ffUidInput.value.trim()
        val amount = _ffAmountInput.value.toIntOrNull() ?: 0

        viewModelScope.launch {
            _isRedeeming.value = true
            val result = repository.redeemFreeFireDiamonds(uid, amount)
            _isRedeeming.value = false
            _redeemResult.value = result

            if (result.isSuccess) {
                _snackbarEvent.emit("🎉 Free Fire Diamonds Redeemed! Check notification.")
            } else {
                _snackbarEvent.emit(result.message)
            }
        }
    }

    fun dismissRedeemDialog() {
        _redeemResult.value = null
    }

    // --- Gift Card Redemption ---
    fun submitGiftCardRedemption(giftCard: GiftCardItem) {
        viewModelScope.launch {
            _isRedeeming.value = true
            val result = repository.redeemGiftCard(giftCard)
            _isRedeeming.value = false
            _redeemResult.value = result

            if (result.isSuccess) {
                _snackbarEvent.emit("🎁 Gift Card Claimed! Voucher: ${result.voucherCode}")
            } else {
                _snackbarEvent.emit(result.message)
            }
        }
    }

    // --- Google Sign-In ---
    fun showGoogleLogin() {
        _showGoogleSignInDialog.value = true
    }

    fun dismissGoogleLogin() {
        _showGoogleSignInDialog.value = false
    }

    fun performGoogleSignIn(name: String, email: String) {
        repository.signInWithGoogle(name, email)
        _showGoogleSignInDialog.value = false
        viewModelScope.launch {
            _snackbarEvent.emit("✓ Signed in as $name ($email)")
        }
    }

    fun getLeaderboard(): List<LeaderboardUser> {
        return repository.getLeaderboard()
    }

    val giftCardsList: List<GiftCardItem> = listOf(
        GiftCardItem("gc_gp_5", "Google Play", "$5 Gift Card", "$5.00", 500, 0xFF34A853, "google_play"),
        GiftCardItem("gc_gp_10", "Google Play", "$10 Gift Card", "$10.00", 1000, 0xFF34A853, "google_play"),
        GiftCardItem("gc_amz_5", "Amazon", "$5 Digital Card", "$5.00", 500, 0xFFFF9900, "amazon"),
        GiftCardItem("gc_amz_10", "Amazon", "$10 Digital Card", "$10.00", 1000, 0xFFFF9900, "amazon"),
        GiftCardItem("gc_stm_5", "Steam Wallet", "$5 USD Code", "$5.00", 500, 0xFF171A21, "steam"),
        GiftCardItem("gc_stm_10", "Steam Wallet", "$10 USD Code", "$10.00", 1000, 0xFF171A21, "steam"),
        GiftCardItem("gc_rzr_10", "Razer Gold", "1000 Razer PIN", "$10.00", 1000, 0xFF00FF00, "razer"),
        GiftCardItem("gc_app_10", "Apple App Store", "$10 Apple Card", "$10.00", 1000, 0xFFA2AAAD, "apple")
    )
}
