package br.com.appforge.kotlinmusicplayer.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import br.com.appforge.kotlinmusicplayer.MusicActivity
import br.com.appforge.kotlinmusicplayer.R

class MusicService : Service() {

    private var mediaPlayer : MediaPlayer? = null
    private var binder = MusicBinder()

    inner class MusicBinder: Binder() {
        fun getService(): MusicService {
            return this@MusicService
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        initializeMediaPlayer()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val action = intent?.action

        when(action){
            "play"-> playMusic()
            "pause"->pauseMusic()
            "stop"->stopMusic()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun playMusic() {
        initializeMusicServiceNotification()
        if(mediaPlayer == null){
            initializeMediaPlayer()
        }
        mediaPlayer?.start()
    }

    private fun pauseMusic() {
        if(mediaPlayer?.isPlaying == true){
            mediaPlayer?.pause()
        }
    }

    private fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        stopSelf()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    private fun initializeMediaPlayer() {
        mediaPlayer = MediaPlayer.create(
            this, R.raw.teste
        )
    }

    private fun initializeMusicServiceNotification(){
        val openAppIntent = Intent(this, MusicActivity::class.java)
        openAppIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "music"
        val notification = NotificationCompat.Builder(this, channelId).apply {
            setSmallIcon(R.drawable.ic_music_24)
            setShowWhen(true)
            setContentIntent(pendingIntent)
            setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.disc_image
                )
            )
            setContentTitle("Music")
            setContentText("Playing")
        }
        startForeground(1,notification.build())


    }

    override fun onDestroy() {
        super.onDestroy()
        stopMusic()
    }

}