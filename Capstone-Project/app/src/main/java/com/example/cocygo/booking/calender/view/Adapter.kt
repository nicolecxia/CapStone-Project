package com.example.cocygo.booking.calender.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.cocygo.booking.calender.model.CalenderModel
import com.example.cocygo.R

class Adapter(private var data: List<CalenderModel>) : RecyclerView.Adapter<ViewHolder>() {
    private var onDeleteClickListener: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.booking_cell, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setOnDeleteClickListener(listener: (String) -> Unit) {
        onDeleteClickListener = listener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val calendarItem = data[position]
        holder.titleDate.text = calendarItem.date
        holder.titleTime.text = calendarItem.time

        holder.btnCancel.setOnClickListener {
            // Use holder.itemView.context to get the context for the dialog
            showDeleteConfirmationDialog(holder.itemView.context, calendarItem.id)
        }
    }

    // Use holder.itemView.context to get the correct context for the dialog
    private fun showDeleteConfirmationDialog(context: android.content.Context, id: String) {
        // Create an AlertDialog to confirm the deletion
        val dialog = AlertDialog.Builder(context)
            .setTitle("Delete Booking")
            .setMessage("Are you sure you want to delete this booking?")
            .setPositiveButton("Yes") { _, _ ->
                Log.d("Adapter", "Delete confirmed for id: $id")
                onDeleteClickListener?.invoke(id) // Trigger delete with id
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss() // Dismiss the dialog if "No" is clicked
            }
            .create()

        dialog.show() // Show the dialog
    }

    // Method to update the data
    fun updateData(newData: List<CalenderModel>) {
        data = newData
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }
}
