package br.com.appforge.kotlinmusicplayer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class CustomApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels(){
        val channelId = "music"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Music",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
            //getSystemService(NotificationManager::class.java).createNotificationChannels(listOf(channel,channel))
            //getSystemService(NotificationManager::class.java).createNotificationChannelGroup()
            //getSystemService(NotificationManager::class.java).createNotificationChannelGroups()
        }
    }
}