package com.shankha.epiceatsadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.shankha.epiceatsadmin.databinding.ActivitySpalshBinding

class SpalshActivity : AppCompatActivity() {
     private val binding:ActivitySpalshBinding by lazy {
         ActivitySpalshBinding.inflate(layoutInflater)
     }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        },2000)

    }
}