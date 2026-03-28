package com.rivest.practiceapp

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.rivest.practiceapp.service.DownloadBackgroundService
import com.rivest.practiceapp.service.MyForegroundService
import com.rivest.practiceapp.ui.activities.MainActivity2
import com.rivest.practiceapp.workers.UploadDocWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("lifecycle A", "onCreate")
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        initView()
        // switchToAnotherActivity()
    }

    private fun initView() {
        val btnForegroundService = findViewById<Button>(R.id.btn_foreground_service)
        val btnBackgroundService = findViewById<Button>(R.id.btn_background_service)
        val btnUploadWorker = findViewById<Button>(R.id.btn_upload_worker)
        val btnPeriodicWorker = findViewById<Button>(R.id.btn_periodic_worker)
        btnForegroundService.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (checkNotificationPermission()) {
                    startMyService()
                } else {
                    requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 100)
                }
            } else {
                startMyService()
            }
        }

        btnBackgroundService.setOnClickListener {
            val intent = Intent(this, DownloadBackgroundService::class.java)
            startService(intent)
        }

        btnUploadWorker.setOnClickListener {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<UploadDocWorker>()
                .setInitialDelay(2, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(this).enqueue(workRequest)
        }

        btnPeriodicWorker.setOnClickListener {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workRequest = PeriodicWorkRequestBuilder<UploadDocWorker>(20, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "UploadDocWork",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        }
    }

    private fun switchToAnotherActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }, 2000)
    }

    override fun onStart() {
        super.onStart()
        Log.e("lifecycle A", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e("lifecycle A", "onResume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.e("lifecycle A", "onRestart")
    }

    override fun onPause() {
        super.onPause()
        Log.e("lifecycle A", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e("lifecycle A", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("lifecycle A", "onDestroy")
    }

    private fun checkNotificationPermission(): Boolean {
        return checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            Handler(Looper.getMainLooper()).postDelayed({
                startMyService()
            }, 500)
        }
    }

    private fun startMyService() {
        val intent = Intent(this, MyForegroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }
}