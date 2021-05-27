package com.bdallnews.notificationsender.services

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bdallnews.notificationsender.database.NotificationDao
import com.bdallnews.notificationsender.model.NotificationModel
import com.bdallnews.notificationsender.ui.MyApp
import com.bdallnews.notificationsender.utils.CommonMethod
import com.bdallnews.notificationsender.utils.Constants
import com.bdallnews.notificationsender.utils.Coroutines

class NotificationTrackerWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext,workerParams) {

    private var flag: Boolean=false

    override fun doWork(): Result {
        val data: Data=inputData

        Coroutines.io {
            val allNotification: List<NotificationModel> =(applicationContext as MyApp).appDatabase.notificationDao().getAllNotificationModel()
            for (notificationModel: NotificationModel in allNotification) {
                if (notificationModel.title.equals(data.getString(Constants.title))) {
                    flag=true
                    break
                } else {
                    flag=false
                }
            }
            if (!flag) {
                CommonMethod.sendNotification(data.getString(Constants.title),"${data.getString(Constants.title)}... Click here for more details",data.getString(Constants.url))
                (applicationContext as MyApp).appDatabase.notificationDao().insertNotificationModel(NotificationModel(null,data.getString(Constants.title),data.getString(Constants.message),data.getString(Constants.url)))
            }
            if (allNotification.size>=200) {
                (applicationContext as MyApp).appDatabase.notificationDao().deleteAllNotificationModel()
            }
        }
        return Result.success()
    }


}