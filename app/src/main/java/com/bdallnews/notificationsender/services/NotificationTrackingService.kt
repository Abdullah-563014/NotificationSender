package com.bdallnews.notificationsender.services

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import android.widget.Toast
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.bdallnews.notificationsender.utils.CommonMethod
import com.bdallnews.notificationsender.utils.Constants
import com.bdallnews.notificationsender.utils.Coroutines
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.lang.reflect.Method
import java.net.URLDecoder

class NotificationTrackingService: NotificationListenerService() {


    override fun onNotificationPosted(sbn: StatusBarNotification?, rankingMap: RankingMap?) {
        sbn?.let {
            handleNotification(it)
        }
    }
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn?.let {
            handleNotification(it)
        }
    }


    private fun handleNotification(notification: StatusBarNotification) {
        val packageName: String=notification.packageName
        if (packageName.equals("com.opera.app.newslite")) {
            try {
                val doc: Document=Jsoup.connect("http://in.opr.news/${getIntent(notification.notification.contentIntent)?.getStringExtra("news_article_id")}?client=newslite").get()
                val allMetaData: Elements=doc.select("meta[content^=op-news://newsfeed/article]")
                for(single: Element in allMetaData) {
                    val temporaryUrl=single.attr("content").split("original_url=")
                    var originalUrl: String=URLDecoder.decode(temporaryUrl[1].replace("]",""),"UTF-8")
                    if (originalUrl.contains("&product=iosnews")) {
                        originalUrl=originalUrl.replace("&product=iosnews","")
                    }
                    val data: Data=Data.Builder()
                            .putString(Constants.title,notification.notification.extras.getString("android.title"))
                            .putString(Constants.message,notification.notification.extras.getString("android.text"))
                            .putString(Constants.url,originalUrl)
                            .build()
                    val oneTimeWorkRequest: OneTimeWorkRequest= OneTimeWorkRequest.Builder(NotificationTrackerWorker::class.java).setInputData(data).build()
                    WorkManager.getInstance(this).enqueue(oneTimeWorkRequest)
                }
            } catch (e: Exception) {
                Log.d(Constants.TAG,"failed to track notification for:- ${e.message}")
            }
        }
    }

    private fun getIntent(pendingIntent: PendingIntent): Intent? {
        try {
            val getIntent: Method=PendingIntent::class.java.getDeclaredMethod("getIntent")
            return getIntent.invoke(pendingIntent) as Intent
        }catch (e: Exception) {
            return null
        }
    }

}