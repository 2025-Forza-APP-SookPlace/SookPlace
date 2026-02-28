package com.example.sookplace.ui.community.postWrite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sookplace.data.remote.response.RestaurantContent
import com.example.sookplace.databinding.ItemPostRestaurantSearchBinding

//식당 리스트를 구성하는 RV
class RestaurantSearchAdapter(private val onClick: (RestaurantContent) -> Unit) :
    ListAdapter<RestaurantContent, RestaurantSearchAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemPostRestaurantSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RestaurantContent) {
            binding.tvName.text = item.name
            binding.tvCategory.text = item.locationName // 카테고리
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPostRestaurantSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<RestaurantContent>() {
            override fun areItemsTheSame(old: RestaurantContent, new: RestaurantContent) = old.id == new.id
            override fun areContentsTheSame(old: RestaurantContent, new: RestaurantContent) = old == new
        }
    }
}