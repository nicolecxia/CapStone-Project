package com.example.cocygo.service.list

import android.media.Image
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cocygo.R
import com.example.cocygo.service.model.Service

class ServiceCellViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val imageName = view.findViewById<ImageView>(R.id.serviceImage)
    val serviceName = view.findViewById<TextView>(R.id.tvServiceName)
    val stylist = view.findViewById<TextView>(R.id.tvStylist)

    // Set the onClickListener to call the onItemClick function
    fun bind(service: Service,onItemClick: (Service) -> Unit){
        view.setOnClickListener{
            onItemClick(service)
        }
    }

}