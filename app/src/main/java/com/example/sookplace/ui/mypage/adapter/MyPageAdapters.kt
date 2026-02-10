package com.example.sookplace.ui.mypage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sookplace.data.remote.response.MyPlaceItem
import com.example.sookplace.data.remote.response.MyPostItem
import com.example.sookplace.databinding.ItemMyPlaceBinding
import com.example.sookplace.databinding.ItemMyPostBinding

// [중요] 코드를 덮어쓴 후, 상단 메뉴 Build > Rebuild Project를 꼭 실행하세요!

// 1. My Place Adapter
class MyPlaceAdapter(private val onItemClick: (MyPlaceItem) -> Unit) :
    ListAdapter<MyPlaceItem, MyPlaceAdapter.PlaceViewHolder>(PlaceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemMyPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlaceViewHolder(private val binding: ItemMyPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyPlaceItem) {
            // binding.root는 CardView입니다.
            binding.root.setOnClickListener { onItemClick(item) }

            // XML ID와 정확히 일치해야 합니다.
            binding.tvPlaceName.text = item.name ?: ""
            binding.tvPlaceCategory.text = item.category ?: ""
            // binding.tvPlaceRating.text = "4.5" // 데이터가 있다면 연결

            if (!item.imageUrl.isNullOrEmpty()) {
                Glide.with(binding.root.context)
                    .load(item.imageUrl)
                    .into(binding.ivPlaceImage)
            }
        }
    }

    class PlaceDiffCallback : DiffUtil.ItemCallback<MyPlaceItem>() {
        override fun areItemsTheSame(oldItem: MyPlaceItem, newItem: MyPlaceItem) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: MyPlaceItem, newItem: MyPlaceItem) = oldItem == newItem
    }
}

// 2. My Post Adapter
class MyPostAdapter(private val onItemClick: (MyPostItem) -> Unit) :
    ListAdapter<MyPostItem, MyPostAdapter.PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemMyPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PostViewHolder(private val binding: ItemMyPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyPostItem) {
            binding.root.setOnClickListener { onItemClick(item) }

            binding.tvPostTitle.text = item.title ?: ""
            binding.tvPostContent.text = item.content ?: ""
            binding.tvDate.text = item.createdAt?.take(10) ?: ""

            binding.tvLikeCount.text = "❤️ ${item.likeCount ?: 0}"
            binding.tvCommentCount.text = "💬 ${item.commentCount ?: 0}"

            if (!item.imageUrl.isNullOrEmpty()) {
                binding.ivPostThumbnail.visibility = View.VISIBLE
                Glide.with(binding.root.context)
                    .load(item.imageUrl)
                    .into(binding.ivPostThumbnail)
            } else {
                binding.ivPostThumbnail.visibility = View.GONE
            }
        }
    }

    class PostDiffCallback : DiffUtil.ItemCallback<MyPostItem>() {
        override fun areItemsTheSame(oldItem: MyPostItem, newItem: MyPostItem) = oldItem.postId == newItem.postId
        override fun areContentsTheSame(oldItem: MyPostItem, newItem: MyPostItem) = oldItem == newItem
    }
}