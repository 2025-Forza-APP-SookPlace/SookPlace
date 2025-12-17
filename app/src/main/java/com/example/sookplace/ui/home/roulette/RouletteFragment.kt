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

    private var optionClickListener: ((Int) -> Unit)? = null

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

    override fun onStart() {
        super.onStart()
        // 다이얼로그 배경을 투명하게 설정 (둥근 모서리 적용을 위함)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 너비 설정 (화면의 90% 정도 차지하도록)
        val params = dialog?.window?.attributes
        params?.width = (resources.displayMetrics.widthPixels * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 클릭 이벤트 설정
        setupClickListeners()
    }

    fun setOptionClickListener(listener: (Int) -> Unit) {
        this.optionClickListener = listener
    }

    private fun setupClickListeners() {
        binding.optionMyPlace.setOnClickListener {
            optionClickListener?.invoke(1)
            dismiss() // 옵션창 닫기
        }
        binding.optionCategory.setOnClickListener {
            optionClickListener?.invoke(2)
            dismiss()
        }
        binding.optionTop20.setOnClickListener {
            optionClickListener?.invoke(3)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}