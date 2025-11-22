package com.example.sookplace.communityList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sookplace.R

class CommunityRVAdapter (val items: ArrayList<String>) : RecyclerView.Adapter<CommunityRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommunityRVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.community_rv_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CommunityRVAdapter.ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item : String) {

        }
    }

}