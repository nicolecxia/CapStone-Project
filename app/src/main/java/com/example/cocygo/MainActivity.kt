package com.example.cocygo

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cocygo.databinding.ActivityMainBinding
import com.example.cocygo.homeFragment.HomeFragment
import com.example.cocygo.intro.SlidePageMenuFragment
import com.example.cocygo.user.UserProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up bottom navigation
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            handleNavigation(item)
            true
        }
        binding.bottomNavigation.itemIconTintList = null

        // Show the welcome message for 5 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            loadFragment(SlidePageMenuFragment())
            binding.textWelcome.visibility = View.GONE
            binding.imageWelcome.visibility = View.GONE
        }, 5000) // 5000 milliseconds = 5 seconds
    }

    private fun handleNavigation(item: MenuItem) {
        when (item.itemId) {
            R.id.nav_home -> {
                loadFragment(HomeFragment())
            }
            R.id.nav_user -> {
                loadFragment(UserProfileFragment())
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment) // Adjust to your container ID
        transaction.commit()
    }
}