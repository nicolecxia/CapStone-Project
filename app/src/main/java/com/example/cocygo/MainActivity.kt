package com.example.cocygo

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.cocygo.booking.calender.view.SelectedDateFragment
import com.example.cocygo.booking.location.LocationFragment
import com.example.cocygo.databinding.ActivityMainBinding
import com.example.cocygo.homeFragment.HomeFragment
import com.example.cocygo.intro.SlidePageMenuFragment
import com.example.cocygo.signIn.SignInViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val signInViewModel: SignInViewModel by viewModels()

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndRequestPermission(Manifest.permission.WRITE_CALENDAR)
        requestNotificationPermission()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //       check display mode
        val isDarkMode = (resources.configuration.uiMode
                and android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES
        if (isDarkMode) {


            binding.ConstraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_dark_background))
        } else {
            binding.ConstraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_light))
        }
        // Observe the signInFlag LiveData
        signInViewModel.signInFlag.observe(this) { success ->
            if (success) {
                setupBottomNavigation()
                loadFragment(HomeFragment()) // Load the home fragment after sign-in
            } else {
                // Handle failed sign-in if needed
            }
        }
        Handler(Looper.getMainLooper()).postDelayed({
            // Animation for imageWelcome (up to down)
            val imageAnimation = TranslateAnimation(
                0f,  // Start X position (no horizontal movement)
                0f,  // End X position
                -500f,  // Start Y position (above the screen)
                0f   // End Y position (original position)
            ).apply {
                duration = 2000 // 2 seconds
                fillAfter = true // Keep the view at the end position after animation
            }
            binding.imageWelcome.startAnimation(imageAnimation)

            imageAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationRepeat(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    // Animation for textWelcome (down to up)
                    val textAnimation = TranslateAnimation(
                        0f,  // Start X position (no horizontal movement)
                        0f,  // End X position
                        500f,  // Start Y position (below the screen)
                        0f   // End Y position (original position)
                    ).apply {
                        duration = 2000 // 2 seconds
                        fillAfter = true // Keep the view at the end position after animation
                    }
                    binding.textWelcome.startAnimation(textAnimation)

                    textAnimation.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation?) {
                        }

                        override fun onAnimationRepeat(animation: Animation?) {}

                        override fun onAnimationEnd(animation: Animation?) {
                            // Set visibility to GONE after both animations are completed
                            binding.textWelcome.visibility = View.GONE
                            binding.textWelcome.clearAnimation()

                            binding.imageWelcome.visibility = View.GONE
                            binding.imageWelcome.clearAnimation()

                            // Load the next fragment
                            loadFragment(SlidePageMenuFragment())
                        }
                    })
                }
            })
        }, 6000) // Delay for 5 seconds

    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.visibility = View.VISIBLE // Show the bottom navigation
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            handleNavigation(item)
            true
        }
        binding.bottomNavigation.itemIconTintList = null
    }

    private fun handleNavigation(item: MenuItem) {
        when (item.itemId) {
            R.id.nav_home -> {
                loadFragment(HomeFragment())
            }

            R.id.nav_appointments -> {
                loadFragment(SelectedDateFragment())
            }

            R.id.nav_Loc -> {
                loadFragment(LocationFragment())
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment) // Adjust to your container ID
        transaction.commit()
    }
    //Check the permission and get one
    private fun requestNotificationPermission() {
        // check if user need permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS

            // Check if granted
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // no granted
                requestPermissionLauncher.launch(permission)
            } else {
                // granted
                println("Notification permission already granted")
            }
        }
    }
    private fun checkAndRequestPermission(permission: String) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            println("Permission $permission not granted. Requesting now...")
            requestPermissionLauncher.launch(permission)
        } else {
            println("Permission $permission is already granted")
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            println("Permission granted")
        } else {
            println("Permission denied")
        }
    }
}