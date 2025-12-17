package com.example.sookplace.ui.home.roulette

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.sookplace.R
import com.example.sookplace.data.remote.response.RestaurantItem
import com.example.sookplace.databinding.FragmentRouletteResultBinding


class RouletteResultFragment (
    private val resultData: Any?, // 백엔드에서 받은 객체 (RestaurantItem 또는 Category명)
    private val optionType: Int,
    private val onRetry: () -> Unit // "다시 돌리기" 눌렀을 때 실행할 함수
): DialogFragment() {

    private var _binding: FragmentRouletteResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRouletteResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // 다이얼로그 크기 및 배경 설정
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val params = attributes
            params.width = (resources.displayMetrics.widthPixels * 0.9).toInt()
            attributes = params
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()

        binding.btnReset.setOnClickListener {
            // '다른 옵션 선택' 클릭 시 결과창 닫고 옵션창 다시 띄우기
            dismiss()
            // 부모 Fragment에서 옵션 다이얼로그를 다시 띄우도록 콜백을 주거나 처리
        }
        binding.btnRetry.setOnClickListener {
            dismiss()
            onRetry() // 부모에게 다시 돌리라고 시킴
        }
    }

    private fun setupUI() {
        when (optionType) {
            1, 3 -> { // 식당 결과 (My Place, TOP 20)
                binding.layoutRestaurantCard.visibility = View.VISIBLE
                binding.layoutCategoryResult.visibility = View.GONE

                // 백엔드에서 받은 식당 데이터 매핑
                val restaurant = resultData as? RestaurantItem
                restaurant?.let {
                    binding.tvRestaurantName.text = it.name
                    binding.tvRestaurantCategory.text = "it.category" //TODO: 나중에 고치기
                    binding.ivRestaurantImage.load(it.thumbnailUrl)
                    // ... 기타 데이터 세팅
                }
            }
            2 -> { // 카테고리 결과
                binding.layoutRestaurantCard.visibility = View.GONE
                binding.layoutCategoryResult.visibility = View.VISIBLE
                binding.tvCategoryName.text = resultData as? String ?: "랜덤 음식"
            }
        }
    }
}