package com.example.sookplace.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.sookplace.R
import com.example.sookplace.databinding.FragmentMypageBinding

class MyPageFragment : Fragment() {

    private lateinit var binding: FragmentMypageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ///하단바 프래그먼트 간의 이동 구현
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false)

        binding.searchTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_mypageFragment_to_searchFragment)
        }

        binding.mapTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_mypageFragment_to_mapFragment)
        }

        binding.communityTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_mypageFragment_to_communityFragment)
        }

        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_mypageFragment_to_homeFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // -------------------------------
        // 🔵 RecyclerView 초기화
        // -------------------------------
        val myPlaceRecycler = view.findViewById<RecyclerView>(R.id.newMyPlaceRecycler)
        val myPostRecycler = view.findViewById<RecyclerView>(R.id.newMyPostRecycler)

        // -------------------------------
        // 🔵 전체 데이터 (예시 10개)
        // -------------------------------
        val allPlaces = (0 until 10).map { "Item $it" }
        val allPosts = (0 until 10).map { "Item $it" }

        // -------------------------------
        // 🔵 홈 화면에서는 2개만 보이게 제한
        // -------------------------------
        val homePlaces = allPlaces.take(2)
        val homePosts = allPosts.take(2)



        // 이후 여기서 findViewById로 뷰 접근 가능!
        // 예시:
        // val usernameTextView = view.findViewById<TextView>(R.id.username)
        // usernameTextView.text = "홍길동"
    }
}