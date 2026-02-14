package com.example.sookplace.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sookplace.data.remote.request.LoginRequest
import com.example.sookplace.data.remote.response.LoginResponse
import com.example.sookplace.data.repository.AuthRepository
import com.example.sookplace.data.local.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    fun login(userId: String, password: String) {
        viewModelScope.launch {
            val request = LoginRequest(userId, password)
            val result = authRepository.login(request)
            result.onSuccess { response ->
                tokenManager.saveTokens(
                    response.accessToken,
                    response.refreshToken
                )
            }
            _loginResult.postValue(result)
        }
    }
}