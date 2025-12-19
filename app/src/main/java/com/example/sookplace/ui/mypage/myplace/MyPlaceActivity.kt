package com.example.sookplace.ui.mypage.myplace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sookplace.R
import com.example.sookplace.databinding.ActivityMyPlaceBinding
import com.example.sookplace.ui.search.SearchRVAdapter

class MyPlaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyPlaceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_place)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_place)

//        ///RecyclerView
//        val rv : RecyclerView = binding.myplaceRv
//        val items = ArrayList<String>()
//        items.add("a")
//        items.add("b")
//
//        val rvAdapter = SearchRVAdapter(items)
//        rv.adapter = rvAdapter
//        rv.layoutManager = LinearLayoutManager(this)

    }
}