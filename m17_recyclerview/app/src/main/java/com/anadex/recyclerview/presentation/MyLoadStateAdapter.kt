package com.anadex.recyclerview.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anadex.recyclerview.databinding.LoadStateBinding

class MyLoadStateAdapter : LoadStateAdapter<LoadStateViewHolder>(){
    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) = Unit

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = LoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding)
    }
}

class LoadStateViewHolder(binding: LoadStateBinding): RecyclerView.ViewHolder(binding.root)
