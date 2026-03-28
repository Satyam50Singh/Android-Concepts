package com.rivest.practiceapp.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.rivest.practiceapp.receivers.CancelWorkReceiver

class UploadFileForegroundWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        setForeground(createForegroundInfo())

        try {
            for (i in 1..10) {
                if (isStopped) return Result.failure()
                Thread.sleep(1000)
                Log.e("TAG", "doWork: Uploading ${i * 10}%")
            }
            return Result.success()
        } catch (e: Exception) {
            Log.e("TAG", "doWork: ${e.message}")
            return Result.failure()
        }
    }

    private fun createForegroundInfo(): ForegroundInfo {
        val channelId = "upload_channel"

        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Upload Service",
                NotificationManager.IMPORTANCE_LOW
            )
            manager.createNotificationChannel(channel)
        }

        val cancelIntent = Intent(applicationContext, CancelWorkReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            1,
            cancelIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Uploading File")
            .setContentText("Upload in progress...")
            .setSmallIcon(android.R.drawable.ic_menu_upload)
            .addAction(0, "Cancel", pendingIntent)
            .setOngoing(false)
            .build()

        return ForegroundInfo(1001, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
    }
}

