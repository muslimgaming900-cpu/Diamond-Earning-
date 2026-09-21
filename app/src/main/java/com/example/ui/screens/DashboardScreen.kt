package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.TransactionRecord
import com.example.data.model.UserAccount
import com.example.ui.AppNavDestination
import com.example.ui.components.DiamondBadge
import com.example.ui.theme.DarkNavyCardBorder
import com.example.ui.theme.DarkNavySurface
import com.example.ui.theme.DarkNavySurfaceVariant
import com.example.ui.theme.DiamondCyan
import com.example.ui.theme.DiamondCyanDark
import com.example.ui.theme.DiamondCyanLight
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.FlameGold
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.SuccessGreen
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    user: UserAccount,
    recentTransactions: List<TransactionRecord>,
    onNavigate: (AppNavDestination) -> Unit,
    onWatchAd: () -> Unit,
    promoInput: String,
    onPromoChange: (String) -> Unit,
    onApplyPromo: () -> Unit,
    isClaimingPromo: Boolean,
    onOpenGoogleSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 720.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // User Top Bar
            item {
                UserHeaderBar(
                    user = user,
                    onOpenGoogleSignIn = onOpenGoogleSignIn
                )
            }

            // Hero Balance Card
            item {
                BalanceHeroCard(
                    user = user,
                    onRedeem = { onNavigate(AppNavDestination.REDEEM) }
                )
            }

            // Exclusive Code MG69 Promo Card
            item {
                PromoCodeBanner(
                    promoInput = promoInput,
                    onPromoChange = onPromoChange,
                    onApplyPromo = onApplyPromo,
                    isClaiming = isClaimingPromo,
                    isAlreadyClaimed = user.promoCodeClaimed
                )
            }

            // Quick Earning Action Grid
            item {
                QuickEarnActions(
                    onWatchAd = onWatchAd,
                    onSolveCaptcha = { onNavigate(AppNavDestination.EARN) },
                    onRedeemDiamonds = { onNavigate(AppNavDestination.REDEEM) }
                )
            }

            // Daily Mission / Progress
            item {
                DailyMissionCard(tasksToday = user.tasksCompletedToday)
            }

            // Recent Transactions Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = DiamondCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Recent Activity",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }

                    Text(
                        text = "Real-Time Sync",
                        style = MaterialTheme.typography.bodySmall,
                        color = SuccessGreen
                    )
                }
            }

            // Recent Transactions List
            if (recentTransactions.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = DarkNavySurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkNavyCardBorder),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No transactions yet!",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Watch an ad, solve a captcha, or enter code 'MG69' to get started.",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            } else {
                items(recentTransactions) { tx ->
                    TransactionItem(tx = tx)
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun UserHeaderBar(
    user: UserAccount,
    onOpenGoogleSignIn: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkNavySurface)
            .border(1.dp, DarkNavyCardBorder, RoundedCornerShape(16.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .clickable { onOpenGoogleSignIn() }
            .testTag("user_profile_header"),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFF2563EB), Color(0xFF1E3A8A))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.displayName.take(1).uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = user.displayName,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(FlameGold.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = user.tier,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = FlameGold
                        )
                    }
                }
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF94A3B8),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        DiamondBadge(count = user.diamonds)
    }
}

@Composable
private fun BalanceHeroCard(
    user: UserAccount,
    onRedeem: () -> Unit
) {
    val formatter = NumberFormat.getNumberInstance(Locale.US)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(
                1.dp,
                Brush.horizontalGradient(listOf(DiamondCyan.copy(alpha = 0.6f), Color(0xFF3B82F6))),
                RoundedCornerShape(24.dp)
            )
            .testTag("hero_balance_card"),
        colors = CardDefaults.cardColors(containerColor = DarkNavySurface)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Optional hero banner background accent
            Image(
                painter = painterResource(id = R.drawable.reward_hero_banner),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alpha = 0.25f,
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(24.dp))
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Diamond,
                            contentDescription = null,
                            tint = DiamondCyan,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "TOTAL BALANCE",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = DiamondCyanLight
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF00363F))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "1 💎 = 1 FF Diamond",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DiamondCyan
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "${formatter.format(user.diamonds)} 💎",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Lifetime Earned vs Redeemed stat chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatPill(
                        label = "Total Earned",
                        value = "+${formatter.format(user.totalEarned)}",
                        valueColor = SuccessGreen,
                        modifier = Modifier.weight(1f)
                    )
                    StatPill(
                        label = "Total Redeemed",
                        value = "${formatter.format(user.totalRedeemed)}",
                        valueColor = FlameGold,
                        modifier = Modifier.weight(1f)
                    )
                    StatPill(
                        label = "Global Rank",
                        value = "#${user.currentRank}",
                        valueColor = DiamondCyan,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onRedeem,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("hero_redeem_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DiamondCyan,
                        contentColor = Color(0xFF00363F)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Gamepad,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "REDEEM FREE FIRE DIAMONDS",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun StatPill(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0A0F1D).copy(alpha = 0.8f))
            .border(1.dp, DarkNavyCardBorder, RoundedCornerShape(12.dp))
            .padding(vertical = 8.dp, horizontal = 8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = label,
                fontSize = 10.sp,
                color = Color(0xFF94A3B8)
            )
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
        }
    }
}

@Composable
private fun PromoCodeBanner(
    promoInput: String,
    onPromoChange: (String) -> Unit,
    onApplyPromo: () -> Unit,
    isClaiming: Boolean,
    isAlreadyClaimed: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(
                1.dp,
                Brush.horizontalGradient(listOf(FlameGold, FlameOrange)),
                RoundedCornerShape(20.dp)
            )
            .testTag("promo_code_banner"),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B150A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Stars,
                        contentDescription = null,
                        tint = FlameGold,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Secret VIP Bonus: Code 'MG69'",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(FlameGold)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "+1000 💎",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (isAlreadyClaimed) {
                    "✓ You claimed +1000 diamonds using code MG69!"
                } else {
                    "Enter code 'MG69' below to instantly get 1,000 app diamonds!"
                },
                fontSize = 12.sp,
                color = if (isAlreadyClaimed) SuccessGreen else Color(0xFFE2E8F0)
            )

            if (!isAlreadyClaimed) {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = promoInput,
                        onValueChange = onPromoChange,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("promo_input_field"),
                        placeholder = { Text("Enter code (e.g. MG69)", fontSize = 13.sp, color = Color.Gray) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FlameGold,
                            unfocusedBorderColor = Color(0xFF422800),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = onApplyPromo,
                        enabled = promoInput.isNotBlank() && !isClaiming,
                        modifier = Modifier
                            .height(52.dp)
                            .testTag("apply_promo_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FlameGold,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = if (isClaiming) "Claiming..." else "Claim",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickEarnActions(
    onWatchAd: () -> Unit,
    onSolveCaptcha: () -> Unit,
    onRedeemDiamonds: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Earn & Redeem Hub",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Watch Ad Card
            ActionTile(
                title = "Watch Ad",
                reward = "+10 💎",
                icon = Icons.Default.PlayCircle,
                accentColor = FlameGold,
                onClick = onWatchAd,
                testTag = "dashboard_watch_ad_tile",
                modifier = Modifier.weight(1f)
            )

            // Solve Captcha Card
            ActionTile(
                title = "Solve Captcha",
                reward = "+10 💎",
                icon = Icons.Default.Security,
                accentColor = DiamondCyan,
                onClick = onSolveCaptcha,
                testTag = "dashboard_solve_captcha_tile",
                modifier = Modifier.weight(1f)
            )

            // Redeem Card
            ActionTile(
                title = "Redeem FF",
                reward = "1:1 Ratio",
                icon = Icons.Default.Gamepad,
                accentColor = Color(0xFF38BDF8),
                onClick = onRedeemDiamonds,
                testTag = "dashboard_redeem_ff_tile",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ActionTile(
    title: String,
    reward: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, DarkNavyCardBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag(testTag),
        colors = CardDefaults.cardColors(containerColor = DarkNavySurface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 13.sp
            )

            Text(
                text = reward,
                color = accentColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun DailyMissionCard(tasksToday: Int) {
    val target = 10
    val progress = (tasksToday / target.toFloat()).coerceIn(0f, 1f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DarkNavyCardBorder, RoundedCornerShape(18.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkNavySurface),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = FlameGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Daily Mission Progress",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }

                Text(
                    text = "$tasksToday / $target Completed",
                    fontSize = 12.sp,
                    color = DiamondCyanLight,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = DiamondCyan,
                trackColor = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (tasksToday >= target) "🎉 Daily goal complete! Extra diamond bonus unlocked!" else "Complete ${target - tasksToday} more ad or captcha tasks today for bonus rewards!",
                fontSize = 11.sp,
                color = Color(0xFF94A3B8)
            )
        }
    }
}

@Composable
fun TransactionItem(tx: TransactionRecord) {
    val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.US)
    val formattedDate = dateFormat.format(Date(tx.timestamp))
    val isPositive = tx.amount > 0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(DarkNavySurface)
            .border(1.dp, DarkNavyCardBorder, RoundedCornerShape(14.dp))
            .padding(14.dp)
            .testTag("transaction_item_${tx.txId}"),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(
                        if (isPositive) SuccessGreen.copy(alpha = 0.15f) else DiamondCyan.copy(alpha = 0.15f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPositive) Icons.Default.Diamond else Icons.Default.Gamepad,
                    contentDescription = null,
                    tint = if (isPositive) SuccessGreen else DiamondCyan,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = tx.title,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 14.sp
                )
                Text(
                    text = tx.description,
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$formattedDate • TxID: ${tx.txId}",
                    color = Color(0xFF64748B),
                    fontSize = 10.sp
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = if (isPositive) "+${tx.amount} 💎" else "${tx.amount} 💎",
                fontWeight = FontWeight.Black,
                color = if (isPositive) SuccessGreen else FlameOrange,
                fontSize = 15.sp
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(SuccessGreen.copy(alpha = 0.2f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "DELIVERED",
                    color = SuccessGreen,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
