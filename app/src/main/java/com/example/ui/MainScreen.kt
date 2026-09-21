package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Gamepad
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AdPlayerDialog
import com.example.ui.components.GoogleSignInDialog
import com.example.ui.components.RedeemConfirmDialog
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.EarnScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.RedeemScreen
import com.example.ui.theme.DarkNavyBackground
import com.example.ui.theme.DarkNavyCardBorder
import com.example.ui.theme.DarkNavySurface
import com.example.ui.theme.DiamondCyan
import com.example.ui.theme.DiamondCyanDark

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val user by viewModel.userAccount.collectAsStateWithLifecycle()
    val recentTransactions by viewModel.recentTransactions.collectAsStateWithLifecycle()
    val currentDestination by viewModel.currentDestination.collectAsStateWithLifecycle()

    val isAdPlaying by viewModel.isAdPlaying.collectAsStateWithLifecycle()
    val adCountdown by viewModel.adCountdown.collectAsStateWithLifecycle()
    val canClaimAdReward by viewModel.canClaimAdReward.collectAsStateWithLifecycle()

    val currentCaptcha by viewModel.currentCaptcha.collectAsStateWithLifecycle()
    val captchaInput by viewModel.captchaInput.collectAsStateWithLifecycle()
    val captchaError by viewModel.captchaError.collectAsStateWithLifecycle()

    val promoInput by viewModel.promoInput.collectAsStateWithLifecycle()
    val isClaimingPromo by viewModel.isClaimingPromo.collectAsStateWithLifecycle()

    val ffUidInput by viewModel.ffUidInput.collectAsStateWithLifecycle()
    val ffAmountInput by viewModel.ffAmountInput.collectAsStateWithLifecycle()
    val isRedeeming by viewModel.isRedeeming.collectAsStateWithLifecycle()
    val redeemResult by viewModel.redeemResult.collectAsStateWithLifecycle()

    val showGoogleSignInDialog by viewModel.showGoogleSignInDialog.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.snackbarEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
    ) {
        val isWideScreen = maxWidth >= 600.dp

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = DarkNavyBackground,
            contentWindowInsets = WindowInsets.safeDrawing,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                if (!isWideScreen) {
                    NavigationBar(
                        containerColor = DarkNavySurface,
                        contentColor = DiamondCyan,
                        tonalElevation = 8.dp,
                        modifier = Modifier
                            .navigationBarsPadding()
                            .border(1.dp, DarkNavyCardBorder)
                            .testTag("mobile_bottom_navigation")
                    ) {
                        AppNavDestination.entries.forEach { destination ->
                            val selected = currentDestination == destination
                            val icon = when (destination) {
                                AppNavDestination.DASHBOARD -> if (selected) Icons.Default.Dashboard else Icons.Outlined.Dashboard
                                AppNavDestination.EARN -> if (selected) Icons.Default.MonetizationOn else Icons.Outlined.MonetizationOn
                                AppNavDestination.REDEEM -> if (selected) Icons.Default.Gamepad else Icons.Outlined.Gamepad
                                AppNavDestination.LEADERBOARD -> if (selected) Icons.Default.EmojiEvents else Icons.Outlined.EmojiEvents
                            }

                            NavigationBarItem(
                                selected = selected,
                                onClick = { viewModel.setDestination(destination) },
                                icon = {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = destination.label,
                                        modifier = Modifier.size(22.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        text = destination.label,
                                        fontSize = 11.sp,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color(0xFF00363F),
                                    selectedTextColor = DiamondCyan,
                                    indicatorColor = DiamondCyan,
                                    unselectedIconColor = Color(0xFF94A3B8),
                                    unselectedTextColor = Color(0xFF94A3B8)
                                ),
                                modifier = Modifier.testTag("nav_item_${destination.name.lowercase()}")
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Wide Screen Navigation Rail (Tablets / Foldables / Desktops)
                if (isWideScreen) {
                    NavigationRail(
                        containerColor = DarkNavySurface,
                        contentColor = DiamondCyan,
                        header = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(DiamondCyanDark),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Diamond,
                                        contentDescription = "Logo",
                                        tint = Color.White,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxHeight()
                            .border(1.dp, DarkNavyCardBorder)
                            .testTag("tablet_navigation_rail")
                    ) {
                        AppNavDestination.entries.forEach { destination ->
                            val selected = currentDestination == destination
                            val icon = when (destination) {
                                AppNavDestination.DASHBOARD -> if (selected) Icons.Default.Dashboard else Icons.Outlined.Dashboard
                                AppNavDestination.EARN -> if (selected) Icons.Default.MonetizationOn else Icons.Outlined.MonetizationOn
                                AppNavDestination.REDEEM -> if (selected) Icons.Default.Gamepad else Icons.Outlined.Gamepad
                                AppNavDestination.LEADERBOARD -> if (selected) Icons.Default.EmojiEvents else Icons.Outlined.EmojiEvents
                            }

                            NavigationRailItem(
                                selected = selected,
                                onClick = { viewModel.setDestination(destination) },
                                icon = {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = destination.label,
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        text = destination.label,
                                        fontSize = 11.sp,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = Color(0xFF00363F),
                                    selectedTextColor = DiamondCyan,
                                    indicatorColor = DiamondCyan,
                                    unselectedIconColor = Color(0xFF94A3B8),
                                    unselectedTextColor = Color(0xFF94A3B8)
                                )
                            )
                        }
                    }
                }

                // Active Destination Screen
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    when (currentDestination) {
                        AppNavDestination.DASHBOARD -> {
                            DashboardScreen(
                                user = user,
                                recentTransactions = recentTransactions,
                                onNavigate = { viewModel.setDestination(it) },
                                onWatchAd = { viewModel.startWatchAd() },
                                promoInput = promoInput,
                                onPromoChange = { viewModel.onPromoInputChange(it) },
                                onApplyPromo = { viewModel.applyPromoCode() },
                                isClaimingPromo = isClaimingPromo,
                                onOpenGoogleSignIn = { viewModel.showGoogleLogin() }
                            )
                        }
                        AppNavDestination.EARN -> {
                            EarnScreen(
                                user = user,
                                onWatchAd = { viewModel.startWatchAd() },
                                captcha = currentCaptcha,
                                captchaInput = captchaInput,
                                onCaptchaInputChange = { viewModel.onCaptchaInputChange(it) },
                                onSubmitCaptcha = { viewModel.submitCaptcha() },
                                onRefreshCaptcha = { viewModel.refreshCaptcha() },
                                captchaError = captchaError,
                                promoInput = promoInput,
                                onPromoChange = { viewModel.onPromoInputChange(it) },
                                onApplyPromo = { viewModel.applyPromoCode() },
                                isClaimingPromo = isClaimingPromo
                            )
                        }
                        AppNavDestination.REDEEM -> {
                            RedeemScreen(
                                user = user,
                                ffUidInput = ffUidInput,
                                onFfUidChange = { viewModel.onFfUidChange(it) },
                                ffAmountInput = ffAmountInput,
                                onFfAmountChange = { viewModel.onFfAmountChange(it) },
                                onSelectPreset = { viewModel.selectPresetAmount(it) },
                                onSubmitFreeFireRedeem = { viewModel.submitFreeFireRedemption() },
                                isRedeeming = isRedeeming,
                                giftCardsList = viewModel.giftCardsList,
                                onRedeemGiftCard = { viewModel.submitGiftCardRedemption(it) }
                            )
                        }
                        AppNavDestination.LEADERBOARD -> {
                            LeaderboardScreen(
                                user = user,
                                leaderboardList = viewModel.getLeaderboard()
                            )
                        }
                    }
                }
            }
        }

        // Ad Player Overlay Dialog
        if (isAdPlaying) {
            AdPlayerDialog(
                secondsLeft = adCountdown,
                canClaim = canClaimAdReward,
                onClaim = { viewModel.claimAdReward() },
                onDismiss = { viewModel.dismissAd() }
            )
        }

        // Redemption Confirmation Dialog
        redeemResult?.let { res ->
            RedeemConfirmDialog(
                result = res,
                onDismiss = { viewModel.dismissRedeemDialog() }
            )
        }

        // Google Sign-In Dialog
        if (showGoogleSignInDialog) {
            GoogleSignInDialog(
                currentEmail = user.email,
                currentDisplayName = user.displayName,
                onSignIn = { name, email -> viewModel.performGoogleSignIn(name, email) },
                onDismiss = { viewModel.dismissGoogleLogin() }
            )
        }
    }
}
