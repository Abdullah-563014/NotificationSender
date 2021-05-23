package com.bdallnews.notificationsender.database

import androidx.room.*
import com.bdallnews.notificationsender.model.NotificationModel

@Dao
interface NotificationDao {

    @Query("SELECT * FROM notification_table ORDER BY id ASC")
    fun getAllNotificationModel(): List<NotificationModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotificationModel(notificationModel: NotificationModel)

    @Delete
    fun deleteNotificationModel(notificationModel: NotificationModel)

    @Query("DELETE FROM notification_table")
    fun deleteAllNotificationModel()
}