package com.module.edqube.fragments

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.module.edqube.MainActivity
import com.module.edqube.R
import com.module.edqube.adapters.ImagePreviewAdapter
import android.app.ProgressDialog
import android.widget.Toast
import com.bumptech.glide.Glide


class CreatePostFragment : Fragment() {

    private lateinit var btnClose: ImageButton
    private lateinit var btnPost: Button
    private lateinit var edtPostText: EditText
    private lateinit var btnAttachImage: ImageView
    private lateinit var btnStartPoll: ImageView
    private lateinit var userImage: ImageView
    private lateinit var userName: TextView
    private lateinit var imagePreviewList: RecyclerView
    private lateinit var pollOptionsContainer: LinearLayout
    private lateinit var btnAddPollOption: LinearLayout

    private val selectedImages = mutableListOf<Uri>()
    private lateinit var imageAdapter: ImagePreviewAdapter
    private val pollOptions = mutableListOf<EditText>()

    private var cameraImageUri: Uri? = null

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && cameraImageUri != null) {
            clearPollOptions()
            selectedImages.clear()
            selectedImages.add(cameraImageUri!!)
            imageAdapter.notifyDataSetChanged()
            imagePreviewList.visibility = View.VISIBLE
            updatePostButtonState()
        }
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        if (!uris.isNullOrEmpty()) {
            clearPollOptions()
            selectedImages.clear()
            selectedImages.addAll(uris)
            imageAdapter.notifyDataSetChanged()
            imagePreviewList.visibility = View.VISIBLE
            updatePostButtonState()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create_post, container, false)
        initViews(view)
        setupListeners()
        return view
    }

    private fun initViews(view: View) {
        btnClose = view.findViewById(R.id.btnClose)
        btnPost = view.findViewById(R.id.btnPost)
        edtPostText = view.findViewById(R.id.edtPostText)
        btnAttachImage = view.findViewById(R.id.btnAttachImage)
        btnStartPoll = view.findViewById(R.id.btnStartPoll)
        imagePreviewList = view.findViewById(R.id.imagePreviewList)
        pollOptionsContainer = view.findViewById(R.id.pollOptionsContainer)
        btnAddPollOption = view.findViewById(R.id.btnAddPollOption)
        userImage = view.findViewById(R.id.imgAvatar)
        userName = view.findViewById(R.id.createPostUsername)
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        firestore.collection("users").document(userId)
            .addSnapshotListener { userDoc, error ->
                if (error != null) {
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (userDoc != null && userDoc.exists()) {
                    val name = userDoc.getString("name")?.takeIf { it.isNotBlank() } ?: "Unknown"
                    val avatarUrl = userDoc.getString("profilePictureUrl")?.takeIf { it.isNotBlank() } ?: ""

                    Glide.with(requireActivity())
                        .load(avatarUrl)
                        .placeholder(R.drawable.profile)
                        .into(userImage)
                    userName.text = name.toString()

                    // Update your UI with userName and avatarUrl
                }
            }


        imageAdapter = ImagePreviewAdapter(selectedImages) { uri ->
            selectedImages.remove(uri)
            imageAdapter.notifyDataSetChanged()
            if (selectedImages.isEmpty()) imagePreviewList.visibility = View.GONE
            updatePostButtonState()
        }

        imagePreviewList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        imagePreviewList.adapter = imageAdapter
    }

    private fun setupListeners() {
        btnClose.setOnClickListener {
            (activity as? MainActivity)?.switchToHome()
        }

        edtPostText.addTextChangedListener(postTextWatcher)

        btnAttachImage.setOnClickListener {
            (activity as? MainActivity)?.checkAndRequestCameraGalleryPermissions {
                showImageSourceDialog()
            }
        }

        btnStartPoll.setOnClickListener {
            if (selectedImages.isNotEmpty()) {
                selectedImages.clear()
                imageAdapter.notifyDataSetChanged()
                imagePreviewList.visibility = View.GONE
            }

            clearPollOptions()
            pollOptionsContainer.visibility = View.VISIBLE
            btnAddPollOption.visibility = View.VISIBLE
            addPollOption()
            addPollOption()
            updatePostButtonState()
        }

        btnAddPollOption.setOnClickListener {
            if (pollOptions.size < 4) {
                addPollOption()
            } else {
                Toast.makeText(requireContext(), "Max 4 options allowed", Toast.LENGTH_SHORT).show()
            }
        }

        btnPost.setOnClickListener {
            btnPost.isEnabled = false // ⛔️ Disable button to prevent multiple taps

            val postText = edtPostText.text.toString().trim()
            val pollTextList = pollOptions.map { it.text.toString().trim() }.filter { it.isNotEmpty() }
            val userId = auth.currentUser?.uid ?: run {
                Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
                btnPost.isEnabled = true
                return@setOnClickListener
            }
            val timestamp = System.currentTimeMillis()

            when {
                selectedImages.isNotEmpty() -> {
                    uploadImagesToFirebaseStorage(userId, selectedImages) { imageUrls ->
                        val data = hashMapOf(
                            "type" to "image",
                            "userId" to userId,
                            "content" to postText,
                            "imageUrls" to imageUrls,
                            "timestamp" to timestamp
                        )
                        firestore.collection("posts").add(data).addOnSuccessListener {
                            Toast.makeText(requireContext(), "Posted with images!", Toast.LENGTH_SHORT).show()
                            resetPostForm()
                            btnPost.isEnabled = true // ✅ Re-enable after success
                        }.addOnFailureListener {
                            Toast.makeText(requireContext(), "Failed to post!", Toast.LENGTH_SHORT).show()
                            btnPost.isEnabled = true
                        }
                    }
                }

                pollOptionsContainer.isVisible && pollTextList.isNotEmpty() -> {
                    val data = hashMapOf(
                        "type" to "poll",
                        "userId" to userId,
                        "question" to postText,
                        "options" to pollTextList,
                        "voteCounts" to MutableList(pollTextList.size) { 0 },
                        "timestamp" to timestamp
                    )
                    firestore.collection("posts").add(data).addOnSuccessListener {
                        Toast.makeText(requireContext(), "Posted with poll!", Toast.LENGTH_SHORT).show()
                        resetPostForm()
                        btnPost.isEnabled = true
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed to post!", Toast.LENGTH_SHORT).show()
                        btnPost.isEnabled = true
                    }
                }

                postText.isNotEmpty() -> {
                    val data = hashMapOf(
                        "type" to "text",
                        "userId" to userId,
                        "content" to postText,
                        "timestamp" to timestamp
                    )
                    firestore.collection("posts").add(data).addOnSuccessListener {
                        Toast.makeText(requireContext(), "Text post submitted!", Toast.LENGTH_SHORT).show()
                        resetPostForm()
                        btnPost.isEnabled = true
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed to post!", Toast.LENGTH_SHORT).show()
                        btnPost.isEnabled = true
                    }
                }

                else -> {
                    Toast.makeText(requireContext(), "Please enter something!", Toast.LENGTH_SHORT).show()
                    btnPost.isEnabled = true
                }
            }
        }

    }

    private fun showImageSourceDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Choose an option")
            .setItems(arrayOf("Camera", "Upload from Gallery")) { _, which ->
                when (which) {
                    0 -> {
                        cameraImageUri = createImageUri()
                        cameraImageUri?.let { cameraLauncher.launch(it) }
                    }
                    1 -> galleryLauncher.launch("image/*")
                }
            }
            .show()
    }

    private fun uploadImagesToFirebaseStorage(
        userId: String,
        uris: List<Uri>,
        onComplete: (List<String>) -> Unit
    ) {
        val imageUrls = mutableListOf<String>()
        val totalImages = uris.size

        val progressDialog = ProgressDialog(requireContext()).apply {
            setTitle("Uploading...")
            setCancelable(false)
            setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            max = 100
            show()
        }

        var currentIndex = 0

        fun uploadNext() {
            if (currentIndex >= uris.size) {
                progressDialog.dismiss()
                onComplete(imageUrls)
                return
            }

            val uri = uris[currentIndex]
            val ref = storage.reference.child("posts/$userId/${System.currentTimeMillis()}_$currentIndex.jpg")

            val uploadTask = ref.putFile(uri)
            uploadTask
                .addOnProgressListener { taskSnapshot ->
                    val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
                    progressDialog.progress = progress
                    progressDialog.setMessage("Uploading image ${currentIndex + 1} of $totalImages")
                }
                .continueWithTask { task ->
                    if (!task.isSuccessful) throw task.exception ?: Exception("Upload failed")
                    ref.downloadUrl
                }
                .addOnSuccessListener { url ->
                    imageUrls.add(url.toString())
                    currentIndex++
                    uploadNext() // Upload next image
                }
                .addOnFailureListener { error ->
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), "Upload failed: ${error.message}", Toast.LENGTH_LONG).show()
                }
        }

        uploadNext()
    }


    private fun resetPostForm() {
        edtPostText.text.clear()
        selectedImages.clear()
        imageAdapter.notifyDataSetChanged()
        imagePreviewList.visibility = View.GONE
        clearPollOptions()
        updatePostButtonState()
    }

    private fun addPollOption() {
        val optionLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 0, 20) }
        }

        val optionEditText = EditText(requireContext()).apply {
            hint = "Option ${pollOptions.size + 1}"
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            setPadding(24, 16, 24, 16)
            background = requireContext().getDrawable(R.drawable.edittext_bg)
        }

        val removeButton = ImageView(requireContext()).apply {
            setImageResource(R.drawable.close)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(16, 0, 0, 0) }
            setOnClickListener {
                pollOptions.remove(optionEditText)
                pollOptionsContainer.removeView(optionLayout)
                updatePostButtonState()
            }
        }

        if (pollOptions.size < 2) removeButton.visibility = View.GONE

        optionEditText.addTextChangedListener(postTextWatcher)
        pollOptions.add(optionEditText)

        optionLayout.addView(optionEditText)
        optionLayout.addView(removeButton)
        pollOptionsContainer.addView(optionLayout)
    }

    private fun clearPollOptions() {
        pollOptions.clear()
        pollOptionsContainer.removeAllViews()
        pollOptionsContainer.visibility = View.GONE
        btnAddPollOption.visibility = View.GONE
    }

    private val postTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = updatePostButtonState()
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun updatePostButtonState() {
        val hasText = edtPostText.text.toString().trim().isNotEmpty()
        val hasImages = selectedImages.isNotEmpty()
        val hasPoll = pollOptionsContainer.isVisible && pollOptions.any {
            it.text.toString().trim().isNotEmpty()
        }

        btnPost.isEnabled = hasText || hasImages || hasPoll
    }

    private fun createImageUri(): Uri? {
        val resolver = requireContext().contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "temp_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }
}
