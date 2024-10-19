package com.example.cocygo.service.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.cocygo.R
import com.example.cocygo.service.model.Service

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

        holder.bind(data[position], onItemClick)
    }

}