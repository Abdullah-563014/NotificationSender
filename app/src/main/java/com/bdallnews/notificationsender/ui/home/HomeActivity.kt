package com.bdallnews.notificationsender.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.*
import com.bdallnews.notificationsender.databinding.ActivityMainBinding
import com.bdallnews.notificationsender.services.MyForgroundService
import com.bdallnews.notificationsender.services.NotificationTrackerWorker
import com.bdallnews.notificationsender.services.NotificationTrackingService
import com.bdallnews.notificationsender.utils.CommonMethod
import com.bdallnews.notificationsender.utils.Constants
import com.bdallnews.notificationsender.utils.Coroutines
import java.util.concurrent.TimeUnit

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        startMyFrogroundService()



    }


    private fun startMyFrogroundService() {
        if (!CommonMethod.isMyServiceRunning(this,MyForgroundService::class.java)) {
            ContextCompat.startForegroundService(this, Intent(this,MyForgroundService::class.java))
        }
    }




}