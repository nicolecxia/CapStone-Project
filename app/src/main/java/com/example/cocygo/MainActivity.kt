package com.example.cocygo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.example.cocygo.intro.SlidePageMenuFragment

class MainActivity : AppCompatActivity() {
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        //Show the Hi data name
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.animator.fade_in)
//
//        // Show the Hi data name
        val introTV = findViewById<TextView>(R.id.text_welcome)
        val introIV = findViewById<ImageView>(R.id.image_welcome)
//      //  name.text = "Hi $dataName,"
//        introTV.startAnimation(fadeInAnimation)
        // Delay for 5 seconds before navigating to SlidePageMenu
//        Handler(Looper.getMainLooper()).postDelayed({
//            val intent = Intent(this, SlidePageMenu::class.java)
//            startActivity(intent)
//            finish()
//        }, 5000) // 5000 milliseconds = 5 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    SlidePageMenuFragment()
                ) // Use the correct ID of your fragment container
                .addToBackStack(null) // Optional: Add to back stack if you want to navigate back
                .commit()
        }, 5000) // 5000 milliseconds = 5 seconds

        Handler(Looper.getMainLooper()).postDelayed({
            introTV.visibility = View.GONE
            introIV.visibility = View.GONE
        }, 5000) // 5000 milliseconds = 5 seconds
    }
}