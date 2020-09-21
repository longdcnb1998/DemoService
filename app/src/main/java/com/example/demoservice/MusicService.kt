package com.example.demoservice

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

class MusicService : Service(), MediaPlayer.OnCompletionListener {

    private var songs = mutableListOf<MySong>()
    private var mediaPlayer: MediaPlayer? = null
    private var iBinder: IBinder? = null
    private var currentState: Int? = null
    private var currentIndex: Int = 0


    override fun onCreate() {
        super.onCreate()
        initData()
        iBinder = MusicBinder()
    }

    private fun setMediaPlayer(player: MediaPlayer) {
        mediaPlayer = player
    }

    fun getState() = currentState

    fun playOrPauseSong() {
        /**
         * Khi nhac chua choi hoac da dung thi goi choi
         */
        if (currentState == MediaState.IDLE || currentState == MediaState.STOPPED) {
            val song: MySong = getCurrentSong()
            val mediaPlayer: MediaPlayer = MediaPlayer.create(this, song.resId)
            setMediaPlayer(mediaPlayer)
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener(this)
            currentState = MediaState.PLAYING
            return
        }

        if (currentState == MediaState.PLAYING) {
            mediaPlayer?.pause()
            currentState = MediaState.PAUSED
            return
        }
        mediaPlayer?.start()
        currentState = MediaState.PLAYING
    }

    private fun getCurrentSong(): MySong {
        return songs[this.currentIndex]
    }

    fun nextSong() {
        if (currentIndex < songs.size - 1) {
            currentIndex++
        } else {
            currentIndex = 0
        }
        stopSong()
        playOrPauseSong()
    }

    private fun stopSong() {
        mediaPlayer?.stop()
        currentState = MediaState.STOPPED
    }

    fun previousSong() {
        if (currentIndex > 0) {
            currentIndex--
        } else {
            currentIndex = songs.size - 1
        }
        stopSong()
        playOrPauseSong()
    }

    private fun initData() {
        songs.add(MySong("Chuyện rằng", R.raw.chuyen_rang))
        songs.add(MySong("Lỗi ở yêu thương", R.raw.loi_o_yeu_thuong))
        songs.add(MySong("Chờ đợi có đáng sợ", R.raw.cho_doi_co_dang_so))
        songs.add(MySong("Đi cùng em", R.raw.the_layzy_song))
        songs.add(MySong("Tình thôi xót xa", R.raw.tinh_thoi_xot_xa))
        songs.add(MySong("Tuyết rơi mùa hè", R.raw.tuyet_roi_mua_he))
    }

    override fun onBind(intent: Intent?): IBinder? = iBinder

    override fun onCompletion(mp: MediaPlayer?) {
        nextSong()
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        super.onDestroy()
    }

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }
}
