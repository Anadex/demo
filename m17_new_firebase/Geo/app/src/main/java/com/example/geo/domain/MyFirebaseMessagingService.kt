package com.example.geo.domain

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color.argb
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.geo.App
import com.example.geo.R
import com.example.geo.presentation.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.data.isNotEmpty()) {
            val intent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            val notification = NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setSmallIcon(R.drawable.place_of_interest)
                .setContentTitle(message.data["name"])
                .setContentText("${message.data["message"]} \nОтправлено ${convertToDate(message.data["timestamp"])}")
                .setColor(argb(255, 0, 200, 0))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(3)
                .build()

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                NotificationManagerCompat.from(this).notify(Random.nextInt(), notification)
            } else {
                val scope = CoroutineScope(Dispatchers.Main)
                scope.launch(Dispatchers.Main) {

                    Toast.makeText(
                        applicationContext,
                        "Необходимо разрешение на отправку уведомлений",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    private fun convertToDate(timestamp: String?): String {
        timestamp ?: return ""
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(Date(timestamp.toLong() * 1000))
    }
}