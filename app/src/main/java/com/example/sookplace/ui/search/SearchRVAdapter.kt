package com.example.sookplace.ui.search

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.sookplace.R
import com.example.sookplace.data.remote.response.RestaurantContent
import com.example.sookplace.data.remote.response.SortRestaurantItem
import com.example.sookplace.databinding.SearchRvItemBinding

class SearchRVAdapter : ListAdapter<SortRestaurantItem, SearchRVAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(val binding: SearchRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SortRestaurantItem) {
            //가게 카드 UI
            binding.thumbnailUrl.load(item.thumbnailUrl)
            binding.category.text = item.category
            binding.name.text = item.name
            binding.name2.text = item.name
            binding.rating.text = item.rating.toString()
            binding.likeCount.text = item.likeCount.toString()
            binding.address.text = item.locationName
            binding.distanceMinutesFromCampus.text = itemView.context.getString(
                R.string.restaurant_minutes_format,
                item.distanceMinutesFromCampus
            )

            //좋아요 여부 UI
            binding.like.isSelected = item.isLiked
            updateHeartUI(item.isLiked)
            binding.like.setOnClickListener(null)

            binding.like.setOnClickListener { //TODO: 근데 좋아요 여부를 백엔드에 넘겨주는 api가 없는거 같은디??
                item.isLiked = !item.isLiked //
                binding.like.isSelected = item.isLiked // 선택 상태 변경
                updateHeartUI(item.isLiked) // 이미지 교체

                if (item.isLiked) {
                    item.likeCount += 1
                } else {
                    item.likeCount -= 1
                }
                binding.likeCount.text = item.likeCount.toString()
                updateHeartUI(item.isLiked)
            }

            //장소 보기: 네이버 링크로 이동
            binding.placeBtn.setOnClickListener {
                val url = item.shareUrl
                if (!url.isNullOrEmpty()) { // null 체크 추가 권장
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        itemView.context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(itemView.context, "링크를 열 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(itemView.context, "제공된 링크가 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun updateHeartUI(isLiked: Boolean) {
            if (isLiked) {
                binding.like.setImageResource(R.drawable.favorite_fill)
            } else {
                binding.like.setImageResource(R.drawable.favorite)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SearchRvItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<SortRestaurantItem>() {
            override fun areItemsTheSame(oldItem: SortRestaurantItem, newItem: SortRestaurantItem) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: SortRestaurantItem, newItem: SortRestaurantItem) =
                oldItem == newItem
        }
    }
}