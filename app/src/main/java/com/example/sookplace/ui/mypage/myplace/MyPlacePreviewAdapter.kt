package com.example.sookplace.ui.mypage.myplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sookplace.databinding.ItemMyplaceBinding
import com.example.sookplace.ui.mypage.model.MyPlace

class MyPlacePreviewAdapter :
    ListAdapter<MyPlace, MyPlacePreviewAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(
        private val binding: ItemMyplaceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyPlace) {
            binding.placeName.text = item.name
            binding.placeCategory.text = item.category
            binding.placeRating.text = item.rating.toString()
            binding.placeImage.setImageResource(item.imageRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMyplaceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<MyPlace>() {
            override fun areItemsTheSame(oldItem: MyPlace, newItem: MyPlace): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MyPlace, newItem: MyPlace): Boolean {
                return oldItem == newItem
            }
        }
    }
}