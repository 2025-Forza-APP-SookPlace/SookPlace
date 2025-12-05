package com.example.sookplace.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sookplace.data.remote.request.UserSignupRequest
import com.example.sookplace.data.remote.response.UserSignupResponse
import com.example.sookplace.data.repository.AuthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.launch

class SignupViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    fun signup(userId: String, nickname: String, email: String, password: String, onSuccess: (UserSignupResponse) -> Unit, onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            try {
                val req = UserSignupRequest(userId, nickname, email, password)
                val response = repository.signup(req)
                onSuccess(response)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}