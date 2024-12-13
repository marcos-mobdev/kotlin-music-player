package br.com.appforge.kotlinmusicplayer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import br.com.appforge.kotlinmusicplayer.databinding.ActivityMusicBinding
import br.com.appforge.kotlinmusicplayer.services.MusicService

class MusicActivity : AppCompatActivity(), ServiceConnection {

    private val binding by lazy{
        ActivityMusicBinding.inflate(layoutInflater)
    }

    private lateinit var serviceConnection: ServiceConnection
    private lateinit var musicService: MusicService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        serviceConnection = this

        val musicService = Intent(this, MusicService::class.java)
        bindService(musicService, serviceConnection, BIND_AUTO_CREATE)

        binding.btnPlay.setOnClickListener { play(musicService) }
        binding.btnPause.setOnClickListener { pause(musicService) }
        binding.btnStop.setOnClickListener { stop(musicService) }

        initializeVolumeControls()
    }

    private fun play(musicService: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(musicService.setAction("play"))
        }else{
            startService(musicService.setAction("play"))
        }
    }

    private fun stop(musicService: Intent) {
        startService(musicService.setAction("stop"))
    }

    private fun pause(musicService: Intent) {
        startService(musicService.setAction("pause"))
    }

    private fun initializeVolumeControls(){

        val audioManager = getSystemService(AudioManager::class.java)
        //Alternative
        //val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        binding.volumeBar.max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        binding.volumeBar.progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        binding.volumeBar.setOnSeekBarChangeListener(object:OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0 )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MusicBinder
        musicService = binder.getService()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        TODO("Not yet implemented")
    }
}