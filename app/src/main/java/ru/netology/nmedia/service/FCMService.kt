package ru.netology.nmedia.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.Action
import ru.netology.nmedia.Like
import ru.netology.nmedia.NotificationType
import ru.netology.nmedia.R


class FCMService : FirebaseMessagingService() {
    private val channelId = "nmedia_notification"
    private val action = "action"
    private val content = "content"
    private val gson = Gson()
    private val tagFCMService = "FCMServiceTag"

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = getString(R.string.chanel_name)
            val channelDescription = getString(R.string.channel_description)

            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            channel.description = channelDescription
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(false)
            val notificationManager =  getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        super.onMessageReceived(remoteMessage)

        try {
            val actionValue = Action.valueOf(remoteMessage.data[action].toString())
            when(actionValue) {
                Action.LIKE -> modifyPost(gson.fromJson(remoteMessage.data[content], Like::class.java), "liked" )
                Action.SHARE -> modifyPost(gson.fromJson(remoteMessage.data[content], Like::class.java), "shared" )
                else -> println(2)
            }
        }
        catch (e: Exception) {
            Log.d(tagFCMService, "Data Message exception: " + e.message);
            Log.d(tagFCMService, "Message Notification Body: " + remoteMessage.getNotification()?.getBody());
            alarmNotification()
        }
    }

    private fun alarmNotification() {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notifications_24)
            .setContentTitle(getString(R.string.imposible_operation))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(NotificationType.ALARM, notification)
    }

    private fun modifyPost(content: Like, whatDo: String) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notifications_24)
            .setContentTitle(
                getString(
                    R.string.notification_user_modify,
                    content.userName,
                    whatDo,
                    content.postAuthor,
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(NotificationType.BASIC_ACTION, notification)
    }
}