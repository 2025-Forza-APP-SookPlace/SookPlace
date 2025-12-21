package com.example.sookplace.ui.community.postDetail

import coil.size.Scale
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.sookplace.databinding.ItemPostImageBinding

class PostImageAdapter(private val images: List<String>) :
    RecyclerView.Adapter<PostImageAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemPostImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(url: String) {
            binding.ivPostImage.load(url) {
                crossfade(true)
                scale(Scale.FILL)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPostImageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size
}