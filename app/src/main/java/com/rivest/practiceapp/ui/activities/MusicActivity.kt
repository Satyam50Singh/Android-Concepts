package com.rivest.practiceapp.ui.activities

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rivest.practiceapp.R
import com.rivest.practiceapp.listeners.MusicStateListener
import com.rivest.practiceapp.service.MusicBoundService

class MusicActivity : AppCompatActivity() {

    private var musicBoundService: MusicBoundService? = null
    private var isBound = false
    private lateinit var playBtn: Button


    private val connection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            val binder = service as MusicBoundService.MusicBinder
            musicBoundService = binder.getService()
            isBound = true

            musicBoundService?.setListener(object : MusicStateListener {
                override fun stateChanged(isPlaying: Boolean) {
                    runOnUiThread {
                        if (!isPlaying) {
                            playBtn.text = getString(R.string.play_music)
                        } else {
                            playBtn.text = getString(R.string.pause_music)
                        }
                    }
                }
            })
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            musicBoundService = null
            isBound = false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_music)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initView()
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, MusicBoundService::class.java)

        startForegroundService(intent)

        bindService(intent, connection, BIND_AUTO_CREATE)
    }

    private fun initView() {
        playBtn = findViewById(R.id.btn_play_music)

        playBtn.setOnClickListener {
            if (isBound) {
                if (musicBoundService?.isMusicPlaying() == true) {
                    playBtn.text = getString(R.string.play_music)
                    musicBoundService?.pauseMusic()
                } else {
                    playBtn.text = getString(R.string.pause_music)
                    musicBoundService?.playMusic()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (isBound) {
            unbindService(connection)
            isBound = false
        }
    }

    fun pauseMusic() {
        musicBoundService?.pauseMusic()
    }

    fun stopMusic() {
        musicBoundService?.stopMusic()
    }
}