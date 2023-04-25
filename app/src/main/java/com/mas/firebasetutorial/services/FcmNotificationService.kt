package com.mas.firebasetutorial.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mas.firebasetutorial.MainActivity
import com.mas.firebasetutorial.R

class FcmNotificationService : FirebaseMessagingService() {

    companion object {
        const val NOTIFICATION_ID = "FCM_Notification"
        const val NOTIFICATION_CHANNEL = "FirebaseTutorial channel"
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val notification = message.notification
        createNotification(notification?.title, notification?.body)
    }

    private fun createNotification(title: String?, message: String?) {
        Log.d("FcmNotificationService", title + ":" + message)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(MainActivity.BUNDLE_NOTIFICATION, true)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val flags = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
            else -> FLAG_UPDATE_CURRENT
        }
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, flags)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_ID,
                NOTIFICATION_CHANNEL,
                NotificationManager.IMPORTANCE_HIGH)

            notificationManager.createNotificationChannel(notificationChannel)
        }

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_ID)
            .setContent(createRemoteView(title, message))
            .setAutoCancel(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    private fun createRemoteView(title: String?, message: String?): RemoteViews {
        val view = RemoteViews(packageName, R.layout.notification_layout)
        view.setTextViewText(R.id.title, title)
        view.setTextViewText(R.id.message, message)
        view.setImageViewResource(R.id.imageView, R.drawable.ic_fire_launcher_foreground)
        return view
    }
}