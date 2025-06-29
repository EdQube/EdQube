package com.module.edqube.initialScreens

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*
import com.module.edqube.R
import com.module.edqube.MainActivity



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

        // Auto-login if user already logged in
        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Restore saved credentials
        val savedEmail = sharedPreferences.getString("email", "")
        val savedPassword = sharedPreferences.getString("password", "")
        if (!savedEmail.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
            emailEt.setText(savedEmail)
            passwordEt.setText(savedPassword)
            rememberCB.isChecked = true
        }

        loginBtn.setOnClickListener {
            startActivity(Intent(this, ProfileDetailsFirstActivity::class.java))
            finish()

            /*val email = emailEt.text.toString().trim()
            val password = passwordEt.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter Email and Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save password if checkbox checked
            if (rememberCB.isChecked) {
                sharedPreferences.edit().apply {
                    putString("email", email)
                    putString("password", password)
                    apply()
                }
            } else {
                sharedPreferences.edit().clear().apply()
            }

            // Login with Firebase
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    // If login fails, create account
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            // Send to profile setup screen if new user
                            Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show()
                            // startActivity(Intent(this, ProfileSetupActivity::class.java))
                            startActivity(Intent(this, MainActivity::class.java)) // temp
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }*/
        }

       /* // Forgot password
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

        // Google Sign-In
        googleBtn.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val client = GoogleSignIn.getClient(this, gso)
            startActivityForResult(client.signInIntent, RC_SIGN_IN)
        }*/
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
                if (isNew) {
                    // Go to profile setup
                    Toast.makeText(this, "Welcome new user!", Toast.LENGTH_SHORT).show()
                    // startActivity(Intent(this, ProfileSetupActivity::class.java))
                } else {
                    Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show()
                }
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Google Auth failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }*/
}
