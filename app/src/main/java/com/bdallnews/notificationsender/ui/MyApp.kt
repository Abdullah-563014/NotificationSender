package com.bdallnews.notificationsender.ui

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.bdallnews.notificationsender.R
import com.bdallnews.notificationsender.database.AppDatabase
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class MyApp: Application() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    val myNotificationChannel: String="MyNotificationChannel"
    val appDatabase by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()


        FirebaseApp.initializeApp(this)
        firebaseAnalytics= Firebase.analytics
        Firebase.analytics.setAnalyticsCollectionEnabled(true)

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            val channel=NotificationChannel(myNotificationChannel,resources.getString(R.string.app_name),NotificationManager.IMPORTANCE_LOW)
            channel.description=resources.getString(R.string.app_name)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }



    }





}