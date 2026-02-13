package com.example.sookplace.ui.mypage.myplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sookplace.data.remote.response.MyPlaceItem

import com.example.sookplace.databinding.ItemMyPlaceBinding


class MyPlacePreviewAdapter(
    private val onClick: (MyPlaceItem) -> Unit
) : ListAdapter<MyPlaceItem, MyPlacePreviewAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(
        private val binding: ItemMyPlaceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyPlaceItem) {
            // [수정] 원본 코드의 변수명(name)으로 복구했습니다.
            binding.tvPlaceName.text = item.name ?: ""
            binding.tvPlaceCategory.text = item.category ?: ""

            // 평점 (숫자 등을 문자열로 변환)
            // binding.tvPlaceRating.text = item.rating.toString()

            // [이미지 처리]
            // Glide.with(binding.root).load(item.imageUrl).into(binding.ivPlaceImage)

            // [유지] 클릭 시 상세 페이지 이동을 위해 필요합니다.
            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMyPlaceBinding.inflate(
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
        val diffUtil = object : DiffUtil.ItemCallback<MyPlaceItem>() {
            override fun areItemsTheSame(oldItem: MyPlaceItem, newItem: MyPlaceItem): Boolean {
                // [수정] 원본 코드의 ID 비교 로직으로 복구했습니다.
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MyPlaceItem, newItem: MyPlaceItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}