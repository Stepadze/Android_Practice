package com.example.consecutivepractices.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.consecutivepractices.MainActivity

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val name = intent.getStringExtra("name") ?: "Студент"
        showNotification(context, name)
    }

    private fun showNotification(context: Context, name: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Создаём канал (нужно для Android 8+)
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Напоминание о паре",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Уведомления о начале пары"
        }
        notificationManager.createNotificationChannel(channel)

        // Intent для открытия приложения по нажатию
        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Время пары!")
            .setContentText("$name, начинается любимая пара по мобильной разработке!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        const val CHANNEL_ID = "pair_reminder_channel"
        const val NOTIFICATION_ID = 1001
    }
}