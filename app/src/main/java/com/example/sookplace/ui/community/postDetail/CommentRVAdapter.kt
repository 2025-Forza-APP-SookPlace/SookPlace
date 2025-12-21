package com.example.sookplace.ui.community.postDetail

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.sookplace.R
import com.example.sookplace.data.remote.response.CommentResponse
import com.example.sookplace.databinding.ItemCommentBinding

class CommentRVAdapter (
    private val currentUserId: String?, //사용자 아이디
    private val onDeleteClick: (String) -> Unit
): ListAdapter<CommentResponse, CommentRVAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: CommentResponse) {
            binding.tvCommentNickname.text = comment.author.nickname
            binding.tvCommentTime.text = comment.displayTime
            binding.tvCommentContent.text = comment.content
            binding.ivCommentProfile.load(comment.author.profileImageUrl) {
                transformations(CircleCropTransformation())
                placeholder(R.drawable.my_page_icon)
            }

            //댓글삭제 버튼(닉네임이 일치하면 보이게)
            Log.d("CommentDebug", "========================================")
            Log.d("CommentDebug", "내 로컬 닉네임: [${currentUserId}]") // currentUserId에 닉네임이 담겨있어야 함
            Log.d("CommentDebug", "댓글 작성 닉네임: [${comment.author.nickname}]")
            if (comment.author.nickname == currentUserId) {
                binding.btnDelete.visibility = View.VISIBLE
            } else {
                binding.btnDelete.visibility = View.GONE
            }

            binding.btnDelete.setOnClickListener {
                onDeleteClick(comment.commentId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CommentResponse>() {
            override fun areItemsTheSame(oldItem: CommentResponse, newItem: CommentResponse) = oldItem.commentId == newItem.commentId
            override fun areContentsTheSame(oldItem: CommentResponse, newItem: CommentResponse) = oldItem == newItem
        }
    }
}