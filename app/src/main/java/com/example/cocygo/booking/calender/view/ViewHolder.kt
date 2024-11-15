package com.example.cocygo.booking.calender.view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cocygo.R

class ViewHolder(val view: View):RecyclerView.ViewHolder(view) {
val titleDate = view.findViewById<TextView>(R.id.textViewDate)
    val titleTime = view.findViewById<TextView>(R.id.Time)
    val titleService = view.findViewById<TextView>(R.id.titleService)
}