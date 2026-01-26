package com.example.sookplace.ui.mypage

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sookplace.ui.mypage.myplace.MyPlacePreviewAdapter
import com.example.sookplace.ui.mypage.mypost.MyPostPreviewAdapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.sookplace.R
import com.example.sookplace.databinding.FragmentMypageBinding

class MyPageFragment : Fragment() {

    private lateinit var binding: FragmentMypageBinding
    private val viewModel: MyPageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ///하단바 프래그먼트 간의 이동 구현
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

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
        binding.myPlaceMoreBtn.setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_mypageFragment_to_myPlaceListFragment)
        }

        binding.myPostMoreBtn.setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_mypageFragment_to_myPostListFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // -------------------------------
        // 🔵 RecyclerView 초기화
        // -------------------------------
        val myPlaceAdapter = MyPlacePreviewAdapter()
        val myPostAdapter = MyPostPreviewAdapter()

        binding.newMyPlaceRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.newMyPostRecycler.layoutManager = LinearLayoutManager(requireContext())

        binding.newMyPlaceRecycler.adapter = myPlaceAdapter
        binding.newMyPostRecycler.adapter = myPostAdapter

        // 🔵 ViewModel 데이터 로드
        viewModel.loadMyPage()

        viewModel.myPlaces.observe(viewLifecycleOwner) { places ->
            myPlaceAdapter.submitList(places.take(2))
        }

        viewModel.myPosts.observe(viewLifecycleOwner) { posts ->
            myPostAdapter.submitList(posts.take(2))
        }



        // 이후 여기서 findViewById로 뷰 접근 가능!
        // 예시:
        // val usernameTextView = view.findViewById<TextView>(R.id.username)
        // usernameTextView.text = "홍길동"
    }
}