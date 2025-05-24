package com.example.photoapp
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.photoapp.databinding.PhotoItemBinding

class PhotoAdapter(private val values: List<Photo>): RecyclerView.Adapter<PhotoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = PhotoItemBinding.inflate(LayoutInflater.from(parent.context))
        return PhotoViewHolder(binding)
    }

    override fun getItemCount(): Int = values.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        with(holder.binding){
            photoDate.text = values[position].date
            values[position].let {
                Glide.with(photoItem.context).load(it.src).into(photoItem)
            }
        }

    }
}

class PhotoViewHolder(val binding: PhotoItemBinding) : RecyclerView.ViewHolder(binding.root)
