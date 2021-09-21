package com.example.proyecto2b

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val LoginFacebook = findViewById<ImageView>(R.id.iv_loginFacebook)
        val LoginGoogle = findViewById<ImageView>(R.id.iv_loginGoogle)
    }
}