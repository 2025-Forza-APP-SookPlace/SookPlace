package com.example.sookplace.ui.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sookplace.data.remote.response.FavoriteItem
import com.example.sookplace.databinding.MapRvItemBinding


class FavoriteAdapter(private val onItemClick: (String) -> Unit) :
    ListAdapter<FavoriteItem, FavoriteAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private val binding: MapRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FavoriteItem) {
            binding.name.text = item.name
            binding.rating.text = item.rating.toString()
            binding.category.text = item.category
            binding.distanceMinutesFromCampus.text = "${item.distanceMinutesFromCampus}분 거리"

            // 이미지 로딩
            Glide.with(binding.thumbnailUrl.context)
                .load(item.thumbnailUrl)
                .into(binding.thumbnailUrl)

            // 상세 페이지 이동
            binding.root.setOnClickListener { onItemClick(item.detailUrl) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MapRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<FavoriteItem>() {
        override fun areItemsTheSame(oldItem: FavoriteItem, newItem: FavoriteItem) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: FavoriteItem, newItem: FavoriteItem) = oldItem == newItem
    }
}