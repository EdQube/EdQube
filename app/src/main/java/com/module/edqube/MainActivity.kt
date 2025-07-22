package com.module.edqube

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.module.edqube.databinding.ActivityMainBinding
import com.module.edqube.fragments.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var activeFragment: androidx.fragment.app.Fragment? = null

    private lateinit var home: HomeFragment
    private lateinit var search: SearchFragment
    private lateinit var post: CreatePostFragment
    private lateinit var jobs: JobsFragment
    private lateinit var profile: ProfileFragment

    // 🔐 Store the action to run after permission is granted
    private var pendingPermissionAction: (() -> Unit)? = null

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        if (allGranted) {
            pendingPermissionAction?.invoke()
        } else {
            Toast.makeText(this, "Permissions are required to continue", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, true)

        // 🧠 Detect keyboard to hide/show bottom nav
        val rootView = binding.root
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            val keyboardOpen = keypadHeight > screenHeight * 0.15

            binding.bottomNavigation.animate()
                .translationY(if (keyboardOpen) binding.bottomNavigation.height.toFloat() else 0f)
                .setDuration(150)
                .start()
        }

        // ⚡ Preload all fragments for smooth switching
        home = HomeFragment()
        search = SearchFragment()
        post = CreatePostFragment()
        jobs = JobsFragment()
        profile = ProfileFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, profile, "profile").hide(profile)
            .add(R.id.fragment_container, jobs, "jobs").hide(jobs)
            .add(R.id.fragment_container, post, "post").hide(post)
            .add(R.id.fragment_container, search, "search").hide(search)
            .add(R.id.fragment_container, home, "home") // show by default
            .commit()

        activeFragment = home

        // 🧭 Navigation
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val next = when (item.itemId) {
                R.id.menu_search -> search
                R.id.menu_post -> post
                R.id.menu_jobs -> jobs
                R.id.menu_profile -> profile
                else -> home
            }
            if (next != activeFragment) {
                supportFragmentManager.beginTransaction()
                    .hide(activeFragment!!)
                    .show(next)
                    .commit()
                activeFragment = next
            }
            true
        }
    }

    // 🏠 Allow fragment (like CreatePostFragment) to switch back to Home
    fun switchToHome() {
        val homeFragment = supportFragmentManager.findFragmentByTag("home")
        val currentFragment = activeFragment
        if (homeFragment != null && currentFragment != homeFragment) {
            supportFragmentManager.beginTransaction()
                .hide(currentFragment!!)
                .show(homeFragment)
                .commit()
            binding.bottomNavigation.selectedItemId = R.id.nav_home
            activeFragment = homeFragment
        }
    }

    // 👈 Handle back press to switch to Home
    override fun onBackPressed() {
        val homeFragment = supportFragmentManager.findFragmentByTag("home")
        if (activeFragment != homeFragment && homeFragment != null) {
            supportFragmentManager.beginTransaction()
                .hide(activeFragment!!)
                .show(homeFragment)
                .commit()
            binding.bottomNavigation.selectedItemId = R.id.nav_home
            activeFragment = homeFragment
        } else {
            super.onBackPressed()
        }
    }

    // ✅ Call this from fragments to check permission
    fun checkAndRequestCameraGalleryPermissions(onGranted: () -> Unit) {
        val requiredPermissions = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requiredPermissions.add(Manifest.permission.CAMERA)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requiredPermissions.add(Manifest.permission.READ_MEDIA_IMAGES)
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requiredPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        if (requiredPermissions.isNotEmpty()) {
            pendingPermissionAction = onGranted
            permissionLauncher.launch(requiredPermissions.toTypedArray())
        } else {
            onGranted()
        }
    }
}
