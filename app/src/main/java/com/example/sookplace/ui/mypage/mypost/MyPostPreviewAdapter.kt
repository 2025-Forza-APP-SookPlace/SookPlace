package com.example.sookplace.ui.mypage.mypost

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sookplace.data.remote.response.MyPostItem
import com.example.sookplace.databinding.ItemMyPostBinding

// [수정] 생성자에 onClick 람다 함수를 추가하여 프래그먼트와 연결합니다.
class MyPostPreviewAdapter(
    private val onClick: (MyPostItem) -> Unit
) : ListAdapter<MyPostItem, MyPostPreviewAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(
        private val binding: ItemMyPostBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyPostItem) {
            binding.tvPostTitle.text = item.title
            binding.tvPostContent.text = item.content

            binding.tvLikeCount.text = "❤️ ${item.likeCount}"
            binding.tvCommentCount.text = "💬 ${item.commentCount}"
            binding.tvDate.text = item.createdAt

            // 이미지 처리 (필요시 활성화)
            binding.ivPostThumbnail.visibility = View.GONE

            // [중요] 아이템 클릭 시 프래그먼트로 이벤트 전달
            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMyPostBinding.inflate(
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
        val diffUtil = object : DiffUtil.ItemCallback<MyPostItem>() {
            override fun areItemsTheSame(oldItem: MyPostItem, newItem: MyPostItem): Boolean {
                // [수정] id가 인식되지 않는 경우 postId로 변경해 봅니다.
                // 만약 MyPostItem에 postId도 없다면 실제 ID 변수명으로 고쳐주세요.
                return oldItem.postId == newItem.postId
            }

            override fun areContentsTheSame(oldItem: MyPostItem, newItem: MyPostItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}