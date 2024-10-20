package com.example.cocygo.service.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.cocygo.R
import com.example.cocygo.service.model.Service
import java.io.ByteArrayInputStream
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import com.example.cocygo.service.model.Base64ToBitmap

class ServiceAdapter(val data: ArrayList<Service>, private val onItemClick: (Service) -> Unit) :
    RecyclerView.Adapter<ServiceCellViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceCellViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.service_cell, parent, false)
        return ServiceCellViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ServiceCellViewHolder, position: Int) {
        holder.serviceName.text = data[position].serviceName.toString()
        holder.stylist.text = data[position].stylist.toString()
        holder.duration.text = "${data[position].duration.toString()} mins"

        val base64Image = data[position].image.toString()
        // Decode the Base64 string to a Bitmap
        val bitmap = Base64ToBitmap.decodeBase64ToBitmap(base64Image)

        holder.bind(data[position], onItemClick)

        if (bitmap != null) {
            holder.showImage(bitmap)
        }
    }

}