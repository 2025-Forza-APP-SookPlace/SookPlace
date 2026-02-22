package com.example.sookplace.ui.mypage.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.sookplace.MainActivity
import com.example.sookplace.R
import com.example.sookplace.databinding.FragmentAccountManagementBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountManagementFragment : Fragment() {

    private var _binding: FragmentAccountManagementBinding? = null
    private val binding get() = _binding!!

    // 직접 만드신 전용 뷰모델을 사용합니다!
    private val viewModel: AccountManagementViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        // 뒤로가기
        binding.backButton.setOnClickListener {
            requireActivity().finish()
        }

        binding.btnNickname.setOnClickListener {
            Toast.makeText(requireContext(), "닉네임 변경 기능 준비 중입니다.", Toast.LENGTH_SHORT).show()
        }

        binding.btnPassword.setOnClickListener {
            Toast.makeText(requireContext(), "비밀번호 변경 기능 준비 중입니다.", Toast.LENGTH_SHORT).show()
        }

        binding.btnVerify.setOnClickListener {
            Toast.makeText(requireContext(), "숙명인증 재확인 기능 준비 중입니다.", Toast.LENGTH_SHORT).show()
        }

        binding.btnDelete.setOnClickListener {
            Toast.makeText(requireContext(), "회원 탈퇴 기능 준비 중입니다.", Toast.LENGTH_SHORT).show()
        }

        // 하단 네비게이션
        val navListener = View.OnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            requireActivity().finish()
        }

        binding.homeTap.setOnClickListener(navListener)
        binding.searchTap.setOnClickListener(navListener)
        binding.mapTap.setOnClickListener(navListener)
        binding.communityTap.setOnClickListener(navListener)

        // 마이페이지 탭 클릭 시 뒤로가기
        binding.mypageTap.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 직접 만든 뷰모델의 userMe 관찰
                viewModel.userMe.collect { user ->
                    user?.let {
                        binding.username.text = it.nickname
                        binding.email.text = it.email
                        binding.badge.visibility = if (it.sookVerified) View.VISIBLE else View.GONE

                        // [수정] 계속 오류를 발생시키던 Glide 로직을 완전히 제거했습니다.
                        // 서버 통신 이미지 대신 기본 이미지가 항상 보이도록 안전하게 처리했습니다.
                        binding.profileImage.setImageResource(R.drawable.egg_song)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}