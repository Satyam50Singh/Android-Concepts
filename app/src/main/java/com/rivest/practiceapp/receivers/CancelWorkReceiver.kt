package com.rivest.practiceapp.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.WorkManager

class CancelWorkReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        WorkManager.getInstance(context).cancelUniqueWork("upload_work")
        println("Work Cancelled from Notification")
    }
}