package com.example.cocygo.Location.Location

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.cocygo.Location.Calander.view.CalanderFragment
import com.example.cocygo.R
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            // Show MapFragment initially
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MapFragment())
                .commit()
        }

        // Example: Set up a button listener to switch to CalendarFragment
        findViewById<Button>(R.id.buttonBook).setOnClickListener {
            findViewById<Button>(R.id.buttonBook).visibility = View.GONE
            // Replace the current fragment with CalendarFragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CalanderFragment())
                .addToBackStack(null) // Add transaction to back stack
                .commit()
        }

        FirebaseApp.initializeApp(this)
    }

}

