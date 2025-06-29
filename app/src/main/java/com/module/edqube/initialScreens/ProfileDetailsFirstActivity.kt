package com.module.edqube.initialScreens

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.module.edqube.R

class ProfileDetailsFirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_details_first)

        val buttonNext = findViewById<Button>(R.id.buttonNext)

        buttonNext.setOnClickListener {
            startActivity(Intent(this, ProfileDetailsSecondActivity::class.java))
            finish()
        }


        val spinnerQualification: Spinner = findViewById(R.id.spinnerQualification)

        val qualifications = resources.getStringArray(R.array.qualifications_array)

        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_item_qualification, // Custom item layout
            qualifications
        )

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_qualification)
        spinnerQualification.adapter = adapter

    }
}