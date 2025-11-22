package com.example.sookplace

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sookplace.communityList.CommunityRVAdapter
import com.example.sookplace.databinding.ActivityMyPlaceBinding
import com.example.sookplace.databinding.ActivityMyPostBinding

class MyPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_post)

        //RecyclerView
        val rv : RecyclerView = binding.mypostRV
        val items = ArrayList<String>()
        items.add("a")
        items.add("b")

        val rvAdapter = CommunityRVAdapter(items)
        rv.adapter = rvAdapter
        rv.layoutManager = LinearLayoutManager(this)
    }
}