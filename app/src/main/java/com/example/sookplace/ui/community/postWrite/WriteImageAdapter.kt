package com.example.sookplace.ui.community.postWrite

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.sookplace.databinding.ItemWriteImageBinding

//선택한 사진을 가로로 리스트업
class WriteImageAdapter(private val onRemoveClick: (Uri) -> Unit) :
    ListAdapter<Uri, WriteImageAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemWriteImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(uri: Uri) {
            binding.ivSelectedImage.load(uri) // Coil 사용
            binding.btnRemove.setOnClickListener { onRemoveClick(uri) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemWriteImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Uri>() {
            override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean = oldItem == newItem
        }
    }
}