package com.example.sookplace.ui.mypage.mypost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sookplace.databinding.ItemMypostBinding
import com.example.sookplace.ui.mypage.model.MyPost

class MyPostPreviewAdapter :
    ListAdapter<MyPost, MyPostPreviewAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(
        private val binding: ItemMypostBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyPost) {
            binding.postTitle.text = item.title
            binding.postPlaceName.text = item.restaurantName
            binding.postRating.text = "${item.rating}"
            binding.postLikes.text = "${item.likes}"
            binding.postComments.text = "${item.comments}"
            binding.postDate.text = item.date
            binding.postImage.setImageResource(item.imageRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMypostBinding.inflate(
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
        val diffUtil = object : DiffUtil.ItemCallback<MyPost>() {
            override fun areItemsTheSame(oldItem: MyPost, newItem: MyPost): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MyPost, newItem: MyPost): Boolean {
                return oldItem == newItem
            }
        }
    }
}