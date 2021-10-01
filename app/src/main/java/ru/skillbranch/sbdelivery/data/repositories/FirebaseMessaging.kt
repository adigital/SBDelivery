// https://www.youtube.com/watch?v=K8TEq4GCCsg&feature=emb_logo
package ru.skillbranch.sbdelivery.data.repositories

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.RootActivity
import ru.skillbranch.sbdelivery.data.local.entities.Notice
import java.util.*

class FirebaseMessaging : FirebaseMessagingService() {

    private val noticesRepository = NoticesRepository

    private lateinit var title: String
    lateinit var message: String
    private var CHANNEL_ID = "CHANNEL"
    private lateinit var manager: NotificationManager

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        val data: Map<String, String> = p0.data

        if (data.isNotEmpty()) {
            title = data["title"].toString()
            message = data["message"].toString()

            manager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            sendNotification()

            saveToDb(title, message)
        }
    }

    private fun sendNotification() {
        val intent = Intent(applicationContext, RootActivity::class.java)

        intent.putExtra("title", title)
        intent.putExtra("message", message)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "pushnotification",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setContentText(message)

        val pendingIntent =
            PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        builder.setContentIntent(pendingIntent)
        manager.notify(0, builder.build())
    }

    private fun saveToDb(title: String, message: String) {
        noticesRepository.insertNoticesDao(
            Notice(
                UUID.randomUUID().toString(),
                true,
                title,
                message
            )
        )
    }
}