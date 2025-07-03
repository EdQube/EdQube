package com.module.edqube.initialScreens

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.module.edqube.R

class ProfileDetailsFirstActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_details_first)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val fullNameET = findViewById<EditText>(R.id.editTextFullName)
        val phoneET = findViewById<EditText>(R.id.editTextPhone)
        val ageET = findViewById<EditText>(R.id.editTextAge)
        val pinCodeET = findViewById<EditText>(R.id.editTextPinCode)
        val addressET = findViewById<EditText>(R.id.editTextAddress)
        val qualificationSpinner = findViewById<Spinner>(R.id.spinnerQualification)
        val buttonNext = findViewById<Button>(R.id.buttonNext)
        val buttonBack = findViewById<Button>(R.id.buttonBack)

        // Set Spinner data
        val qualifications = resources.getStringArray(R.array.qualifications_array)
        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_item_qualification,
            qualifications
        )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_qualification)
        qualificationSpinner.adapter = adapter

        buttonBack.setOnClickListener {
            finish() // Go back to login or previous screen
        }

        buttonNext.setOnClickListener {
            val fullName = fullNameET.text.toString().trim()
            val phone = phoneET.text.toString().trim()
            val age = ageET.text.toString().trim()
            val pinCode = pinCodeET.text.toString().trim()
            val address = addressET.text.toString().trim()
            val qualification = qualificationSpinner.selectedItem.toString()

            if (fullName.isEmpty() || phone.isEmpty() || age.isEmpty() || pinCode.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = auth.currentUser?.uid
            if (userId == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userMap = mapOf(
                "name" to fullName,
                "phone" to phone,
                "age" to age,
                "pinCode" to pinCode,
                "address" to address,
                "qualification" to qualification,
                "stepProgress" to 2
            )

            db.collection("users").document(userId)
                .update(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, ProfileDetailsSecondActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
