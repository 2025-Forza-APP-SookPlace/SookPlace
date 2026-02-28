package com.example.sookplace.ui.mypage.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sookplace.data.remote.response.UserMeResponse
import com.example.sookplace.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountManagementViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userMe = MutableStateFlow<UserMeResponse?>(null)
    val userMe: StateFlow<UserMeResponse?> = _userMe.asStateFlow()

    // 화면(Activity)에 성공/실패 알림을 띄우기 위한 이벤트 플로우
    private val _successEvent = MutableSharedFlow<String>()
    val successEvent: SharedFlow<String> = _successEvent.asSharedFlow()

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent: SharedFlow<String> = _errorEvent.asSharedFlow()

    // 메일 발송 성공 여부
    private val _emailSent = MutableStateFlow(false)
    val emailSent: StateFlow<Boolean> = _emailSent.asStateFlow()

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            try {
                val response = userRepository.getUserMe()
                _userMe.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 1. 닉네임 변경 API 호출
    fun changeNickname(nickname: String) {
        viewModelScope.launch {
            try {
                userRepository.updateNickname(nickname)
                _successEvent.emit("닉네임이 성공적으로 변경되었습니다.")
                loadUserProfile() // 닉네임 변경 후 프로필 새로고침
            } catch (e: Exception) {
                _errorEvent.emit("이미 사용 중이거나 변경할 수 없는 닉네임입니다.")
            }
        }
    }

    // 2. 비밀번호 변경 API 호출
    fun changePassword(current: String, newPass: String) {
        viewModelScope.launch {
            try {
                val response = userRepository.updatePassword(current, newPass)
                if (response.isSuccessful) {
                    _successEvent.emit("비밀번호가 성공적으로 변경되었습니다.")
                } else {
                    _errorEvent.emit("현재 비밀번호가 올바르지 않거나 변경 기준에 맞지 않습니다.")
                }
            } catch (e: Exception) {
                _errorEvent.emit("비밀번호 변경에 실패했습니다.")
            }
        }
    }

    // 3. 숙명인증: 인증 메일 발송 API 호출 (code = null)
    fun sendVerificationEmail(email: String) {
        viewModelScope.launch {
            try {
                userRepository.verifyUniversity(email, null)
                _emailSent.value = true
                _successEvent.emit("인증 메일이 발송되었습니다. 메일함을 확인해주세요.")
            } catch (e: Exception) {
                _errorEvent.emit("인증 메일 발송에 실패했습니다. (이메일을 확인해주세요)")
            }
        }
    }

    // 4. 숙명인증: 인증번호 확인 API 호출 (code 포함)
    fun confirmVerificationCode(email: String, code: String) {
        viewModelScope.launch {
            try {
                val response = userRepository.verifyUniversity(email, code)
                if (response.verified) {
                    _successEvent.emit("숙명인증이 완료되었습니다.")
                    loadUserProfile() // 뱃지 업데이트를 위해 프로필 새로고침
                } else {
                    _errorEvent.emit("인증에 실패했습니다. 코드를 다시 확인해주세요.")
                }
            } catch (e: Exception) {
                _errorEvent.emit("인증번호가 올바르지 않거나 만료되었습니다.")
            }
        }
    }
}