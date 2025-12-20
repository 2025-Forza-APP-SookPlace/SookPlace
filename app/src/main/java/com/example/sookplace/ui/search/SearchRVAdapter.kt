package com.example.sookplace.ui.search

import android.content.Context
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

class SearchRVAdapter (
    private val onItemClick: (Int) -> Unit, //클릭 시 id 전달(상세 페이지로 이동)
    private val onLikeClick: (SortRestaurantItem) -> Unit //종아요 클릭
): ListAdapter<SortRestaurantItem, SearchRVAdapter.ViewHolder>(DiffCallback) {

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
                onLikeClick(item)
            }

            //공유 버튼 클릭(클립보드에 링크 복사)
            binding.share.setOnClickListener {
                val context = itemView.context
                val urlToCopy = item.shareUrl ?: ""

                if (urlToCopy.isNotBlank()) {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                    val clip = android.content.ClipData.newPlainText("Restaurant Link", urlToCopy)
                    clipboard.setPrimaryClip(clip)
                    android.widget.Toast.makeText(context, "링크가 클립보드에 복사되었습니다!", android.widget.Toast.LENGTH_SHORT).show()
                } else {
                    android.widget.Toast.makeText(context, "복사할 링크가 없습니다.", android.widget.Toast.LENGTH_SHORT).show()
                }
            }

            //장소 보기: 상세페이지로 이동
            binding.placeBtn.setOnClickListener {
                onItemClick(item.id)
            }

            //식당 카드 클릭 시 상세 페이지로 이동
//            binding.root.setOnClickListener {
//                onItemClick(item.id)
//            }
        }

        private fun updateHeartUI(isLiked: Boolean) {
            val icon = if (isLiked) R.drawable.favorite_fill else R.drawable.favorite
            binding.like.setImageResource(icon)
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