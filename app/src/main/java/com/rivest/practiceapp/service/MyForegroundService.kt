package com.rivest.practiceapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.rivest.practiceapp.R

class MyForegroundService : Service() {
    private var isServiceRunning = true;

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "STOP") {
            stopSelf()
            stopForeground(STOP_FOREGROUND_REMOVE)
            return START_NOT_STICKY
        }

        startForegroundService()

        // Background Task Example
        Thread {
            var i = 1
            while (isServiceRunning && i <= 10) {
                Thread.sleep(1000)
                Log.d("ForegroundService", "Running $i")
                i++
            }
            stopSelf()
        }.start()

        return START_STICKY
    }

    private fun startForegroundService() {
        val channelId = "foreground_service_channel"

        // Create Notification Channel (Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                channelId, "Foreground Service Channel", NotificationManager.IMPORTANCE_LOW
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MyForegroundService::class.java).apply {
            action = "STOP"
        }

        val stopPendingIntent =
            PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Notification
        val notification =
            NotificationCompat.Builder(this, channelId).setContentTitle("Foreground Service")
                .setContentText("Running in the background")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .addAction(1, "Stop", stopPendingIntent).build()

        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        isServiceRunning = false
    }
}