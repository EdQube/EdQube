package com.module.edqube.initialScreens

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.module.edqube.MainActivity
import com.module.edqube.R

class PhotoActivity : AppCompatActivity() {

    private lateinit var profileImage: ImageView
    private lateinit var coverImage: ImageView
    private lateinit var progressProfile: ProgressBar
    private lateinit var progressCover: ProgressBar
    private lateinit var buttonNext: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var firestore: FirebaseFirestore

    private var profileUri: Uri? = null
    private var coverUri: Uri? = null

    private val REQUEST_CAMERA_PROFILE = 1001
    private val REQUEST_GALLERY_PROFILE = 1002
    private val REQUEST_CAMERA_COVER = 1003
    private val REQUEST_GALLERY_COVER = 1004

    private var isProfile = true
    private lateinit var loadingDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        firestore = FirebaseFirestore.getInstance()

        profileImage = findViewById(R.id.profileImage)
        coverImage = findViewById(R.id.coverImage)
        progressProfile = findViewById(R.id.progressProfile)
        progressCover = findViewById(R.id.progressCover)
        buttonNext = findViewById(R.id.buttonNext)

        val uploadProfileButton: Button = findViewById(R.id.uploadProfileButton)
        val uploadCoverButton: Button = findViewById(R.id.uploadCoverButton)

        loadingDialog = ProgressDialog(this)
        loadingDialog.setMessage("Saving your profile...")
        loadingDialog.setCancelable(false)

        uploadProfileButton.setOnClickListener {
            isProfile = true
            showImagePickerDialog()
        }

        uploadCoverButton.setOnClickListener {
            isProfile = false
            showImagePickerDialog()
        }

        buttonNext.setOnClickListener {
            val userId = auth.currentUser?.uid
            if (userId == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (profileUri == null || coverUri == null) {
                Toast.makeText(this, "Please select both profile and cover pictures", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            buttonNext.isEnabled = false
            loadingDialog.show()

            uploadImage(profileUri!!, "profile_pictures/$userId.jpg", isProfile = true) { profileUrl ->
                uploadImage(coverUri!!, "cover_pictures/$userId.jpg", isProfile = false) { coverUrl ->
                    val data = mapOf(
                        "profilePictureUrl" to profileUrl,
                        "coverPictureUrl" to coverUrl,
                        "stepProgress" to 4
                    )

                    firestore.collection("users").document(userId)
                        .update(data)
                        .addOnSuccessListener {
                            loadingDialog.dismiss()
                            Toast.makeText(this, "Profile completed successfully!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener {
                            loadingDialog.dismiss()
                            buttonNext.isEnabled = true
                            Toast.makeText(this, "Failed to save data: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_MEDIA_IMAGES), 102)
                return
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 102)
                return
            }
        }

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val requestCode = if (isProfile) REQUEST_GALLERY_PROFILE else REQUEST_GALLERY_COVER
        startActivityForResult(intent, requestCode)
    }

    private fun getImageUriFromBitmap(context: Context, bitmap: Bitmap?): Uri? {
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "IMG_${System.currentTimeMillis()}", null)
        return Uri.parse(path)
    }

    private fun uploadImage(fileUri: Uri, path: String, isProfile: Boolean, callback: (String) -> Unit) {
        val ref = storage.reference.child(path)
        val progressBar = if (isProfile) progressProfile else progressCover

        progressBar.visibility = ProgressBar.VISIBLE
        progressBar.progress = 0

        val uploadTask = ref.putFile(fileUri)
        uploadTask
            .addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
                progressBar.progress = progress
            }
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { uri ->
                    progressBar.visibility = ProgressBar.GONE
                    callback(uri.toString())
                }
            }
            .addOnFailureListener {
                progressBar.visibility = ProgressBar.GONE
                buttonNext.isEnabled = true
                loadingDialog.dismiss()
                Toast.makeText(this, "Upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CAMERA_PROFILE, REQUEST_CAMERA_COVER -> {
                    val bitmap = data.extras?.get("data") as? Bitmap
                    val uri = getImageUriFromBitmap(this, bitmap)
                    if (uri != null) {
                        if (requestCode == REQUEST_CAMERA_PROFILE) {
                            profileImage.setImageBitmap(bitmap)
                            profileUri = uri
                        } else {
                            coverImage.setImageBitmap(bitmap)
                            coverUri = uri
                        }
                    }
                }

                REQUEST_GALLERY_PROFILE, REQUEST_GALLERY_COVER -> {
                    val imageUri: Uri? = data.data
                    if (imageUri != null) {
                        if (requestCode == REQUEST_GALLERY_PROFILE) {
                            profileImage.setImageURI(imageUri)
                            profileUri = imageUri
                        } else {
                            coverImage.setImageURI(imageUri)
                            coverUri = imageUri
                        }
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
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
