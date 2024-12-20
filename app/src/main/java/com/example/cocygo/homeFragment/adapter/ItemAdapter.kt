package com.example.cocygo.homeFragment.adapter


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cocygo.R
import android.util.Base64
import android.widget.ImageButton
import android.widget.ImageView

class ItemAdapter(
    private val items: List<Item>,
    private val onItemClick: (Item) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    private lateinit var context: android.content.Context // Define context variable

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView = view.findViewById(R.id.item_image)
        val itemTitle: TextView = view.findViewById(R.id.item_title)
        val itemName: TextView = view.findViewById(R.id.item_description)
        val btnCall: ImageButton = view.findViewById(R.id.btnCall)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        context = parent.context // Assign context from parent ViewGroup
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        val bitmap = decodeBase64ToBitmap(item.image.trim())

        holder.itemImage.setImageBitmap(bitmap)
        holder.itemTitle.text = item.tittle
        holder.itemName.text = item.name
        holder.itemView.setOnClickListener { onItemClick(item) }

        holder.btnCall.setOnClickListener {
            makePhoneCall("5488811368")
        }
    }

    override fun getItemCount() = items.size

    private fun decodeBase64ToBitmap(base64: String): Bitmap? {
        val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    private fun makePhoneCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        context.startActivity(intent)
    }
}