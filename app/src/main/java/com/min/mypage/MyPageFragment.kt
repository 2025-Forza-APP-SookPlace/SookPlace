package com.min.mypage.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.min.mypage.R
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

class MyPageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_home.xml을 연결
        return inflater.inflate(R.layout.fragment_map, container, false)
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