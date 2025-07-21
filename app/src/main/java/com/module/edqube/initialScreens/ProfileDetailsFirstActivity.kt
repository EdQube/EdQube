package com.module.edqube.initialScreens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.module.edqube.R
import java.util.*

class ProfileDetailsFirstActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var latitude: Double? = null
    private var longitude: Double? = null

    private lateinit var pinCodeET: EditText
    private lateinit var addressET: EditText
    private lateinit var detectedAddressTV: TextView

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_details_first)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val fullNameET = findViewById<EditText>(R.id.editTextFullName)
        val phoneET = findViewById<EditText>(R.id.editTextPhone)
        val ageET = findViewById<EditText>(R.id.editTextAge)
        pinCodeET = findViewById(R.id.editTextPinCode)
        addressET = findViewById(R.id.editTextAddress)
        val qualificationSpinner = findViewById<Spinner>(R.id.spinnerQualification)
        val buttonNext = findViewById<Button>(R.id.buttonNext)
        val buttonBack = findViewById<Button>(R.id.buttonBack)
        val detectLocationButton = findViewById<Button>(R.id.buttonDetectLocation)
        detectedAddressTV = findViewById(R.id.textViewDetectedAddress)

        // Set Spinner
        val qualifications = resources.getStringArray(R.array.qualifications_array)
        val adapter = ArrayAdapter(this, R.layout.spinner_item_qualification, qualifications)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_qualification)
        qualificationSpinner.adapter = adapter

        buttonBack.setOnClickListener {
            finish()
        }

        detectLocationButton.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            } else {
                detectLocation()
            }
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

            val userMap = mutableMapOf(
                "name" to fullName,
                "phone" to phone,
                "age" to age,
                "pinCode" to pinCode,
                "address" to address,
                "qualification" to qualification,
                "stepProgress" to 2
            )

            latitude?.let { userMap["latitude"] = it }
            longitude?.let { userMap["longitude"] = it }

            db.collection("users").document(userId)
                .update(userMap as Map<String, Any>)
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

    private fun detectLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude

                val geocoder = Geocoder(this, Locale.getDefault())
                try {
                    val addressList = geocoder.getFromLocation(latitude!!, longitude!!, 1)
                    if (!addressList.isNullOrEmpty()) {
                        val addressObj = addressList[0]
                        val fullAddress = addressObj.getAddressLine(0)
                        val postalCode = addressObj.postalCode

                        addressET.setText(fullAddress)
                        pinCodeET.setText(postalCode)
                        detectedAddressTV.text = fullAddress
                        Toast.makeText(this, "Location detected", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Could not get address", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Location is null", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to get location: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            detectLocation()
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }
}
