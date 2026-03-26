package com.rivest.practiceapp.ui.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rivest.practiceapp.R
import com.rivest.practiceapp.ui.fragments.FragmentA

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("lifecycle B", "onCreate")
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Toast.makeText(applicationContext, "Welcome to Activity 2", Toast.LENGTH_SHORT).show()

        if (savedInstanceState == null) {
            bindFragment()
        }
    }

    private fun bindFragment() {
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container_view_tag,
            FragmentA()
        ).commit()
    }

    override fun onStart() {
        super.onStart()
        Log.e("lifecycle B", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e("lifecycle B", "onResume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.e("lifecycle B", "onRestart")
    }

    override fun onPause() {
        super.onPause()
        Log.e("lifecycle B", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e("lifecycle B", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("lifecycle B", "onDestroy")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.e("lifecycle B", "onBackPressed")

        Handler(Looper.getMainLooper()).postDelayed({
            Toast.makeText(applicationContext, "Moved to activity A", Toast.LENGTH_SHORT).show()
            Log.e("lifecycle B", "toast appeared")
        }, 1000)
    }
}