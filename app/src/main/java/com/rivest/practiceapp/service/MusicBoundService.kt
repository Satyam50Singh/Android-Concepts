package com.rivest.practiceapp.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MusicBoundService : Service() {

    private val binder = MusicBinder()
    private var isPlaying = false

    inner class MusicBinder : Binder() {
        fun getService(): MusicBoundService = this@MusicBoundService
    }

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun playMusic() {
        isPlaying = true
        Log.d("MusicService", "Music Playing")
    }

    fun pauseMusic() {
        isPlaying = false
        Log.d("MusicService", "Music Paused")
    }

    fun isMusicPlaying(): Boolean = isPlaying
}