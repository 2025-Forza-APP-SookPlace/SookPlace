package com.example.sookplace.ui.home.roulette

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.sookplace.data.remote.response.RestaurantItem
import com.example.sookplace.databinding.FragmentRouletteBinding

class RouletteFragment : DialogFragment() {

    private var _binding: FragmentRouletteBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRouletteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 다이얼로그 배경을 투명하게 설정 (둥근 모서리 적용을 위함)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 너비 설정 (화면의 90% 정도 차지하도록)
        val params = dialog?.window?.attributes
        params?.width = (resources.displayMetrics.widthPixels * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams

        // 클릭 이벤트 설정 (예시)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.optionMyPlace.setOnClickListener {
            fetchResultAndShow(1)
        }
        binding.optionCategory.setOnClickListener {
            fetchResultAndShow(2)
        }
        binding.optionTop20.setOnClickListener {
            fetchResultAndShow(3)
        }
    }

    private fun fetchResultAndShow(type: Int) {
        // 1. 서버에 요청 보내기 (ViewModel을 통해)
        // viewModel.getRouletteResult(type)

        // 2. 서버에서 결과가 왔다고 가정 (임시 로직)
        val dummyResult = RestaurantItem(name = "백엔드 추천 맛집", isLiked = true, address = "숙명여대 후문", thumbnailUrl = "")

        // 3. 현재 옵션창 닫기
        dismiss()

        // 4. 결과 다이얼로그 띄우기
        val resultDialog = RouletteResultFragment(dummyResult, type) {
            // '다시 돌리기' 콜백: 서버에 다시 요청하는 로직
            fetchResultAndShow(type)
        }
        resultDialog.show(parentFragmentManager, "RouletteResult")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}