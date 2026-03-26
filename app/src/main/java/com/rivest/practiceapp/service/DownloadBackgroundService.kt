package com.rivest.practiceapp.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class DownloadBackgroundService : Service() {

    private var isRunning = false;

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (isRunning) {
            Log.d("Service", "Already running, ignoring new request")
            return START_NOT_STICKY
        }

        Thread {
            isRunning = true
            downloadFile()
        }.start()

        return START_STICKY
    }

    private fun downloadFile() {
        for (i in 1..10) {
            Thread.sleep(1000)
            Log.d("DownloadService", "Downloading file ${i * 10}%")
        }

        Log.d("DownloadService", "Download Complete")
        isRunning = false
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("DownloadService", "Download Service Destroyed")
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

}