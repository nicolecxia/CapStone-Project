package com.example.cocygo

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cocygo.booking.calender.view.CalenderFragment
import com.example.cocygo.booking.calender.view.SelectedDateFragment
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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observe the signInFlag LiveData
        signInViewModel.signInFlag.observe(this) { success ->
            if (success) {
                setupBottomNavigation()
                loadFragment(HomeFragment()) // Load the home fragment after sign-in
            } else {
                // Handle failed sign-in if needed
            }
        }

        // Show the welcome message for 5 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            loadFragment(SlidePageMenuFragment())
            binding.textWelcome.visibility = View.GONE
            binding.imageWelcome.visibility = View.GONE
        }, 5000) // 5000 milliseconds = 5 seconds
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
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment) // Adjust to your container ID
        transaction.commit()
    }
}