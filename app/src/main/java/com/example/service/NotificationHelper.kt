package com.example.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R

class NotificationHelper(private val context: Context) {

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Diamond Redemptions",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for Free Fire diamond top-ups and gift card redemptions"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 250, 100, 250)
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun showRedemptionNotification(
        uid: String,
        amount: Int,
        txId: String,
        nickname: String = "FreeFire Hero"
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("💎 Free Fire Diamonds Delivered!")
            .setContentText("Delivered $amount FF Diamonds to UID: $uid")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(
                        "🎉 Top-Up Confirmed!\n" +
                        "• Player UID: $uid ($nickname)\n" +
                        "• Amount Credited: $amount Free Fire Diamonds 💎\n" +
                        "• Transaction ID: $txId\n" +
                        "• Status: Completed & Synchronized\n" +
                        "Check your Free Fire in-game vault/mailbox!"
                    )
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(soundUri)
            .setVibrate(longArrayOf(0, 250, 100, 250))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
            }
        } else {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
        }
    }

    fun showGiftCardNotification(
        brandName: String,
        value: String,
        code: String,
        txId: String
    ) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("🎁 $brandName $value Voucher Ready!")
            .setContentText("Your digital gift card code: $code")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(
                        "Voucher Redeemed Successfully!\n" +
                        "• Card: $brandName ($value)\n" +
                        "• Code: $code\n" +
                        "• TxID: $txId\n" +
                        "Copy this code and redeem in store."
                    )
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify((System.currentTimeMillis() % 10000).toInt(), builder.build())
        }
    }

    companion object {
        const val CHANNEL_ID = "diamond_redemptions_channel"
        const val NOTIFICATION_ID = 1001
    }
}
