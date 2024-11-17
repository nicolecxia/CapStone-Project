package com.example.cocygo.booking.calender.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cocygo.booking.calender.model.CalenderModel
import com.example.cocygo.R

class Adapter(private var data: List<CalenderModel>) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.booking_cell, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val calendarItem = data[position]
        holder.titleDate.text = calendarItem.date
        holder.titleTime.text = calendarItem.time
    }

    // Method to update the data
    fun updateData(newData: List<CalenderModel>) {
        data = newData
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }
}