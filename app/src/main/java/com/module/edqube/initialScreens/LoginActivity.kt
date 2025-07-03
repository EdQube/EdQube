package com.module.edqube.initialScreens

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.module.edqube.MainActivity
import com.module.edqube.R

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private val RC_SIGN_IN = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE)

        val emailEt = findViewById<EditText>(R.id.emailEditText)
        val passwordEt = findViewById<EditText>(R.id.passwordEditText)
        val rememberCB = findViewById<CheckBox>(R.id.rememberPasswordCheckBox)
        val loginBtn = findViewById<Button>(R.id.btnLogin)
        val googleBtn = findViewById<ConstraintLayout>(R.id.btnGoogle)
        val forgetTV = findViewById<TextView>(R.id.textView5)

        if (auth.currentUser != null) {
            checkAndRedirectUser(auth.currentUser!!.uid)
        }

        val savedEmail = sharedPreferences.getString("email", "")
        val savedPassword = sharedPreferences.getString("password", "")
        if (!savedEmail.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
            emailEt.setText(savedEmail)
            passwordEt.setText(savedPassword)
            rememberCB.isChecked = true
        }

        loginBtn.setOnClickListener {
            val email = emailEt.text.toString().trim()
            val password = passwordEt.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter Email and Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (rememberCB.isChecked) {
                sharedPreferences.edit().apply {
                    putString("email", email)
                    putString("password", password)
                    apply()
                }
            } else {
                sharedPreferences.edit().clear().apply()
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    checkAndRedirectUser(auth.currentUser!!.uid)
                }
                .addOnFailureListener {
                    // Create new account if login fails
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener { result ->
                            val uid = result.user?.uid ?: return@addOnSuccessListener
                            val user = hashMapOf(
                                "email" to email,
                                "name" to "",
                                "userType" to "student",
                                "stepProgress" to 1
                            )
                            FirebaseFirestore.getInstance().collection("users")
                                .document(uid)
                                .set(user)
                                .addOnSuccessListener {
                                    checkAndRedirectUser(uid)
                                }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Account creation failed: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
        }

        forgetTV.setOnClickListener {
            val email = emailEt.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Enter email to reset password", Toast.LENGTH_SHORT).show()
            } else {
                auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Reset link sent to your email", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        googleBtn.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val client = GoogleSignIn.getClient(this, gso)
            startActivityForResult(client.signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { result ->
                val isNew = result.additionalUserInfo?.isNewUser ?: false
                val uid = result.user?.uid ?: return@addOnSuccessListener
                if (isNew) {
                    val name = result.user?.displayName ?: ""
                    val email = result.user?.email ?: ""
                    val user = hashMapOf(
                        "email" to email,
                        "name" to name,
                        "userType" to "student",
                        "stepProgress" to 1
                    )
                    FirebaseFirestore.getInstance().collection("users")
                        .document(uid)
                        .set(user)
                        .addOnSuccessListener {
                            checkAndRedirectUser(uid)
                        }
                } else {
                    checkAndRedirectUser(uid)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Google Auth failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkAndRedirectUser(uid: String) {
        FirebaseFirestore.getInstance().collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                val step = doc.getLong("stepProgress")?.toInt() ?: 1
                when (step) {
                    1 -> startActivity(Intent(this, ProfileDetailsFirstActivity::class.java))
                    2 -> startActivity(Intent(this, ProfileDetailsSecondActivity::class.java))
                    3 -> startActivity(Intent(this, PhotoActivity::class.java))
                    else -> startActivity(Intent(this, MainActivity::class.java))
                }
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error loading user data", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ProfileDetailsFirstActivity::class.java))
                finish()
            }
    }
}
