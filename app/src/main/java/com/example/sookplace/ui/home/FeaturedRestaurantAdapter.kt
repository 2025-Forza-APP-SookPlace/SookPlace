package com.example.sookplace.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.sookplace.data.remote.response.RestaurantItem
import com.example.sookplace.databinding.ItemFeaturedRestaurantBinding

class FeaturedRestaurantAdapter : RecyclerView.Adapter<FeaturedRestaurantAdapter.ViewHolder>() {

    private val items = mutableListOf<RestaurantItem>()

    fun submitList(list: List<RestaurantItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemFeaturedRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RestaurantItem) {
            binding.thumbnail.load(item.thumbnailUrl)
            binding.name.text = item.name
            binding.address.text = item.address
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemFeaturedRestaurantBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: FeaturedRestaurantAdapter.ViewHolder,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}