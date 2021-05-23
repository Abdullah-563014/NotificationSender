package com.bdallnews.notificationsender.services

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bdallnews.notificationsender.R
import com.bdallnews.notificationsender.ui.MyApp
import com.bdallnews.notificationsender.ui.home.HomeActivity
import com.bdallnews.notificationsender.utils.CommonMethod
import com.bdallnews.notificationsender.utils.Constants

class MyForgroundService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val pendingIntent: PendingIntent = Intent(this, HomeActivity::class.java)
            .let { notificationIntent ->
                notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }
        var notification: Notification
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            notification = NotificationCompat.Builder(this,
                (applicationContext as MyApp).myNotificationChannel)
                .setContentTitle(resources.getString(R.string.notification_tracking))
                .setContentText(resources.getString(R.string.watching_notification))
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_tracking)
                .setTicker(resources.getString(R.string.notification_tracking))
                .setOngoing(true)
                .build()
        } else {
            notification = NotificationCompat.Builder(this)
                .setContentTitle(resources.getString(R.string.notification_tracking))
                .setContentText(resources.getString(R.string.watching_notification))
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_tracking)
                .setTicker(resources.getString(R.string.notification_tracking))
                .setOngoing(true)
                .build()
        }
        notification.flags= Notification.FLAG_NO_CLEAR
        startForeground(Constants.forgroundNotificationId, notification)
        if (CommonMethod.isMyServiceRunning(this,NotificationTrackingService::class.java)) {
            stopService(Intent(this,NotificationTrackingService::class.java))
        }
        startService(Intent(this,NotificationTrackingService::class.java))
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


}