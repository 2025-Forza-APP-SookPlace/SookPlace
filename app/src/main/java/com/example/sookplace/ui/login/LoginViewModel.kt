package com.example.sookplace.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sookplace.data.remote.request.LoginRequest
import com.example.sookplace.data.remote.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: com.example.sookplace.data.repository.AuthRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    fun login(userId: String, password: String) {
        viewModelScope.launch {
            val request = LoginRequest(userId, password)
            val result = repo.login(request)
            _loginResult.postValue(result)
        }
    }
}