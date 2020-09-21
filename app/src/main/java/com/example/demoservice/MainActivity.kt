package com.example.demoservice

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {


    private var ivPlayPause: ImageView? = null
    private var ivNext: ImageView? = null
    private var ivPrevious: ImageView? = null

    private var myMusicService: MusicService? = null
    private var myServiceConnection: ServiceConnection? = null

    private var bound = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    override fun onStart() {
        super.onStart()
        Intent(this, MusicService::class.java).also { intent ->
            bindService(intent, myServiceConnection as ServiceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun initView() {
        ivNext = findViewById(R.id.image_next)
        ivPrevious = findViewById(R.id.image_previous)
        ivPlayPause = findViewById(R.id.image_play_pause)

        ivPlayPause?.setOnClickListener(this)
        ivNext?.setOnClickListener(this)
        ivPrevious?.setOnClickListener(this)

        bindToService()
    }

    private fun bindToService() {
        myServiceConnection = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                bound = false
                Toast.makeText(this@MainActivity, "Disconnected!", Toast.LENGTH_SHORT).show()
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val musicBinder: MusicService.MusicBinder = service as MusicService.MusicBinder
                myMusicService = musicBinder.getService()
                bound = true
                Toast.makeText(this@MainActivity, "Connected!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.image_next -> {
                nextSong()
            }
            R.id.image_previous -> {
                previousSong()
            }
            R.id.image_play_pause -> {
                playOrPauseSong()
            }
        }
    }

    private fun previousSong() {
        if (!bound) {
            return
        }
        myMusicService?.previousSong()
    }

    private fun nextSong() {
        if (!bound) {
            return
        }
        myMusicService?.nextSong()
    }

    private fun playOrPauseSong() {
        if (!bound) {
            return
        }
        myMusicService?.playOrPauseSong()
        if (myMusicService?.getState() == MediaState.PLAYING) {
            ivPlayPause?.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
        } else {
            ivPlayPause?.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
        }
    }


    override fun onDestroy() {
        myServiceConnection?.let { unbindService(it) }
        bound = false
        super.onDestroy()
    }
}
