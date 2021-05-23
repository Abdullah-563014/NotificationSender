package com.bdallnews.notificationsender.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_table")
data class NotificationModel(

    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo(name = "Title")
    val title: String?,

    @ColumnInfo(name = "Description")
    val description: String?,

    @ColumnInfo(name = "Url")
    val url: String?
)
