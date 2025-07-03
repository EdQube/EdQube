package com.module.edqube.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.module.edqube.R
import com.module.edqube.adapters.DummyImageAdapter

class CreatePostFragment : Fragment() {

    private lateinit var btnClose: ImageButton
    private lateinit var btnPost: Button
    private lateinit var edtPostText: EditText
    private lateinit var btnAttachImage: ImageView
    private lateinit var btnStartPoll: ImageView
    private lateinit var imagePreviewList: RecyclerView
    private lateinit var pollOptionsContainer: LinearLayout
    private lateinit var btnAddPollOption: Button

    private val selectedImages = mutableListOf<String>() // use URIs in real case
    private lateinit var imageAdapter: DummyImageAdapter
    private val pollOptions = mutableListOf<EditText>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
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

        imageAdapter = DummyImageAdapter(selectedImages)
        imagePreviewList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        imagePreviewList.adapter = imageAdapter
    }

    private fun setupListeners() {
        btnClose.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        edtPostText.addTextChangedListener(postTextWatcher)

        btnAttachImage.setOnClickListener {
            // Simulate adding an image
            selectedImages.add("Image ${selectedImages.size + 1}")
            imageAdapter.notifyItemInserted(selectedImages.size - 1)
            imagePreviewList.visibility = View.VISIBLE
            updatePostButtonState()
        }

        btnStartPoll.setOnClickListener {
            pollOptionsContainer.visibility = View.VISIBLE
            btnAddPollOption.visibility = View.VISIBLE
            if (pollOptions.isEmpty()) {
                addPollOption()
                addPollOption()
            }
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
            Toast.makeText(requireContext(), "Post submitted!", Toast.LENGTH_SHORT).show()
            // Send data to backend here
        }
    }

    private fun addPollOption() {
        val optionEditText = EditText(requireContext()).apply {
            hint = "Option ${pollOptions.size + 1}"
            setPadding(24, 16, 24, 16)
            background = requireContext().getDrawable(R.drawable.edittext_bg)
        }

        pollOptions.add(optionEditText)
        pollOptionsContainer.addView(optionEditText)
        optionEditText.addTextChangedListener(postTextWatcher)
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
}
