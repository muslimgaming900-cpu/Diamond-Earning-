package com.example.ui.screens

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GiftCardItem
import com.example.data.model.UserAccount
import com.example.ui.components.DiamondBadge
import com.example.ui.theme.DarkNavyCardBorder
import com.example.ui.theme.DarkNavySurface
import com.example.ui.theme.DarkNavySurfaceVariant
import com.example.ui.theme.DiamondCyan
import com.example.ui.theme.DiamondCyanDark
import com.example.ui.theme.DiamondCyanLight
import com.example.ui.theme.FlameGold
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.SuccessGreen

@Composable
fun RedeemScreen(
    user: UserAccount,
    ffUidInput: String,
    onFfUidChange: (String) -> Unit,
    ffAmountInput: String,
    onFfAmountChange: (String) -> Unit,
    onSelectPreset: (Int) -> Unit,
    onSubmitFreeFireRedeem: () -> Unit,
    isRedeeming: Boolean,
    giftCardsList: List<GiftCardItem>,
    onRedeemGiftCard: (GiftCardItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

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
            item { Spacer(modifier = Modifier.height(6.dp)) }

            // Title & Current Balance
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Withdrawal Center",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = "1 App Diamond = 1 Free Fire Diamond",
                            style = MaterialTheme.typography.bodySmall,
                            color = DiamondCyan
                        )
                    }
                    DiamondBadge(count = user.diamonds)
                }
            }

            // Tabs: Free Fire vs Gift Cards
            item {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = DarkNavySurface,
                    contentColor = DiamondCyan,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = DiamondCyan
                        )
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, DarkNavyCardBorder, RoundedCornerShape(14.dp))
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Gamepad, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Free Fire Diamonds", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CardGiftcard, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Digital Gift Cards", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    )
                }
            }

            if (selectedTabIndex == 0) {
                // Free Fire Tab
                item {
                    FreeFireRedeemForm(
                        user = user,
                        uidInput = ffUidInput,
                        onUidChange = onFfUidChange,
                        amountInput = ffAmountInput,
                        onAmountChange = onFfAmountChange,
                        onSelectPreset = onSelectPreset,
                        onSubmit = onSubmitFreeFireRedeem,
                        isRedeeming = isRedeeming
                    )
                }
            } else {
                // Digital Gift Cards Tab
                items(giftCardsList) { card ->
                    GiftCardRow(
                        card = card,
                        userDiamonds = user.diamonds,
                        onRedeem = { onRedeemGiftCard(card) },
                        isRedeeming = isRedeeming
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun FreeFireRedeemForm(
    user: UserAccount,
    uidInput: String,
    onUidChange: (String) -> Unit,
    amountInput: String,
    onAmountChange: (String) -> Unit,
    onSelectPreset: (Int) -> Unit,
    onSubmit: () -> Unit,
    isRedeeming: Boolean
) {
    val presets = listOf(50, 100, 310, 520, 1060, 2180)
    val parsedAmount = amountInput.toIntOrNull() ?: 0
    val canRedeem = uidInput.trim().length >= 6 && parsedAmount > 0 && parsedAmount <= user.diamonds

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .border(
                1.dp,
                Brush.horizontalGradient(listOf(DiamondCyan.copy(alpha = 0.7f), Color(0xFF2563EB))),
                RoundedCornerShape(22.dp)
            )
            .testTag("freefire_redeem_form"),
        colors = CardDefaults.cardColors(containerColor = DarkNavySurface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF00363F)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Gamepad,
                            contentDescription = null,
                            tint = DiamondCyan,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Free Fire UID Top-Up",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = "Direct delivery to game account",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(SuccessGreen.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Real API Verified",
                        color = SuccessGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Player UID Field
            Text(
                text = "FREE FIRE PLAYER UID",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = DiamondCyanLight
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = uidInput,
                onValueChange = onUidChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("freefire_uid_input"),
                placeholder = { Text("e.g. 1083921820", color = Color.Gray, fontSize = 14.sp) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DiamondCyan,
                    unfocusedBorderColor = DarkNavyCardBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Amount Field
            Text(
                text = "DIAMONDS TO REDEEM (1 App Diamond = 1 FF Diamond)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = DiamondCyanLight
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = amountInput,
                onValueChange = onAmountChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("freefire_amount_input"),
                placeholder = { Text("Enter diamond amount...", color = Color.Gray, fontSize = 14.sp) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                trailingIcon = {
                    Text(
                        text = "💎 FF",
                        fontWeight = FontWeight.Bold,
                        color = DiamondCyan,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DiamondCyan,
                    unfocusedBorderColor = DarkNavyCardBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Preset Amount Chips
            Text(
                text = "Popular Packs:",
                fontSize = 11.sp,
                color = Color(0xFF94A3B8)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                presets.take(4).forEach { pack ->
                    FilterChip(
                        selected = amountInput == pack.toString(),
                        onClick = { onSelectPreset(pack) },
                        label = { Text("$pack 💎", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DiamondCyan,
                            selectedLabelColor = Color(0xFF00363F),
                            containerColor = Color(0xFF0F172A),
                            labelColor = Color.White
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = DarkNavyCardBorder,
                            selectedBorderColor = DiamondCyan,
                            enabled = true,
                            selected = amountInput == pack.toString()
                        ),
                        modifier = Modifier.testTag("preset_chip_$pack")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Security note
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF0F172A))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = DiamondCyan,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Encrypted API gateway. Confirmation notification sent with TxID.",
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Redeem Button
            Button(
                onClick = onSubmit,
                enabled = canRedeem && !isRedeeming,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_ff_redeem_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DiamondCyan,
                    contentColor = Color(0xFF00363F),
                    disabledContainerColor = Color(0xFF1E293B),
                    disabledContentColor = Color(0xFF475569)
                )
            ) {
                if (isRedeeming) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color(0xFF00363F),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Processing via Secure API...", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                } else {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (parsedAmount > user.diamonds) "Insufficient Diamonds ($parsedAmount > ${user.diamonds})"
                        else "REDEEM $parsedAmount FREE FIRE DIAMONDS",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun GiftCardRow(
    card: GiftCardItem,
    userDiamonds: Int,
    onRedeem: () -> Unit,
    isRedeeming: Boolean
) {
    val canAfford = userDiamonds >= card.diamondsCost

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DarkNavyCardBorder, RoundedCornerShape(16.dp))
            .testTag("giftcard_${card.id}"),
        colors = CardDefaults.cardColors(containerColor = DarkNavySurface),
        shape = RoundedCornerShape(16.dp)
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
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(card.brandColorHex).copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = null,
                        tint = Color(card.brandColorHex),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "${card.brandName} (${card.valueUSD})",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Instant digital redemption code",
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp
                    )
                    Text(
                        text = "${card.diamondsCost} Diamonds",
                        color = if (canAfford) DiamondCyan else FlameGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            Button(
                onClick = onRedeem,
                enabled = canAfford && !isRedeeming,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (canAfford) DiamondCyanDark else Color(0xFF1E293B),
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFF1E293B),
                    disabledContentColor = Color(0xFF475569)
                )
            ) {
                Text(
                    text = if (canAfford) "Claim" else "Need 💎",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}
