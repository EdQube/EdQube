package com.module.edqube.initialScreens

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.module.edqube.R

class ProfileDetailsSecondActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_details_second)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val editTextBio = findViewById<EditText>(R.id.editTextBio)
        val chipGroup = findViewById<ChipGroup>(R.id.topicChips)
        val buttonNext = findViewById<Button>(R.id.buttonNext)
        val buttonBack = findViewById<Button>(R.id.buttonBack)

        buttonBack.setOnClickListener {
            finish()
        }

        buttonNext.setOnClickListener {
            val bio = editTextBio.text.toString().trim()

            val selectedTopics = mutableListOf<String>()
            for (i in 0 until chipGroup.childCount) {
                val chip = chipGroup.getChildAt(i)
                if (chip is Chip && chip.isChecked) {
                    selectedTopics.add(chip.text.toString())
                }
            }

            val userId = auth.currentUser?.uid
            if (userId == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val profileStep2Data = mapOf(
                "bio" to bio,
                "topics" to selectedTopics,
                "stepProgress" to 3
            )

            db.collection("users").document(userId)
                .update(profileStep2Data)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, PhotoActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
