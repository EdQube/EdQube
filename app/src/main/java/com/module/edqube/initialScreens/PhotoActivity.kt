package com.module.edqube.initialScreens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.module.edqube.MainActivity
import com.module.edqube.R

class PhotoActivity : AppCompatActivity() {

    private lateinit var profileImage: ImageView
    private lateinit var coverImage: ImageView

    private val REQUEST_CAMERA_PROFILE = 1001
    private val REQUEST_GALLERY_PROFILE = 1002
    private val REQUEST_CAMERA_COVER = 1003
    private val REQUEST_GALLERY_COVER = 1004

    private var isProfile = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        val buttonNext = findViewById<Button>(R.id.buttonNext)

        buttonNext.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        profileImage = findViewById(R.id.profileImage)
        coverImage = findViewById(R.id.coverImage)

        val uploadProfileButton: Button = findViewById(R.id.uploadProfileButton)
        val uploadCoverButton: Button = findViewById(R.id.uploadCoverButton)

        uploadProfileButton.setOnClickListener {
            isProfile = true
            showImagePickerDialog()
        }

        uploadCoverButton.setOnClickListener {
            isProfile = false
            showImagePickerDialog()
        }
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Camera", "Gallery")
        AlertDialog.Builder(this)
            .setTitle("Select Image")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }
            .show()
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 101)
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val requestCode = if (isProfile) REQUEST_CAMERA_PROFILE else REQUEST_CAMERA_COVER
            startActivityForResult(intent, requestCode)
        }
    }

    private fun openGallery() {
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_MEDIA_IMAGES), 102)
        } else if (
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 102)
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            val requestCode = if (isProfile) REQUEST_GALLERY_PROFILE else REQUEST_GALLERY_COVER
            startActivityForResult(intent, requestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CAMERA_PROFILE, REQUEST_CAMERA_COVER -> {
                    val bitmap = data.extras?.get("data") as? Bitmap
                    bitmap?.let {
                        if (requestCode == REQUEST_CAMERA_PROFILE) {
                            profileImage.setImageBitmap(it)
                        } else {
                            coverImage.setImageBitmap(it)
                        }
                    }
                }

                REQUEST_GALLERY_PROFILE, REQUEST_GALLERY_COVER -> {
                    val imageUri: Uri? = data.data
                    if (requestCode == REQUEST_GALLERY_PROFILE) {
                        profileImage.setImageURI(imageUri)
                    } else {
                        coverImage.setImageURI(imageUri)
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                101 -> openCamera()
                102 -> openGallery()
            }
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
}
