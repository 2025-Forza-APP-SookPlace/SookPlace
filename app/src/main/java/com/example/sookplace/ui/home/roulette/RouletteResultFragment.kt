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
import com.example.sookplace.data.remote.response.RouletteSpinResponse
import com.example.sookplace.databinding.FragmentRouletteResultBinding


class RouletteResultFragment : DialogFragment() {

    private var _binding: FragmentRouletteResultBinding? = null
    private val binding get() = _binding!!

    var onRetry: (() -> Unit)? = null
    var onReset: (() -> Unit)? = null
    var resultData: RouletteSpinResponse? = null // 백엔드 응답 객체
    var optionType: Int = 1

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
            dismiss()
            onReset?.invoke()
        }
        binding.btnRetry.setOnClickListener {
            dismiss()
            onRetry?.invoke()
        }
    }

    private fun setupUI() {
        val data = resultData ?: return

        when (optionType) {
            1, 3 -> { // 식당 결과 (MY_PLACE, TOP_20)
                binding.layoutRestaurantCard.visibility = View.VISIBLE
                binding.layoutCategoryResult.visibility = View.GONE

                data.restaurant?.let { restaurant ->
                    binding.tvRestaurantName.text = restaurant.name
                    binding.tvRestaurantCategory.text = restaurant.category
                    binding.ivRestaurantImage.load(restaurant.thumbnailUrl) {
                        crossfade(true)
                        placeholder(R.drawable.background_radius_gray) // 로딩 중 이미지
                        error(R.drawable.background_radius_gray) // 에러 시 이미지
                    }
                    binding.tvRestaurantRating.text = restaurant.rating.toString()
                }
            }
            2 -> { // 카테고리 결과
                binding.layoutRestaurantCard.visibility = View.GONE
                binding.layoutCategoryResult.visibility = View.VISIBLE
                binding.tvCategoryName.text = data.category?.name ?: "추천 메뉴"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}