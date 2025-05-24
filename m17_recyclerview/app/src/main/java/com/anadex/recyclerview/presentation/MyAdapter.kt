package com.anadex.recyclerview.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anadex.recyclerview.R
import com.anadex.recyclerview.data.PhotoDTO
import com.anadex.recyclerview.databinding.PhotoViewBinding
import com.bumptech.glide.Glide

class MyAdapter(
    private val onClick: (PhotoDTO) -> Unit
) : PagingDataAdapter<PhotoDTO, MyViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = PhotoViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            photoItem = item
            itemPosition = (position + 1).toString()
            item?.let {
                Glide
                    .with(holder.itemView.context)
                    .load(it.imgSrc)
                    .error(R.drawable.ic_launcher_background)
                    .into(photo)
            }
        }
        holder.binding.root.setOnClickListener {
            item?.let { onClick(item) }
        }
    }
}

class MyViewHolder(val binding: PhotoViewBinding) : RecyclerView.ViewHolder(binding.root)

class DiffUtilCallback : DiffUtil.ItemCallback<PhotoDTO>() {
    override fun areItemsTheSame(oldItem: PhotoDTO, newItem: PhotoDTO): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: PhotoDTO, newItem: PhotoDTO): Boolean =
        oldItem == newItem
}
