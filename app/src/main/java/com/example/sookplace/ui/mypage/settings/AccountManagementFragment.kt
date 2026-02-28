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

    // 뷰모델 연결
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
        // [🔥핵심 해결!] 클릭이 되는지 확인하기 위해 Toast(알림창)를 추가하고, 가장 강력한 종료 함수를 썼습니다.
        binding.ivBack.setOnClickListener {
            Toast.makeText(requireContext(), "뒤로가기 클릭됨!", Toast.LENGTH_SHORT).show()

            // 현재 프래그먼트를 담고 있는 액티비티를 강제로 종료하여 마이페이지로 돌아갑니다.
            activity?.finish()
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
            Toast.makeText(requireContext(), "화면 이동 중...", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            activity?.finish()
        }

        // 하단 탭 아이콘들에 리스너 연결
        try {
            binding.homeTap?.setOnClickListener(navListener)
            binding.searchTap?.setOnClickListener(navListener)
            binding.mapTap?.setOnClickListener(navListener)
            binding.communityTap?.setOnClickListener(navListener)
        } catch (e: Exception) {
            e.printStackTrace() // 하단 탭이 XML에 없더라도 앱이 죽지 않게 방어
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userMe.collect { user ->
                    user?.let {
                        binding.username.text = it.nickname
                        binding.email.text = it.email
                        binding.badge.visibility = if (it.sookVerified) View.VISIBLE else View.GONE

                        // 서버 이미지 대신 기본 이미지 표시
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