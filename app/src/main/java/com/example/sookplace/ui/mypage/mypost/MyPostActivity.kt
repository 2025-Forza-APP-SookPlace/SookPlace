package com.example.sookplace.ui.mypage.mypost

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sookplace.R
import com.example.sookplace.databinding.ActivityMyPostBinding
import com.example.sookplace.ui.community.CommunityRVAdapter

class MyPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_post)

        //RecyclerView
//        val rv : RecyclerView = binding.mypostRV
//        val items = ArrayList<String>()
//        items.add("a")
//        items.add("b")
//
//        val rvAdapter = CommunityRVAdapter(items)
//        rv.adapter = rvAdapter
//        rv.layoutManager = LinearLayoutManager(this)
    }
}