package com.module.edqube.initialScreens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.module.edqube.R

class ProfileDetailsSecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_details_second)

        val buttonNext = findViewById<Button>(R.id.buttonNext)


        buttonNext.setOnClickListener {
            startActivity(Intent(this, PhotoActivity::class.java))
            finish()
        }

    }
}