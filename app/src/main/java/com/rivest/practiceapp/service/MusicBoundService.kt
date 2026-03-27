package com.rivest.practiceapp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.rivest.practiceapp.listeners.MusicStateListener

class MusicBoundService : Service() {

    private val binder = MusicBinder()
    private var isPlaying = false
    private var isForegroundStarted = false
    val channelId: String = "music_channel"

    private var listener: MusicStateListener? = null

    fun setListener(listener: MusicStateListener) {
        this.listener = listener
    }

    inner class MusicBinder : Binder() {
        fun getService(): MusicBoundService = this@MusicBoundService
    }

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "PLAY" -> playMusic()
            "PAUSE" -> pauseMusic()
            "STOP" -> {
                stopMusic()
                return START_NOT_STICKY
            }
        }

        if (!isForegroundStarted) {
            startForeground(101, createNotification())
            isForegroundStarted = true
        } else {
            updateNotification()
        }

        return START_STICKY
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            channelId, "Music Channel", NotificationManager.IMPORTANCE_LOW
        )

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    fun playMusic() {
        isPlaying = true
        updateNotification()
        listener?.stateChanged(isPlaying)
    }

    fun pauseMusic() {
        isPlaying = false
        updateNotification()
        listener?.stateChanged(isPlaying)
    }

    fun isMusicPlaying(): Boolean = isPlaying

    private fun updateNotification() {
        if (!isForegroundStarted) return
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(101, createNotification())
    }

    private fun createNotification(): Notification {

        // PLAY intent
        val playIntent = Intent(this, MusicBoundService::class.java).apply {
            action = "PLAY"
        }
        val playPendingIntent = PendingIntent.getService(
            this, 1, playIntent, PendingIntent.FLAG_IMMUTABLE
        )

        // PAUSE intent
        val pauseIntent = Intent(this, MusicBoundService::class.java).apply {
            action = "PAUSE"
        }
        val pausePendingIntent = PendingIntent.getService(
            this, 2, pauseIntent, PendingIntent.FLAG_IMMUTABLE
        )

        // STOP intent
        val stopIntent = Intent(this, MusicBoundService::class.java).apply {
            action = "STOP"
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 3, stopIntent, PendingIntent.FLAG_IMMUTABLE
        )

        return Notification.Builder(this, channelId)
            .setContentTitle("Music Service")
            .setContentText("Music is ${if (isPlaying) "Playing" else "Paused"}")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .addAction(0, "Play", playPendingIntent)
            .addAction(0, "Pause", pausePendingIntent)
            .addAction(0, "Stop", stopPendingIntent)
            .build()
    }

    fun stopMusic() {
        isPlaying = false
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        listener?.stateChanged(isPlaying)
    }

}