package com.example.sookplace.searchList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sookplace.R

class SearchRVAdapter (val items : ArrayList<String>) : RecyclerView.Adapter<SearchRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchRVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.search_rv_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: SearchRVAdapter.ViewHolder, position: Int) {
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