package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LeaderboardUser
import com.example.data.model.UserAccount
import com.example.ui.components.DiamondBadge
import com.example.ui.theme.DarkNavyCardBorder
import com.example.ui.theme.DarkNavySurface
import com.example.ui.theme.DiamondCyan
import com.example.ui.theme.FlameGold
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.FlameYellow
import java.text.NumberFormat
import java.util.Locale

@Composable
fun LeaderboardScreen(
    user: UserAccount,
    leaderboardList: List<LeaderboardUser>,
    modifier: Modifier = Modifier
) {
    val formatter = NumberFormat.getNumberInstance(Locale.US)

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 720.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(modifier = Modifier.height(6.dp)) }

            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Leaderboard Rankings",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = "Top Diamond Earners Worldwide",
                            style = MaterialTheme.typography.bodySmall,
                            color = FlameGold
                        )
                    }
                    DiamondBadge(count = user.diamonds)
                }
            }

            // Current User Ranking Card (Sticky spotlight)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(
                            1.dp,
                            Brush.horizontalGradient(listOf(DiamondCyan, Color(0xFF3B82F6))),
                            RoundedCornerShape(20.dp)
                        )
                        .testTag("current_user_rank_card"),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1A2E))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF00363F)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "#${user.currentRank}",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp,
                                    color = DiamondCyan
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${user.displayName} (You)",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 15.sp
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
                                    text = "Lifetime Earnings: ${formatter.format(user.totalEarned)} 💎",
                                    fontSize = 12.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = FlameGold,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            // Monthly Prize Banner
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF1B150A))
                        .border(1.dp, FlameGold.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = FlameYellow,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Season Grand Prize Pool",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Top 3 players win an extra 5,000 Free Fire Diamonds this Sunday!",
                            fontSize = 11.sp,
                            color = FlameGold
                        )
                    }
                }
            }

            // Leaderboard Items
            items(leaderboardList) { lbUser ->
                LeaderboardRow(lbUser = lbUser, formatter = formatter)
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun LeaderboardRow(lbUser: LeaderboardUser, formatter: NumberFormat) {
    val rankColor = when (lbUser.rank) {
        1 -> Color(0xFFFFD700) // Gold
        2 -> Color(0xFFE0E0E0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> Color(0xFF94A3B8)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (lbUser.isCurrentUser) DiamondCyan else DarkNavyCardBorder,
                RoundedCornerShape(16.dp)
            )
            .testTag("leaderboard_user_${lbUser.rank}"),
        colors = CardDefaults.cardColors(
            containerColor = if (lbUser.isCurrentUser) Color(0xFF0F1A2E) else DarkNavySurface
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Rank position badge
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(rankColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "#${lbUser.rank}",
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                        color = rankColor
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Avatar / Icon
                Text(
                    text = lbUser.avatarEmoji,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (lbUser.isCurrentUser) "${lbUser.username} (You)" else lbUser.username,
                            fontWeight = FontWeight.Bold,
                            color = if (lbUser.isCurrentUser) DiamondCyan else Color.White,
                            fontSize = 14.sp
                        )
                    }
                    Text(
                        text = lbUser.badge,
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            // Diamonds Earned
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Diamond,
                    contentDescription = null,
                    tint = DiamondCyan,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = formatter.format(lbUser.diamondsEarned),
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}
